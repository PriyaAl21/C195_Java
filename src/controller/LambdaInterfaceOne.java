package controller;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface LambdaInterfaceOne {
    void checkForAppointment(ResultSet rs) throws SQLException;
}
