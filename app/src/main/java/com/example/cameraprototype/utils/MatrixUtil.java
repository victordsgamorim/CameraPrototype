package com.example.cameraprototype.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.cameraprototype.ui.constants.QuizActivityConstant;

import tflite.Classifier;

public class MatrixUtil {

    private static Matrix createCropMatrix(Bitmap imageBitmap, Classifier classifier) {
        Matrix frameToCropTranform =
                ImageUtils.getTransformationMatrix(
                        imageBitmap.getWidth(),
                        imageBitmap.getHeight(),
                        classifier.getImageSizeX(),
                        classifier.getImageSizeY(),
                        QuizActivityConstant.ROTATION,
                        QuizActivityConstant.ASPECT_RATIO);

        Matrix cropToFrameTranform = new Matrix();
        frameToCropTranform.invert(cropToFrameTranform);
        return frameToCropTranform;
    }

    public static Bitmap cropeImageWithMatrix(Bitmap imageBitmap, Classifier classifier) {
        Bitmap newBitmap = Bitmap.createBitmap(classifier.getImageSizeX(), classifier.getImageSizeY(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(imageBitmap, createCropMatrix(imageBitmap, classifier), null);

        /**getting the cropped imageTransition and treated in order to classify*/
        imageBitmap = newBitmap;
        return imageBitmap;
    }


}
