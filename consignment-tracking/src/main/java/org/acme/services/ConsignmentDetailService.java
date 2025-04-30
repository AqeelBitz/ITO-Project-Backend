package org.acme.services;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.acme.exceptions.ConsignDatailsException;

// import java.sql.SQLException;

import org.acme.models.ConsignDatails;
import org.acme.respository.ConsignmentDetailRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConsignmentDetailService {

    @Inject
    ConsignmentDetailRepository consignmentDetailRepository;
    
    public List<ConsignDatails> AddConsginmentDetails(List<ConsignDatails> consignDatails, Connection connection) throws  ConsignDatailsException{
        
        return consignmentDetailRepository.addConsignmentDetails(consignDatails, connection);
    }

        public List<ConsignDatails> searchConsignmentDetails(Map<String, String> searchCriteria) throws ConsignDatailsException {
        return consignmentDetailRepository.searchConsignmentDetails(searchCriteria);
    }
    
}
