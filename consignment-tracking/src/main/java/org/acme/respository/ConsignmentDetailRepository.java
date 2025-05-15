package org.acme.respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.sql.DataSource;

import org.acme.exceptions.ConsignDatailsException;
import org.acme.exceptions.LogginFileDetailsException;
import org.acme.models.ConsignDatails;
// import org.acme.models.LogginFile;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
// import java.sql.Date;
import java.util.stream.Collectors;

@ApplicationScoped
public class ConsignmentDetailRepository {
    @Inject
    DataSource dataSource;

    private static final List<String> ALL_COLUMNS = Arrays.asList(
            "consignment_id", "courier", "booking_date", "account_no", "account_title",
            "shipping_bill", "address", "city", "email", "mobile_no", "letter_type",
            "card_no", "card_type", "card_creation_date", "return_reason", "return_date",
            "branch_cd", "receiver_name_b", "receiver_name_d", "delivery_date", "status",
            "relationship", "receiver_cnic", "card_status", "customer_cnic_number");

    private static final String INSERT_SQL_TEMPLATE = "INSERT INTO Consignment_Details ("
            + String.join(", ", ALL_COLUMNS) + ") VALUES ("
            + String.join(", ",
                    Arrays.asList(new String[ALL_COLUMNS.size()]).stream().map(s -> "?").toArray(String[]::new))
            + ")";

            public List<ConsignDatails> addConsignmentDetails(List<ConsignDatails> consignmentDetailsList,
            Connection connection) throws ConsignDatailsException {
        List<ConsignDatails> processedDetails = new ArrayList<>();

        try {
            for (ConsignDatails consignDetails : consignmentDetailsList) {
                Integer consignmentId = null;
                String status = null;
                String selectQuery = "SELECT consignment_id, status FROM Consignment_Details WHERE consignment_id = ?";
                System.out.println("consignDetails.getConsignment_id(): " + consignDetails.getConsignment_id());
                try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                    selectStatement.setObject(1,
                            consignDetails.getConsignment_id() != null ? consignDetails.getConsignment_id() : null);

                    try (ResultSet rs = selectStatement.executeQuery()) {
                        if (rs.next()) {
                            consignmentId = rs.getInt("consignment_id");
                            status = rs.getString("status");
                            System.out.println("Existing consignmentId and status: " + consignmentId + ", " + status);
                        } else {
                            System.out.println("Consignment ID not found. Proceeding with insertion.");
                        }
                    }
                }
                System.out.println("consignDetails.getConsignment_id(): " + consignDetails.getConsignment_id());
                System.out.println("consignDetails.getStatus() and status: " + consignDetails.getStatus() + status);

                if (consignmentId == null && status == null) {
                    // --- Insertion Logic (remains largely the same) ---
                    if ("In Transit".equals(consignDetails.getStatus())) {
                        List<Object> params = new ArrayList<>();

                        params.add(consignDetails.getConsignment_id() != null ? consignDetails.getConsignment_id() : null);
                        params.add(consignDetails.getCourier() != null ? consignDetails.getCourier() : null);
                        params.add(consignDetails.getBooking_date() != null ? consignDetails.getBooking_date() : null);
                        params.add(consignDetails.getAccount_no() != null ? consignDetails.getAccount_no() : null);
                        params.add(consignDetails.getAccount_title() != null ? consignDetails.getAccount_title() : null);
                        params.add(consignDetails.getShipping_bill() != null ? consignDetails.getShipping_bill() : null);
                        params.add(consignDetails.getAddress() != null ? consignDetails.getAddress() : null);
                        params.add(consignDetails.getCity() != null ? consignDetails.getCity() : null);
                        params.add(consignDetails.getEmail() != null ? consignDetails.getEmail() : null);
                        params.add(consignDetails.getMobile_no() != null ? consignDetails.getMobile_no() : null);
                        params.add(consignDetails.getLetter_type() != null ? consignDetails.getLetter_type() : null);
                        params.add(consignDetails.getCard_no() != null ? consignDetails.getCard_no() : null);
                        params.add(consignDetails.getCard_type() != null ? consignDetails.getCard_type() : null);
                        params.add(consignDetails.getCard_creation_date() != null ? consignDetails.getCard_creation_date(): null);
                        params.add(consignDetails.getReturn_reason() != null ? consignDetails.getReturn_reason() : null);
                        params.add(consignDetails.getReturn_date() != null ? consignDetails.getReturn_date() : null);
                        params.add(consignDetails.getBranch_cd() != null ? consignDetails.getBranch_cd() : null);
                        params.add(consignDetails.getReceiver_name_b() != null ? consignDetails.getReceiver_name_b(): null);
                        params.add(consignDetails.getReceiver_name_d() != null ? consignDetails.getReceiver_name_d(): null);
                        params.add(consignDetails.getDelivery_date() != null ? consignDetails.getDelivery_date() : null);
                        params.add(consignDetails.getStatus() != null ? consignDetails.getStatus() : null);
                        params.add(consignDetails.getRelationship() != null ? consignDetails.getRelationship() : null);
                        params.add(consignDetails.getReceiver_cnic() != null ? consignDetails.getReceiver_cnic() : null);
                        params.add(consignDetails.getCard_status() != null ? consignDetails.getCard_status() : null);
                        params.add(consignDetails.getCustomer_cnic_number() != null ? consignDetails.getCustomer_cnic_number(): null);


                        try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL_TEMPLATE)) {
                            for (int i = 0; i < params.size(); i++) {
                                insertStatement.setObject(i + 1, params.get(i));
                            }
                            insertStatement.executeUpdate();
                            processedDetails.add(consignDetails);
                        } catch (SQLException e) {
                            System.out.println("insert- inside catch");
                            throw new ConsignDatailsException(
                                    "Cannot accept consignment, no shipment found against consignment_id"
                                            + e.getMessage(),
                                    e);
                        }
                    } else {
                        String message = "Cannot accept consignment with ID " + consignDetails.getConsignment_id() + ": Invalid initial status '" + consignDetails.getStatus() + "' for a new record.";
                        System.out.println(message);
                        throw new ConsignDatailsException(message, null);
                    }

                } else {
                    // --- Dynamic Update Logic ---
                    if ("Delivered".equals(consignDetails.getStatus()) && "In Transit".equals(status)
                            || "Returned".equals(consignDetails.getStatus()) && "In Transit".equals(status)) {

                        List<String> setClauses = new ArrayList<>();
                        List<Object> params = new ArrayList<>();

                        // Dynamically build SET clauses and parameters based on non-null fields in consignDetails
                        if (consignDetails.getCourier() != null) {
                            setClauses.add("courier = ?");
                            params.add(consignDetails.getCourier());
                        }
                        if (consignDetails.getBooking_date() != null) {
                            setClauses.add("booking_date = ?");
                            params.add(consignDetails.getBooking_date());
                        }
                        if (consignDetails.getAccount_no() != null) {
                             setClauses.add("account_no = ?");
                             params.add(consignDetails.getAccount_no());
                         }
                         if (consignDetails.getAccount_title() != null) {
                             setClauses.add("account_title = ?");
                             params.add(consignDetails.getAccount_title());
                         }
                         if (consignDetails.getShipping_bill() != null) {
                             setClauses.add("shipping_bill = ?");
                             params.add(consignDetails.getShipping_bill());
                         }
                         if (consignDetails.getAddress() != null) {
                             setClauses.add("address = ?");
                             params.add(consignDetails.getAddress());
                         }
                         if (consignDetails.getCity() != null) {
                             setClauses.add("city = ?");
                             params.add(consignDetails.getCity());
                         }
                         if (consignDetails.getEmail() != null) {
                             setClauses.add("email = ?");
                             params.add(consignDetails.getEmail());
                         }
                         if (consignDetails.getMobile_no() != null) {
                             setClauses.add("mobile_no = ?");
                             params.add(consignDetails.getMobile_no());
                         }
                         if (consignDetails.getLetter_type() != null) {
                             setClauses.add("letter_type = ?");
                             params.add(consignDetails.getLetter_type());
                         }
                         if (consignDetails.getCard_no() != null) {
                             setClauses.add("card_no = ?");
                             params.add(consignDetails.getCard_no());
                         }
                         if (consignDetails.getCard_type() != null) {
                             setClauses.add("card_type = ?");
                             params.add(consignDetails.getCard_type());
                         }
                         if (consignDetails.getCard_creation_date() != null) {
                             setClauses.add("card_creation_date = ?");
                             params.add(consignDetails.getCard_creation_date());
                         }
                         if (consignDetails.getReturn_reason() != null) {
                             setClauses.add("return_reason = ?");
                             params.add(consignDetails.getReturn_reason());
                         }
                         if (consignDetails.getReturn_date() != null) {
                             setClauses.add("return_date = ?");
                             params.add(consignDetails.getReturn_date());
                         }
                         if (consignDetails.getBranch_cd() != null) {
                             setClauses.add("branch_cd = ?");
                             params.add(consignDetails.getBranch_cd());
                         }
                         if (consignDetails.getReceiver_name_b() != null) {
                             setClauses.add("receiver_name_b = ?");
                             params.add(consignDetails.getReceiver_name_b());
                         }
                         if (consignDetails.getReceiver_name_d() != null) {
                             setClauses.add("receiver_name_d = ?");
                             params.add(consignDetails.getReceiver_name_d());
                         }
                         if (consignDetails.getDelivery_date() != null) {
                             setClauses.add("delivery_date = ?");
                             params.add(consignDetails.getDelivery_date());
                         }
                         // Always include status if the update condition is met, as it's the trigger
                         if (consignDetails.getStatus() != null) {
                             setClauses.add("status = ?");
                             params.add(consignDetails.getStatus());
                         }
                         if (consignDetails.getRelationship() != null) {
                             setClauses.add("relationship = ?");
                             params.add(consignDetails.getRelationship());
                         }
                         if (consignDetails.getReceiver_cnic() != null) {
                             setClauses.add("receiver_cnic = ?");
                             params.add(consignDetails.getReceiver_cnic());
                         }
                         if (consignDetails.getCard_status() != null) {
                             setClauses.add("card_status = ?");
                             params.add(consignDetails.getCard_status());
                         }
                         if (consignDetails.getCustomer_cnic_number() != null) {
                             setClauses.add("customer_cnic_number = ?");
                             params.add(consignDetails.getCustomer_cnic_number());
                         }


                        // Only proceed with update if there are fields to update
                        if (!setClauses.isEmpty()) {
                            String updateSql = "UPDATE Consignment_Details SET "
                                    + String.join(", ", setClauses)
                                    + " WHERE consignment_id = ?";
                            params.add(consignmentId); // Add consignment_id for the WHERE clause

                            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                                int paramIndex = 1;
                                for (Object param : params) {
                                    updateStatement.setObject(paramIndex++, param);
                                }
                                updateStatement.executeUpdate();
                                processedDetails.add(consignDetails);
                            } catch (SQLException e) {
                                System.out.println("update- inside catch");
                                throw new ConsignDatailsException("Error updating consignment details with ID " + consignmentId + ": " + e.getMessage(), e);
                            }
                        } else {
                             System.out.println("No fields to update for consignment ID: " + consignmentId);
                             // Optionally add the original object to processedDetails if no update was needed but processed
                             // processedDetails.add(consignDetails);
                         }

                    }
                    else if ("Delivered".equals(consignDetails.getStatus()) && "Delivered".equals(status)) {
                        String message = "Already Delivered consignment with ID " + consignDetails.getConsignment_id() + ".";
                        System.out.println(message);
                        throw new ConsignDatailsException(message, null);
                    } else if ("Returned".equals(consignDetails.getStatus()) && "Returned".equals(status)) {
                        String message = "Already Returned consignment with ID " + consignDetails.getConsignment_id() + ".";
                        System.out.println(message);
                        throw new ConsignDatailsException(message, null);
                    } else if ("Returned".equals(consignDetails.getStatus()) && "Delivered".equals(status)) {
                        String message = "Can not Return, already Delivered consignment with ID " + consignDetails.getConsignment_id() + ".";
                        System.out.println(message);
                        throw new ConsignDatailsException(message, null);
                    } else if ("Delivered".equals(consignDetails.getStatus()) && "Returned".equals(status)) {
                         String message = "Can not Deliver, already Returned consignment with ID " + consignDetails.getConsignment_id() + ".";
                         System.out.println(message);
                         throw new ConsignDatailsException(message, null);
                     }
                     else if ("In Transit".equals(consignDetails.getStatus()) && "In Transit".equals(status)) {
                          String message = "Already Shipped consignment with ID " + consignDetails.getConsignment_id() + ".";
                          System.out.println(message);
                          throw new ConsignDatailsException(message, null);
                     }
                    else {
                        String message = "Can not update consignment with ID " + consignDetails.getConsignment_id() + ": Invalid status transition from '" + status + "' to '" + consignDetails.getStatus() + "'.";
                        System.out.println(message);
                        throw new ConsignDatailsException(message, null);
                    }
                }
                System.err.println("Processed consignDatails: " + consignDetails);
            }
            return processedDetails;

        } catch (SQLException e) {
            // Rollback transaction here if necessary
            throw new ConsignDatailsException("Failed to process consignment details. " + e.getMessage(), e);
        }
    }
    public void incrementAcceptedCount(String filename, int userId, int record_count, int accept_record_count, int reject_record_count,String rejectedRows,String acceptedRows,String uploadedBy,String fileType, Connection connection)
            throws SQLException {
                System.out.println("record_count="+record_count+","+"accept_record_count="+ accept_record_count+","+"reject_record_count="+ reject_record_count+","+"rejectedRows="+ rejectedRows+","+ "acceptedRows="+acceptedRows);
        String selectSql = "SELECT record_count, accept_record_count, reject_record_count FROM Logging_File_Details " +
                "WHERE file_name = ? AND user_id = ?" +
                "ORDER BY file_date DESC, file_time DESC LIMIT 1";

        String insertSql = "INSERT INTO Logging_File_Details " +
                "(user_id, file_name, file_date, file_time, record_count, accept_record_count, reject_record_count, rejectedRows, acceptedRows, uploadedBy, fileType) " +
                "VALUES (?, ?, CURRENT DATE, CURRENT TIME, ?, ?, ?, ?, ?, ?, ?)";

        try {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, filename);
                insertStmt.setInt(3, record_count);
                insertStmt.setInt(4, accept_record_count);
                insertStmt.setInt(5, reject_record_count);
                insertStmt.setString(6, rejectedRows);
                insertStmt.setString(7, acceptedRows);
                insertStmt.setString(8, uploadedBy);
                insertStmt.setString(9, fileType);
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("--=-=> " + e.toString());
            throw new SQLException("Error inserting accepted file log", e);
        }
    }

    public void incrementRejectedCount(String filename, int userId, int record_count, int accept_record_count, int reject_record_count,String rejectedRows,String acceptedRows,String uploadedBy,String fileType)
            throws LogginFileDetailsException {
                System.out.println("record_count="+record_count+","+"accept_record_count="+ accept_record_count+","+"reject_record_count="+ reject_record_count+","+"rejectedRows="+ rejectedRows+","+ "acceptedRows="+acceptedRows);

        String selectSql = "SELECT record_count,accept_record_count, reject_record_count FROM Logging_File_Details " +
                "WHERE file_name = ? AND user_id = ? " +
                "ORDER BY file_date DESC, file_time DESC LIMIT 1"; 


        String insertSql = "INSERT INTO Logging_File_Details " +
                "(user_id, file_name, file_date, file_time, record_count, accept_record_count, reject_record_count, rejectedRows, acceptedRows, uploadedBy, fileType) " +
                "VALUES (?, ?, CURRENT DATE, CURRENT TIME, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Insert new entry with updated counts
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, filename);
                insertStmt.setInt(3, record_count);
                insertStmt.setInt(4, accept_record_count);
                insertStmt.setInt(5, reject_record_count);
                insertStmt.setString(6, rejectedRows);
                insertStmt.setString(7, acceptedRows);
                insertStmt.setString(8, uploadedBy);
                insertStmt.setString(9, fileType);
                insertStmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            System.out.println("===> " + e.toString());
            throw new LogginFileDetailsException("Error inserting accepted file log" + e.getMessage(), e);
        }
    }

    public List<ConsignDatails> searchConsignmentDetails(Map<String, String> searchCriteria)
            throws ConsignDatailsException {
        List<ConsignDatails> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Consignment_Details WHERE ");
        System.out.println("sql 1 :" + sql);
        List<String> conditions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
                String columnName = entry.getKey();
                String value = entry.getValue();

                if (isValidColumnName(columnName)) {
                    conditions.add("UPPER("+columnName+") like UPPER('%"+value+"%')");
                } else {
                    throw new ConsignDatailsException("Invalid column name: " + columnName, null);
                }
            }

            if (conditions.isEmpty()) {
                throw new ConsignDatailsException("No search criteria provided.", null);
            }

            sql.append(String.join(" AND ", conditions));
            System.out.println("sql 2 :" + sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            // int parameterIndex = 1;
            // for (String value : searchCriteria.values()) {
            //     preparedStatement.setString(parameterIndex++, "'%"+value+"%'");
            //     System.out.println("PS : " +preparedStatement.toString());
            //     System.out.println("'%"+value+"%'");
            // }

           

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ConsignDatails consignDatails = new ConsignDatails();

                // getInt returns 0 for SQL NULL for primitive int
                consignDatails.setConsignment_id(resultSet.getInt("consignment_id"));

                // Add null checks for String fields using ternary operator
                consignDatails
                        .setCourier(resultSet.getString("courier") != null ? resultSet.getString("courier") : null);
                consignDatails.setAccount_no(
                        resultSet.getString("account_no") != null ? resultSet.getString("account_no") : null);
                consignDatails.setAccount_title(
                        resultSet.getString("account_title") != null ? resultSet.getString("account_title") : null);
                consignDatails.setShipping_bill(
                        resultSet.getString("shipping_bill") != null ? resultSet.getString("shipping_bill") : null);
                consignDatails
                        .setAddress(resultSet.getString("address") != null ? resultSet.getString("address") : null);
                consignDatails.setCity(resultSet.getString("city") != null ? resultSet.getString("city") : null);
                consignDatails.setEmail(resultSet.getString("email") != null ? resultSet.getString("email") : null);
                consignDatails.setMobile_no(
                        resultSet.getString("mobile_no") != null ? resultSet.getString("mobile_no") : null);
                consignDatails.setLetter_type(
                        resultSet.getString("letter_type") != null ? resultSet.getString("letter_type") : null);
                consignDatails
                        .setCard_no(resultSet.getString("card_no") != null ? resultSet.getString("card_no") : null);
                consignDatails.setCard_type(
                        resultSet.getString("card_type") != null ? resultSet.getString("card_type") : null);
                consignDatails.setReturn_reason(
                        resultSet.getString("return_reason") != null ? resultSet.getString("return_reason") : null);
                consignDatails.setBranch_cd(
                        resultSet.getString("branch_cd") != null ? resultSet.getString("branch_cd") : null);
                consignDatails.setReceiver_name_b(
                        resultSet.getString("receiver_name_b") != null ? resultSet.getString("receiver_name_b") : null);
                consignDatails.setReceiver_name_d(
                        resultSet.getString("receiver_name_d") != null ? resultSet.getString("receiver_name_d") : null);
                consignDatails.setStatus(resultSet.getString("status") != null ? resultSet.getString("status") : null);
                consignDatails.setRelationship(
                        resultSet.getString("relationship") != null ? resultSet.getString("relationship") : null);
                consignDatails.setReceiver_cnic(
                        resultSet.getString("receiver_cnic") != null ? resultSet.getString("receiver_cnic") : null);
                consignDatails.setCard_status(
                        resultSet.getString("card_status") != null ? resultSet.getString("card_status") : null);
                consignDatails.setCustomer_cnic_number(resultSet.getString("customer_cnic_number") != null
                        ? resultSet.getString("customer_cnic_number")
                        : null);

                // Add null checks for LocalDate fields using getObject and ternary operator
                consignDatails.setBooking_date(
                        resultSet.getObject("booking_date") != null
                                ? resultSet.getObject("booking_date", java.time.LocalDate.class)
                                : null);

                consignDatails.setCard_creation_date(
                        resultSet.getObject("card_creation_date") != null
                                ? resultSet.getObject("card_creation_date", java.time.LocalDate.class)
                                : null);

                consignDatails.setReturn_date(
                        resultSet.getObject("return_date") != null
                                ? resultSet.getObject("return_date", java.time.LocalDate.class)
                                : null);

                consignDatails.setDelivery_date(
                        resultSet.getObject("delivery_date") != null
                                ? resultSet.getObject("delivery_date", java.time.LocalDate.class)
                                : null);

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
                "relationship", "receiver_cnic", "card_status", "customer_cnic_number"
        };

        for (String validColumn : validColumns) {
            if (validColumn.equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }

}