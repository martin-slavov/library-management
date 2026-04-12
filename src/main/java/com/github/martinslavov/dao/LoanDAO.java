package com.github.martinslavov.dao;

import com.github.martinslavov.config.DatabaseConnection;
import com.github.martinslavov.exception.DatabaseException;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.model.enums.LoanStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanDAO {

    public Optional<Loan> save(Loan loan) {

        String sql = "INSERT INTO loans (book_id, member_id, start_date, end_date, return_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psInsertLoan = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            psInsertLoan.setInt(1, loan.getBookId());
            psInsertLoan.setInt(2, loan.getMemberId());
            psInsertLoan.setDate(3, java.sql.Date.valueOf(loan.getStartDate()));
            psInsertLoan.setDate(4, java.sql.Date.valueOf(loan.getEndDate()));
            if (loan.getReturnDate() != null) {
                psInsertLoan.setDate(5, java.sql.Date.valueOf(loan.getReturnDate()));
            } else {
                psInsertLoan.setNull(5, Types.DATE);
            }
            psInsertLoan.setString(6, loan.getStatus().name());

            psInsertLoan.executeUpdate();

            ResultSet generatedKey = psInsertLoan.getGeneratedKeys();
            if (generatedKey.next()) {
                loan.setLoanId(generatedKey.getInt(1));
            }
            return Optional.of(loan);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save loan", e);
        }
    }

    public List<Loan> findAll() {

        String sql = "SELECT * FROM loans";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindAll = conn.prepareStatement(sql)
        ) {
            ResultSet rs = psFindAll.executeQuery();
            List<Loan> allLoans = new ArrayList<>();
            while (rs.next()) {
                allLoans.add(mapResultSetToLoan(rs));
            }
            return allLoans;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve all loans", e);
        }
    }

    public Optional<Loan> findById(int id) {

        String sql = "SELECT * FROM loans WHERE loan_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindById = conn.prepareStatement(sql)
        ) {
            psFindById.setInt(1, id);

            ResultSet rs = psFindById.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToLoan(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find loan by ID", e);
        }
    }

    public List<Loan> findByBookId(int bookId) {

        String sql = "SELECT * FROM loans WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByBookId = conn.prepareStatement(sql)
        ) {
            psFindByBookId.setInt(1, bookId);
            ResultSet rs = psFindByBookId.executeQuery();
            List<Loan> loanByBookId = new ArrayList<>();
            while (rs.next()) {
                loanByBookId.add(mapResultSetToLoan(rs));
            }
            return loanByBookId;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find loan by book ID", e);
        }
    }

    public List<Loan> findByMemberId(int memberId) {

        String sql = "SELECT * FROM loans WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByMemberId = conn.prepareStatement(sql)
        ) {
            psFindByMemberId.setInt(1, memberId);
            ResultSet rs = psFindByMemberId.executeQuery();
            List<Loan> loanByMemberId = new ArrayList<>();
            while (rs.next()) {
                loanByMemberId.add(mapResultSetToLoan(rs));
            }
            return loanByMemberId;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find loan by member ID", e);
        }
    }

    public List<Loan> findByStatus(LoanStatus status) {

        String sql = "SELECT * FROM loans WHERE status = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByStatus = conn.prepareStatement(sql)
        ) {
            psFindByStatus.setString(1, status.name());
            ResultSet rs = psFindByStatus.executeQuery();
            List<Loan> loanByStatus = new ArrayList<>();
            while (rs.next()) {
                loanByStatus.add(mapResultSetToLoan(rs));
            }
            return loanByStatus;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find loan by status", e);
        }
    }

    public int borrowBook(int memberId, int bookId) {

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             CallableStatement csBorrowBook = conn.prepareCall("{CALL borrow_book(?, ?, ?)}")
        ) {
            csBorrowBook.setInt(1, memberId);
            csBorrowBook.setInt(2, bookId);
            csBorrowBook.registerOutParameter(3, Types.INTEGER);

            csBorrowBook.execute();
            return csBorrowBook.getInt(3);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to execute borrow book procedure", e);
        }
    }

    public int returnBook(int loanId) {

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             CallableStatement csReturnBook = conn.prepareCall("{CALL return_book(?, ?)}")
        ) {
            csReturnBook.setInt(1, loanId);
            csReturnBook.registerOutParameter(2, Types.INTEGER);

            csReturnBook.execute();
            return csReturnBook.getInt(2);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to execute return book procedure", e);
        }
    }

    public void calculateFine(int loanId) {

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             CallableStatement csCalculateFine = conn.prepareCall("{CALL calculate_fine(?)}")
        ) {
            csCalculateFine.setInt(1, loanId);
            csCalculateFine.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to execute calculate fine procedure", e);
        }
    }

    public List<Loan> getMemberLoanHistory(int memberId) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             CallableStatement cs = conn.prepareCall("{CALL get_member_loan_history(?)}")) {

            cs.setInt(1, memberId);
            ResultSet rs = cs.executeQuery();
            List<Loan> loans = new ArrayList<>();
            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("loan_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                        LoanStatus.valueOf(rs.getString("loan_status"))
                ));
            }
            return loans;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to execute get member loan history procedure", e);
        }
    }

    public boolean updateStatus(Loan loan, LoanStatus status) {

        String sql = "UPDATE loans SET status = ? WHERE loan_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psUpdateLoan = conn.prepareStatement(sql)
        ) {
            psUpdateLoan.setString(1, status.name());
            psUpdateLoan.setInt(2, loan.getLoanId());

            return psUpdateLoan.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update loan status", e);
        }
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        java.sql.Date returnDate = rs.getDate("return_date");
        return new Loan(
                rs.getInt("loan_id"),
                rs.getInt("book_id"),
                rs.getInt("member_id"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                returnDate != null ? returnDate.toLocalDate() : null,
                LoanStatus.valueOf(rs.getString("status"))
        );
    }
}
