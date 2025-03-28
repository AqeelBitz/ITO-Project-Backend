package org.acme.services;

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
    public ConsignDatails AddConsginmentDetails(ConsignDatails consignDatails) throws  ConsignDatailsException{
        return consignmentDetailRepository.addConsignmentDetails(consignDatails);
    }

        public List<ConsignDatails> searchConsignmentDetails(Map<String, String> searchCriteria) throws ConsignDatailsException {
        return consignmentDetailRepository.searchConsignmentDetails(searchCriteria);
    }
    
}
