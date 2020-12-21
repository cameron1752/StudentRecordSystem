/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw4_integration;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// chart imports
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Cammy
 */
public class HW4_Integration extends javax.swing.JFrame {
    // create new instance of DB manager class
    DBManager db = new DBManager();
    // create linked list for student records
    List<StudentRecord> studentList = new LinkedList();
    // string to hold filepath for things 
    String xmlOutput = "/Spring 2020/IST 411/projects/HW 4 - Integration/HW4_Integration/StudentRecord.xml";
    /**
     * Creates new form NewJFrame
     */
    public HW4_Integration() {
        initComponents();
        
        // set warning labels to false on init
        warningLabel.setVisible(false);
        removeFailLabel.setVisible(false);
        
        // init the GUI information
        initBoard();
    }
    
    // calls the methods to initialize the database, initialize and load the table, and read the xml file
    public void initBoard(){
        // drop students table before closing database
        db.dropTable();
        
        // init database
        db.createTable();
        
        // clear the table of default values
        DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
        model.setRowCount(0);
        
        // read xml file
        readFile(filePathTextField.getText());
        
        // print the length of the string just in case
        System.out.println(studentList.size());
        
        // for each node in the list we'll have to save it to the database and add it to the table
        for (int i = 0; i < studentList.size(); i++){
            // save node to temp node
            StudentRecord temp = studentList.get(i);
            // insert record into the database
            db.insertRecord(temp);
            // add record to the table
            fillTable(temp);
        }
        
        // init the graphics
        initCharts();
    }
    
    // calls both circleChart and barChart method to create the two charts with list data
    private void initCharts(){
        // call the barChart function to create the bar chart graphic
        barChart();
        circleChart();
    }
    
    // creates pie graph represetning the parts / whole of types of major
    public void circleChart(){
        // create new dataset
        DefaultPieDataset ds = new DefaultPieDataset();
        
        // values for bar chart
        int val1 = 0, val2 = 0, val3 = 0, val4 = 0;
        
        // for each node in the list we'll have to save it to the database and add it to the table
        for (int i = 0; i < studentList.size(); i++){
            StudentRecord temp = studentList.get(i);
            
            // for each of the types of majors iterate a counter if found
            switch(temp.getMajor()){
                case "IST":
                    val1++;
                    break;
                    
                case "Business":
                    val2++;
                    break;
                    
                case "Nursing":
                    val3++;
                    break;
                
                case "HDFS":
                    val4++;
                    break;
            }
        }
        
        // set the values into the dataset for each entry
        ds.setValue("IST", new Double (val1));
        ds.setValue("Business", new Double(val2));
        ds.setValue("Nursing", new Double(val3));
        ds.setValue("HDFS", new Double(val4));
        
        // create the pie chart
        JFreeChart pie = ChartFactory.createPieChart("Majors", ds, true, true, false);
        
        // create the chart panel and insert the pie chart
        ChartPanel cp = new ChartPanel(pie);
        
        // set size of chart
        cp.setPreferredSize(new java.awt.Dimension( 600 , 250 ) );  
        
        // set layout of chart panel holding panel
        circlePanel.setLayout(new java.awt.BorderLayout());
        
        // add the chart panel to the holding panel
        circlePanel.add(cp, BorderLayout.CENTER);
        circlePanel.validate();
    }
    
    // creates bar graph representing the counts of each major type
    public void barChart(){
        // create dataset
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        
        // values for bar chart
        int val1 = 0, val2 = 0, val3 = 0, val4 = 0;
        
        // for each node in the list we'll have to save it to the database and add it to the table
        for (int i = 0; i < studentList.size(); i++){
            StudentRecord temp = studentList.get(i);
            
            // for each major status count a variable if we find one
            switch(temp.getDegreeStatus()){
                case "PartTime":
                    val1++;
                    break;
                    
                case "FullTime":
                    val2++;
                    break;
                    
                case "Provisional":
                    val3++;
                    break;
                
                case "NonDegree":
                    val4++;
                    break;
            }
        }
        
        // add values to our dataset from the switch counter
        ds.addValue(val1, "Part-time", "Part-time");
        ds.addValue(val2, "Full-time", "Full-time");
        ds.addValue(val3, "Provisional", "Provisional");
        ds.addValue(val4, "non-degree", "non-degree");
        
        // create the chart
        JFreeChart chart = ChartFactory.createBarChart("Degree Status",
                "Degree Type", "Number of Students", ds, PlotOrientation.VERTICAL, true, true,
                false);
        
        // create the chart panel and add the chart to it
        ChartPanel cp = new ChartPanel(chart);
        
        // set the chart size
        cp.setPreferredSize(new java.awt.Dimension( 600 , 250 ) );  
        
        // set the layout of the barPanel JPanel where teh graph will go
        barPanel.setLayout(new java.awt.BorderLayout());
        
        // add the chart panel to the barPanel
        barPanel.add(cp, BorderLayout.CENTER);
        barPanel.validate();
        
    }
    
    
    // function fills the jtable given input of student record
    public void fillTable(StudentRecord student){
        DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
        model.addRow(new Object[]{student.getFName(), student.getLName(), student.getDegreeStatus(), student.getMajor()});
    }
    
    // reads info from the input XML file, and then stores it in the StudentRecord List
    public void readFile(String filename){
        try{   
            // xml reader building
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setValidating(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new File(filename));
            NodeList list = document.getElementsByTagName("student");
           
            //This for loop gathers all the student attributes, puts them in a StudentRecord object
            //then stores that student in the StudentList
            for(int i = 0; i < list.getLength(); i++)
            { 
                Element element = (Element)list.item(i);
                String xmlDegreeStatus = element.getAttribute("status");
                String xmlFirstName = getFirstName(element);
                String xmlLastName = getLastName(element);
                String xmlMajor = getMajor(element);
                StudentRecord student = new StudentRecord(xmlFirstName, xmlLastName, xmlDegreeStatus, xmlMajor);
                
                // add the student to the list
                studentList.add(student);
            }//end for loop loading the studentArray[] with full student records
            
        }//end try block
        catch (ParserConfigurationException parserException)
        {
            parserException.printStackTrace();   
        }//end catch block
        catch (SAXException saxException)
        {
            saxException.printStackTrace();
        }//end catch block
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }//end catch block
       
    }//end readFile()
    
    // writes to xml file for student records
    public void writeFile() throws TransformerException, FileNotFoundException{
        try{
            // xml reader building
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
           
            // create the root element
            Element rootEle = document.createElement("Student_Records");
            document.appendChild(rootEle);
            
            Element studentE = document.createElement("Student");
        
        for (int i = 0; i < studentList.size(); i++){
            // store current element 
            StudentRecord student = studentList.get(i);
            rootEle.appendChild(studentE);
            // create data elements and place them under root
            studentE = document.createElement("First_Name");
            studentE.appendChild(document.createTextNode(student.getFName()));
            rootEle.appendChild(studentE);

            studentE = document.createElement("Last_Name");
            studentE.appendChild(document.createTextNode(student.getLName()));
            rootEle.appendChild(studentE);

            studentE = document.createElement("Degree_Status");
            studentE.appendChild(document.createTextNode(student.getDegreeStatus()));
            rootEle.appendChild(studentE);

            studentE = document.createElement("Major");
            studentE.appendChild(document.createTextNode(student.getMajor()));
            rootEle.appendChild(studentE);

            rootEle.appendChild(studentE);
        }
        
        
        Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMImplementation domImpl = document.getImplementation();
            
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            // send DOM to file
            tr.transform(new DOMSource(document), 
                                 new StreamResult(new FileOutputStream(xmlOutput)));
        } catch (ParserConfigurationException pce){
            pce.printStackTrace();
        } catch (TransformerException tfe){
            tfe.printStackTrace();
        }
    }
    
    //RETURNS THE FIRST NAME OF THE STUDENT
    public String getFirstName(Element parent){ 
        NodeList child = parent.getElementsByTagName("firstName");
        Node childTextNode = child.item(0).getFirstChild();
        return childTextNode.getNodeValue();  
    }//end getFirstName
    
   //RETURNS THE LAST NAME OF THE STUDENT    
    public String getLastName(Element parent){ 
        NodeList child = parent.getElementsByTagName("lastName");
        Node childTextNode = child.item(0).getFirstChild();
        return childTextNode.getNodeValue();  
    }//end getLastName
    
    //RETURNS THE DEGREE STATUS OF THE STUDENT    
    public String getDegreeStatus(Element parent){ 
        NodeList child = parent.getElementsByTagName("degreeStatus");
        Node childTextNode = child.item(0).getFirstChild();
        return childTextNode.getNodeValue();  
    }//end getDegreeStatus
    
    //RETURNS THE MAJOR OF THE STUDENT    
    public String getMajor(Element parent){ 
        NodeList child = parent.getElementsByTagName("major");
        Node childTextNode = child.item(0).getFirstChild();
        return childTextNode.getNodeValue();  
    }//end getFirstName
    
    
    // Function takes input student record to remove from the list returns true if deleted
    public boolean removeTable(StudentRecord student){
        boolean removed = false;
        
        // for each node in the list we'll have to check and see if all fields match before deleting it from the list
        for (int i = 0; i < studentList.size(); i++){
            StudentRecord temp = studentList.get(i);
            
            // if all fields match
            if (temp.getFName().equals(student.getFName()) && temp.getLName().equals(student.getLName()) &&
                    temp.getDegreeStatus().equals(student.getDegreeStatus()) && temp.getMajor().equals(student.getMajor())){
                // set removed validator to true
                removed = true;
                
                // remove the found item from the list using our found index
                studentList.remove(i);
                
            }
        }
        
        // clear the table of values
        DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
        model.setRowCount(0);
        
        // refil the table with list data
        for (int k = 0; k < studentList.size(); k++){
            StudentRecord tempB = studentList.get(k);
            // add record to table
            fillTable(tempB);
        }
        
        return removed;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        firstNameLabel = new javax.swing.JLabel();
        lastNameLabel = new javax.swing.JLabel();
        degreeStatusLabel = new javax.swing.JLabel();
        majorLabel = new javax.swing.JLabel();
        addLabel = new javax.swing.JLabel();
        firstNameText = new javax.swing.JTextField();
        lastNameText = new javax.swing.JTextField();
        addStudentButton = new javax.swing.JButton();
        degreeStatusBox = new javax.swing.JComboBox<>();
        majorBox = new javax.swing.JComboBox<>();
        warningLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        studentTable = new javax.swing.JTable();
        filePathLabel = new javax.swing.JLabel();
        filePathTextField = new javax.swing.JTextField();
        backupButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        removeFailLabel = new javax.swing.JLabel();
        barPanel = new javax.swing.JPanel();
        circlePanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        titleLabel.setText("Student Record Manager");

        firstNameLabel.setText("First Name");

        lastNameLabel.setText("Last Name");

        degreeStatusLabel.setText("Degree Status");

        majorLabel.setText("Major");

        addLabel.setText("Add Student:");

        addStudentButton.setText("Add");
        addStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentButtonActionPerformed(evt);
            }
        });

        degreeStatusBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FullTime", "PartTime", "NonDegree", "Provisional" }));

        majorBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Business", "IST", "Nursing", "HDFS" }));

        warningLabel.setText("All Fields are required!");

        studentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "First Name", "Last Name", "Degree Status", "Major"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(studentTable);

        filePathLabel.setText("Import File Path: ");

        filePathTextField.setText("/Spring 2020/IST 411/projects/HW 4 - Integration/HW4_Integration/Students.xml");

        backupButton.setText("Backup");
        backupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backupButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        removeFailLabel.setText("Student not found in list, please try again!");

        javax.swing.GroupLayout barPanelLayout = new javax.swing.GroupLayout(barPanel);
        barPanel.setLayout(barPanelLayout);
        barPanelLayout.setHorizontalGroup(
            barPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        barPanelLayout.setVerticalGroup(
            barPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout circlePanelLayout = new javax.swing.GroupLayout(circlePanel);
        circlePanel.setLayout(circlePanelLayout);
        circlePanelLayout.setHorizontalGroup(
            circlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 632, Short.MAX_VALUE)
        );
        circlePanelLayout.setVerticalGroup(
            circlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(161, 161, 161)
                        .addComponent(removeFailLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lastNameLabel)
                                .addGap(75, 75, 75)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(degreeStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(degreeStatusBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(majorBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(majorLabel)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(firstNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(40, 40, 40)
                                    .addComponent(lastNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(firstNameLabel)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(addLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(warningLabel)
                                .addGap(34, 34, 34)
                                .addComponent(addStudentButton)
                                .addGap(18, 18, 18)
                                .addComponent(deleteButton)
                                .addGap(168, 168, 168)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 153, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(titleLabel)
                            .addGap(273, 273, 273)
                            .addComponent(filePathLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(filePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(backupButton))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(circlePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(barPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(35, 35, 35))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addLabel)
                    .addComponent(titleLabel)
                    .addComponent(filePathLabel)
                    .addComponent(filePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNameLabel)
                    .addComponent(lastNameLabel)
                    .addComponent(degreeStatusLabel)
                    .addComponent(majorLabel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(degreeStatusBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(majorBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(backupButton)))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(warningLabel)
                            .addComponent(addStudentButton)
                            .addComponent(deleteButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(removeFailLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(barPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(circlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // adds a student to the student table in database, adds student to the students list, updates table
    private void addStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentButtonActionPerformed
        
        // get student info from GUI
        String fName = firstNameText.getText();
        String lName = lastNameText.getText();
        String degree = degreeStatusBox.getSelectedItem().toString();
        String major = majorBox.getSelectedItem().toString();
        
        // reset warning label
        warningLabel.setVisible(false);
        
        // if any of the fields are empty alert the user that all fields are required
        if (fName.isEmpty() || lName.isEmpty() || degree.isEmpty() || major.isEmpty()){
            
            
            // set warning label
            warningLabel.setVisible(true);
        } else {
            // take the values input from the GUI and create a new string
            StudentRecord newStudent = new StudentRecord(fName, lName, degree, major);
            
            // print the student record
            newStudent.toString();
            
            // add it to the database
            db.insertRecord(newStudent);
            
            // add it to the list
            studentList.add(newStudent);
            
            // add it to the GUI
            fillTable(newStudent);
            
        } // end GUI validation
        
        // reset GUI fields
        firstNameText.setText("");
        lastNameText.setText("");
        
        // reset charts
        initCharts();
    }//GEN-LAST:event_addStudentButtonActionPerformed
    // will handle the backup process and saving of the student table to and xml file
    private void backupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backupButtonActionPerformed
        try {
            // TODO add your handling code here:
            writeFile();
        } catch (TransformerException ex) {
            Logger.getLogger(HW4_Integration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HW4_Integration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_backupButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        // get student info from GUI
        String fName = firstNameText.getText();
        String lName = lastNameText.getText();
        String degree = degreeStatusBox.getSelectedItem().toString();
        String major = majorBox.getSelectedItem().toString();
        
        // reset warning label
        removeFailLabel.setVisible(false);
        
        // if any of the fields are empty alert the user that all fields are required
        if (fName.isEmpty() || lName.isEmpty() || degree.isEmpty() || major.isEmpty()){
            // set warning label
            warningLabel.setVisible(true);
        } else {
            // take the values input from the GUI and create a new string
            StudentRecord newStudent = new StudentRecord(fName, lName, degree, major);
            
            // print the student record
            newStudent.toString();
                        
            // remove it from GUI
            if (removeTable(newStudent)){
                // delete it from the database
                db.deleteRecord(newStudent);
            } else{ 
                // show not removed label
                removeFailLabel.setVisible(true);
            }
            
        } // end GUI validation
        
        // reset GUI fields
        firstNameText.setText("");
        lastNameText.setText("");
        
        // reset charts
        initCharts();
    }//GEN-LAST:event_deleteButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HW4_Integration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HW4_Integration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HW4_Integration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HW4_Integration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HW4_Integration().setVisible(true);
            }
            
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addLabel;
    private javax.swing.JButton addStudentButton;
    private javax.swing.JButton backupButton;
    private javax.swing.JPanel barPanel;
    private javax.swing.JPanel circlePanel;
    private javax.swing.JComboBox<String> degreeStatusBox;
    private javax.swing.JLabel degreeStatusLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel filePathLabel;
    private javax.swing.JTextField filePathTextField;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameText;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JTextField lastNameText;
    private javax.swing.JComboBox<String> majorBox;
    private javax.swing.JLabel majorLabel;
    private javax.swing.JLabel removeFailLabel;
    private javax.swing.JTable studentTable;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel warningLabel;
    // End of variables declaration//GEN-END:variables

    
}
