//package controller;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.stage.Stage;
//import model.Appointment;
//import model.Customer;
//import model.DataStorage;
//import utilities.Crud;
//import utilities.JDBC;
//
//import java.net.URL;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.ResourceBundle;
//
//public class CustomerAppointmentScreen extends Crud  implements Initializable {
//
//    public TableView<Customer> customerTable;
//    public TableColumn<Customer,Integer> customerIdCol;
//    public TableColumn<Customer, String> customerNameCol;
//    public TableColumn<Customer, String> addressCol;
//    public TableColumn<Customer, String> postalCodeCol;
//    public TableColumn<Customer, String> phoneCol;
//    public TableColumn<Customer, LocalDateTime> createDateCol;
//    public TableColumn<Customer, String> createdByCol;
//    public TableColumn<Customer, Timestamp> lastUpdateCol;
//    public TableColumn<Customer, String> lastUpdatedByCol;
//    public TableColumn<Customer, Integer> divisionIdCol;
//    public Button addApptButton;
//    public Button editApptButton;
//    public Button deleteApptButton;
//    public Button aptDisplayButton;
//    public RadioButton radio;
//    public ToggleGroup weekMonth;
//
//
//
//    public ObservableList<Customer> getDbList() {
//        return dbList;
//    }
//
//    public ObservableList<Customer> dbList = FXCollections.observableArrayList();
//    public TableView aptTable;
//    public TableColumn<Appointment,Integer> aptIdCol;
//    public TableColumn titleCol;
//    public TableColumn descriptionCol;
//    public TableColumn locationCol;
//    public TableColumn contactCol;
//    public TableColumn typeCol;
//    public TableColumn startDateTimeCol;
//    public TableColumn endDateTimeCol;
//    public TableColumn customerIDCol;
//    public TableColumn userIdCol;
//
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//        ResultSet rs = null;
//        try {
//            rs = Select("Select * from customers");
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        while (true) {
//            try {
//                if (!rs.next()) break;
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//
//            try {
//                Customer.populate(rs);
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//
//            }
//
//
//            customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
//            customerNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
//            addressCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
//            postalCodeCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
//            phoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
//            createDateCol.setCellValueFactory(new PropertyValueFactory<Customer, LocalDateTime>("createDate"));
//            createdByCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("createdBy"));
//            lastUpdateCol.setCellValueFactory(new PropertyValueFactory<Customer, Timestamp>("lastUpdate"));
//            lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastUpdatedBy"));
//            divisionIdCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("divisionId"));
//
//            customerTable.setItems(DataStorage.getAllCustomers());
//
//
//        ResultSet rs1 = null;
//        try {
//         rs1 = Select("select appointments.Appointment_ID,  appointments.Title, appointments.Description ,appointments.Location,contacts.contact_Name,\n" +
//                    "appointments.Type,appointments.Start,appointments.End,appointments.Customer_ID,appointments.User_ID\n" +
//                    "from appointments \n" +
//                    "join contacts\n" +" on appointments.Contact_ID = contacts.Contact_ID");
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        while (true) {
//            try {
//                if (!rs1.next()) break;
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//
//            try {
//                Appointment.populate(rs,"contact");
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//
//        }
//
//        aptIdCol.setCellValueFactory(new PropertyValueFactory<>("aptId"));
//        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
//        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
//        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
//        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
//        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
//        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateNTime"));
//        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateNTime"));
//        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
//        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
//
//        aptTable.setItems(DataStorage.getAllAppointments());
//
//            }
//
//
//
//
//
//
//
//
//    public void onAdd(ActionEvent actionEvent) {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/view/AddCustomerScreen.fxml"));
//            Stage stage = new Stage();
//            stage.setTitle("Add Customer");
//            stage.setScene(new Scene(root, 800, 600));
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void onEdit(ActionEvent actionEvent) {
//        if ((Customer) customerTable.getSelectionModel().getSelectedItem() != null) {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyCustomerScreen.fxml"));
//                Parent root = loader.load();
//                ModifyCustomerScreen mc = loader.getController();
//                mc.populateModifyForm((Customer) customerTable.getSelectionModel().getSelectedItem());
//                Stage stage = new Stage();
//                stage.setTitle("Modify Customer");
//                stage.setScene(new Scene(root, 800, 600));
//                stage.show();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        else{
//            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
//            noSelection.setTitle("No Selection made");
//            noSelection.setContentText("Please Select a Customer to Modify!");
//            noSelection.showAndWait();
//        }
//
//    }
//
//    public void onDelete(ActionEvent actionEvent) {
//    }
//
//    public void onAddAppt(ActionEvent actionEvent) {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppointmentScreen.fxml"));
//            Stage stage = new Stage();
//            stage.setTitle("Add Customer");
//            stage.setScene(new Scene(root, 800, 600));
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void onEditAppt(ActionEvent actionEvent) {
//    }
//
//    public void onDeleteAppt(ActionEvent actionEvent) {
//    }
//
////    public void OnDisplay(ActionEvent actionEvent) throws SQLException {
////        Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
////        int customerId = customer.getCustomerId();
////
////
////        while (rs.next()) {
////
////            Appointment.populate(rs);
////        }
////
////
////    }
//
//    public void OnSelectWeek(ActionEvent actionEvent) {
//    }
//
//    public void OnSelectMonth(ActionEvent actionEvent) {
//    }
//}
