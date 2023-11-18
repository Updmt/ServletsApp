package org.example.service;

import org.example.model.Event;

import java.util.List;

public interface EventService {

    Event save(Event event);
    Event get(Long id);
    List<Event> getAll();
}
