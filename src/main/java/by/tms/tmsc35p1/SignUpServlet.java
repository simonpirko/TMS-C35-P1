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


@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/pages/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();

        AccountStorage accountStorage = new AccountStorage();

        String username = req.getParameter("username");

        String password = req.getParameter("password");

        Account account = new Account(null, username, password);

        if (username.isBlank()) {
            errors.add("Username is empty.");
        }
        if (password.isBlank()) {
            errors.add("Password is empty.");
        }

        String confirmPassword = req.getParameter("confirmPassword");
        if (!password.equals(confirmPassword)) {
            errors.add("Passwords do not match.");
        }

        if (!accountStorage.isUnique(account)){
            errors.add("Account is already exists.");
        }

        if(!password.equals(confirmPassword) || username.isBlank() || password.isBlank() || !accountStorage.isUnique(account))
        {
            errors.add("Failed to create account!");
            req.setAttribute("errors", errors);
            req.getServletContext().getRequestDispatcher("/pages/signup.jsp").forward(req, resp);
        }
        else {
            if (accountStorage.addAccount(account)) {
                HttpSession session = req.getSession();
                session.setAttribute("account", account);
                resp.sendRedirect("/");
            }
            else {
                errors.add("Failed to add account to the database!");
                req.setAttribute("errors", errors);
                req.getServletContext().getRequestDispatcher("/pages/signup.jsp").forward(req, resp);
            }
        }


    }
}
