package com.example.mypersonalandroidproject.listener;

import com.example.mypersonalandroidproject.model.Event;

import java.io.Serializable;

public interface UpdateEventListener extends Serializable {
    void onUpdated(Event event);
}
