package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Base64;

public class CaptchaFilter extends HttpFilter {
    private static final String PASSED_KEY = "captchaPassed";
    private static final String EXPECTED_KEY = "captchaExpected";
    private static final String ANSWER_PARAMETER = "captchaAnswer";
    private static final String CAPTCHA_PAGE =
            "<!doctype html><html lang=\"ru\"><head><meta charset=\"UTF-8\">"
                    + "<title>Проверка</title></head><body><main>"
                    + "<h1>Введите код с картинки</h1>"
                    + "<p%s>Неверный код. Попробуйте ещё раз.</p>"
                    + "<img src=\"data:image/png;base64,%s\" alt=\"Код проверки\">"
                    + "<form method=\"post\">"
                    + "<label>Код: <input name=\"%s\" inputmode=\"numeric\""
                    + " pattern=\"[0-9]{3}\" maxlength=\"3\" required autofocus></label>"
                    + "<button type=\"submit\">Продолжить</button>"
                    + "</form></main></body></html>";
    private final SecureRandom random = new SecureRandom();

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        if (Boolean.TRUE.equals(session.getAttribute(PASSED_KEY))) {
            chain.doFilter(request, response);
            return;
        }

        if ("GET".equals(request.getMethod())) {
            showCaptcha(response, getOrCreateAnswer(session), false);
            return;
        }

        if ("POST".equals(request.getMethod()) && request.getParameter(ANSWER_PARAMETER) != null) {
            String answer = request.getParameter(ANSWER_PARAMETER);
            boolean correct;
            int nextAnswer = 0;

            Integer expected = (Integer) session.getAttribute(EXPECTED_KEY);
            correct = expected != null && expected.toString().equals(answer);
            if (correct) {
                session.setAttribute(PASSED_KEY, Boolean.TRUE);
                session.removeAttribute(EXPECTED_KEY);
            } else {
                nextAnswer = newAnswer();
                session.setAttribute(EXPECTED_KEY, nextAnswer);
            }

            if (correct) {
                response.sendRedirect(request.getRequestURI());
            } else {
                showCaptcha(response, nextAnswer, true);
            }
            return;
        }

        chain.doFilter(request, response);
    }

    private int getOrCreateAnswer(HttpSession session) {
        Integer answer = (Integer) session.getAttribute(EXPECTED_KEY);
        if (answer == null) {
            answer = newAnswer();
            session.setAttribute(EXPECTED_KEY, answer);
        }
        return answer;
    }

    private int newAnswer() {
        return 100 + random.nextInt(900);
    }

    private void showCaptcha(HttpServletResponse response, int answer, boolean incorrect) throws IOException {
        String image = Base64.getEncoder().encodeToString(ImageUtils.toPng(Integer.toString(answer)));
        response.setContentType("text/html; charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        PrintWriter writer = response.getWriter();
        writer.write(String.format(CAPTCHA_PAGE, incorrect ? "" : " hidden", image, ANSWER_PARAMETER));
        writer.flush();
    }
}
