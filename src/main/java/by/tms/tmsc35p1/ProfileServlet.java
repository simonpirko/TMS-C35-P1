package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final AccountDetailsStorage detailsStorage = new AccountDetailsStorage();
    private final AccountStorage accountStorage = new AccountStorage();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account != null) {
            // Загружаем детали профиля
            Optional<AccountDetails> detailsOpt = detailsStorage.getAccountDetails(account.id());

            if (detailsOpt.isPresent()) {
                AccountDetails details = detailsOpt.get();
                req.setAttribute("accountDetails", details);
            } else {
                // Создаем пустые детали если их нет
                req.setAttribute("accountDetails", new AccountDetails(account.id()));
            }

            req.setAttribute("account", account);
            req.setAttribute("postCount", 0);
            req.setAttribute("followingCount", 0);
            req.setAttribute("followersCount", 0);

            req.getServletContext().getRequestDispatcher("/pages/profile.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account != null) {
            String action = req.getParameter("action");

            if ("updateProfile".equals(action)) {
                updateProfileDetails(req, resp, session, account);
            } else if ("updateUsername".equals(action)) {
                updateUsername(req, resp, session, account);
            } else if ("updatePassword".equals(action)) {
                updatePassword(req, resp, session, account);
            } else {
                session.setAttribute("error", "Invalid action");
                resp.sendRedirect(req.getContextPath() + "/profile");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    private void updateProfileDetails(HttpServletRequest req, HttpServletResponse resp,
                                      HttpSession session, Account account) throws IOException {
        // Получаем текущие детали профиля
        Optional<AccountDetails> currentDetailsOpt = detailsStorage.getAccountDetails(account.id());
        AccountDetails currentDetails = currentDetailsOpt.orElse(new AccountDetails(account.id()));

        // Получаем данные из формы
        String email = req.getParameter("email");
        String gender = req.getParameter("gender");
        String bio = req.getParameter("bio");
        String location = req.getParameter("location");
        String website = req.getParameter("website");
        String birthDateStr = req.getParameter("birthDate");

        LocalDate birthDate = null;
        if (birthDateStr != null && !birthDateStr.isEmpty()) {
            birthDate = LocalDate.parse(birthDateStr);
        }

        // Сохраняем
        AccountDetails details = new AccountDetails(
                account.id(),
                email,
                gender,
                bio,
                location,
                website,
                birthDate,
                currentDetails.avatarUrl(),
                currentDetails.headerUrl()
        );


        // Сохраняем в базу
        if (detailsStorage.updateAccountDetails(details)) {
            session.setAttribute("success", "Profile updated successfully");
        } else {
            session.setAttribute("error", "Failed to update profile");
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }

    private void updateUsername(HttpServletRequest req, HttpServletResponse resp,
                                HttpSession session, Account account) throws IOException {
        String newUsername = req.getParameter("newUsername");
        String currentPassword = req.getParameter("currentPassword");

        if (newUsername == null || newUsername.trim().isEmpty()) {
            session.setAttribute("error", "Username cannot be empty");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            session.setAttribute("error", "Current password is required");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        // Проверяем текущий пароль
        Optional<Account> currentAccount = accountStorage.getAccountByUsername(account.username(), currentPassword);
        if (!currentAccount.isPresent()) {
            session.setAttribute("error", "Current password is incorrect");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        // Проверяем, уникален ли новый username
        if (!accountStorage.isUsernameUnique(newUsername, account.id())) {
            session.setAttribute("error", "Username is already taken");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        // Обновляем username
        if (accountStorage.updateUsername(account.id(), newUsername)) {
            // Обновляем сессию
            Account updatedAccount = new Account(account.id(), newUsername, account.password());
            session.setAttribute("account", updatedAccount);
            session.setAttribute("success", "Username updated successfully");
        } else {
            session.setAttribute("error", "Failed to update username");
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }

    private void updatePassword(HttpServletRequest req, HttpServletResponse resp,
                                HttpSession session, Account account) throws IOException {
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            session.setAttribute("error", "Current password is required");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            session.setAttribute("error", "New password cannot be empty");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("error", "New passwords do not match");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        // Проверяем текущий пароль
        Optional<Account> currentAccount = accountStorage.getAccountByUsername(account.username(), currentPassword);
        if (!currentAccount.isPresent()) {
            session.setAttribute("error", "Current password is incorrect");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        // Обновляем пароль
        if (accountStorage.updatePassword(account.id(), newPassword)) {
            session.setAttribute("success", "Password updated successfully");
        } else {
            session.setAttribute("error", "Failed to update password");
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}