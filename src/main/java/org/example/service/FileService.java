package org.example.service;

import org.example.model.File;

import java.util.List;

public interface FileService {
    public void save(File file);
    public File get(Long id);
    public List<File> getAll();
    public File update(File file);
    public void delete(Long id);
}
