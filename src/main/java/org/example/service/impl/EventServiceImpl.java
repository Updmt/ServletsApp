package org.example.service.impl;

import org.example.dao.EventDao;
import org.example.model.Event;
import org.example.service.EventService;

import java.util.List;

public class EventServiceImpl implements EventService {

    private final EventDao eventDao;

    public EventServiceImpl(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public Event save(Event event) {
        return eventDao.save(event);
    }

    @Override
    public Event get(Long id) {
        return eventDao.getById(id);
    }

    @Override
    public List<Event> getAll() {
        return eventDao.getAll();
    }
}
