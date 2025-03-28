package org.acme.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acme.exceptions.ConsignDatailsException;

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
    @POST
    @Path("/add")
    public Response AddConsginmentDetails(ConsignDatails consignDatails) throws SQLException {
        try{
            ConsignDatails addeDatails = consignmentDetailService.AddConsginmentDetails(consignDatails);
            
            Integer file_id = 1;
            // Call the incrementAcceptedCount method.
            consignmentDetailsRepository.incrementAcceptedCount(file_id);
            return Response.status(Response.Status.CREATED).entity(addeDatails).build();
        }
        catch(ConsignDatailsException e){
            System.err.println("Error Adding Consignment Details: "+ e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add Consignment Details: "+e.getMessage()).build();
        }
    }

        @GET //Use get because we are retrieving data from the url.
        @Path("/search")
    public Response searchConsignmentDetails(
            @QueryParam("consignment_id") String consignmentId,
            @QueryParam("address") String address,
            @QueryParam("city") String city,
            // Add other query parameters as needed
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
            // Log the error
            System.err.println("Error searching consignment details: " + e.getMessage());

            // Create an error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to search consignment details: " + e.getMessage())
                    .build();
        }
    }


}
