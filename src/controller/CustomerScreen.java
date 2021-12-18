package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import model.DataStorage;
import utilities.Crud;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class CustomerScreen extends Crud implements Initializable {
    public TableView customerTable;
    public TableColumn customerIdCol;
    public TableColumn customerNameCol;
    public TableColumn addressCol;
    public TableColumn postalCodeCol;
    public TableColumn phoneCol;
    public TableColumn createDateCol;
    public TableColumn createdByCol;
    public TableColumn lastUpdateCol;
    public TableColumn lastUpdatedByCol;
    public TableColumn divisionIdCol;
    public Button addCustomer;
    public Button modifyCustomer;
    public Button deleteCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResultSet rs = null;
        try {
            rs = Select("Select * from customers");
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
                Customer.populate(rs);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }


        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<Customer, LocalDateTime>("createDate"));
        createdByCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("createdBy"));
        lastUpdateCol.setCellValueFactory(new PropertyValueFactory<Customer, Timestamp>("lastUpdate"));
        lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastUpdatedBy"));
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("divisionId"));

        customerTable.setItems(DataStorage.getAllCustomers());

    }

    public void onAddCustomer(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AddCustomerScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Customer");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnModifyCustomer(ActionEvent actionEvent) {
        if ((Customer) customerTable.getSelectionModel().getSelectedItem() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyCustomerScreen.fxml"));
                Parent root = loader.load();
                ModifyCustomerScreen mc = loader.getController();
                mc.populateModifyForm((Customer) customerTable.getSelectionModel().getSelectedItem());
                Stage stage = new Stage();
                stage.setTitle("Modify Customer");
                stage.setScene(new Scene(root, 800, 600));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
            noSelection.setTitle("No Selection made");
            noSelection.setContentText("Please Select a Customer to Modify!");
            noSelection.showAndWait();
        }
    }

    public void onDeleteCustomer(ActionEvent actionEvent) throws SQLException {
        if ((Customer) customerTable.getSelectionModel().getSelectedItem() != null) {

                Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
               System.out.println(customer.getCustomerId());
               InsertUpdateDelete("Delete from customers where Customer_ID= "+ customer.getCustomerId());
               InsertUpdateDelete("Delete from appointments where Customer_ID= "+customer.getCustomerId());
               DataStorage.deleteAppointmentWithCustomer(customer);
                DataStorage.deleteCustomer(customer);
        }
        else{
            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
            noSelection.setTitle("No Selection made");
            noSelection.setContentText("Please Select a Customer to Delete!");
            noSelection.showAndWait();
        }
    }


}
