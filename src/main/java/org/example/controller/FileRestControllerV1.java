package org.example.controller;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.example.constants.Constants;
import org.example.dao.impl.EventDaoImpl;
import org.example.dao.impl.FileDaoImpl;
import org.example.dto.FileDTO;
import org.example.model.File;
import org.example.service.*;
import org.example.service.impl.EventServiceImpl;
import org.example.service.impl.FileServiceImpl;
import org.example.service.impl.UserManagementService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.example.util.Util.getDataFromRequest;

@WebServlet("/api/v1/files")
public class FileRestControllerV1 extends HttpServlet {

    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private UserManagementService userManagementService;
    private FileService fileService;

    @Override
    public void init() {
        userManagementService = new UserManagementService();
        fileService = new FileServiceImpl(new FileDaoImpl());
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ServletRequestContext requestContext = new ServletRequestContext(req);
        if (ServletFileUpload.isMultipartContent(requestContext)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> fileItems;
            try {
                fileItems = upload.parseRequest(requestContext);
            } catch (FileUploadException e) {
                throw new RuntimeException(e);
        }

            for (FileItem item : fileItems) {
                if (!item.isFormField()) {
                    String userId = req.getHeader("USER-ID");
                    String content = item.getString("UTF-8");
                    String fileName = item.getName();

                    String filePath = Constants.SAVE_PATH + java.io.File.separator + fileName;
                    java.io.File storeFile = new java.io.File(filePath);
                    try {
                        item.write(storeFile);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    File file = userManagementService.createFileAndEvent(Long.valueOf(userId), content, fileName);

                    FileDTO fileDTO = FileDTO.fromEntity(file);

                    String json = gson.toJson(fileDTO);
                    resp.getWriter().write(json);
                }
            }
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String userId = req.getHeader("USER-ID");
        String body = getDataFromRequest(req);
        Map<String, String> map = null;
        if (!body.isEmpty()) {
            map = gson.fromJson(body, Map.class);
        }

        String fileId = Objects.nonNull(map) ? map.get("id") : null;

        if (Objects.nonNull(userId)) {
            List<FileDTO> usersFileDTOWithLinkList = userManagementService.getAllFilesDTOFromUser(Long.valueOf(userId));
            String json = gson.toJson(usersFileDTOWithLinkList);
            resp.getWriter().write(json);
        }
        if (Objects.nonNull(fileId)) {
            File file = fileService.get(Long.valueOf(fileId));
            FileDTO fileDTO = new FileDTO(file.getId(), file.getFileName(), Constants.SAVE_PATH + file.getFileName());
            String json = gson.toJson(fileDTO);
            resp.getWriter().write(json);
        }
        if (Objects.isNull(userId) && Objects.isNull(fileId)) {
            List<File> fileList = fileService.getAll();
            List<FileDTO> fileDTO = fileList.stream()
                    .map(file -> new FileDTO(file.getId(), file.getFileName(), Constants.SAVE_PATH + file.getFileName()))
                    .toList();
            String json = gson.toJson(fileDTO);
            resp.getWriter().write(json);
        }
    }
}
