package HospitalManagementProject;

import java.sql.*;

public class Doctor
{
    private int id;
    private String name;
    private String specialty;

    public Doctor(String name, String speciality,Connection con) {

        try
        {
            String query = "INSERT INTO doctors(name,speciality) VALUES (?,?)";
            PreparedStatement ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,name);
            ps.setString(2,speciality);
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
        this.specialty = speciality;
    }

    public Doctor(int targetId,String name, String speciality)
    {
        this.name = name;
        this.specialty = speciality;
        this.id = targetId;
    }
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Doctor ID: " + id + ", Name: " + name + ", Specialty: " + specialty;
    }
}
