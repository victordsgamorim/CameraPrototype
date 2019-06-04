package com.example.cameraprototype.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.cameraprototype.ui.QuizActivity;

public class CameraUtil {

    public static void cameraResultAfterPhoto(Context context, Uri imageUri) {

        cameraQuizResult(context, imageUri);
        Intent intent = new Intent(context, QuizActivity.class);
        context.startActivity(intent);
    }

    public static void cameraQuizResult(Context context, Uri imageUri) {
        Bitmap bitmap = ImageUtils.saveImageIntoBitmap(context, imageUri);
        CacheMemoryUtil.savePhotoIntoCacheMemory(context, bitmap);
    }

}
