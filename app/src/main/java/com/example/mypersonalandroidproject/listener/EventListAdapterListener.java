package com.example.mypersonalandroidproject.listener;

import com.example.mypersonalandroidproject.model.Event;

public interface EventListAdapterListener {
    void onUpdateClicked(Event event, int position);
    void onDeleteClicked(Event event, int position);
}