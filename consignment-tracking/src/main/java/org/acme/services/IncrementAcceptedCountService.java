package org.acme.services;

import java.sql.Connection;
import java.sql.SQLException;

import org.acme.respository.ConsignmentDetailRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class IncrementAcceptedCountService {
    
    @Inject
    ConsignmentDetailRepository consignmentDetailRepository;
    public void incrementAcceptedCount(String file_name, Integer file_id, Integer record_count,Integer accept_record_count, Integer reject_record_count, String rejectedRows, String acceptedRows, String uploadedBy, String fileType,   Connection connection) throws SQLException{
        consignmentDetailRepository.incrementAcceptedCount(file_name, file_id, record_count,accept_record_count, reject_record_count,rejectedRows, acceptedRows, uploadedBy,fileType,  connection);
    }
}
