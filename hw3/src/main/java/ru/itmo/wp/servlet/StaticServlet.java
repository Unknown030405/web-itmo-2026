package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StaticServlet extends HttpServlet {
    private final Path root = Paths.get("src/main/webapp/static").toAbsolutePath().normalize();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String requests = request.getRequestURI().substring(1); // first symbol is /
        final String[] uris = requests.split("\\+");
        final List<File> files = new ArrayList<>();

        for (String uri : uris) {
            sendFile(response, uri, files);
        }

        if (!response.isCommitted()) {
            try (OutputStream outputStream = response.getOutputStream()) {
                for (File file : files) {
                    Files.copy(file.toPath(), outputStream);
                }
            }
        }
    }

    private void sendFile(HttpServletResponse response, String uri, List<File> files) throws IOException {
        File file = root.resolve(uri).toFile();
        final Path staticRoot = Paths.get(getServletContext().getRealPath("/static")).toAbsolutePath().normalize();

        if (!file.isFile()) {
            file = new File(getServletContext().getRealPath("/static/" + uri));
        }

        if (file.isFile() &&
                (file.toPath().toRealPath().normalize().startsWith(root) ||
                        file.toPath().toRealPath().normalize().startsWith(staticRoot))) {
            if (response.getContentType() == null) {
                response.setContentType(getServletContext().getMimeType(file.getName()));
            }
            files.add(file);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
