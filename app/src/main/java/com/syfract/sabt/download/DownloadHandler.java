package com.syfract.sabt.download;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.base.api.BaseApiInterface;

/**
 * Created by shayan on 12/13/2017.
 */

public class DownloadHandler implements BaseApiInterface, DownloadManager.Callback {

    private Context context;

    public DownloadHandler(Context context) {
        this.context = context;
    }

    public void Handle(String url) {
        Log.d(getClass().getSimpleName(), "Handle: " + url);
        onResponse(url);
    }

    @Override
    public void onResponse(String response) {
        DownloadManager downloadManager = new DownloadManager();
        downloadManager.download(response, this);
    }

    @Override
    public void onError(String message) {
    }

    @Override
    public void onLogout() {

    }

    @Override
    public void onDownloadFinished(String uri) {
        DownloadService.Companion.startActionComplete(context, uri);
        Log.d("Handler", "sending broadCast");
    }

    @Override
    public void onUpdateProgress(int progressPercent) {
        DownloadService.Companion.startActionProgress(context, progressPercent);
    }

    @Override
    public void onDownloadFailed() {
        DownloadService.Companion.startActionFailed(context);
    }
}
