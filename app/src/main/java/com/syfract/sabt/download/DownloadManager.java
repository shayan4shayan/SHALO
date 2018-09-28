package com.syfract.sabt.download;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by shayan on 12/13/2017.
 */

public class DownloadManager {
    private static DownloadManager instance;

    private HashMap<Integer,String> map ;

    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }

    public void download(String url, Callback callback) {
        DownloaderTask task = new DownloaderTask(getFilePath(url),callback);
        task.execute(url);
    }

    private String getFilePath(String url) {
        String md5 = md5(url);
        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path + "/sabt/");
        if (!file.exists()){
            file.mkdir();
        }
        return path + "/sabt/" + md5 + getTypeOf(url);
    }

    private String getTypeOf(String url) {
        return url.substring(url.lastIndexOf("."));
    }

    public String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public interface Callback {
        void onDownloadFinished(String uri);

        void onUpdateProgress(int progressPercent);

        void onDownloadFailed();
    }
}
