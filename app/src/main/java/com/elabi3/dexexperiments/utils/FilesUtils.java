package com.elabi3.dexexperiments.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elabi3 on 5/2/16.
 **/
public class FilesUtils {

    /**************************************************************************
     * Create & Remove
     **************************************************************************/

    //region Create & Remove
    public static void createFolderIfNeeded(String path) {
        File newFile = new File(path);
        if (!newFile.exists()) {
            newFile.mkdir();
        }
    }

    public static void removeFolder(String path) {
        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -rf " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }
    }

    //endregion


    /**************************************************************************
     * Read, Move & Copy
     **************************************************************************/

    //region Read, Move & Copy
    public static List<String> readFilesFromDir(String path) {
        List<String> resultList = new ArrayList<>();

        File[] dirFiles = new File(path).listFiles();
        try {
            for (int i = 0; i < dirFiles.length; i++) {
                String file_name = StringUtils.substringAfterLast(dirFiles[i].toString(), "/");
                resultList.add(file_name);
            }
        } catch (Exception e) {
        }
        return resultList;
    }

    public static void moveFile(String inputPath, String outputPath, String inputFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            FilesUtils.createFolderIfNeeded(outputPath);

            in = new FileInputStream(inputPath + File.separator + inputFile);
            out = new FileOutputStream(outputPath + File.separator + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + File.separator + inputFile).delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(String inputPath, String outputPath, String inputFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            FilesUtils.createFolderIfNeeded(outputPath);

            in = new FileInputStream(inputPath + File.separator + inputFile);
            out = new FileOutputStream(outputPath + File.separator + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //endregion


    /**************************************************************************
     * Package Name from Apk
     **************************************************************************/

    //region Package Name from Apk
    public static String getPackageNameByAPK(Context context, String apkPath) {
        String strRetVal = "";
        if (apkPath == null || apkPath.isEmpty() || context == null) {
            return strRetVal;
        }

        try {
            PackageInfo packInfo = context.getPackageManager().getPackageArchiveInfo(apkPath, 0);
            strRetVal = packInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strRetVal;
    }


    //endregion

}
