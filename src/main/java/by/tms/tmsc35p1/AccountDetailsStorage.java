package by.tms.tmsc35p1;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountDetailsStorage {

    public static final String DETAILS_INSERT_SQL =
            "INSERT INTO account_details (account_id, email,gender, bio, location, website, birth_date, avatar_url, header_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String DETAILS_UPDATE_SQL =
            "UPDATE account_details SET email=?,gender=?, bio=?, location=?, website=?, birth_date=?, avatar_url=?, header_url=? " +
                    "WHERE account_id=?";

    public static final String DETAILS_GET_SQL =
            "SELECT * FROM account_details WHERE account_id = ?";

    public static final String DETAILS_CHECK_SQL =
            "SELECT COUNT(*) FROM account_details WHERE account_id = ?";

    // Создать детали аккаунта
    public boolean createAccountDetails(AccountDetails details) {
        try {
            Connection conn = PostgresConnector.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(DETAILS_INSERT_SQL);

            stmt.setInt(1, details.accountId());
            setNullableString(stmt, 2, details.email());
            setNullableString(stmt, 3, details.gender());
            setNullableString(stmt, 4, details.bio());
            setNullableString(stmt, 5, details.location());
            setNullableString(stmt, 6, details.website());
            setNullableDate(stmt, 7, details.birthDate());
            setNullableString(stmt, 8, details.avatarUrl());
            setNullableString(stmt, 9, details.headerUrl());

            stmt.executeUpdate();
            conn.commit();
            conn.close();

            return true;
        } catch (SQLException e) {
            Logger.getLogger(AccountDetailsStorage.class.getName()).log(Level.SEVERE, "Failed to create account details", e);
            return false;
        }
    }

    // Обновить детали аккаунта
    public boolean updateAccountDetails(AccountDetails details) {
        try {
            Connection conn = PostgresConnector.getConnection();
            conn.setAutoCommit(false);

            // существуют ли уже детали
            PreparedStatement checkStmt = conn.prepareStatement(DETAILS_CHECK_SQL);
            checkStmt.setInt(1, details.accountId());
            ResultSet rs = checkStmt.executeQuery();

            PreparedStatement stmt;
            if (rs.next() && rs.getInt(1) > 0) {
                // Обновляем существующие
                stmt = conn.prepareStatement(DETAILS_UPDATE_SQL);
                setNullableString(stmt, 1, details.email());
                setNullableString(stmt, 2, details.gender());
                setNullableString(stmt, 3, details.bio());
                setNullableString(stmt, 4, details.location());
                setNullableString(stmt, 5, details.website());
                setNullableDate(stmt, 6, details.birthDate());
                setNullableString(stmt, 7, details.avatarUrl());
                setNullableString(stmt, 8, details.headerUrl());
                stmt.setInt(9, details.accountId());
            } else {
                // Создаем новые
                stmt = conn.prepareStatement(DETAILS_INSERT_SQL);
                stmt.setInt(1, details.accountId());
                setNullableString(stmt, 2, details.email());
                setNullableString(stmt, 3, details.gender());
                setNullableString(stmt, 4, details.bio());
                setNullableString(stmt, 5, details.location());
                setNullableString(stmt, 6, details.website());
                setNullableDate(stmt, 7, details.birthDate());
                setNullableString(stmt, 8, details.avatarUrl());
                setNullableString(stmt, 9, details.headerUrl());
            }

            int rowsAffected = stmt.executeUpdate();
            conn.commit();
            conn.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            Logger.getLogger(AccountDetailsStorage.class.getName()).log(Level.SEVERE, "Failed to update account details", e);
            return false;
        }
    }

    // Получить детали аккаунта
    public Optional<AccountDetails> getAccountDetails(Integer accountId) {
        try {
            Connection conn = PostgresConnector.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement stmt = conn.prepareStatement(DETAILS_GET_SQL);
            stmt.setInt(1, accountId);

            ResultSet rs = stmt.executeQuery();
            AccountDetails details = null;

            if (rs.next()) {
                details = new AccountDetails(
                        rs.getInt("account_id"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("bio"),
                        rs.getString("location"),
                        rs.getString("website"),
                        rs.getDate("birth_date") != null ? rs.getDate("birth_date").toLocalDate() : null,
                        rs.getString("avatar_url"),
                        rs.getString("header_url")
                );
            }

            conn.commit();
            conn.close();

            return Optional.ofNullable(details);
        } catch (SQLException e) {
            Logger.getLogger(AccountDetailsStorage.class.getName()).log(Level.SEVERE, "Failed to get account details", e);
            return Optional.empty();
        }
    }

    // Вспомогательные методы для работы с nullable значениями
    private void setNullableString(PreparedStatement stmt, int parameterIndex, String value) throws SQLException {
        if (value != null && !value.trim().isEmpty()) {
            stmt.setString(parameterIndex, value);
        } else {
            stmt.setNull(parameterIndex, Types.VARCHAR);
        }
    }

    private void setNullableDate(PreparedStatement stmt, int parameterIndex, LocalDate value) throws SQLException {
        if (value != null) {
            stmt.setDate(parameterIndex, Date.valueOf(value));
        } else {
            stmt.setNull(parameterIndex, Types.DATE);
        }
    }
}