package com.taraba.gulfoilapp.util;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.taraba.gulfoilapp.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

import static android.os.Build.VERSION_CODES.N;

public class FileUtils {
    public static String BROCHURE_LOCAL_PATH = Environment.getExternalStorageDirectory().toString() + "/Gulfpdf/";
    private static String TAG = "FileUtils";

    public static boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            // todo change the file location/name according to your needs
            File folder = new File(Environment.getExternalStorageDirectory(), "Gulfpdf");
            Log.e(TAG, "writeResponseBodyToDisk: IS Directory: " + folder.exists());
            folder.mkdir();

            String filePath = BROCHURE_LOCAL_PATH + fileName;
            Log.e(TAG, "writeResponseBodyToDisk: File Path: " + filePath);
            File futureStudioIconFile = new File(filePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize");
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                inputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            return false;
        }

    }


    public static boolean isFileExists(String fileName) {
        File file = new File(BROCHURE_LOCAL_PATH + fileName);
        return file.exists();
    }


    public static String getFileNameFromURL(String url) {
        if (url == null)
            return "";
        return url.substring(url.lastIndexOf("/") + 1);
    }


    public static String getLocalFullPath(String mp3FileName) {
        return BROCHURE_LOCAL_PATH + mp3FileName;
    }


    public static Uri getLocalUri(String mp3FileName) {
        Uri uri;
        File file = new File(mp3FileName);
        if (Build.VERSION.SDK_INT >= N) {
            uri = FileProvider.getUriForFile(
                    AppConfig.getInstance().getApplicationContext(),
                    AppConfig.getInstance().getApplicationContext().getPackageName() + ".provider",
                    file
            );
            Log.d(TAG, "getLocalUri: if > $uri");
        } else {
            uri = Uri.parse(mp3FileName);
            Log.d(TAG, "getLocalUri: else > $uri");
        }
        return uri;
    }

}
