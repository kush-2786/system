package HospitalManagementProject;

import java.sql.*;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String gender;

    public Patient(String name, int age, String gender,Connection con) {
        try
        {
            String query = "INSERT INTO patients(name,age,gender) VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,name);
            ps.setInt(2,age);
            ps.setString(3,gender);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
            {
                this.id = rs.getInt(1);   // database ID
            }
            rs.close();
            ps.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        this.name = name;
        this.age = age;
        this.gender = gender;
    }
    public Patient(int targetId,String name, int age, String gender)
    {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.id = targetId;
    }
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Patient ID: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender;
    }
}
