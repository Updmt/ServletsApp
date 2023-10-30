package org.example.service;

import org.example.model.File;

import java.util.List;

public interface FileService {
    void save(File file);
    File get(Long id);
    List<File> getAll();
    File update(File file);
    void delete(Long id);
}
