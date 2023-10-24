package org.example.dao.impl;

import org.example.dao.EventDao;
import org.example.model.Event;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class EventDaoImpl implements EventDao {

    Transaction transaction = null;

    @Override
    public void save(Event event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(event);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public Event get(Long id) {
        Event event = new Event();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            event = session.get(Event.class, id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return event;
    }

    @Override
    public List<Event> getAll() {
        List<Event> events = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            events = session.createQuery("from Event").list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return events;
    }
}
