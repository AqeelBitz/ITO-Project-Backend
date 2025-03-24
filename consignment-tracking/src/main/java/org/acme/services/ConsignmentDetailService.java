package org.acme.services;

import java.sql.SQLException;

import org.acme.models.ConsignDatails;
import org.acme.respository.ConsignmentDetailRepository;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConsignmentDetailService {

    @Inject
    ConsignmentDetailRepository consignmentDetailRepository;
    public ConsignDatails AddConsginmentDetails(ConsignDatails consignDatails) throws SQLException {
        return consignmentDetailRepository.AddConsginmentDetails(consignDatails);
    }
    
}
