package by.tms.tmsc35p1;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/post/add-like")
public class PostLikeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int postId = Integer.parseInt(request.getParameter("id")); // имя параметра должно совпадать с формой

        try (Connection conn = PostgresConnector.getConnection()) {
            PostDAO dao = new PostDAO(conn);
            dao.likePost(postId);

            request.setAttribute("posts", dao.getAllPostsSortedByDate());
            request.getRequestDispatcher("/index.jsp").forward(request, response);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при обработке лайка");
            e.printStackTrace();
        }
    }
}