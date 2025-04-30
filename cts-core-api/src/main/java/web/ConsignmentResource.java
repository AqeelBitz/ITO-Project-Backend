// package web/ConsignmentResource.java
package web;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import util.ResponseUtil;
import client.ConsignmentClient;
import entities.ConsignDetailsDTO;
import entities.Roles;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Map;

@Path("/consignments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsignmentResource {

    @Inject
    @RestClient
    ConsignmentClient consignmentClient;

    @POST
    @Path("/accept")
    @RolesAllowed(Roles.UPDATER)
    public Response acceptMultipleConsignments(@QueryParam("fileName") String fileName,
                                                  @QueryParam("userId") int userId,
                                                  @QueryParam("recordCount") int recordCount,
                                                  @QueryParam("accept_record_count") int accept_record_count,
                                                  @QueryParam("reject_record_count") int reject_record_count,
                                                  @QueryParam("rejectedRows") String rejectedRows,
                                                  @QueryParam("acceptedRows") String acceptedRows,
                                                  @QueryParam("uploadedBy") String uploadedBy,
                                                  @QueryParam("fileType") String fileType,
                                                  List<ConsignDetailsDTO> consignDetailsDTOList) {
        try {
            Response clientResponse = consignmentClient.acceptConsignment(fileName, userId, recordCount,accept_record_count, reject_record_count,rejectedRows,acceptedRows,uploadedBy,fileType, consignDetailsDTOList);
            if (clientResponse.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                List<ConsignDetailsDTO> result = clientResponse.readEntity(new GenericType<List<ConsignDetailsDTO>>() {});
                return ResponseUtil.buildSuccessResponse(result, "Consignments accepted successfully");
            } else {
                // Handle the error response from the downstream service
                return ResponseUtil.buildErrorResponse(Response.Status.fromStatusCode(clientResponse.getStatus()),
                        clientResponse.readEntity(Map.class).getOrDefault("message", "Failed to accept consignments").toString());
            }
        } catch (jakarta.ws.rs.WebApplicationException e) {
            // Catch the standard JAX-RS exception for web application errors
            if (e.getResponse() != null) {
                return ResponseUtil.buildErrorResponse(Response.Status.fromStatusCode(e.getResponse().getStatus()),
                        e.getResponse().readEntity(Map.class).getOrDefault("message", "Failed to accept consignments from downstream service.").toString());
            } else {
                return ResponseUtil.buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, "Error calling consignment service.");
            }
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return ResponseUtil.buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }
    @POST
    @Path("/reject")
    @RolesAllowed(Roles.UPDATER)
    public Response rejectConsignment(@QueryParam("fileName") String fileName,
                                       @QueryParam("userId") int userId,
                                       @QueryParam("recordCount") int  recordCount
                                       ) {
        Response clientResponse = consignmentClient.addRejectedCount(fileName, userId,recordCount);
        if (clientResponse.getStatus() == Response.Status.CREATED.getStatusCode()) {
            return ResponseUtil.buildSuccessResponse(null, "Consignment rejected successfully!"); // No data to return typically
        } else {
            return ResponseUtil.buildErrorResponse(Response.Status.fromStatusCode(clientResponse.getStatus()),
                    "Failed to update rejection count."); // You might want to read the error body if the data access service provides more details
        }
    }

    @GET
    @Path("/search")
    @RolesAllowed({Roles.UPDATER, Roles.VIEWER})
    public Response searchConsignment(@QueryParam("consignment_id") String consignmentId,
                                      @QueryParam("address") String address,
                                      @QueryParam("city") String city,
                                      @QueryParam("account_no") String accountNo,
                                      @QueryParam("receiver_cnic") String receiverCnic) {
        Response clientResponse = consignmentClient.searchConsignmentDetails(consignmentId, address, city, accountNo, receiverCnic);
        if (clientResponse.getStatus() == Response.Status.OK.getStatusCode()) {
            List<ConsignDetailsDTO> responsePayload = clientResponse.readEntity(List.class);
            System.out.println("Response Payload (No Results?): " + responsePayload); // Add this line
            System.out.println("Response Payload Class: " + responsePayload.getClass().getName()); // Add this line
            if (responsePayload.isEmpty()) {
                return ResponseUtil.buildNotFoundResponse("Consignments");
            } else {
                return ResponseUtil.buildSuccessResponse(responsePayload, "Consignments found");
            }
        } else {
            String errorMessage;
            Object errorPayload = clientResponse.getEntity();
            if (errorPayload instanceof String) {
                errorMessage = (String) errorPayload;
            } else if (errorPayload instanceof Map) {
                Map<?, ?> errorMap = (Map<?, ?>) errorPayload;
                if (errorMap.containsKey("message")) {
                    errorMessage = String.valueOf(errorMap.get("message"));
                } else {
                    errorMessage = "Failed to search consignments.";
                }
            } else {
                errorMessage = "Failed to search consignments.";
            }
            return ResponseUtil.buildErrorResponse(Response.Status.fromStatusCode(clientResponse.getStatus()), errorMessage);
        }
    }
    @GET
    @Path("/lov")
    @RolesAllowed({Roles.UPDATER, Roles.VIEWER})
    public Response getLovTable() {
        Response clientResponse = consignmentClient.getLOVs();
        if (clientResponse.getStatus() == Response.Status.OK.getStatusCode()) {
            return Response.ok(clientResponse.readEntity(List.class)).build();
        } else {
            return ResponseUtil.buildErrorResponse(Response.Status.fromStatusCode(clientResponse.getStatus()),
                    clientResponse.readEntity(String.class));
        }
    }
}