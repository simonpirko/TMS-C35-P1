package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/pages/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account account;

        String username = req.getParameter("username");

        String password = req.getParameter("password");

        String confirmPassword = req.getParameter("confirmPassword");

        if(!password.equals(confirmPassword)){
            req.setAttribute("error", "Passwords do not match");
        }
        else {
            account = new Account(username, password);
            PostgreSQLService postgreSQLService = new PostgreSQLService();
            String message = postgreSQLService.addAccount(account);
        }

        req.getServletContext().getRequestDispatcher("/pages/signup.jsp").forward(req, resp);
    }
}
