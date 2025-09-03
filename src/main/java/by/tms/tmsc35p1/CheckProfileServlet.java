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
    final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    final String DB_USER = "postgres";
    final String DB_PASSWORD = "root";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User id is required");
            return;
        }
        try (Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD)){
            PreparedStatement stmt = connection.prepareStatement("SELECT id, username FROM accounts WHERE id = ?");
            stmt.setInt(1,Integer.parseInt(idParam));

            try (ResultSet rs= stmt.executeQuery()){
                if (rs.next()){
                    req.setAttribute("id",rs.getInt("id"));
                    req.setAttribute("username", rs.getString("username"));

                    req.getRequestDispatcher("/pages/CheckProfile.jsp").forward(req, resp);
                }else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                }
            }
        }catch (NumberFormatException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id format");
        }catch (SQLException e){
            throw new ServletException("Database error", e);
        }
    }
}
