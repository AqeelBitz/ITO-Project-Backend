package org.acme.controller;

import java.sql.SQLException;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.models.*;
import org.acme.services.LoggingFileDetailsService;



@Path("logging")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoggingFileDetailsController {

    @Inject
    LoggingFileDetailsService loggingFileDetailsService;

    @POST
    public LoggingFileDetails AddLoggingFileDetails(LoggingFileDetails loggingFileDetails) throws SQLException{
        // System.out.println(loggingFileDetails);
        return loggingFileDetailsService.AddLoggingFileDetails(loggingFileDetails);
    }
}
