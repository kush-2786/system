package HospitalManagementProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Appointment
{
    private Patient patient;
    private Doctor doctor;
    private String date;

    public Appointment(Patient patient, Doctor doctor, String date,Connection con)
    {
        try
        {
            String query = "INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,patient.getId());
            ps.setInt(2,doctor.getId());
            ps.setString(3,date);
            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
    }
    @Override
    public String toString()
    {
        return "Appointment: [Patient: " + patient + ", Doctor: " + doctor + ", Date: " + date + "]";
    }
}
