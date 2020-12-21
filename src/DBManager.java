/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw4_integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Cammy
 */
public class DBManager {
    // variable to connection path
    String filePath = "jdbc:ucanaccess://G:/Spring 2020/IST 411/projects/HW 4 - Integration/HW4_Integration/StudentRegistration.accdb";
    
    public void createTable(){
        try{
            // load database driver class
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // connect to database
            Connection con = DriverManager.getConnection(filePath);
            Statement stmt = con.createStatement();
            
            stmt.execute("CREATE TABLE students (FirstName VARCHAR(255), LastName VARCHAR(255),"
                    + " DegreeStatus VARCHAR(255), Major VARCHAR(255))");
            
            System.out.println("Students table created");
            
        } // detect problems interacting with the database
        catch ( SQLException sqlException ) {
            JOptionPane.showMessageDialog( null, 
            sqlException.getMessage(), "Database Error",
            JOptionPane.ERROR_MESSAGE );
         
            System.exit( 1 );
        }//end catch block
      
        // detect problems loading database driver
        catch ( ClassNotFoundException classNotFound ) {
            JOptionPane.showMessageDialog( null, 
            classNotFound.getMessage(), "Driver Not Found",
            JOptionPane.ERROR_MESSAGE );

            System.exit( 1 );
        }//end catch block
    } // end create table
    
    /*
    
    function inserts a record into the student table
    PRE: student table has been created
    
    */
    public void insertRecord(StudentRecord student){
        try{
            // load database driver class
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // connect to database
            Connection con = DriverManager.getConnection(filePath);
            Statement stmt = con.createStatement();
            
            // get the values from the student record
            String fName = student.getFName();
            String lName = student.getLName();
            String degree = student.getDegreeStatus();
            String major = student.getMajor();
            
            // sql to enter it into the table
            stmt.execute("INSERT INTO students VALUES('" + fName + "','" + lName + "','" + degree + "','" + major + "')");
            
            System.out.println("Student: " + fName + " " + lName + " Status: " + degree + " Degree: " + major + " added to student table");
            
        } // detect problems interacting with the database
        catch ( SQLException sqlException ) {
            JOptionPane.showMessageDialog( null, 
            sqlException.getMessage(), "Database Error",
            JOptionPane.ERROR_MESSAGE );
         
            System.exit( 1 );
        }//end catch block
      
        // detect problems loading database driver
        catch ( ClassNotFoundException classNotFound ) {
            JOptionPane.showMessageDialog( null, 
            classNotFound.getMessage(), "Driver Not Found",
            JOptionPane.ERROR_MESSAGE );

            System.exit( 1 );
        }//end catch block
    }
    
    public void deleteRecord(StudentRecord student){
        try{
            // load database driver class
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // connect to database
            Connection con = DriverManager.getConnection(filePath);
            Statement stmt = con.createStatement();
            
            // get the values from the student record
            String fName = student.getFName();
            String lName = student.getLName();
            String degree = student.getDegreeStatus();
            String major = student.getMajor();
            
            // sql to enter it into the table
            stmt.execute("DELETE * FROM students WHERE FirstName = '" + fName + "' and LastName = '" + lName + "' and DegreeStatus = '" + degree + "' and Major = '" + major + "'");
            
            System.out.println("Student: " + fName + " " + lName + " Status: " + degree + " Degree: " + major + " deleted from student table");
            
        } // detect problems interacting with the database
        catch ( SQLException sqlException ) {
            JOptionPane.showMessageDialog( null, 
            sqlException.getMessage(), "Database Error",
            JOptionPane.ERROR_MESSAGE );
         
            System.exit( 1 );
        }//end catch block
      
        // detect problems loading database driver
        catch ( ClassNotFoundException classNotFound ) {
            JOptionPane.showMessageDialog( null, 
            classNotFound.getMessage(), "Driver Not Found",
            JOptionPane.ERROR_MESSAGE );

            System.exit( 1 );
        }//end catch block
    }
    
    /*
    
    Function drops the students table before closing the database to prevent errors

    */
    public void dropTable(){
        try{
            // load database driver class
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // connect to database
            Connection con = DriverManager.getConnection(filePath);
            Statement stmt = con.createStatement();
            
            stmt.execute("DROP TABLE students");
            
            System.out.println("Students table destroyed");
            
        } // detect problems interacting with the database
        catch ( SQLException sqlException ) {
            JOptionPane.showMessageDialog( null, 
            sqlException.getMessage(), "Database Error",
            JOptionPane.ERROR_MESSAGE );
         
            System.exit( 1 );
        }//end catch block
      
        // detect problems loading database driver
        catch ( ClassNotFoundException classNotFound ) {
            JOptionPane.showMessageDialog( null, 
            classNotFound.getMessage(), "Driver Not Found",
            JOptionPane.ERROR_MESSAGE );

            System.exit( 1 );
        }//end catch block
    } // end create table
    
}
