package org.example.dao.impl;

import org.example.dao.EventDao;
import org.example.model.Event;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EventDaoImpl implements EventDao {

    Transaction transaction = null;

    @Override
    public Event save(Event event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(event);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return event;
    }

    @Override
    public Event update(Event event) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {
    }

    @Override
    public Event getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Event) session.createQuery("FROM Event e LEFT JOIN FETCH e.file WHERE e.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();
        }
    }

    @Override
    public List<Event> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Event e LEFT JOIN FETCH e.file").list();
        }
    }
}
