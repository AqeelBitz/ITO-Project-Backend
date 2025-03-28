package org.acme.respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.acme.exceptions.ConsignDatailsException;
import org.acme.models.ConsignDatails;
import org.acme.models.LogginFile;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConsignmentDetailRepository {
    @Inject
    DataSource dataSource;


    public ConsignDatails addConsignmentDetails(ConsignDatails consignDatails) throws ConsignDatailsException {
        try (Connection connection = dataSource.getConnection()) {
            List<String> columns = new ArrayList<>();
            List<String> values = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            addToLists(consignDatails.getConsignment_id(), "consignment_id", columns, values, params);
            addToLists(consignDatails.getCourier(), "courier", columns, values, params);
            addToLists(consignDatails.getBooking_date(), "booking_date", columns, values, params);
            addToLists(consignDatails.getAccount_no(), "account_no", columns, values, params);
            addToLists(consignDatails.getAccount_title(), "account_title", columns, values, params);
            addToLists(consignDatails.getShipping_bill(), "shipping_bill", columns, values, params);
            addToLists(consignDatails.getAddress(), "address", columns, values, params);
            addToLists(consignDatails.getCity(), "city", columns, values, params);
            addToLists(consignDatails.getEmail(), "email", columns, values, params);
            addToLists(consignDatails.getMobile_no(), "mobile_no", columns, values, params);
            addToLists(consignDatails.getLetter_type(), "letter_type", columns, values, params);
            addToLists(consignDatails.getCard_no(), "card_no", columns, values, params);
            addToLists(consignDatails.getCard_type(), "card_type", columns, values, params);
            addToLists(consignDatails.getCard_creation_date(), "card_creation_date", columns, values, params);
            addToLists(consignDatails.getReturn_reason(), "return_reason", columns, values, params);
            addToLists(consignDatails.getReturn_date(), "return_date", columns, values, params);
            addToLists(consignDatails.getBranch_cd(), "branch_cd", columns, values, params);
            addToLists(consignDatails.getReceiver_name_b(), "receiver_name_b", columns, values, params);
            addToLists(consignDatails.getReceiver_name_d(), "receiver_name_d", columns, values, params);
            addToLists(consignDatails.getDelivery_date(), "delivery_date", columns, values, params);
            addToLists(consignDatails.getStatus(), "status", columns, values, params);
            addToLists(consignDatails.getRelationship(), "relationship", columns, values, params);
            addToLists(consignDatails.getReceiver_cnic(), "receiver_cnic", columns, values, params);
            addToLists(consignDatails.getCard_status(), "card_status", columns, values, params);

            String sql = "INSERT INTO Consignment_Details (" + String.join(", ", columns) + ") VALUES (" + String.join(", ", values) + ")";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            preparedStatement.executeUpdate();
            return consignDatails;

        } catch (SQLException e) {
            throw new ConsignDatailsException("Failed to add consignment details: " + e.getMessage(), e);
        }
    }

    private void addToLists(Object value, String columnName, List<String> columns, List<String> values, List<Object> params) {
        if (value != null) {
            columns.add(columnName);
            values.add("?");
            params.add(value instanceof java.util.Date ? new java.sql.Date(((java.util.Date) value).getTime()) : value);
        }
    }

    public void UpdateLoggingFileDetails(int fileId) throws SQLException {
        String sql = "UPDATE Logging_File_Details SET accpeted_count = accpeted_count + 1 WHERE file_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, fileId);
            preparedStatement.executeUpdate();
        }
    }

        public List<ConsignDatails> searchConsignmentDetails(Map<String, String> searchCriteria) throws ConsignDatailsException {
        List<ConsignDatails> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Consignment_Details WHERE ");
        List<String> conditions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
                String columnName = entry.getKey();
                String value = entry.getValue();

                // Sanitize column names to prevent SQL injection (important!)
                if (isValidColumnName(columnName)) {
                    conditions.add(columnName + " = ?");
                } else {
                    throw new ConsignDatailsException("Invalid column name: " + columnName, null);
                }
            }

            if (conditions.isEmpty()) {
                // No search criteria provided, return all or throw an exception
                throw new ConsignDatailsException("No search criteria provided.", null);
                // or return all data
                // PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Consignment_Details");
            }

            sql.append(String.join(" AND ", conditions));
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            int parameterIndex = 1;
            for (String value : searchCriteria.values()) {
                preparedStatement.setString(parameterIndex++, value);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ConsignDatails consignDatails = new ConsignDatails();
                consignDatails.setConsignment_id(resultSet.getInt("consignment_id"));
                consignDatails.setCourier(resultSet.getString("courier"));
                consignDatails.setBooking_date(resultSet.getDate("booking_date"));
                consignDatails.setAccount_no(resultSet.getString("account_no"));
                consignDatails.setAccount_title(resultSet.getString("account_title"));
                consignDatails.setShipping_bill(resultSet.getString("shipping_bill"));
                consignDatails.setAddress(resultSet.getString("address"));
                consignDatails.setCity(resultSet.getString("city"));
                consignDatails.setEmail(resultSet.getString("email"));
                consignDatails.setMobile_no(resultSet.getString("mobile_no"));
                consignDatails.setLetter_type(resultSet.getString("letter_type"));
                consignDatails.setCard_no(resultSet.getString("card_no"));
                consignDatails.setCard_type(resultSet.getString("card_type"));
                consignDatails.setCard_creation_date(resultSet.getDate("card_creation_date"));
                consignDatails.setReturn_reason(resultSet.getString("return_reason"));
                consignDatails.setReturn_date(resultSet.getDate("return_date"));
                consignDatails.setBranch_cd(resultSet.getString("branch_cd"));
                consignDatails.setReceiver_name_b(resultSet.getString("receiver_name_b"));
                consignDatails.setReceiver_name_d(resultSet.getString("receiver_name_d"));
                consignDatails.setDelivery_date(resultSet.getDate("delivery_date"));
                consignDatails.setStatus(resultSet.getString("status"));
                consignDatails.setRelationship(resultSet.getString("relationship"));
                consignDatails.setReceiver_cnic(resultSet.getString("receiver_cnic"));
                consignDatails.setCard_status(resultSet.getString("card_status"));
                results.add(consignDatails);
            }

        } catch (SQLException e) {
            throw new ConsignDatailsException("Failed to search consignment details: " + e.getMessage(), e);
        }

        return results;
    }

    private boolean isValidColumnName(String columnName) {
        // Implement logic to check if the column name is valid
        // Example: Compare against a list of known column names
        String[] validColumns = {
            "consignment_id", "courier", "booking_date", "account_no", "account_title",
            "shipping_bill", "address", "city", "email", "mobile_no", "letter_type",
            "card_no", "card_type", "card_creation_date", "return_reason", "return_date",
            "branch_cd", "receiver_name_b", "receiver_name_d", "delivery_date", "status",
            "relationship", "receiver_cnic", "card_status"
        };

        for (String validColumn : validColumns) {
            if (validColumn.equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }

}
