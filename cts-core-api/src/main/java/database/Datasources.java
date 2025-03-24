package database;

import io.agroal.api.AgroalDataSource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

@RequestScoped
public class Datasources {

    @Inject
    AgroalDataSource conventionalDataSource;

    public Connection getConnection() {
        try
        {
            return conventionalDataSource.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}