package org.example.service.impl;

import org.example.dao.FileDao;
import org.example.dao.impl.FileDaoImpl;
import org.example.model.File;
import org.example.service.FileService;

import java.util.List;

public class FileServiceImpl implements FileService {

    private final FileDao fileDao = new FileDaoImpl();

    @Override
    public void save(File file) {
        fileDao.save(file);
    }

    @Override
    public File get(Long id) {
        return fileDao.getById(id);
    }

    @Override
    public List<File> getAll() {
        return fileDao.getAll();
    }

    @Override
    public File update(File file) {
        return fileDao.update(file);
    }

    @Override
    public void delete(Long id) {
        fileDao.deleteById(id);
    }
}
