package com.sunhongbin.noiseDetect.Utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by su on 2016/6/4.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    public static final String LOCAL = "SoundMeter";

    public static final String LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    /**
     * 录音文件目录
     */
    public static final String REC_PATH = LOCAL_PATH + LOCAL + File.separator;



    /**
     * 自动在SD卡创建相关的目录
     */
    static {
        File dirRootFile = new File(LOCAL_PATH);
        if (!dirRootFile.exists()) {
            dirRootFile.mkdirs();
        }
        File recFile = new File(REC_PATH);
        if (!recFile.exists()) {
            recFile.mkdirs();
        }
    }

    private FileUtil() {
    }

    /**
     * 判断是否存在存储空间	 *
     *
     * @return
     */
    public static boolean isExitSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static File writeFileSdCard(String fileName, StringBuffer value) {

        File file = new File(REC_PATH + fileName);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file, true);
            outputStream.write(value.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File createTempFile(String fileName) {

        File myCaptureFile = new File(REC_PATH + fileName);
        if (myCaptureFile.exists()) {
            myCaptureFile.delete();
        }
        try {
            myCaptureFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }


}
