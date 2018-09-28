package com.base;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ALL IN ONE on 7/11/2017.
 */

public class Cache {

    private static Cache mCache;
    private Context context;

    public static Cache getInstance(Context context){
        if (mCache==null) mCache = new Cache(context);
        return mCache;
    }

    private Cache(Context context){
        this.context = context;
    }

    public String read(String Tag) throws IOException {
        String  path = context.getCacheDir().getAbsolutePath();
        File file = new File(path + "/"+Tag);
        FileInputStream fis = new FileInputStream(file);
        String out = "";
        byte[] buffer;
        while (fis.available()>16*1024){
            buffer = new byte[16*1024];
            fis.read(buffer);
            out+= new String(buffer);
        }
        buffer = new byte[fis.available()];
        fis.read(buffer);
        out+= new String(buffer);
        return out;
    }

    public void write(String Tag, String data) throws IOException {
        String path = context.getCacheDir().getAbsolutePath();
        File file = new File(path + "/" +Tag);
        if (file.exists()) file.delete();
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data.getBytes());
    }


}
