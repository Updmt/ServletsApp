package org.example.service;

import org.example.model.Event;

import java.util.List;

public interface EventService {

    void save(Event file);
    Event get(Long id);
    List<Event> getAll();
}
