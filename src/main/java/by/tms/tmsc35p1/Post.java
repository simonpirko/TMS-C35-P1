package by.tms.tmsc35p1;

import java.time.LocalDateTime;

public record Post(int id, String title, String content, int userId, java.sql.Timestamp timestamp,int likes) {

    public void setId(int id) {
    }

    public void setContent(String content) {
    }

    public void setCreatedAt(LocalDateTime createdAt) {

    }
    public void setLikes(int likes) {
    }

}
