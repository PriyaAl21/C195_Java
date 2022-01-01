package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.DataStorage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import utilities.Crud;

/**
 * This class renders the page that displays the appointment table by getting data from database
 */

public  class AppointmentScreen extends Crud implements Initializable, LambdaInterfaceOne, LambdaInterfaceTwo{
    public TableView aptTable;
    public TableColumn aptIdCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startDateTimeCol;
    public TableColumn endDateTimeCol;
    public TableColumn customerIDCol;
    public TableColumn userIdCol;
    public Button addAppointment;
    public Button modifyAppointment;
    public Button deleteAppointment;
    public RadioButton viewWeek;
    public ToggleGroup weekMonth;
    public RadioButton viewMonth;
    public RadioButton viewAll;
    public Button viewCustomers;
    public Button exitButton;
    public Button typeReport;
    public Button contactReport;
    public Button customerReport;

    /**
     * This method contains code that runs when the page loads
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ResultSet rs = null;

        try {
            rs = Select("select appointments.Appointment_ID,  appointments.Title, appointments.Description ,appointments.Location,contacts.contact_Name,\n" +
                    "appointments.Type,appointments.Start,appointments.End,appointments.Customer_ID,appointments.User_ID\n" +
                    "from appointments \n" +
                    "join contacts\n" + " on appointments.Contact_ID = contacts.Contact_ID\n order by appointments.Appointment_ID ");


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                Appointment.populate(rs);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }



        }

        try {
            check.checkForAppointment();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        aptIdCol.setCellValueFactory(new PropertyValueFactory<>("aptId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateNTime"));
        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateNTime"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        aptTable.setItems(DataStorage.getAllAppointments());



    }

    /**
     *  This is a Lambda expression which implements the LambdaInterfaceOne
     *  It checks for any appointment with in 15 min of log in time
     *  and sends an alert accordingly
     */
        LambdaInterfaceOne check = () -> {
        boolean foo = true;

        for (Appointment apt : DataStorage.getAllAppointments()) {
            LocalDateTime startDateNTime = apt.getStartDateNTime();
             int id = apt.getAptId();
             LocalDate date = apt.getStartDateNTime().toLocalDate();
             LocalTime aptTime = apt.getStartDateNTime().toLocalTime();
            // ZonedDateTime zonedDateTime = startDateTime.atZone(ZoneId.systemDefault());
            //ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(),ZoneId.of("America/Chicago"));
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
            LocalDateTime now = zonedDateTime.toLocalDateTime();
            //LocalDateTime now = LocalDateTime.now();

            //System.out.println(now.toString());

            if (now.toLocalDate().toString().equals(startDateNTime.toLocalDate().toString()) && startDateNTime.toLocalTime().isAfter(now.toLocalTime())) {
                //System.out.println(startDateNTime.toLocalTime().until(now.toLocalTime(), ChronoUnit.MINUTES));
                if (startDateNTime.toLocalTime().until(now.toLocalTime(), ChronoUnit.MINUTES) <= 15) {
                        foo = false;
                    //System.out.println("You have an appointment within 15 min!");
                    Alert time = new Alert(Alert.AlertType.INFORMATION);
                    time.setTitle("Appointment alert");
                    time.setContentText("You have an appointment within 15 min!\n"+
                            "Appointment ID : "+id+"\n" +
                            "Date : "+date+"\n"+
                    "Time : "+aptTime);
                    time.showAndWait();
                }
            }


        }
       if(foo==true) {
           Alert time = new Alert(Alert.AlertType.INFORMATION);
           time.setTitle("Appointment alert");
           time.setContentText("You do not have any upcoming appointments!");
           time.showAndWait();

       }

    };


    /**
     * This method navigates to the Add appointment page on clicking the add button
     * @param actionEvent
     */

    public void OnAddAppointment(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppointmentScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Appointment");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method navigates to Modify appointment page on clicking the edit button
     * @param actionEvent
     */

    public void OnModifyAppointment(ActionEvent actionEvent) {
        try {
            Appointment apt = (Appointment) aptTable.getSelectionModel().getSelectedItem();
            if(apt == null){
                Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                noSelection.setTitle("No Selection made");
                noSelection.setContentText("Please Select an Appointment to modify!");
                noSelection.showAndWait();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyAppointmentScreen.fxml"));
            Parent root = loader.load();
            ModifyAppointmentScreen ma = loader.getController();
            ma.fillDetails(apt);
            Stage stage = new Stage();
            stage.setTitle("Modify Appointment");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deletes the selected appointment and shows an alert message about it
     * @param actionEvent
     * @throws SQLException
     */

    public void OnDeleteAppointment(ActionEvent actionEvent) throws SQLException {

            if (aptTable.getSelectionModel().getSelectedItem() != null) {

                Appointment apt = (Appointment) aptTable.getSelectionModel().getSelectedItem();
                System.out.println(apt.getAptId());
                InsertUpdateDelete("Delete from appointments where Appointment_ID= "+ apt.getAptId());
                DataStorage.deleteAppointment(apt);

                Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                noSelection.setTitle("Appointment cancelled");
                noSelection.setContentText("Appointment with id - "+apt.getAptId()+"\n and type- "+"'"+apt.getType()+"'"+" is cancelled!");
                noSelection.showAndWait();

            }
            else{
                Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                noSelection.setTitle("No Selection made");
                noSelection.setContentText("Please Select an Appointment to Delete!");
                noSelection.showAndWait();
            }

    }

    /**
     * This method displays appointments for the week when the radio button is checked
     * @param actionEvent
     */

    public void OnViewWeek(ActionEvent actionEvent) {
        ObservableList<Appointment> weeklyApts = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        //Month month = today.getMonth();
        DayOfWeek day = today.getDayOfWeek();
        System.out.println(day.toString());
        int year = today.getYear();
        int n=0;
        switch(day){
            case SUNDAY:
                n= 6;
                break;
            case MONDAY:
                n=5;
                break;
            case TUESDAY:
                n=4;
                break;
            case WEDNESDAY:
                n=3;
                break;
            case THURSDAY:
                n=2;
                break;
            case FRIDAY:
                n=1;
                break;
            case SATURDAY:
                n=0;
                break;
        }

        ObservableList<LocalDate> dates = FXCollections.observableArrayList();
        LocalDate tomw ;
        for(int i=1; i<=n; i++){
            System.out.println("day- "+n);
            tomw = today.plusDays(i);
            System.out.println(tomw);
           dates.add(tomw);
        }
        for(Appointment apt : DataStorage.getAllAppointments()){
          if(dates.contains(apt.getStartDateNTime().toLocalDate())){
              weeklyApts.add(apt);
          }
        }
        aptTable.setItems(weeklyApts);

    }

    /**
     * This method displays appointments for the month when the radio button is checked
     * @param actionEvent
     */
    public void OnViewMonth(ActionEvent actionEvent) {
        ObservableList<Appointment> monthlyApts = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        Month month = today.getMonth();
        int year = today.getYear();
        for(Appointment apt : DataStorage.getAllAppointments()){
            if(apt.getStartDateNTime().getYear()==year){
                if(apt.getStartDateNTime().getMonth()==month){
                    monthlyApts.add(apt);
                }
            }
        }
        aptTable.setItems(monthlyApts);
    }

    /**
     * This is a Lambda Expression which implements the LambdaInterfacetwo interface
     * sets the apoointment table with the list of all  appointments
     */
    LambdaInterfaceTwo view = (ActionEvent actionEvent) -> {

        aptTable.setItems(DataStorage.getAllAppointments());

    };

    /**
     * This method displays all appointments when the view all radio button is clicked
     * @param actionEvent
     */

    public void OnViewAll(ActionEvent actionEvent) {
        view.viewAll(actionEvent);
    }


    /**
     * This method navigates to the customers page
     * @param actionEvent
     */
    public void OnViewCustomers(ActionEvent actionEvent) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Customers");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method closes the page when the exit button is clicked
     * @param actionEvent
     */
    public void OnExit(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * This method navigates to the Appointment type report screen when button is clicked
     * @param actionEvent
     */
    public void onTypeReport(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentTypeScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Appointment types");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method navigates to the contact appointments report page when the button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void onContactReport(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/ContactAppointments.fxml"));
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    /**
     * This method navigates to the customer appointments report page when the button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void onCustomerReport(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerAppointments.fxml"));
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }




    @Override
    public void viewAll(ActionEvent actionEvent) {

    }

    @Override
    public void checkForAppointment() {

    }
}
