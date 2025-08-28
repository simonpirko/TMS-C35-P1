package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/login")
public class LogInServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();

        String username = req.getParameter("username");

        String password = req.getParameter("password");

        PostgreSQLService postgreSQLService = new PostgreSQLService();

        Optional<Account> account = postgreSQLService.getAccount(username, password);
        if (account.isPresent()) {
            Account account1 = account.get();
            HttpSession session = req.getSession();
            session.setAttribute("account", account1);
            req.setAttribute("account", account1);
            resp.sendRedirect("/");
        }
        else {
            errors.add("Invalid username or password");
            req.setAttribute("errors", errors);
            req.getServletContext().getRequestDispatcher("/pages/login.jsp").forward(req, resp);
        }
    }
}
