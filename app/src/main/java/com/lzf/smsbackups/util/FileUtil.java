package com.lzf.smsbackups.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by MJCoder on 2018-06-06.
 */

public class FileUtil {

    /**
     * @param folder   文件夹
     * @param fileName 文件名
     * @return File[0]是文件保存目录及文件夹；File[1]是缓存文件（去掉 ".cache" 则是对应的文件）
     */
    /**
     * 在指定目录下新建文件
     *
     * @param context      环境、上下文
     * @param isPckageData 该文件是否存储在包目录下（PckageData:("Android/data/com.lzf.smsBackups/files/")；包目录下的数据会随着卸载而全部删除）；true：该文件将存储在包目录下
     * @param folder       文件夹
     * @param fileName     文件名
     * @return File[0]是文件保存目录及文件夹；File[1]是缓存文件（去掉 ".cache" 则是对应的文件）
     */
    public static File[] mkdirsAndCackeFile(Context context, boolean isPckageData, String folder, String fileName) {
        File saveDir = null;
        File cacheFile;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (isPckageData) { //该文件将存储在包目录下
                saveDir = new File(context.getExternalFilesDir(null), folder);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
            } else {
                saveDir = new File(Environment.getExternalStorageDirectory(), folder);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
            }
            cacheFile = new File(saveDir, fileName + ".cache");
        } else {
            String dirTemp = null;
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod;
            try {
                getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
                getVolumePathsMethod.setAccessible(true);
                Object[] params = {};
                Object invoke = getVolumePathsMethod.invoke(storageManager, params);
                for (int i = 0; i < ((String[]) invoke).length; i++) {
                    if (!((String[]) invoke)[i]
                            .equals(Environment.getExternalStorageDirectory().toString())) {
                        dirTemp = ((String[]) invoke)[i];
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            saveDir = new File(dirTemp, folder);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            cacheFile = new File(saveDir, fileName + ".cache");
        }
        return new File[]{saveDir, cacheFile};
    }

    /**
     * 获取内存的可用容量
     */
    public static long getAvailableStorage() {
        //        检索关于文件系统上空间的总体信息。这是Unix statvfs（）的包装器。
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockSize = statFs.getBlockSize();
        long availableBlocks = statFs.getAvailableBlocks();
        long availableSize = blockSize * availableBlocks;
        //        Formatter.formatFileSize(context, availableSize);
        return availableSize;
    }

    /**
     * 检测该手机的存储路径
     *
     * @param context
     * @param folder  必须指定一个文件夹
     * @return File[0]是手机的存储位置（内置卡）手机内置卡的存储路径（及根目录）文件夹；File[1]是手机的存储位置（外置SD卡）手机外置SD卡的存储路径（及根目录）文件夹
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static File[] detectionStoragePath(Context context, String folder) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File phoneCardFile = null; //手机的存储位置（内置卡）手机内置卡的存储路径（及根目录）文件夹
        File sdCardFile = null; //手机的存储位置（外置SD卡）手机外置SD卡的存储路径（及根目录）文件夹
        StorageManager storageManager = (StorageManager) context.getSystemService(Activity.STORAGE_SERVICE);
        Method method = storageManager.getClass().getMethod("getVolumePaths");
        String[] paths = (String[]) method.invoke(storageManager);
        for (int i = 0; i < paths.length; i++) {
            Log.v("paths[i].toString()", paths[i].toString() + "");
            if (paths[i].toString().equals(Environment.getExternalStorageDirectory().toString())) { //手机的内部存储（内置的SD卡，不可拆卸）
                phoneCardFile = new File(paths[i], folder);
                if (!phoneCardFile.exists()) {
                    phoneCardFile.mkdirs();
                }
            } else if (!paths[i].toString().contains("otg")) { //手机的外部存储（外置的SD卡，可拆卸）
                sdCardFile = new File(paths[i], "Android/data/" + context.getPackageName() + "/files/" + folder);
                if (!sdCardFile.exists()) {
                    sdCardFile.mkdirs();
                }
            }
        }
        if (phoneCardFile != null && sdCardFile != null && phoneCardFile.exists() && sdCardFile.exists()) {
        } else {
            Toast.makeText(context, "没有检测到其他的存储路径", Toast.LENGTH_SHORT).show();
        }
        return new File[]{phoneCardFile, sdCardFile};
    }
}
