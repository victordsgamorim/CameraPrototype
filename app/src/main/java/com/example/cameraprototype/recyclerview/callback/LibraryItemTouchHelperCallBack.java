package com.example.cameraprototype.recyclerview.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.cameraprototype.recyclerview.adapter.RecyclerViewAdapter;
import com.example.cameraprototype.dao.LibraryDAO;


public class LibraryItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private RecyclerViewAdapter adapter;

    public LibraryItemTouchHelperCallBack(RecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int drag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(0, drag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
      return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        int adapterPosition = viewHolder.getAdapterPosition();
        new LibraryDAO().remove(adapterPosition);
        adapter.remove(adapterPosition);
    }
}
