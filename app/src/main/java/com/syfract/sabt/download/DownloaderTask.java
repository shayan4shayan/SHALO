package com.syfract.sabt.download;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ALL IN ONE on 9/4/2017.
 */

public class DownloaderTask extends AsyncTask<String, Integer, String> {

    private final String path;
    private DownloadManager.Callback callback;

    public DownloaderTask(String path, DownloadManager.Callback callback) {
        this.path = path;
        this.callback = callback;
        File file = new File(path);
        Log.d(getClass().getSimpleName(), "DownloaderTask: " + path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String doInBackground(String... params) {


        if (params.length == 0) return null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        URLConnection connection = null;
        String url = params[0];
        Log.d(getClass().getSimpleName(), "url is " + url);
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
                Log.d(getClass().getSimpleName(), "doInBackground: next buffer downloaded " + total + " from " + fileLength);
                total += count;
                publishProgress((int) total * 100 / fileLength);

                outputStream.write(buffer, 0, count);
                outputStream.flush();
                buffer = new byte[4096];
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onDownloadFailed();
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
        Log.d(getClass().getSimpleName(), "download finished");
        callback.onDownloadFinished(path);
        return path;
    }

    private Uri getUri(String path) {
        return Uri.fromFile(new File(path));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onUpdateProgress(0);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        callback.onUpdateProgress(values[0]);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        callback.onDownloadFailed();
    }
}
