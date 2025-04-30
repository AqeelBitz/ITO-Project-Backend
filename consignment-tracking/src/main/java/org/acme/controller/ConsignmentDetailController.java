package org.acme.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import java.sql.Connection;
import org.acme.exceptions.ConsignDatailsException;
import org.acme.exceptions.LogginFileDetailsException;

// import java.sql.SQLException;

import org.acme.models.ConsignDatails;
import org.acme.respository.ConsignmentDetailRepository;
import org.acme.services.ConsignmentDetailService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("api/data-access/consignment-details")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsignmentDetailController {
    
    @Inject
    ConsignmentDetailService consignmentDetailService;
    @Inject
    ConsignmentDetailRepository consignmentDetailsRepository; 

    @Inject 
    DataSource dataSource;
    @POST
    @Path("/accept")
    public Response AddConsginmentDetails(@QueryParam("fileName") String file_name, 
                                          @QueryParam("userId") int user_id, 
                                          @QueryParam("recordCount") int record_count,
                                          @QueryParam("accept_record_count") int accept_record_count,
                                          @QueryParam("reject_record_count") int reject_record_count,
                                          @QueryParam("rejectedRows") String rejectedRows,
                                          @QueryParam("acceptedRows") String acceptedRows,
                                          @QueryParam("uploadedBy") String uploadedBy,
                                          @QueryParam("fileType") String fileType,
                                          List<ConsignDatails> consignDatails) {
        Connection connection = null;
        System.out.println("consignDatails: "+consignDatails);
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Insert into consignment details
            List<ConsignDatails> addedDetails = consignmentDetailService.AddConsginmentDetails(consignDatails, connection);

            // Update logging file details
            consignmentDetailsRepository.incrementAcceptedCount(file_name, user_id,record_count,accept_record_count, reject_record_count,rejectedRows, acceptedRows, uploadedBy,fileType, connection);

            connection.commit(); // Commit transaction
            return Response.status(Response.Status.CREATED).entity(addedDetails).build();
        } catch (SQLException | ConsignDatailsException e) {
            System.out.println("---> "+e.getStackTrace().toString());
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback if any operation fails
                } catch (SQLException rollbackEx) {
                    System.err.println("Transaction rollback failed: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Error Adding Consignment Details: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(Map.of("error","Failed to add Consignment Details. ", "message", e.getMessage())).build();
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Reset auto-commit
                    connection.close(); // Close connection
                } catch (SQLException closeEx) {
                    System.err.println("Error closing connection: " + closeEx.getMessage());
                }
            }
        }
    }
    
    @POST
    @Path("/reject")
    public Response addRejectedCount(@QueryParam("fileName") String fileName, @QueryParam("userId") int userId, @QueryParam("recordCount") int record_count) throws LogginFileDetailsException {
        try {
            consignmentDetailsRepository.incrementRejectedCount(fileName, userId, record_count);
            return Response.status(Response.Status.CREATED).build();
        } catch (LogginFileDetailsException e) {
            // System.err.println("Error Adding Rejected Count: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
        @GET 
        @Path("/search")
    public Response searchConsignmentDetails(
            @QueryParam("consignment_id") String consignmentId,
            @QueryParam("address") String address,
            @QueryParam("city") String city,
            @QueryParam("account_no") String accountNo,
            @QueryParam("receiver_cnic") String receiver_cnic
            ) {

        Map<String, String> searchCriteria = new HashMap<>();

        if (consignmentId != null) {
            searchCriteria.put("consignment_id", consignmentId);
        }
        if (address != null) {
            searchCriteria.put("address", address);
        }
        if (city != null){
            searchCriteria.put("city", city);
        }
        if (accountNo != null){
            searchCriteria.put("account_no", accountNo);
        }

        if (receiver_cnic != null){
            searchCriteria.put("receiver_cnic", receiver_cnic);
        }
        try {
            List<ConsignDatails> results = consignmentDetailService.searchConsignmentDetails(searchCriteria);
        

            return Response.ok(results).build();
        } catch (ConsignDatailsException e) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


}
