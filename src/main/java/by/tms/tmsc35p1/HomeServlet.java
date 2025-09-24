package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/")
public class HomeServlet extends HttpServlet {
    PostDAO postDAO;

    FollowService followService;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        if(req.getSession().getAttribute("account") == null){
            req.getServletContext().getRequestDispatcher("/pages/home.jsp").forward(req, res);
        }

        Account account = (Account) req.getSession().getAttribute("account");

        Integer userId = account.id();

        try {
            List<Integer> followingUsersIds = followService.getFollowingUsersIds(userId);
            for(Integer followingUsersId : followingUsersIds){
                postDAO.getAllPostsByUserIdSortedByDate(followingUsersId).forEach(post -> {
                    req.setAttribute("postId", post.id());
                    req.setAttribute("postTitle", post.title());
                    req.setAttribute("postContent", post.content());
                });

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        req.getServletContext().getRequestDispatcher("/pages/home.jsp").forward(req, res);

    }


}
