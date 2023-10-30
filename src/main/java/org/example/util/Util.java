package org.example.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

public class Util {

    public static String getDataFromRequest(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while (Objects.nonNull(line = reader.readLine())) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }
}
