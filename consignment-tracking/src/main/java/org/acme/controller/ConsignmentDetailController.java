package org.acme.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import java.sql.Connection;
import org.acme.exceptions.ConsignDatailsException;
import org.acme.exceptions.LogginFileDetailsException;
// import java.sql.SQLException
import org.acme.models.ConsignDatails;
import org.acme.respository.ConsignmentDetailRepository;
import org.acme.services.ConsignmentDetailService;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.sql.Date;
import jakarta.ws.rs.*;
import org.acme.respository.BirtService;
import org.eclipse.birt.core.exception.BirtException;


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

    @Inject
    BirtService birtService;
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
            @QueryParam("customer_cnic_number") String customer_cnic_number
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

        if (customer_cnic_number != null){
            searchCriteria.put("customer_cnic_number", customer_cnic_number);
        }
        try {
            List<ConsignDatails> results = consignmentDetailService.searchConsignmentDetails(searchCriteria);
        

            return Response.ok(results).build();
        } catch (ConsignDatailsException e) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
    @GET
    @Path("/generate")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateReport(
        @QueryParam("design") @DefaultValue("your-report.rptdesign") String designFile,
                                   @QueryParam("format") @DefaultValue("pdf") String format,
                                   @QueryParam("username") String username,
                                   @QueryParam("fromDate") Date fromDate,
                                   @QueryParam("toDate") Date toDate
                                   ) {
        try {
            // System.out.println("Username received in controller:" + username);
            Map<String, Object> params = new HashMap<>();
            params.put("username", username);
            if (fromDate != null) {
                params.put("fromDate", new java.sql.Date(fromDate.getTime()));
                
            }
            if (toDate!= null) {
                params.put("toDate", new java.sql.Date(toDate.getTime()));
                
            }
            // System.out.println("Params map:" + params);
       byte[] bytes= birtService.generateReport(designFile, format, params);
       return  Response.ok(bytes).header("Content-Disposition","inline; filename=birtfile.pdf").header("Content-Type","application/pdf").build();
        } catch (BirtException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
