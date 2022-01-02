package controller;


import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * This Interface has one method declared in it which is used as lambda expression in the application
 * (in the controller.AppointmentScreen)
 */
public interface LambdaInterfaceOne {
    void checkForAppointment() throws SQLException;
}
