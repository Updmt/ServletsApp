package org.example.service;

import org.example.model.Event;

import java.util.List;

public interface EventService {

    public void save(Event file);
    public Event get(Long id);
    public List<Event> getAll();
}
