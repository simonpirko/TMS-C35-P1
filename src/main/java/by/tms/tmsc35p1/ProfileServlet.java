package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final AccountDetailsStorage detailsStorage = new AccountDetailsStorage();
    private final AccountStorage accountStorage = new AccountStorage();
    FollowService followService = new FollowService(new FollowRepository());
    private PostStorage postStorage;
    private CommentStorage commentStorage;

    public void init() {
        DataSource ds = (DataSource) getServletContext().getAttribute("ds");
        this.postStorage = new PostStorage(ds);
        this.commentStorage = new CommentStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("account");

        String idParam = req.getParameter("id");
        int userId;

        if (idParam != null) {
            userId = Integer.parseInt(idParam); // чужой профиль
        } else if (account != null) {
            userId = account.id(); // свой профиль
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            List<Post> posts = postStorage.findByUserId(userId);
            req.setAttribute("posts", posts);

            List<Comment> comments = commentStorage.getAllComments();
            req.setAttribute("comments", comments);

            Optional<AccountDetails> detailsOpt = detailsStorage.getAccountDetails(userId);
            if (detailsOpt.isPresent()) {
                req.setAttribute("accountDetails", detailsOpt.get());
            } else {
                req.setAttribute("accountDetails", new AccountDetails(userId));
            }

            Optional<Account> accountOpt = accountStorage.getAccountById(userId);
            accountOpt.ifPresent(acc -> req.setAttribute("account", acc));

            req.setAttribute("postCount", posts.size());
            req.setAttribute("followingCount", 0); //у
            req.setAttribute("followersCount", 0);
            req.setAttribute("followersCount", followService.getFollowersCount(userId));
            req.setAttribute("followingCount", followService.getFollowingCount(userId));

            req.getServletContext().getRequestDispatcher("/pages/profile.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
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