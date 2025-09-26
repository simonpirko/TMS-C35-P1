package by.tms.tmsc35p1;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        HttpSession session = req.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("account") != null);
//        boolean loginRequest = path.equals(req.getContextPath() + "/login");
//        boolean homePage = path.equals(req.getContextPath() + "/");

        if (AuthExclusions.isAuthExclusion(path)) {
            chain.doFilter(request, response);
            return;
        }
        if (!loggedIn) {
            req.getSession(true).setAttribute("redirect", path);
            resp.sendRedirect(req.getContextPath() + "/login?error=auth");
            return;
        }
        chain.doFilter(request, response);
    }
}
