package com.github.martinslavov.dao;

import com.github.martinslavov.config.DatabaseConnection;
import com.github.martinslavov.model.Fine;
import com.github.martinslavov.model.enums.FineStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FineDAO {

    public Optional<Fine> save(Fine fine) {

        String sql = "INSERT INTO fines (loan_id, amount, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psInsertFine = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            psInsertFine.setInt(1, fine.getLoanId());
            psInsertFine.setBigDecimal(2, fine.getAmount());
            psInsertFine.setString(3, fine.getStatus().name());

            psInsertFine.executeUpdate();

            ResultSet generatedKey = psInsertFine.getGeneratedKeys();
            if (generatedKey.next()) {
                fine.setFineId(generatedKey.getInt(1));
            }
            return Optional.of(fine);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Fine> findAll() {

        String sql = "SELECT * FROM fines";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindAll = conn.prepareStatement(sql)
        ) {
            ResultSet rs = psFindAll.executeQuery();
            List<Fine> allFines = new ArrayList<>();
            while (rs.next()) {
                allFines.add(mapResultSetToFine(rs));
            }
            return allFines;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Fine> findById(int id) {

        String sql = "SELECT * FROM fines WHERE fine_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindById = conn.prepareStatement(sql)
        ) {
            psFindById.setInt(1, id);

            ResultSet rs = psFindById.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToFine(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Fine> findByLoanId(int loanId) {

        String sql = "SELECT * FROM fines WHERE loan_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByLoanId = conn.prepareStatement(sql)
        ) {
            psFindByLoanId.setInt(1, loanId);

            ResultSet rs = psFindByLoanId.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToFine(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Fine> findByStatus(FineStatus status) {

        String sql = "SELECT * FROM fines WHERE status = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByStatus = conn.prepareStatement(sql)
        ) {
            psFindByStatus.setString(1, status.name());

            ResultSet rs = psFindByStatus.executeQuery();
            List<Fine> finesByStatus = new ArrayList<>();
            while (rs.next()) {
                finesByStatus.add(mapResultSetToFine(rs));
            }
            return finesByStatus;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Fine fine) {

        String sql = "UPDATE fines SET loan_id = ?, amount = ?, status = ? WHERE fine_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psUpdateFine = conn.prepareStatement(sql)
        ) {
            psUpdateFine.setInt(1, fine.getLoanId());
            psUpdateFine.setBigDecimal(2, fine.getAmount());
            psUpdateFine.setString(3, fine.getStatus().name());
            psUpdateFine.setInt(4, fine.getFineId());

            return psUpdateFine.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(int id) {

        String sql = "DELETE FROM fines WHERE fine_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psDeleteFine = conn.prepareStatement(sql)
        ) {
            psDeleteFine.setInt(1, id);
            return psDeleteFine.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Fine mapResultSetToFine(ResultSet rs) throws SQLException {
        return new Fine(
                rs.getInt("fine_id"),
                rs.getInt("loan_id"),
                rs.getBigDecimal("amount"),
                FineStatus.valueOf(rs.getString("status"))
        );
    }
}
