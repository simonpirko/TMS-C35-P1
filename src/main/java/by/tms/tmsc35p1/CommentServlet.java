package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commentAuthor = req.getParameter("commentAuthor"); // автор комментария
        String postId = req.getParameter("postId"); // название прокомментированного поста
        String commentText = req.getParameter("commentText"); // текст комментария

        int convertedPostId = Integer.parseInt(postId);

        CommentStorage.save(commentAuthor, convertedPostId , commentText);

        resp.sendRedirect(req.getContextPath() + "/");







    }
}
