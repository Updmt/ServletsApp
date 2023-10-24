package org.example.service.impl;

import org.example.model.Event;
import org.example.model.File;
import org.example.model.User;
import org.example.service.EventService;
import org.example.service.FileService;
import org.example.service.UserService;

import java.time.Instant;
import java.util.List;

public class UserManagementService {

    private final UserService userService = new UserServiceImpl();
    private final EventService eventService = new EventServiceImpl();
    private final FileService fileService = new FileServiceImpl();


    public File createFileAndEvent(Long userId, String text) {
        User user = userService.get(userId);

        File file = new File();
        file.setText(text);

        Event event = new Event();
        event.setCreatedAt(Instant.now().toString());
        event.setUser(user);
        event.setFile(file);

        fileService.save(file);
        eventService.save(event);
        return file;
    }

    public List<File> getAllFilesFromUser(Long userId) {
        User user = userService.get(userId);
        List<Event> userEventList = user.getEvents();
        return userEventList.stream().map(Event::getFile).toList();
    }

    public List<Event> getAllEventsFromUser(Long userId) {
        User user = userService.get(userId);
        return user.getEvents();
    }
}
