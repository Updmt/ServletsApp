package org.example.dao;

import org.example.model.Event;

import java.util.List;

public interface EventDao {

    public void save(Event event);
    public Event get(Long id);
    public List<Event> getAll();
}
