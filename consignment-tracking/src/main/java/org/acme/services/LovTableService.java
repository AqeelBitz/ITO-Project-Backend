package org.acme.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.models.LOVTable;
import org.acme.respository.LovTabelRepository;

import java.sql.SQLException;
import java.util.List;


@ApplicationScoped
public class LovTableService {

    @Inject
    LovTabelRepository lovTabelRepository;

    public List<LOVTable> findAllLovs() throws SQLException {
        return lovTabelRepository.findAllLov();
    }
}
