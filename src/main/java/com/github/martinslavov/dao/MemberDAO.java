package com.github.martinslavov.dao;

import com.github.martinslavov.config.DatabaseConnection;
import com.github.martinslavov.exception.DatabaseException;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.model.enums.MemberStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberDAO {

    public Optional<Member> save(Member member) {

        String sql = "INSERT INTO members (first_name, last_name, phone, email, start_date, expire_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psInsertMember = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            psInsertMember.setString(1, member.getFirstName());
            psInsertMember.setString(2, member.getLastName());
            if (member.getPhone() != null) {
                psInsertMember.setString(3, member.getPhone());
            } else {
                psInsertMember.setNull(3, Types.VARCHAR);
            }
            psInsertMember.setString(4, member.getEmail());
            psInsertMember.setDate(5, java.sql.Date.valueOf(member.getStartDate()));
            psInsertMember.setDate(6, java.sql.Date.valueOf(member.getExpireDate()));
            psInsertMember.setString(7, member.getStatus().name());

            psInsertMember.executeUpdate();

            ResultSet generatedKey = psInsertMember.getGeneratedKeys();
            if (generatedKey.next()) {
                member.setMemberId(generatedKey.getInt(1));
            }

            return Optional.of(member);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save member", e);
        }
    }

    public List<Member> findAll() {

        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindAll = conn.prepareStatement(sql)
        ) {
            ResultSet rs = psFindAll.executeQuery();
            List<Member> allMembers = new ArrayList<>();
            while (rs.next()) {
                allMembers.add(mapResultSetToMember(rs));
            }
            return allMembers;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve all members", e);
        }
    }

    public Optional<Member> findById(int id) {

        String sql = "SELECT * FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindById = conn.prepareStatement(sql)
        ) {
            psFindById.setInt(1, id);

            ResultSet rs = psFindById.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find member by ID", e);
        }
    }

    public List<Member> findByFirstName(String firstName) {

        String sql = "SELECT * FROM members WHERE first_name LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByFirstName = conn.prepareStatement(sql)
        ) {
            psFindByFirstName.setString(1, "%" + firstName + "%");
            ResultSet rs = psFindByFirstName.executeQuery();
            List<Member> membersByFirstName = new ArrayList<>();
            while (rs.next()) {
                membersByFirstName.add(mapResultSetToMember(rs));
            }
            return membersByFirstName;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find member by first name", e);
        }
    }

    public List<Member> findByLastName(String lastName) {

        String sql = "SELECT * FROM members WHERE last_name LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByLastName = conn.prepareStatement(sql)
        ) {
            psFindByLastName.setString(1, "%" + lastName + "%");
            ResultSet rs = psFindByLastName.executeQuery();
            List<Member> membersByLastName = new ArrayList<>();
            while (rs.next()) {
                membersByLastName.add(mapResultSetToMember(rs));
            }
            return membersByLastName;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find member by last name", e);
        }
    }

    public Optional<Member> findByPhone(String phone) {

        String sql = "SELECT * FROM members WHERE phone = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByPhone = conn.prepareStatement(sql)
        ) {
            psFindByPhone.setString(1, phone);
            ResultSet rs = psFindByPhone.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find member by phone", e);
        }
    }

    public Optional<Member> findByEmail(String email) {

        String sql = "SELECT * FROM members WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByEmail = conn.prepareStatement(sql)
        ) {
            psFindByEmail.setString(1, email);
            ResultSet rs = psFindByEmail.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find member by email", e);
        }
    }

    public List<Member> findByStatus(MemberStatus status) {

        String sql = "SELECT * FROM members WHERE status = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByStatus = conn.prepareStatement(sql)
        ) {
            psFindByStatus.setString(1, status.name());
            ResultSet rs = psFindByStatus.executeQuery();
            List<Member> membersByStatus = new ArrayList<>();
            while (rs.next()) {
                membersByStatus.add(mapResultSetToMember(rs));
            }
            return membersByStatus;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find member by status", e);
        }
    }

    public boolean update(Member member) {

        String sql = "UPDATE members SET first_name = ?, last_name = ?, phone = ?, email = ?, start_date = ?, expire_date = ?, status = ? WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psUpdateMember = conn.prepareStatement(sql)
        ) {
            psUpdateMember.setString(1, member.getFirstName());
            psUpdateMember.setString(2, member.getLastName());
            if (member.getPhone() != null) {
                psUpdateMember.setString(3, member.getPhone());
            } else {
                psUpdateMember.setNull(3, Types.VARCHAR);
            }
            psUpdateMember.setString(4, member.getEmail());
            psUpdateMember.setDate(5, java.sql.Date.valueOf(member.getStartDate()));
            psUpdateMember.setDate(6, java.sql.Date.valueOf(member.getExpireDate()));
            psUpdateMember.setString(7, member.getStatus().name());
            psUpdateMember.setInt(8, member.getMemberId());

            return psUpdateMember.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update member", e);
        }
    }

    public boolean delete(int id) {

        String sql = "DELETE FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psDeleteMember = conn.prepareStatement(sql)
        ) {
            psDeleteMember.setInt(1, id);
            return psDeleteMember.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete member", e);
        }
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("member_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("expire_date").toLocalDate(),
                MemberStatus.valueOf(rs.getString("status"))
        );
    }
}
