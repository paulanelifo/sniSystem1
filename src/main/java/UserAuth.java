
/**
 *
 * @author Pololoers
 */
import java.sql.*;
public class UserAuth {
    private Connection connection;
    public UserAuth(){
        connection = Fns.getConnection();
    }
    public boolean usernameExists(String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean createUser(String username, String password) {
        try {
            if(usernameExists(username)){
                return false;
            }
            // Insert user credentials into the database
            String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean validateLogin(String username, String password) {
        try {
            // Check if the provided username and password match any record in the database
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean isValid = resultSet.next();
            preparedStatement.close();
            return isValid;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
