package org.acme.respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.acme.models.ConsignDatails;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConsignmentDetailRepository {
    @Inject
    DataSource dataSource;

    public ConsignDatails AddConsginmentDetails(ConsignDatails consignDatails) throws SQLException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Consignment_Details(consignment_id, courier, booking_date, account_no, account_title, shipping_bill, address, city, email, mobile_no, letter_type, card_no, card_type, card_creation_date, return_reason, return_date, branch_cd, receiver_name_b, receiver_name_d, delivery_date,status, relationship, receiver_cnic, card_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, consignDatails.getConsignment_id());
            preparedStatement.setString(2, consignDatails.getCourier());
            preparedStatement.setDate(3, new java.sql.Date(consignDatails.getBooking_date().getTime()));
            preparedStatement.setString(4, consignDatails.getAccount_no());
            preparedStatement.setString(5, consignDatails.getAccount_title());
            preparedStatement.setString(6, consignDatails.getShipping_bill());
            preparedStatement.setString(7, consignDatails.getAddress());
            preparedStatement.setString(8, consignDatails.getCity());
            preparedStatement.setString(9, consignDatails.getEmail());
            preparedStatement.setString(10, consignDatails.getMobile_no());
            preparedStatement.setString(11, consignDatails.getLetter_type());
            preparedStatement.setString(12, consignDatails.getCard_no());
            preparedStatement.setString(13, consignDatails.getCard_type());
            preparedStatement.setDate(14, new java.sql.Date(consignDatails.getCard_creation_date().getTime()));
            preparedStatement.setString(15, consignDatails.getReturn_reason());
            preparedStatement.setDate(16, new java.sql.Date(consignDatails.getReturn_date().getTime()));
            preparedStatement.setString(17, consignDatails.getBranch_cd());
            preparedStatement.setString(18, consignDatails.getReceiver_name_b());
            preparedStatement.setString(19, consignDatails.getReceiver_name_d());
            preparedStatement.setDate(20, new java.sql.Date(consignDatails.getDelivery_date().getTime()));
            preparedStatement.setString(21, consignDatails.getStatus());
            preparedStatement.setString(22, consignDatails.getRelationship());
            preparedStatement.setString(23, consignDatails.getReceiver_cnic());
            preparedStatement.setString(24, consignDatails.getCard_status());

            preparedStatement.executeUpdate();
            return consignDatails;

        } catch (SQLException e) {
            throw e;
        }
    }

}
