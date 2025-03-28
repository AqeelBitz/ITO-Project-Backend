package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.exceptions.LogginFileDetailsException;
import org.acme.models.*;
import org.acme.services.LoggingFileDetailsService;



@Path("api/data-access/logging-details")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoggingFileDetailsController {

    @Inject
    LoggingFileDetailsService loggingFileDetailsService;

    @POST
    public Response AddLoggingFileDetails(LoggingFileDetails loggingFileDetails) throws LogginFileDetailsException{
        try{
                LoggingFileDetails addedloggingFileDetails = loggingFileDetailsService.AddLoggingFileDetails(loggingFileDetails);
            return Response.status(Response.Status.CREATED).entity(addedloggingFileDetails).build();
        }
        catch(LogginFileDetailsException e){
            System.err.println("Error adding Logging file details: "+e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add logging file details: "+ e.getMessage()).build();
        }
    }
}
