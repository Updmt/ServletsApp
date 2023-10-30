package org.example.dao.impl;

import org.example.dao.UserDao;
import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;

public class UserDaoImpl implements UserDao {

    Transaction transaction = null;

    @Override
    public User save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return user;
    }

    @Override
    public User getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (User) session.createQuery("FROM User u LEFT JOIN FETCH u.events e LEFT JOIN FETCH e.file WHERE u.id = :userId")
                    .setParameter("userId", id)
                    .getSingleResult();
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User u LEFT JOIN FETCH u.events e LEFT JOIN FETCH e.file")
                    .list();
        }
    }

    @Override
    public User update(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return user;
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);

            if (Objects.nonNull(user)) {
                session.delete(user);
                transaction.commit();
            } else {
                System.err.println("Пользователь с ID " + id + " не найден");
            }
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }
}
