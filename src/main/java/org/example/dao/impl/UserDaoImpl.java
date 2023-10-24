package org.example.dao.impl;

import org.example.dao.UserDao;
import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDaoImpl implements UserDao {

    Transaction transaction = null;

    @Override
    public void save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public User get(Long id) {
        User user = new User();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            users = session.createQuery("from User").list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return users;
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
    public void delete(Long id) {
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
