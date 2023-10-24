package org.example.dao;

import org.example.model.File;

import java.util.List;

public interface FileDao {

    public void save(File file);
    public File get(Long id);
    public List<File> getAll();
    public File update(File file);
    public void delete(Long id);
}
