package com.example.cameraprototype.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cameraprototype.R;
import com.example.cameraprototype.dao.LibraryDAO;
import com.example.cameraprototype.model.Library;
import com.example.cameraprototype.ui.constants.LibraryActivityConstant;
import com.example.cameraprototype.ui.constants.NoteActivityConstant;

import java.util.List;

public class NoteActivity extends AppCompatActivity {


    private int itemPosition;
    private Library foundItem;
    private TextView itemName;
    private ImageView itemImage;
    private EditText itemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        findViewsById();
        checkIntentData();
        foundItem = getLibrary(itemPosition);
        setValuesToItems();
    }

    private void setValuesToItems() {
        itemName.setText(foundItem.getItem());
        itemImage.setImageBitmap(foundItem.getImage());

        if (foundItem.getNote() != null) {
            itemDescription.setText(foundItem.getNote());
        }
    }

    private void checkIntentData() {
        Intent name = getIntent();

        if (hasLibraryData(name)) {
            itemPosition = name.getIntExtra(
                    LibraryActivityConstant.LIBRARY_POSITION_LIST, NoteActivityConstant.DEFAULT_RESULT_VALUE);
        }
    }

    public void findViewsById() {
        itemName = findViewById(R.id.activity_item_note);
        itemImage = findViewById(R.id.activity_image_note);
        itemDescription = findViewById(R.id.activity_note_field);
    }


    private boolean hasLibraryData(Intent name) {
        return name.hasExtra(LibraryActivityConstant.LIBRARY_POSITION_LIST);
    }

    public Library getLibrary(int position) {
        //LibraryDao dao = LibraryDatabase.getInstance(NoteActivity.this).getLibraryDao();
        LibraryDAO dao = new LibraryDAO();
        List<Library> libraries = dao.readList();
        return libraries.get(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isSameId(item)) {
            foundItem.setNote(itemDescription.getText().toString());
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isSameId(MenuItem item) {
        return item.getItemId() == R.id.menu_save_note;
    }
}
