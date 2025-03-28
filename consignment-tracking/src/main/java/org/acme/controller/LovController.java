package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.models.LOVTable;
import org.acme.services.LovTableService;

import java.sql.SQLException;
import java.util.List;

@Path("api/data-access/lov")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LovController {
    @Inject
    LovTableService lovTableService;
    @GET
    public List<LOVTable> findAllLovs() throws SQLException {
        return lovTableService.findAllLovs();
    }
}
