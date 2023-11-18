package org.example.controller;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.dao.impl.UserDaoImpl;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.example.util.Util.getDataFromRequest;

@WebServlet("/api/v1/users")
public class UserRestControllerV1 extends HttpServlet {

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private UserService userService;

    @Override
    public void init() {
        userService = new UserServiceImpl(new UserDaoImpl());
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String body = getDataFromRequest(req);
        Map<String, String> map = gson.fromJson(body, Map.class);

        User user = new User();
        user.setFirstName(map.get("first_name"));
        user.setLastName(map.get("last_name"));
        userService.save(user);

        UserDTO userDTO = UserDTO.fromEntity(user);
        String userJson = gson.toJson(userDTO);
        resp.getWriter().write(userJson);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String userId = req.getHeader("USER-ID");

        if (Objects.nonNull(userId)) {
            User user = userService.get(Long.valueOf(userId));
            UserDTO userDTO = UserDTO.fromEntity(user);
            String userJson = gson.toJson(userDTO);
            resp.getWriter().write(userJson);
        } else {
            List<User> users = userService.getAll();
            List<UserDTO> userDTOs = UserDTO.fromEntityList(users);
            String userJson = gson.toJson(userDTOs);
            resp.getWriter().write(userJson);
        }
    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String body = getDataFromRequest(req);
        Map<String, String> map = gson.fromJson(body, Map.class);

        User existingUser = userService.get(Long.valueOf(map.get("id")));
        existingUser.setFirstName(map.get("first_name"));
        existingUser.setLastName(map.get("last_name"));

        userService.update(existingUser);

        UserDTO userDTO = UserDTO.fromEntity(existingUser);
        String userJson = gson.toJson(userDTO);
        resp.getWriter().write(userJson);
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String body = getDataFromRequest(req);
        Map<String, String> map = gson.fromJson(body, Map.class);
        String userId = map.get("id");

        User user = userService.get(Long.parseLong(userId));
        UserDTO userDTO = UserDTO.fromEntity(user);
        userService.delete(Long.parseLong(userId));

        String userJson = gson.toJson(userDTO);
        resp.getWriter().write(userJson);
    }
}
