package entities;
import java.sql.*;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ConsignDetailsDTO {
    
    public Integer consignment_id;
    public String courier;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date booking_date;
    public String account_no;
    public String account_title;
    public String shipping_bill;
    public String address;
    public String city;
    public String email;
    public String mobile_no;
    public String letter_type;
    public String card_no;
    public String card_type;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date card_creation_date;
    public String return_reason;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date return_date;
    public String branch_cd;
    public String receiver_name_b;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date delivery_date;
    public String status;
    public String receiver_name_d;
    public String relationship;
    public String receiver_cnic;
    public String card_status;
    public String customer_cnic_number;
    public String return_date_courier;
    public void setCustomer_cnic_number(String customer_cnic_number) {
        this.customer_cnic_number = customer_cnic_number;
    }

    public String getCustomer_cnic_number() {
        return customer_cnic_number;
    }

    public void setReturn_date_courier(String return_date_courier) {
        this.return_date_courier = return_date_courier;
    }

    public String getReturn_date_courier() {
        return return_date_courier;
    }

    public Integer getConsignment_id() {
        return consignment_id;
    }

    public void setConsignment_id(Integer consignment_id) {
        this.consignment_id = consignment_id;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public Date getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(Date booking_date) {
        this.booking_date = booking_date;
    }

    public String getShipping_bill() {
        return shipping_bill;
    }

    public void setShipping_bill(String shipping_bill) {
        this.shipping_bill = shipping_bill;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount_title() {
        return account_title;
    }

    public void setAccount_title(String account_title) {
        this.account_title = account_title;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getLetter_type() {
        return letter_type;
    }

    public void setLetter_type(String letter_type) {
        this.letter_type = letter_type;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public Date getCard_creation_date() {
        return card_creation_date;
    }

    public void setCard_creation_date(Date card_creation_date) {
        this.card_creation_date = card_creation_date;
    }

    public String getReturn_reason() {
        return return_reason;
    }

    public void setReturn_reason(String return_reason) {
        this.return_reason = return_reason;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }

    public String getBranch_cd() {
        return branch_cd;
    }

    public void setBranch_cd(String branch_cd) {
        this.branch_cd = branch_cd;
    }

    public String getReceiver_name_b() {
        return receiver_name_b;
    }

    public void setReceiver_name_b(String receiver_name_b) {
        this.receiver_name_b = receiver_name_b;
    }

    public Date getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(Date delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiver_name_d() {
        return receiver_name_d;
    }

    public void setReceiver_name_d(String receiver_name_d) {
        this.receiver_name_d = receiver_name_d;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getReceiver_cnic() {
        return receiver_cnic;
    }

    public void setReceiver_cnic(String receiver_cnic) {
        this.receiver_cnic = receiver_cnic;
    }

    public String getCard_status() {
        return card_status;
    }

    public void setCard_status(String card_status) {
        this.card_status = card_status;
    }

    @Override
    public String toString() {
        return "ConsignDatails [consignment_id=" + consignment_id + ", courier=" + courier + ", booking_date="
                + booking_date + ", account_no=" + account_no + ", account_title=" + account_title + ", shipping_bill="
                + shipping_bill + ", address=" + address + ", city=" + city + ", email=" + email + ", mobile_no="
                + mobile_no + ", letter_type=" + letter_type + ", card_no=" + card_no + ", card_type=" + card_type
                + ", card_creation_date=" + card_creation_date + ", return_reason=" + return_reason + ", return_date="
                + return_date + ", branch_cd=" + branch_cd + ", receiver_name_b=" + receiver_name_b + ", delivery_date="
                + delivery_date + ", status=" + status + ", receiver_name_d=" + receiver_name_d + ", relationship="
                + relationship + ", receiver_cnic=" + receiver_cnic + ", card_status=" + card_status + "]";
    }
}
