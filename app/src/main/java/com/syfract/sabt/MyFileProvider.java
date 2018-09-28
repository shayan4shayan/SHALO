package com.syfract.sabt;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shayan on 11/3/2017.
 */

public class MyFileProvider extends FileProvider {
    public static String newFilePath(String type) {
        File file = Environment.getExternalStorageDirectory();
        String path = file.getAbsolutePath();
        path += "/coall";

        file = new File(path);
        file.mkdir();

        path += "/" + System.currentTimeMillis() + type;
        file = new File(path);

        if (file.exists()) return path;
        else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
