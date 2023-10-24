package org.example.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Event;
import org.example.service.EventService;
import org.example.service.impl.EventServiceImpl;
import org.example.service.impl.UserManagementService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.example.util.Util.getDataFromRequest;

@WebServlet("/event")
public class EventServlet extends HttpServlet {

    private final UserManagementService userManagementService = new UserManagementService();
    private final EventService eventService = new EventServiceImpl();

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

        String eventId = Objects.nonNull(map) ? map.get("id") : null;

        if (Objects.nonNull(userId)) {
            List<Event> usersEventList = userManagementService.getAllEventsFromUser(Long.valueOf(userId));
            String json = gson.toJson(usersEventList);
            resp.getWriter().write(json);
        } else if (Objects.nonNull(eventId)) {
            Event event = eventService.get(Long.valueOf(eventId));
            String json = gson.toJson(event);
            resp.getWriter().write(json);
        } else {
            List<Event> eventList = eventService.getAll();
            String json = gson.toJson(eventList);
            resp.getWriter().write(json);
        }
    }
}
