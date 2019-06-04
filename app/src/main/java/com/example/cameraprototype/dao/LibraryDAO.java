package com.example.cameraprototype.dao;

import com.example.cameraprototype.model.Library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryDAO {

    private static List<Library> list = new ArrayList<>();

    public void addItem(Library item) {
        list.add(item);
    }

    public List<Library> readList() {
        return new ArrayList<>(list);
    }

    public void remove(int position) {
        list.remove(position);
    }

}
