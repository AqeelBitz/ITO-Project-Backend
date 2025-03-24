package org.acme.controller;

import java.sql.SQLException;

import org.acme.models.ConsignDatails;
import org.acme.services.ConsignmentDetailService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("consignment-details")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsignmentDetailController {
    
    @Inject
    ConsignmentDetailService consignmentDetailService;

    @POST
    public ConsignDatails AddConsginmentDetails(ConsignDatails consignDatails) throws SQLException{
        return consignmentDetailService.AddConsginmentDetails(consignDatails);
    }
}
