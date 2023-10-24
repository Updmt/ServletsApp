package org.example.dao.impl;

import org.example.dao.FileDao;
import org.example.model.Event;
import org.example.model.File;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileDaoImpl implements FileDao {

    Transaction transaction = null;

    @Override
    public void save(File file) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(file);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public File get(Long id) {
        File file = new File();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            file = session.get(File.class, id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return file;
    }

    @Override
    public List<File> getAll() {
        List<File> files = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            files = session.createQuery("from File").list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return files;
    }

    @Override
    public File update(File file) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(file);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return file;
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            File file = session.get(File.class, id);

            if (Objects.nonNull(file)) {
                Event event = file.getEvent();
                if (Objects.nonNull(event)) {
                    event.setFile(null);
                }
                session.delete(file);
                transaction.commit();
            }
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
