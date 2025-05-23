package client;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response; // Import Response

import java.sql.Date;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import entities.ConsignDetailsDTO;

// @RegisterRestClient(baseUri = "http://localhost:8081/api/data-access/consignment-details")
@RegisterRestClient
@Path("/api/data-access")
@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
public interface ConsignmentClient {

    @POST
    @Path("/consignment-details/accept")
    Response acceptConsignment( // Change to Response
        @QueryParam("fileName") String fileName,
        @QueryParam("userId") int userId,
        @QueryParam("recordCount") int recordCount,
        @QueryParam("accept_record_count") int accept_record_count,
        @QueryParam("reject_record_count") int reject_record_count,
        @QueryParam("rejectedRows") String rejectedRows,
        @QueryParam("acceptedRows") String acceptedRows,
        @QueryParam("uploadedBy") String uploadedBy,
        @QueryParam("fileType") String fileType,
        List<ConsignDetailsDTO>  consignDetails
    );

    @POST
    @Path("/consignment-details/reject")
    Response addRejectedCount(
        @QueryParam("fileName") String fileName,
        @QueryParam("userId") int userId,
        @QueryParam("recordCount") int recordCount,
        @QueryParam("accept_record_count") int accept_record_count,
        @QueryParam("reject_record_count") int reject_record_count,
        @QueryParam("rejectedRows") String rejectedRows,
        @QueryParam("acceptedRows") String acceptedRows,
        @QueryParam("uploadedBy") String uploadedBy,
        @QueryParam("fileType") String fileType
        );

    @GET
    @Path("/consignment-details/search")
    Response searchConsignmentDetails(
            @QueryParam("consignment_id") String consignmentId,
            @QueryParam("address") String address,
            @QueryParam("city") String city,
            @QueryParam("account_no") String accountNo,
            @QueryParam("customer_cnic_number") String customer_cnic_number
    );
    @GET
    @Path("/consignment-details/generate")
//    @Produces(MediaType.TEXT_PLAIN)
     byte[] generateReport(
        @QueryParam("design") @DefaultValue("your-report.rptdesign") String designFile,
                                   @QueryParam("format") @DefaultValue("pdf") String format,
                                   @QueryParam("username") String username,
                                   @QueryParam("fromDate") Date fromDate,
                                   @QueryParam("toDate") Date toDate
                                   ) ;
    @GET
    @Path("/lov")
    Response getLOVs();
}