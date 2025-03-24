package org.acme.services;

import java.sql.SQLException;

import org.acme.models.LoggingFileDetails;
import org.acme.respository.LoggingFileDetailsRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoggingFileDetailsService {
    
    @Inject
    LoggingFileDetailsRepository loggingFileDetailsRepository;

    public LoggingFileDetails AddLoggingFileDetails(LoggingFileDetails loggingFileDetails) throws SQLException{
        return loggingFileDetailsRepository.AddLoggingFileDetails(loggingFileDetails);
    } 
}
