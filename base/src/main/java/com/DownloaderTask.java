package com;

import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ALL IN ONE on 9/4/2017.
 */

public class DownloaderTask extends AsyncTask<String, Integer, String> {

    private final Context context;
    private final String path;

    public DownloaderTask(Context context, String path) {

        this.context = context;
        this.path = path;
    }

    @Override
    protected String doInBackground(String... params) {


        if (params.length == 0) return null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        URLConnection connection = null;
        String url = params[0];
        try {
            URL downloadUrl = new URL(url);
            connection = downloadUrl.openConnection();
            connection.connect();


            int fileLength = connection.getContentLength();
            boolean updateProgress = fileLength > 0;

            inputStream = connection.getInputStream();

            outputStream = new FileOutputStream(path);
            Log.d(getClass().getSimpleName(), "doInBackground: starting");

            byte[] buffer = new byte[4096];
            long total = 0;
            int count;
            while ((count = inputStream.read(buffer)) != -1) {
                if (isCancelled()) {
                    inputStream.close();
                    outputStream.close();
                    Log.d(getClass().getSimpleName(), "doInBackground: download canceled");
                    return null;
                }
                Log.d(getClass().getSimpleName(), "doInBackground: next buffer downloaded");
                total += count;
                if (updateProgress) {
                    publishProgress((int) total * 100 / fileLength);
                }
                outputStream.write(buffer,0,count);
                outputStream.flush();
                buffer = new byte[4096];
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
}
