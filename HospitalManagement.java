package HospitalManagementProject;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HospitalManagement {

    private static ArrayList<Patient> patients = new ArrayList<>();
    private static ArrayList<Doctor> doctors = new ArrayList<>();
    private static ArrayList<Appointment> appointments = new ArrayList<>();

    private static final String url = "jdbc:mysql://127.0.0.1:3306/Hospital";
    private static final String user = "root";
    private static final String passwd = "yourPasswordHere";

    public static void main(String[] args) {

        Connection con = null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }

        try
        {
            con = DriverManager.getConnection(url,user,passwd);
            Statement st = con.createStatement();
            st.execute("use Hospital");
            st.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Hospital Management System");
            System.out.println("1. Add Patient");
            System.out.println("2. Add Doctor");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. View Patients");
            System.out.println("5. View Doctors");
            System.out.println("6. View Appointments");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = intInput(scanner);
            switch (choice) {
                case 1:
                    addPatient(scanner,con);
                    break;
                case 2:
                    addDoctor(scanner,con);
                    break;
                case 3:
                    scheduleAppointment(scanner,con);
                    break;
                case 4:
                    viewPatients(con);
                    break;
                case 5:
                    viewDoctors(con);
                    break;
                case 6:
                    viewAppointments(con);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("Press any key to continue...");
            try {System.in.read();} catch (IOException e) {}

        } while (choice != 0);

        scanner.close();
        try       // closing connection
        {
            con.close();
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }

    private static void addPatient(Scanner scanner,Connection con) {
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Patient Age: ");
        int age = intInput(scanner);
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.nextLine();

        Patient patient = new Patient(name, age, gender,con);
        patients.add(patient);
        System.out.println("Patient added successfully!");
    }

    private static void addDoctor(Scanner scanner,Connection con) {
        System.out.print("Enter Doctor Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Doctor Speciality: ");
        String speciality = scanner.nextLine();

        Doctor doctor = new Doctor(name, speciality,con);
        doctors.add(doctor);
        System.out.println("Doctor added successfully!");
    }

    private static void scheduleAppointment(Scanner scanner,Connection con) {
        System.out.print("Enter Patient ID: ");
        int patientId = intInput(scanner);
        System.out.print("Enter Doctor ID: ");
        int doctorId = intInput(scanner);
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        Patient patient = findPatientById(patientId,con);
        Doctor doctor = findDoctorById(doctorId,con);

        if (patient != null && doctor != null) {
            Appointment appointment = new Appointment(patient, doctor, date,con);
            appointments.add(appointment);
            System.out.println("Appointment scheduled successfully!");
        } else {
            System.out.println("Invalid Patient ID or Doctor ID.");
        }
    }

    private static void viewPatients(Connection con) {
        System.out.println("List of Patients:");
        System.out.println("id\t" + "name\t" + "age\t" + "gender");
        try
        {
            Statement st = con.createStatement();
            ResultSet result  = st.executeQuery("select * from patients");
            while(result.next())
            {
                int id = result.getInt("id");
                String name = result.getString("name");
                int age = result.getInt("age");
                String gender = result.getString("gender");
                System.out.println(id + "\t" + name + "\t" + age + "\t" + gender);
            }
            result.close();
            st.close();
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void viewDoctors(Connection con) {
        System.out.println("List of Doctors:");
        System.out.println("id\t" + "name\t" + "speciality");
        try
        {
            Statement st = con.createStatement();
            ResultSet result  = st.executeQuery("select * from doctors");
            while(result.next())
            {
                int id = result.getInt("id");
                String name = result.getString("name");
                String speciality = result.getString("speciality");
                System.out.println(id + "\t" + name + "\t" + speciality);
            }
            result.close();
            st.close();
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void viewAppointments(Connection con) {
        System.out.println("List of Appointments:");
        System.out.println("id\t" + "patient_id\t" + "doctor_id\t" + "appointment_date");
        try
        {
            Statement st = con.createStatement();
            ResultSet result  = st.executeQuery("select * from appointments");
            while(result.next())
            {
                int id = result.getInt("id");
                int patient_id = result.getInt("patient_id");
                int doctor_id = result.getInt("doctor_id");
                String date = result.getString("appointment_date");
                System.out.println(id + "\t" + patient_id + "\t" + doctor_id + "\t" + date);
            }
            result.close();
            st.close();
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static Patient findPatientById(int id,Connection con) {
        try
        {
            String query = "select * from patients where id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet result = ps.executeQuery();
            if(result.next())
            {
                String name = result.getString("name");
                int age = result.getInt("age");
                String gender = result.getString("gender");
                return new Patient(id,name,age,gender);
            }
            result.close();
            ps.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static Doctor findDoctorById(int id,Connection con) {
        try
        {
            String query = "select * from doctors where id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet result = ps.executeQuery();
            if(result.next())
            {
                String name = result.getString("name");
                String speciality = result.getString("speciality");
                return new Doctor(id,name,speciality);
            }
            result.close();
            ps.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    private static int intInput(Scanner scanner)
    {
        while(!scanner.hasNextInt())
        {
            System.out.println("Wrong Input!!!");
            System.out.println("Enter integer value from the given choices:");
            scanner.next();
        }
        int data = scanner.nextInt();
        scanner.nextLine();
        return data;
    }
}
