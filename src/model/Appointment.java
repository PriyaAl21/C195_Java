package model;

import com.mysql.cj.protocol.Resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

    public int getAptId() {
        return aptId;
    }

    public void setAptId(int aptId) {
        this.aptId = aptId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStartDateNTime() {
        return startDateNTime;
    }

    public void setStartDateNTime(LocalDateTime startDateNTime) {
        this.startDateNTime = startDateNTime;
    }

    public LocalDateTime getEndDateNTime() {
        return endDateNTime;
    }

    public void setEndDateNTime(LocalDateTime endDateNTime) {
        this.endDateNTime = endDateNTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public static void populate(ResultSet rs)throws SQLException {

        int aptId = Integer.parseInt(rs.getString("Appointment_ID"));
        String title = rs.getString("Title");
        String description = rs.getString("Description");
        String location = rs.getString("Location");
        String contact = rs.getString("Contact_Name");
        String type = rs.getString("Type");
      //
        LocalDateTime start =rs.getTimestamp("Start").toLocalDateTime();

        ZonedDateTime ldtZoned = start.atZone(ZoneId.systemDefault());
        LocalDateTime startDateNTime = ldtZoned.toLocalDateTime();
       // String str = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(changedDateTime);

     //check if appointments are within 15 min of local time zone

        LocalDateTime now = LocalDateTime.now();
        //System.out.println(now.toString());
        System.out.println(startDateNTime.toLocalDate());
        System.out.println(now.toLocalDate());
        if(now.toLocalDate().toString().equals(startDateNTime.toLocalDate().toString()) && startDateNTime.toLocalTime().isAfter(now.toLocalTime()) ) {
            System.out.println(startDateNTime.toLocalTime().until(now.toLocalTime(), ChronoUnit.MINUTES));
            if (startDateNTime.toLocalTime().until(now.toLocalTime(), ChronoUnit.MINUTES) <= 15) {

                System.out.println("You have an appointment within 15 min!");

            }
        }
       // System.out.println(startDT);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime localDateTime= LocalDateTime.parse(start.toString(), formatter);
//        ZonedDateTime zonedDateTime = localDateTime
//                .atZone(ZoneId.of("UTC", ZoneId.SHORT_IDS))
//                .withZoneSameInstant(ZoneId.of("EST", ZoneId.SHORT_IDS));
//        System.out.println(zonedDateTime);

       // LocalDateTime startDateNTime = rs.getTimestamp("Start").toLocalDateTime();;


        LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
        ZonedDateTime ldtZoned1 = end.atZone(ZoneId.systemDefault());
        LocalDateTime endDateNTime = ldtZoned1.toLocalDateTime();
       //
        int customerId =(rs.getInt("Customer_ID"));
        int userId =(rs.getInt("User_ID"));

        Appointment one = new Appointment(aptId, title, description, location, contact,type,
                startDateNTime, endDateNTime, customerId,  userId);
        DataStorage.getAllAppointments().add(one);




    }
}
