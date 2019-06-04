package com.example.cameraprototype.ui;

import android.app.Activity;

import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;


import com.example.cameraprototype.ui.constants.LibraryActivityConstant;
import com.example.cameraprototype.R;
import com.example.cameraprototype.dao.LibraryDAO;
import com.example.cameraprototype.model.Library;
import com.example.cameraprototype.recyclerview.adapter.OnItemClickListener;
import com.example.cameraprototype.recyclerview.adapter.RecyclerViewAdapter;
import com.example.cameraprototype.recyclerview.callback.LibraryItemTouchHelperCallBack;
import com.example.cameraprototype.utils.CameraUtil;
import com.example.cameraprototype.utils.ImageUtils;

import java.util.List;

import static com.example.cameraprototype.ui.constants.LibraryActivityConstant.CAMERA_RESULT;
import static com.example.cameraprototype.ui.constants.LibraryActivityConstant.FROM_CAMERA;
import static com.example.cameraprototype.ui.constants.LibraryActivityConstant.NEW_PICTURE;

public class LibraryActivity extends Activity {

    private RecyclerView recycleView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Library> libraries;
    private Uri imageUri;
    private ImageView image;
    private FloatingActionButton fab;
    private LibraryDAO libDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        findViewsById();
        checkListSizeIfEmpty();
        configRecyclerView();
        fabTakePhoto();
    }


    @Override
    protected void onResume() {
        refreshRecyclerView();
        recyclerViewAdapterClickListener();

        /**it will make the empty image invisible */
        if (isDifferentOfZero()) {
            image.setVisibility(View.INVISIBLE);
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
            CameraUtil.cameraResultAfterPhoto(this, imageUri);
        }
    }


    private boolean isDifferentOfZero() {
        return libraries.size() != 0;
    }


    private void takePicture() {
        imageUri = ImageUtils.imageUriContent(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA_RESULT);
        }
    }


    /**
     * recycler view
     */
    private void configRecyclerView() {
        recyclerViewAdapter = new RecyclerViewAdapter(this, libraries);
        recycleView.setAdapter(recyclerViewAdapter);
        itemTouchHelperAnimation();
    }

    private void itemTouchHelperAnimation() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new LibraryItemTouchHelperCallBack(recyclerViewAdapter));
        itemTouchHelper.attachToRecyclerView(recycleView);
    }

    private void checkListSizeIfEmpty() {
        libDao = new LibraryDAO();
        libraries = libDao.readList();

        if (libraries.size() == 0) {
            image.setVisibility(View.VISIBLE);
        }
    }

    private void refreshRecyclerView() {
        libraries.clear();
        libraries.addAll(libDao.readList());
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void recyclerViewAdapterClickListener() {
        recyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Library library, int position) {
                Intent intent = new Intent(LibraryActivity.this, NoteActivity.class);
                intent.putExtra(LibraryActivityConstant.LIBRARY_POSITION_LIST, position);
                startActivity(intent);
            }
        });
    }

    private void fabTakePhoto() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void findViewsById() {
        image = findViewById(R.id.imageview_empty_list);
        recycleView = findViewById(R.id.recycle_view);
        fab = findViewById(R.id.take_photo);
    }

}
