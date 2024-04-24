
/**
 *
 * @author Pololoers
 */

/*
NOTES:
    - UserAuth.java should be accessible.
    - Add this line of code to the main method in the Main class to get it started.
    Fns.home();
    - You can always use 'this' whenever a JFrame is a method argument/parameter.
    - Make sure the components used as method arguments/parameters are 'public' and 'static'.
        to achieve this...
            1. Right click the component.
            2. Select customize code.
            3. Change 'private' to 'public'.
            4. Check the 'static' checkbox.
*/

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class Fns {
    
    public static void home(){
        //btnLogout();//default
        
        authFrame.dispose();
        frmdashboard.setVisible(true);
        user="admin";
    }
    
    private static UserAuth userAuth = new UserAuth();
    public static Connection getConnection(){
        try {
                // Connect to the SQLite database
                Connection connection = DriverManager.getConnection("jdbc:sqlite:myDb.db");
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
    }
    //variables
    public static String user="";
    
    //LOGIN
    public static frmDashboard frmdashboard = new frmDashboard(){{            
            setLocationRelativeTo(null);
            dispose();
        }};
    public static frmAuth authFrame = new frmAuth(){{            
            setLocationRelativeTo(null);
            setVisible(true);
        }};
    private static int registerUser(String name, String pass){
        /*
        1 = success
        2 = if name is too short
        3 = if password is too short
        */
        if (name.length() < 4) {
        return 2; // Username is too short
        }
        if (pass.length() < 8) {
            return 3; // Password is too short
        }
        if (userAuth.usernameExists(name)) {
            return 4; // Username already exists
        }

        // Register user
        if (userAuth.createUser(name, pass)) {
            return 1; // Success
        } else {
            return 0; // Error occurred during registration
        }
    }
    private static boolean isValidLogin(String name, String pass){
        return userAuth.validateLogin(name, pass);
    }
    public static void btnLogin(JTextField username, JPasswordField password, JFrame frm){
        /*
        sample execution in button action:
        Fns.btnLogin(jTextField1.getText(), jPasswordField1.getText(), this);
        */
        if(Fns.isValidLogin(username.getText(), password.getText())){
            JOptionPane.showMessageDialog(frm, "Login success!");
            user = frmAuth.jTextField1.getText();
            username.setText("");
            password.setText("");
            authFrame.dispose();
            frmdashboard.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(frm, "Login failed.");
        }
    }
    public static void btnRegister(JTextField username, JPasswordField password, JFrame frm){
        int reg = registerUser(username.getText(), password.getText());
        switch(reg){
            case 1: JOptionPane.showMessageDialog(frm, "Register success!");
            username.setText("");
            password.setText("");
            break;
            case 2: JOptionPane.showMessageDialog(frm, "Username should be atleast 4 characters.");break;
            case 3: JOptionPane.showMessageDialog(frm, "Password should be atleast 8 characters.");break;
            case 4: JOptionPane.showMessageDialog(frm, "Username already exists.");break;

            default: JOptionPane.showMessageDialog(frm, "bruh");
        }
    }
    public static void btnLogout(){
        user="";
        frmdashboard.dispose();
        authFrame.setLocationRelativeTo(null);
        authFrame.setVisible(true);
    }
    
    //DASHBOARD    
    private static DefaultTableModel model;    
    public static void populateTable(JTable table, String tablename) {
        model = (DefaultTableModel) table.getModel(); // Initialize model

        String query = "SELECT * FROM "+tablename;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Clear existing data
            model.setRowCount(0);
            model.setColumnCount(0);

            // Get metadata
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add columns to table model
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Add rows to table model
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                model.addRow(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
