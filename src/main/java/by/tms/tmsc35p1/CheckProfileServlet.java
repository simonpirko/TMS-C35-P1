package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;

import java.io.IOException;
import java.sql.*;

@WebServlet("/check-profile")
public class CheckProfileServlet extends HttpServlet {
    private CommentStorage commentStorage;

    public void init(){
        this.commentStorage = new CommentStorage();
    }

    FollowService followService = new FollowService(new FollowRepository());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Comment> comments = commentStorage.getAllComments();
        req.setAttribute("comments", comments);

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User id is required");
            return;
        }

        try (Connection conn = PostgresConnector.getConnection()) {
            String sql = "SELECT a.id, a.username,a.password," +
                         "d.account_id, d.email, d.bio, d.location, d.website, d.birth_date,d.avatar_url, d.header_url " +
                         "FROM accounts a " +
                         "JOIN account_details d ON a.id = d.account_id " +
                         "WHERE a.id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(idParam));

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Account account = new Account(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("password") // не хотел добавлять, но ругается на то что его нет
                        );


                        AccountDetails details = new AccountDetails(
                                rs.getInt("account_id"),
                                rs.getString("email"),
                                rs.getString("bio"),
                                rs.getString("location"),
                                rs.getString("website"),
                                rs.getDate("birth_date").toLocalDate(),
                                rs.getString("avatar_url"),
                                rs.getString("header_url")
                        );

                        int followersCount = followService.getFollowersCount(Integer.parseInt(idParam));
                        int followingCount = followService.getFollowingCount(Integer.parseInt(idParam));

                        Account currentUser = (Account) req.getSession().getAttribute("account");
                        boolean isFollowing = false;

                        if (currentUser != null) {
                            isFollowing = followService.checkIfFollowing(currentUser.id(), Integer.parseInt(idParam));
                        }
                      
                        req.setAttribute("account", account);
                        req.setAttribute("accountDetails", details);
                        req.setAttribute("followersCount", followersCount);
                        req.setAttribute("followingCount", followingCount);
                        req.setAttribute("isFollowing", isFollowing);
                        req.setAttribute("currentUser", currentUser);


                        req.getRequestDispatcher("/pages/profile.jsp").forward(req, resp);
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    }
                }
            }

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Account currentUser = (Account) req.getSession().getAttribute("account");

        if (currentUser == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in to follow users");
            return;
        }

        String action = req.getParameter("action");
        String profileIdParam = req.getParameter("profileId");

        if (action == null || profileIdParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action and profileId are required");
            return;
        }

        try (Connection conn = PostgresConnector.getConnection()) {
            int profileId = Integer.parseInt(profileIdParam);
            int currentUserId = currentUser.id();

            if (currentUserId == profileId) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You cannot follow yourself");
                return;
            }

            boolean success = false;

            if ("follow".equals(action)) {
                success = followService.follow(currentUserId, profileId);
            } else if ("unfollow".equals(action)) {
                success = followService.unfollow(currentUserId, profileId);
            }

            if (success) {
                resp.sendRedirect("/check-profile?id=" + profileId);
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Operation failed");
            }

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid profile id format");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}