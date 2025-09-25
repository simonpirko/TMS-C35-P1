package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/check-profile")
public class CheckProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User id is required");
            return;
        }

        try (Connection conn = PostgresConnector.getConnection()) {
            String sql = "SELECT a.id, a.username,a.password," +
                         "d.account_id, d.email, d.gender, d.bio, d.location, d.website, d.birth_date,d.avatar_url, d.header_url" +
                         " FROM accounts a" +
                         " JOIN account_details d ON a.id = d.account_id " +
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

                        java.sql.Date sqlDate = rs.getDate("birth_date");
                        LocalDate birthDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                        AccountDetails details = new AccountDetails(
                                rs.getInt("account_id"),
                                rs.getString("email"),
                                rs.getString("gender"),
                                rs.getString("bio"),
                                rs.getString("location"),
                                rs.getString("website"),
                                birthDate,
                                rs.getString("avatar_url"),
                                rs.getString("header_url")

                        );

                        String countSql = "SELECT COUNT(*) AS cnt FROM posts WHERE user_id = ?";
                        try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                            countStmt.setInt(1, account.id());
                            try (ResultSet countRs = countStmt.executeQuery()) {
                                if (countRs.next()) {
                                    req.setAttribute("postCount", countRs.getInt("cnt"));
                                }
                            }
                        }

                        String postSql = "SELECT id, title, content, created_at " +
                                "FROM posts " +
                                "WHERE user_id = ? " +
                                "ORDER BY created_at DESC";

                        try (PreparedStatement postStmt = conn.prepareStatement(postSql)) {
                            postStmt.setInt(1, account.id());

                            try (ResultSet postRs = postStmt.executeQuery()) {
                                List<Post> posts = new ArrayList<>();
                                while (postRs.next()) {
                                    Post post = new Post(
                                            postRs.getInt("id"),
                                            postRs.getString("title"),
                                            postRs.getString("content"),
                                            account.id(),
                                            postRs.getTimestamp("created_at")
                                             // user_id
                                    );
                                    posts.add(post);

                                }
                                req.setAttribute("posts", posts);
                                req.setAttribute("post_count", posts.size());
                            }
                        }

                        req.setAttribute("account", account);
                        req.setAttribute("accountDetails", details);


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
}