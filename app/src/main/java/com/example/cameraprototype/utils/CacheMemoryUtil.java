package com.example.cameraprototype.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.cameraprototype.ui.constants.LibraryActivityConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.cameraprototype.ui.constants.LibraryActivityConstant.CACHE_PATH_NAME;
import static com.example.cameraprototype.ui.constants.LibraryActivityConstant.PICTURE_NAME;

public class CacheMemoryUtil {

    public static void savePhotoIntoCacheMemory(Context context, Bitmap bitmap) {
        try {
            File myDir = new File(context.getCacheDir() + CACHE_PATH_NAME);
            myDir.mkdirs();

            File file = new File(myDir, LibraryActivityConstant.PICTURE_NAME);

            if (file.exists()) {
                file.delete();
            }

            FileOutputStream out = null;

            out = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadImageFromCacheMemory(Context context) {

        File dir = new File(context.getCacheDir() + CACHE_PATH_NAME);
        File file = new File(dir, PICTURE_NAME);

        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }

        return null;
    }
}
