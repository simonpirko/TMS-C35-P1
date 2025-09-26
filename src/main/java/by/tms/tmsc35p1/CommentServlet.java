package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("account");
        String commentAuthor = account.username();
        int user_id = account.id();

        String commentText = req.getParameter("commentText"); // текст комментария
        int postId = Integer.parseInt(req.getParameter("postId"));

        String avatarUrl = null;
        try {
            Connection conn = PostgresConnector.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT avatar_url FROM account_details WHERE account_id = ?");
            preparedStatement.setInt(1, user_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                avatarUrl = rs.getString("avatar_url");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        CommentStorage.save(postId, commentAuthor, commentText, avatarUrl);

        String referer = req.getHeader("Referer");
        resp.sendRedirect(referer != null ? referer : req.getContextPath() + "/");


    }
}
