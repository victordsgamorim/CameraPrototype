package com.example.cameraprototype.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

//@Entity(tableName = "library")
public class Library{

    //@PrimaryKey(autoGenerate = true)
    private int id;

    //@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private Bitmap image;
    private String item;
    private String note;

    //@Ignore
    public Library(Bitmap image, String item) {
        //this.image = image;
        this.item = item;
    }

    public Library(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
