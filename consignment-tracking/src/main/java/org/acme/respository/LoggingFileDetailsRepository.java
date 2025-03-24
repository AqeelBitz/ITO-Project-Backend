package org.acme.respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.acme.models.LoggingFileDetails;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoggingFileDetailsRepository {
    @Inject
    DataSource dataSource;

    public LoggingFileDetails AddLoggingFileDetails(LoggingFileDetails loggingFileDetails) throws SQLException{
        
        try
        (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Logging_File_Details (file_id,file_name,record_count,accept_record_count,reject_record_count,user_id,file_date,file_time) VALUES (?,?,?,?,?,?,?,?)")
        ){
            preparedStatement.setInt(1, loggingFileDetails.getFile_id());
            preparedStatement.setString(2, loggingFileDetails.getFile_name());
            preparedStatement.setInt(3, loggingFileDetails.getRecord_count());
            preparedStatement.setInt(4, loggingFileDetails.getAccept_record_count());
            preparedStatement.setInt(5, loggingFileDetails.getReject_record_count());
            preparedStatement.setInt(6, loggingFileDetails.getUser_id());
            preparedStatement.setDate(7, new java.sql.Date(loggingFileDetails.getFileDate().getTime()));
            preparedStatement.setTime(8, new java.sql.Time(loggingFileDetails.getFileTime().getTime()));
            
            System.out.println("loggingFileDetails: "+loggingFileDetails);
            preparedStatement.executeUpdate();
            return loggingFileDetails;
        }
      catch (SQLException e) {
    // Log the error
    System.err.println("Database error: " + e.getMessage());
    // Or use a logging framework like SLF4J:
    // logger.error("Database error: {}", e.getMessage(), e);

    // Throw a custom exception
    throw new RuntimeException("Failed to add logging file details: " + e.getMessage(), e);
}
    }

}
