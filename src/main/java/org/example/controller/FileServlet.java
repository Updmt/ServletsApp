package org.example.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.File;
import org.example.service.*;
import org.example.service.impl.FileServiceImpl;
import org.example.service.impl.UserManagementService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.example.util.Util.getDataFromRequest;

@WebServlet("/file")
public class FileServlet extends HttpServlet {

    private final UserManagementService userManagementService = new UserManagementService();
    private final FileService fileService = new FileServiceImpl();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String userId = req.getHeader("USER-ID");
        String body = getDataFromRequest(req);
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(body, Map.class);
        String text = map.get("text");

        File file = userManagementService.createFileAndEvent(Long.valueOf(userId), text);

        String json = gson.toJson(file);
        resp.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String userId = req.getHeader("USER-ID");
        String body = getDataFromRequest(req);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        Map<String, String> map = null;
        if (!body.isEmpty()) {
            map = gson.fromJson(body, Map.class);
        }

        String fileId = Objects.nonNull(map) ? map.get("id") : null;

        if (Objects.nonNull(userId)) {
            List<File> usersFileList = userManagementService.getAllFilesFromUser(Long.valueOf(userId));
            String json = gson.toJson(usersFileList);
            resp.getWriter().write(json);
        } else if (Objects.nonNull(fileId)) {
            File file = fileService.get(Long.valueOf(fileId));
            String json = gson.toJson(file);
            resp.getWriter().write(json);
        } else {
            List<File> fileList = fileService.getAll();
            String json = gson.toJson(fileList);
            resp.getWriter().write(json);
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String body = getDataFromRequest(req);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        Map<String, String> map = gson.fromJson(body, Map.class);

        File existingFile = fileService.get(Long.valueOf(map.get("id")));
        existingFile.setText(map.get("text"));

        fileService.update(existingFile);

        String userJson = gson.toJson(existingFile);
        resp.getWriter().write(userJson);
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String body = getDataFromRequest(req);
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        Map<String, String> map = gson.fromJson(body, Map.class);
        String fileId = map.get("id");

        File file = fileService.get(Long.parseLong(fileId));
        fileService.delete(Long.parseLong(fileId));
        String userJson = gson.toJson(file);
        resp.getWriter().write(userJson);
    }
}
