package com.chris.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件操作相关的工具类
 */
public final class FileUtil {

    /**
     * 读取文件为字符串
     * @param file 文件
     * @return 文件内容字符串
     * @throws IOException
     */
    public static String read(File file) throws IOException {
        String text = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            text = read(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return text;
    }

    /**
     * 读取输入流为字符串,最常见的是网络请求
     * @param is 输入流
     * @return 输入流内容字符串
     * @throws IOException
     */
    public static String read(InputStream is) throws IOException {
        StringBuffer strbuffer = new StringBuffer();
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                strbuffer.append(line).append("\r\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return strbuffer.toString();
    }

    /**
     * 把字符串写入到文件中
     * @param file 被写入的目标文件
     * @param str 要写入的字符串内容
     * @throws IOException
     */
    public static void write(File file, String str) throws IOException {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file));
            out.write(str.getBytes());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * unzip zip file to dest folder
     * @param zipFilePath
     * @param destPath
     */
    public static void unzip(String zipFilePath, String destPath) throws IOException {
        // check or create dest folder
        File destFile = new File(destPath);
        if (!destFile.exists()) {
            destFile.mkdirs();
        }

        // start unzip
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry;
        String zipEntryName;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            zipEntryName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                File folder = new File(destPath + File.separator + zipEntryName);
                folder.mkdirs();
            } else {
                File file = new File(destPath + File.separator + zipEntryName);
                if (file != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = zipInputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        zipInputStream.close();
    }

    /**
     * 删除文件或者文件夹，默认保留根目录
     * @param directory
     */
    public static void del(File directory) {
        del(directory, false);
    }

    /**
     * 删除文件或者文件夹
     * @param directory
     */
    public static void del(File directory, boolean keepRoot) {
        if (directory != null && directory.exists()) {
            if (directory.isDirectory()) {
                for (File subDirectory : directory.listFiles()) {
                    del(subDirectory, false);
                }
            }

            if (!keepRoot) {
                directory.delete();
            }
        }
    }

    /**
     * 复制单个文件
     */
    public static void copyFile(Context context, File fromeFile, File toFile) {
        try {
            int bytesum = 0;
            int byteread = 0;
            if (fromeFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(fromeFile); //读入原文件
                FileOutputStream fs = new FileOutputStream(toFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                if (toFile.setLastModified(System.currentTimeMillis())) {
                    context.getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + toFile.getPath())));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
