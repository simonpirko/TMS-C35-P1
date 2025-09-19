package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

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