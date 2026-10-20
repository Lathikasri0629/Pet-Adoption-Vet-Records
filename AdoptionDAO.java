import java.sql.*;
import java.util.*;

public class AdoptionDAO {

    public int addAdopter(String name, String phone, String address) throws SQLException {
        String sql = "INSERT INTO Adopters (name, phone, address) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, address);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void adoptPet(int petId, int adopterId) throws SQLException, PetAlreadyAdoptedException {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                String status = null;
                try (PreparedStatement ps = con.prepareStatement("SELECT status FROM Pets WHERE pet_id = ?")) {
                    ps.setInt(1, petId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) status = rs.getString("status");
                    }
                }
                if (status == null) throw new SQLException("Pet ID " + petId + " not found.");
                if (status.equalsIgnoreCase("Adopted"))
                    throw new PetAlreadyAdoptedException("This pet is already adopted!");

                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Adoptions (pet_id, adopter_id, adoption_date) VALUES (?, ?, CURDATE())")) {
                    ps.setInt(1, petId);
                    ps.setInt(2, adopterId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE Pets SET status = 'Adopted' WHERE pet_id = ?")) {
                    ps.setInt(1, petId);
                    ps.executeUpdate();
                }
                con.commit();
            } catch (SQLException | PetAlreadyAdoptedException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public void addVaccination(int petId, String vaccine, String givenDate, String nextDue) throws SQLException {
        String sql = "INSERT INTO Vaccinations (pet_id, vaccine_name, given_date, next_due_date) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, petId);
            ps.setString(2, vaccine);
            ps.setDate(3, java.sql.Date.valueOf(givenDate));
            ps.setDate(4, java.sql.Date.valueOf(nextDue));
            ps.executeUpdate();
        }
    }

    public List<String> getVaccinesDue(int days) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "SELECT p.name, v.vaccine_name, v.next_due_date FROM Vaccinations v "
                + "JOIN Pets p ON v.pet_id = p.pet_id "
                + "WHERE v.next_due_date <= DATE_ADD(CURDATE(), INTERVAL ? DAY) "
                + "ORDER BY v.next_due_date";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, days);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("name") + " - " + rs.getString("vaccine_name")
                            + " due on " + rs.getDate("next_due_date"));
                }
            }
        }
        return result;
    }
}