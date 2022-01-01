package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * This class creates a Customer object
 * @author Priya
 */
public class Customer {

    /**
     * This method returns customer id
     * @return customerId
     */

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    /**
     * This method returns customer name
     * @return customer name
     */

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * This method returns customer address
     * @return address
     */

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * This method returns postal code
     * @return postal code
     */

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * This method returns customer phone number
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * This method returns created date
     * @return createDate
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * This method returns user name who added this customer
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    /**
     * This method returns divisionId
     * @return divisionId
     */
    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int customerId;
    public String customerName;
    public String address;
    public String postalCode;
    public String phone;
    public LocalDateTime createDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdatedBy;
    public int divisionId;

    public Customer(){

    }

    /**
     * This method is a constructor for creating an instance of the Customer class
     * @param customerId
     * @param customerName
     * @param address
     * @param phone
     * @param postalCode
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param divisionId
     */
    public Customer(int customerId,String customerName,String address,String phone,String postalCode,LocalDateTime createDate,String createdBy,Timestamp lastUpdate,
                    String lastUpdatedBy,int divisionId){
        this.customerId = customerId ;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;

    }


    /**
     * This method adds customers to Datastorage with data obtained from the database
     * @param rs
     * @throws SQLException
     */

    public static void populate(ResultSet rs) throws SQLException {

            int customerId = Integer.parseInt(rs.getString("Customer_ID"));
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = Integer.parseInt(rs.getString("Division_ID"));

            Customer one = new Customer(customerId, customerName, address, phone, postalCode, createDate, createdBy, lastUpdate,
                    lastUpdatedBy, divisionId);

                DataStorage.getAllCustomers().add(one);

    }

}
