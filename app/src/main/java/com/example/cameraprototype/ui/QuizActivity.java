package com.example.cameraprototype.ui;

import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cameraprototype.R;
import com.example.cameraprototype.dao.LibraryDAO;
import com.example.cameraprototype.model.Library;
import com.example.cameraprototype.utils.CacheMemoryUtil;
import com.example.cameraprototype.utils.CameraUtil;
import com.example.cameraprototype.ui.asynctask.ClassificationAsyncTask;
import com.example.cameraprototype.utils.ImageUtils;
import com.example.cameraprototype.utils.MatrixUtil;
import com.example.cameraprototype.utils.RecognizedUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.List;

import tflite.Classifier;
import tflite.ClassifierQuantizedMobileNet;

import static com.example.cameraprototype.ui.constants.LibraryActivityConstant.CAMERA_RESULT;
import static com.example.cameraprototype.ui.constants.QuizActivityConstant.DEFAULT_ANSWER_VALUE;
import static com.example.cameraprototype.ui.constants.QuizActivityConstant.IMAGE_TITLE;
import static com.example.cameraprototype.ui.constants.QuizActivityConstant.RIGHT_ANSWER;
import static com.example.cameraprototype.ui.constants.QuizActivityConstant.WRONG_ANSWER;
import static com.example.cameraprototype.ui.constants.QuizActivityConstant.imageRightAnswer;
import static com.example.cameraprototype.ui.constants.QuizActivityConstant.imageWrongAnswer;


public class QuizActivity extends AppCompatActivity {

    private Classifier classifier = null;

    private Button firstButton, secondButton, thirdButton, forthButton;
    private CircularImageView imageView;
    private FloatingActionButton fab;
    private boolean answer = DEFAULT_ANSWER_VALUE;
    private ViewGroup transitionsContainer;
    private TextView text;
    private ImageView imageTransition;
    private Uri imageUri;
    private String recognizedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTitle(IMAGE_TITLE);
        findViewsById();
        loadClassifier();
        classificationByImage();
    }

    /**
     * use image to start classification
     */
    public void classificationByImage() {
        Bitmap imageBitmap = CacheMemoryUtil.loadImageFromCacheMemory(this);
        imageBitmap = MatrixUtil.cropeImageWithMatrix(imageBitmap, classifier);

        /**async task for classification*/
        new ClassificationAsyncTask(classifier, firstButton, secondButton, thirdButton, forthButton).execute(imageBitmap);

        imageView.setImageBitmap(imageBitmap);
        answerButtonClickListener();
        fabAddItemToLibrary(imageBitmap);
    }


    private void loadClassifier() {
        try {
            classifier = new ClassifierQuantizedMobileNet(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Menu Button Action
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_take_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_screen_take_picture) {
            takePicture();
        }

        if(item.getItemId() == R.id.menu_screen_folder){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * activate camera intent
     */
    private void takePicture() {
        imageUri = ImageUtils.imageUriContent(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA_RESULT);
            text.setVisibility(View.INVISIBLE);
            imageTransition.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * next step after taking the picture
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
            CameraUtil.cameraQuizResult(this, imageUri);
            classificationByImage();
        }
    }

    /**
     * button check answer
     */
    private void answerButtonClickListener() {
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickCheckAnswer(firstButton);
            }
        });

        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickCheckAnswer(secondButton);
            }
        });

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickCheckAnswer(thirdButton);
            }
        });

        forthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickCheckAnswer(forthButton);
            }
        });
    }

    private void buttonClickCheckAnswer(Button button) {
        if (checkRightAnswer(button)) {
            answer = true;
        } else {
            answer = false;
        }

    }

    public void addTransitionToAnswer(String answer, int image) {
        text.setText(answer);
        imageTransition.setImageResource(image);
        TransitionManager.beginDelayedTransition(transitionsContainer);
        text.setVisibility(View.VISIBLE);
        imageTransition.setVisibility(View.VISIBLE);
    }

    private boolean checkRightAnswer(Button buttonText) {
        recognizedItem = new RecognizedUtil().getRecognizedItem();
        if (buttonText.getText().equals(recognizedItem)) {
            addTransitionToAnswer(RIGHT_ANSWER, imageRightAnswer);
            return true;
        }
        addTransitionToAnswer(WRONG_ANSWER, imageWrongAnswer);
        return false;
    }


    private void fabAddItemToLibrary(final Bitmap myBitmap) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibraryDAO libDao = new LibraryDAO();
                List<Library> libraries = libDao.readList();

                if (answer == false) {
                    wrongAnswerToast();
                    return;
                }

                String recognitionCap = transformFirstCharWordUpperCase();
                Library lib = new Library();
                lib.setImage(myBitmap);
                lib.setItem(recognitionCap);
                libDao.addItem(lib);

                finish();
            }
        });
    }

    private void wrongAnswerToast() {
        Toast toast = Toast.makeText(QuizActivity.this, "Please, select the right answer to proceed!", Toast.LENGTH_SHORT);
        TextView text = toast.getView().findViewById(android.R.id.message);
        if (text != null) text.setGravity(Gravity.CENTER);
        toast.show();
    }

    private String transformFirstCharWordUpperCase() {
        return recognizedItem.substring(0, 1).toUpperCase() + recognizedItem.substring(1);
    }


    private void findViewsById() {
        firstButton = findViewById(R.id.option1);
        secondButton = findViewById(R.id.option2);
        thirdButton = findViewById(R.id.option3);
        forthButton = findViewById(R.id.option4);

        imageView = findViewById(R.id.secondImageView);
        fab = findViewById(R.id.add_image_library);

        transitionsContainer = findViewById(R.id.transitions_container);
        text = transitionsContainer.findViewById(R.id.textTransition);
        imageTransition = transitionsContainer.findViewById(R.id.image_answer);
    }

}
