package org.example.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.example.util.Util.getDataFromRequest;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String body = getDataFromRequest(req);
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(body, Map.class);

        User user = new User();
        user.setFirstName(map.get("first_name"));
        user.setLastName(map.get("last_name"));
        userService.save(user);

        String userJson = gson.toJson(user);
        resp.getWriter().write(userJson);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String userId = req.getHeader("USER-ID");

        if (Objects.nonNull(userId)) {
            User user = userService.get(Long.valueOf(userId));
            String userJson = gson.toJson(user);
            resp.getWriter().write(userJson);
        } else {
            List<User> users = userService.getAll();
            String userJson = gson.toJson(users);
            resp.getWriter().write(userJson);
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

        User existingUser = userService.get(Long.valueOf(map.get("id")));
        existingUser.setFirstName(map.get("first_name"));
        existingUser.setLastName(map.get("last_name"));

        userService.update(existingUser);

        String userJson = gson.toJson(existingUser);
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
        String userId = map.get("id");

        User user = userService.get(Long.parseLong(userId));
        userService.delete(Long.parseLong(userId));
        String userJson = gson.toJson(user);
        resp.getWriter().write(userJson);
    }
}
