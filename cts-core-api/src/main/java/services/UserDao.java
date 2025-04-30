package services;

import org.mindrot.jbcrypt.BCrypt;
import database.Datasources;
import dto.UserLoginResponseDTO;

import java.sql.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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

        String sql = "INSERT INTO Users (USERNAME, PASSWORD, USEREMAIL, ROLE_ID, BRANCH_CD) VALUES (?, ?, ?, ?, ?)";

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
    public UserLoginResponseDTO loginUser(String username, String password, String branchCode) throws SQLException {
        String sql = "SELECT u.USER_ID,u.BRANCH_CD,u.USERNAME, u.ROLE_ID, r.ROLE_TYPE ,u.UserEMAIL, u.PASSWORD  FROM Users u JOIN ROLE r on u.ROLE_ID=r.ROLE_ID WHERE USERNAME = ? and BRANCH_CD=? ";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            int branchCodeInt = Integer.parseInt(branchCode);
            statement.setInt(2, branchCodeInt); 
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("USER_ID");
                int branchCodeQuery = resultSet.getInt("BRANCH_CD");
                String userNameQuery = resultSet.getString("USERNAME");
                String roleId = resultSet.getString("ROLE_ID");
                String roleName = resultSet.getString("ROLE_TYPE");
                String email = resultSet.getString("UserEMAIL");
                String hashedPassword = resultSet.getString("PASSWORD");



                if (BCrypt.checkpw(password, hashedPassword)) {
                     UserLoginResponseDTO userLoginResponseDTO=new  UserLoginResponseDTO();
                             userLoginResponseDTO.setUserId(userId);
                             userLoginResponseDTO.setUserName(username);
                             userLoginResponseDTO.setBranchCode(branchCodeQuery);
                             userLoginResponseDTO.setRoleName(roleName);
                             userLoginResponseDTO.setRoleId(roleId);

                             userLoginResponseDTO.setToken(tokenService.generateToken(userNameQuery,email,roleName));

                    return userLoginResponseDTO;
                } else {
                    return null; // Incorrect password
                }
            } else {
                return null; // User not found
            }
        }
    }

}