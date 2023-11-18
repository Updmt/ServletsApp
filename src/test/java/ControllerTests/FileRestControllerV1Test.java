package ControllerTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.example.controller.FileRestControllerV1;
import org.example.model.File;
import org.example.service.FileService;
import org.example.service.impl.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

@ExtendWith({MockitoExtension.class})
public class FileRestControllerV1Test {

    private static final String USER_ID_HEADER = "USER-ID";
    private static final String FILE_NAME = "test_file.txt";

    @Mock
    private UserManagementService userManagementService;

    @Mock
    private FileService fileService;

    @InjectMocks
    FileRestControllerV1 fileController;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;

    @BeforeEach
    public void setup() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void testDoPostWithFileUpload() throws IOException, FileUploadException {
        when(request.getHeader(USER_ID_HEADER)).thenReturn("1");

        MockedStatic<ServletFileUpload> mocked = mockStatic(ServletFileUpload.class);
        ServletRequestContext mockRequestContext = new ServletRequestContext(request);
        when(mockRequestContext.getContentType()).thenReturn("multipart/form-data");
        mocked.when(() -> ServletFileUpload.isMultipartContent(mockRequestContext)).thenReturn(true);

        FileItem fileItemMock = mock(FileItem.class);
        when(fileItemMock.isFormField()).thenReturn(false);
        when(fileItemMock.getString("UTF-8")).thenReturn("file_content");
        when(fileItemMock.getName()).thenReturn(FILE_NAME);

        List<FileItem> fileItems = Arrays.asList(fileItemMock);
        ServletFileUpload servletFileUpload = mock(ServletFileUpload.class);
        when(servletFileUpload.parseRequest(mockRequestContext)).thenReturn(fileItems);

        File file = new File(1L, "file_content", FILE_NAME, null);
        when(userManagementService.createFileAndEvent(1L, "file_content", FILE_NAME)).thenReturn(file);

        fileController.doPost(request, response);

        String output = getResponseOutput();
        assertThat(output).contains("\"file_name\":\"test_file.txt\"");
    }

    private String getResponseOutput() {
        return stringWriter.getBuffer().toString();
    }

}
