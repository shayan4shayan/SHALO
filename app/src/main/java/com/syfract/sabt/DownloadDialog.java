package com.syfract.sabt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.syfract.sabt.download.DownloadHandler;
import com.syfract.sabt.download.DownloadService;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by shayan4shayan on 4/9/18.
 */

public class DownloadDialog extends AlertDialog {
    private Activity context;
    private String url;

    public DownloadDialog(Activity context, String url) {
        super(context);
        this.context = context;
        this.url = url;

    }

    private ProgressBar pg;

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.dialog_download);
        setCancelable(false);
        pg = findViewById(R.id.progress);

        DownloadHandler handler = new DownloadHandler(getContext());
        handler.Handle(url);
    }


    public void onUpdateProgress(final int prgress) {
        pg.setProgress(prgress);
    }

    public void onComplete(@NotNull Uri path) {
        String mimeTypeMap = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path.toString()));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Log.d(getClass().getSimpleName(), "onClick: " + mimeTypeMap);
        intent.setDataAndType(path, mimeTypeMap);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getContext().startActivity(intent);
    }
}
