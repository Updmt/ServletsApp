package org.example.service.impl;

import org.example.dao.EventDao;
import org.example.dao.impl.EventDaoImpl;
import org.example.model.Event;
import org.example.service.EventService;

import java.util.List;

public class EventServiceImpl implements EventService {

    EventDao eventDao = new EventDaoImpl();

    @Override
    public void save(Event event) {
        eventDao.save(event);
    }

    @Override
    public Event get(Long id) {
        return eventDao.get(id);
    }

    @Override
    public List<Event> getAll() {
        return eventDao.getAll();
    }
}
