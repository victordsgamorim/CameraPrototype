package com.example.cameraprototype.ui.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Button;

import com.example.cameraprototype.utils.RecognizedUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tflite.Classifier;

public class ClassificationAsyncTask extends AsyncTask<Bitmap, Void, List<Classifier.Recognition>> {


    private Classifier classifier;
    private final Button firstButton;
    private final Button secondButton;
    private final Button thirdButton;
    private final Button forthButton;
    private String recognized;

    public ClassificationAsyncTask(
            Classifier classifier, Button firstButton, Button secondButton,
            Button thirdButton, Button forthButton) {
        this.classifier = classifier;
        this.firstButton = firstButton;
        this.secondButton = secondButton;
        this.thirdButton = thirdButton;
        this.forthButton = forthButton;
    }

    @Override
    protected List<Classifier.Recognition> doInBackground(Bitmap... bitmaps) {

        Bitmap bitmap = bitmaps[0];

        try {
            List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Classifier.Recognition> recognitions) {
        super.onPostExecute(recognitions);
        recognized = recognitions.get(0).getTitle();
        new RecognizedUtil().setRecognizedItem(recognized);

        List<String> possibleResults = possibleResults(recognitions);

        List<String> labelList = insertingButtonOptions(recognized, possibleResults);
        insertLabelsIntoButtons(labelList);
    }


    private List<String> possibleResults(List<Classifier.Recognition> results) {
        /**it gets from the 2nd to 4th result of the imageTransition recognition in order
         * to add to the button later on*/
        List<String> possibleResults = new ArrayList<>();
        possibleResults.add(results.get(1).getTitle());
        possibleResults.add(results.get(2).getTitle());
        possibleResults.add(results.get(3).getTitle());
        return possibleResults;
    }

    private List<String> insertingButtonOptions(
            String recognition, List<String> results) {

        List<String> labelList = new ArrayList<>();
        labelList.add(recognition);
        labelList.add(results.get(0));
        labelList.add(results.get(1));
        labelList.add(results.get(2));

        Collections.shuffle(labelList);
        return labelList;
    }

    private void insertLabelsIntoButtons(List<String> labelList) {
        firstButton.setText(labelList.get(0));
        secondButton.setText(labelList.get(1));
        thirdButton.setText(labelList.get(2));
        forthButton.setText(labelList.get(3));
    }
}
