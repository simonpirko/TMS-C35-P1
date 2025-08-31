package by.tms.tmsc35p1;

import java.time.LocalDateTime;

public record Post(int id, String title, String content, LocalDateTime created_at) {
}
