/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw4_integration;

/**
 *
 * @author Cammy
 */
public class StudentRecord {
    // student record variables
    private String firstName;
    private String lastName;
    private String degreeStatus;
    private String major;
    
    // constructor
    public StudentRecord(String fName, String lName, String degree, String degreeMajor) {
        
        firstName = fName;
        lastName = lName;
        degreeStatus = degree;
        major = degreeMajor;
        
    }
    /*
    
    Accessor methods for student record
    
    */
    public String getFName(){
        return firstName;
    }
    
    public String getLName(){
        return lastName;
    }
    
    public String getDegreeStatus(){
        return degreeStatus;
    }
    
    public String getMajor(){
        return major;
    }
    
    /*
    
    Mutator methods for student record
    
    */
    public String setFName(String fName){
        firstName = fName;
        return firstName;
    }
    
    public String setLName(String lName){
        lastName = lName;
        return lastName;
    }
    
    public String setDegreeStatus(String degree){
        degreeStatus = degree;
        return degreeStatus;
    }
    
    public String setMajor(String majorPath){
        major = majorPath;
        return major;
    }
    
    /*
    
    Function to format the contents of a student record into a readable delimited string to read into list
    
    */
    public String toString(){
        // new formatted string
        String studentString = lastName + "," + firstName + "," + degreeStatus + "," + major;
        
        // print the string to the console
        System.out.println(studentString);
        
        return studentString;
    }
}
