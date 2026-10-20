import java.sql.*;
import java.util.*;

public class PetDAO {

    // CREATE
    public void addPet(String name, String species, String breed, int age) throws SQLException {
        String sql = "INSERT INTO Pets (name, species, breed, age) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, species);
            ps.setString(3, breed);
            ps.setInt(4, age);
            ps.executeUpdate();
        }
    }

    // READ
    public List<Pet> getAllPets() throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM Pets";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("pet_id");
                String name = rs.getString("name");
                String species = rs.getString("species");
                String breed = rs.getString("breed");
                int age = rs.getInt("age");
                String status = rs.getString("status");
                if (species.equalsIgnoreCase("Dog")) {
                    pets.add(new Dog(id, name, breed, age, status));
                } else if (species.equalsIgnoreCase("Cat")) {
                    pets.add(new Cat(id, name, breed, age, status));
                } else {
                    pets.add(new Bird(id, name, breed, age, status));
                }
            }
        }
        return pets;
    }

    // UPDATE
    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE Pets SET status = ? WHERE pet_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // DELETE
    public void deletePet(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM Vaccinations WHERE pet_id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM Adoptions WHERE pet_id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM Pets WHERE pet_id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }
}