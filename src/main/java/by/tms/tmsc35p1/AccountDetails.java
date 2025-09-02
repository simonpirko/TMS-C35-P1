package by.tms.tmsc35p1;

import java.time.LocalDate;

public record AccountDetails(
        Integer accountId,
        String email,
        String bio,
        String location,
        String website,
        LocalDate birthDate,
        String avatarUrl,
        String headerUrl
) {
    public AccountDetails {
        // может позже добавлю валидацию.. :3
    }

    public AccountDetails(Integer accountId) {
        this(accountId, null, null, null, null, null, null, null);
    }
}
