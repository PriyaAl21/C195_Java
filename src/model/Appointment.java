package model;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

/**
 * This class creates an Appointment object/instance
 * @author Priya
 */


public class Appointment {
    public int aptId;
    public String title;
    public String description;
    public String location;
    public String contact;
    public String type;
    public LocalDateTime startDateNTime;
    public LocalDateTime endDateNTime;
    public int customerId;
    public int userId;

    /**
     * This method is a constructor for creating an instance of the Appointment class
     * @param aptId
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param type
     * @param startDateNTime
     * @param endDateNTime
     * @param customerId
     * @param userId
     */

    public Appointment(int aptId, String title, String description, String location, String contact, String type,
                       LocalDateTime startDateNTime, LocalDateTime endDateNTime, int customerId, int userId) {
        this.aptId = aptId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.startDateNTime = startDateNTime;
        this.endDateNTime = endDateNTime;
        this.customerId = customerId;
        this.userId = userId;
    }

    public Appointment(int aptId, String title, String description, String type, LocalDateTime startDateNTime, LocalDateTime endDateNTime, int customerId) {
        this.aptId = aptId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.startDateNTime = startDateNTime;
        this.endDateNTime = endDateNTime;
        this.customerId = customerId;

    }


    /**
     * This method returns Appointment id of an appointment
     * @return aptId
     */

    public int getAptId() {
        return aptId;
    }

    public void setAptId(int aptId) {
        this.aptId = aptId;
    }

    /**
     * This method returns title of an appointment
     * @return title
     */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method returns description of an appointment
     * @return description
     */

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method returns location of an appointment
     * @return location
     */

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * This method returns contact name of an appointment
     * @return contact
     */

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * This method returns type of an appointment
     * @return type
     */

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * This method returns start date and time of an appointment
     * @return startDateNTime
     */

    public LocalDateTime getStartDateNTime() {
        return startDateNTime;
    }

    public void setStartDateNTime(LocalDateTime startDateNTime) {
        this.startDateNTime = startDateNTime;
    }

    /**
     * This method returns end date and time of an appointment
     * @return endDateNTime
     */
    public LocalDateTime getEndDateNTime() {
        return endDateNTime;
    }

    public void setEndDateNTime(LocalDateTime endDateNTime) {
        this.endDateNTime = endDateNTime;
    }

    /**
     * This method returns customer id of an appointment
     * @return customerId
     */

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * This method returns user id of an appointment
     * @return userId
     */

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    /**
     * This method adds appoinments to Datastorage with data obtained from the database
     * @param rs
     * @throws SQLException
     */
    public static void populate(ResultSet rs)throws SQLException {

        int aptId = Integer.parseInt(rs.getString("Appointment_ID"));
        String title = rs.getString("Title");
        String description = rs.getString("Description");
        String location = rs.getString("Location");
        String contact = rs.getString("Contact_Name");
        String type = rs.getString("Type");

        LocalDateTime start =rs.getTimestamp("Start").toLocalDateTime();
        LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
        //ZonedDateTime ldtZoned = start.atZone(ZoneId.systemDefault());
        ZonedDateTime utcStart = start.atZone(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = end.atZone(ZoneId.of("UTC"));
        ZonedDateTime ldtZoned = utcStart.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime endZoned = utcEnd.withZoneSameInstant(ZoneId.systemDefault());

        //ZonedDateTime ldtZoned = start.atZone(ZoneId.of("America/Chicago"));
        LocalDateTime startDateNTime = ldtZoned.toLocalDateTime();
        LocalDateTime endDateNTime = endZoned.toLocalDateTime();


            int customerId = (rs.getInt("Customer_ID"));
            int userId = (rs.getInt("User_ID"));

            Appointment one = new Appointment(aptId, title, description, location, contact, type,
                    startDateNTime, endDateNTime, customerId, userId);
            DataStorage.getAllAppointments().add(one);
    }


}
