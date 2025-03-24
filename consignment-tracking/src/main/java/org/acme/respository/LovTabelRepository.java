package org.acme.respository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.models.LOVTable;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LovTabelRepository {

    @Inject
    DataSource dataSource;

    public List<LOVTable> findAllLov() throws SQLException {

        List<LOVTable> lovs = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT lov_id, lov_value FROM LOV_TABLE");
            ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()){
                lovs.add(new LOVTable(
                        resultSet.getLong("lov_id"),
                        resultSet.getString("lov_value")
                ));
            }
        }
        return lovs;
    }

}
