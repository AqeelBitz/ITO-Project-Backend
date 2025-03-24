package services;

import org.mindrot.jbcrypt.BCrypt;
import database.Datasources;

import java.sql.*;
import jakarta.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import jakarta.transaction.Transactional;

@RequestScoped
public class UserDao{

    @Inject
    private DataSource dataSource;

    @Inject
    private TokenService tokenService;


    @Transactional
    public void registerUser(String username, String password, String email, int roleId, int branchId) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String sql = "INSERT INTO User (USERNAME, PASSWORD_HASH, EMAIL, ROLE_ID, BRANCH_CODE) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, email);
            statement.setInt(4, roleId);
            statement.setInt(5, branchId);

            statement.executeUpdate();
        }
    }


    @Transactional
    public String loginUser(String username, String password, String branchCode) throws SQLException {
        String sql = "SELECT USER_ID,EMAIL, PASSWORD_HASH FROM User WHERE USERNAME = ? and BRANCH_CODE=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, branchCode);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("USER_ID");
                String hashedPassword = resultSet.getString("PASSWORD_HASH");
                String email = resultSet.getString("EMAIL");


                if (BCrypt.checkpw(password, hashedPassword)) {
                    return tokenService.generateUserToken(email,username);
                } else {
                    return null; // Incorrect password
                }
            } else {
                return null; // User not found
            }
        }
    }

}