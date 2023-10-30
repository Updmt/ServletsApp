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
    public File save(File file) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(file);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return file;
    }

    @Override
    public File getById(Long id) {
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
        return null;
    }

    @Override
    public void deleteById(Long id) {
    }
}
