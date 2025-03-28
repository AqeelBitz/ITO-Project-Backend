package org.acme.services;

import org.acme.exceptions.LogginFileDetailsException;
import org.acme.models.LoggingFileDetails;
import org.acme.respository.LoggingFileDetailsRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoggingFileDetailsService {
    
    @Inject
    LoggingFileDetailsRepository loggingFileDetailsRepository;

    public LoggingFileDetails AddLoggingFileDetails(LoggingFileDetails loggingFileDetails) throws LogginFileDetailsException{
        return loggingFileDetailsRepository.AddLoggingFileDetails(loggingFileDetails);
    } 
}
