package ru.itmo.web.hw4.util;

import ru.itmo.web.hw4.model.Post;
import ru.itmo.web.hw4.model.User;
import ru.itmo.web.hw4.model.User.Color;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class DataUtil {
    private static final List<User> USERS = Collections.unmodifiableList(Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov", Color.RED),
            new User(6, "pashka", "Pavel Mavrin", Color.GREEN),
            new User(9, "geranazavr555", "Georgiy Nazarov", Color.BLUE),
            new User(11, "tourist", "Gennady Korotkevich", Color.RED)
    ));

    private static final List<Post> POSTS = Collections.unmodifiableList(Arrays.asList(
            new Post(1, "Welcome to Codeforces", "Welcome to our community! Here you can solve problems, participate in contests, and share ideas with other programmers", USERS.get(0)),
            new Post(5, "A note about contests", repeat('A', 350), USERS.get(1)),
            new Post(9, "Preparing for the next round", "The next round is nearly here. " + repeat('B', 370) + " Join us for the full announcement and details", USERS.get(0)),
            new Post(100, "Practice and progress", "Regular practice helps. Try different problem types, review your solutions, and discuss approaches with friends", USERS.get(3))
    ));

    private static String repeat(char c, int count) {
        char[] chars = new char[count];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    private DataUtil() {
    }

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);
        data.put("currentPath", request.getRequestURI());
        data.put("basePath", request.getContextPath());
        data.put("user", findUserById(request.getParameter("logged_user_id")));
        data.put("selectedPost", findPostById(request.getParameter("post_id")));

        User selectedUser = findUserById(request.getParameter("user_id"));
        if (selectedUser == null) {
            selectedUser = findUserByHandle(request.getParameter("handle"));
        }
        data.put("selectedUser", selectedUser);
    }

    public static List<Post> getPostsNewestFirst() {
        List<Post> result = new ArrayList<>(POSTS);
        Collections.reverse(result);
        return result;
    }

    public static User findUserById(String id) {
        try {
            for (User user : USERS) {
                if (user.getId() == Integer.parseInt(id)) return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User findUserByHandle(String handle) {
        for (User user : USERS) {
            if (user.getHandle().equals(handle)) return user;
        }
        return null;
    }

    public static Post findPostById(String id) {
        try {
            for (Post post : POSTS) {
                if (post.getId() == Integer.parseInt(id)) return post;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
