package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class MessageServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final Set<Message> messages = new TreeSet<>(
            Comparator.comparingLong(message -> message.creationOrder));
    private long nextCreationOrder;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getRequestURI()) {
            case "/message/auth":
                authUser(request, response);
                break;
            case "/message/add":
                addMessage(request, response);
                break;
            case "/message/findAll":
                getAllMessages(response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getAllMessages(HttpServletResponse response) throws IOException {
        synchronized (messages) {
            List<Message> messageList = new ArrayList<>(messages);
            writeJsonResponse(response, messageList);
        }
    }

    private void addMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");
        String text = request.getParameter("text");

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (text == null) {
            response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
            return;
        }

        synchronized (messages) {
            messages.add(new Message(user, text, nextCreationOrder++));
        }
    }

    private void authUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = request.getParameter("user");
        HttpSession session = request.getSession();
        if (user != null) {
            session.setAttribute("user", user);
        }

        String authedUser = (String) session.getAttribute("user");
        writeJsonResponse(response, authedUser == null ? "" : authedUser);
    }

    private void writeJsonResponse(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(gson.toJson(object));
        response.getWriter().flush();
    }

    private static final class Message {
        final String user;
        final String text;
        final transient long creationOrder;

        Message(String user, String text, long creationOrder) {
            this.user = user;
            this.text = text;
            this.creationOrder = creationOrder;
        }
    }
}
