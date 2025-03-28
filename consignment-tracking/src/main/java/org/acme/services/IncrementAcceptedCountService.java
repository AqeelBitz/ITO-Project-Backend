package org.acme.services;

import java.sql.SQLException;

import org.acme.respository.ConsignmentDetailRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class IncrementAcceptedCountService {
    
    @Inject
    ConsignmentDetailRepository consignmentDetailRepository;
    public void incrementAcceptedCount(Integer file_id) throws SQLException{
        consignmentDetailRepository.incrementAcceptedCount(file_id);
    }
}
