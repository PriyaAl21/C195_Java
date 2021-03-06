package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import model.DataStorage;
import utilities.Crud;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static controller.LogInScreen.name;

/**
 * This class contains methods to modify customer information
 */
public class ModifyCustomerScreen extends Crud implements Initializable {
    public TextField customerNameId;
    public TextField customerNameField;
    public TextField streetNameField;
    public TextField phoneField;
    public TextField postalCodeField;
    public ComboBox<String> chooseCountry;
    public ComboBox<String> chooseDivision;
    public Button modifyCustomerButton;
    public Button OnCancel;
    public Customer customer;
    public Button cancelButton;

    /**
     * This method gets the details of the selected customer and populates them in to modify customer form
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void populateModifyForm(Customer customer) throws SQLException {
        this.customer = customer;
        customerNameId.setPromptText(String.valueOf(customer.getCustomerId()));
        customerNameField.setText(customer.getCustomerName());
        streetNameField.setText(customer.getAddress());
        phoneField.setText(customer.getPhone());
        postalCodeField.setText(customer.getPostalCode());
        int id = customer.getDivisionId();
        ResultSet rs3 = Select("Select * from first_level_divisions where Division_ID = "+ id);
        String divName = null;
        while(rs3.next()){
            divName = rs3.getString("Division");
        }

        chooseDivision.setPromptText(divName);


        String countryName = null;
        ResultSet rs = Select("Select * from countries join first_level_divisions \n" +
                "on countries.Country_ID=first_level_divisions.Country_ID\n" +
                "where first_level_divisions.Division_ID = " + customer.getDivisionId());
        while (rs.next()) {
            countryName = rs.getString("Country");
            chooseCountry.setPromptText(countryName);
        }

        ObservableList<String> countryNames = FXCollections.observableArrayList();
        try {
            ResultSet rs1= (ResultSet) Select("select * from countries");
            while (rs1.next()) {
                countryNames.add(rs1.getString("Country"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        chooseCountry.setItems(countryNames);


        ObservableList<String>divisionNames = FXCollections.observableArrayList();

        ResultSet rs2 = Select("Select * from first_level_divisions join countries \n" +
                "on countries.Country_ID=first_level_divisions.Country_ID\n" +
                "where countries.Country = "+'"'+countryName+'"' );
        while (rs2.next()) {
            divisionNames.add(rs2.getString("Division"));
        }
        chooseDivision.setItems(divisionNames);
    }

    /**
     * This method creates a list of divisions to choose from based on country selected
     * @param actionEvent
     * @throws SQLException
     */
    public void OnChooseCountry(ActionEvent actionEvent) throws SQLException {
        chooseDivision.setPromptText("State/Province");
        String countryName = chooseCountry.getSelectionModel().getSelectedItem();
        ObservableList<String>divisionNames = FXCollections.observableArrayList();

        ResultSet rs2 = Select("Select * from first_level_divisions join countries \n" +
                "on countries.Country_ID=first_level_divisions.Country_ID\n" +
                "where countries.Country = "+'"'+countryName+'"' );
        while (rs2.next()) {
            divisionNames.add(rs2.getString("Division"));
        }
        chooseDivision.setItems(divisionNames);
    }

    public void OnChooseDivision(ActionEvent actionEvent) {
    }

    /**
     * This method updates customer information on the new data provided and also does validation checks in the input data
     * @param actionEvent
     * @return
     * @throws SQLException
     */
    public int OnUpdateCustomer(ActionEvent actionEvent) throws SQLException {
        int customerId = customer.getCustomerId();
        String customerName = customerNameField.getText();
        String street = streetNameField.getText();
        String phone = phoneField.getText();
        String postalCode= postalCodeField.getText();

        if( customerName.equals("")||street.equals("")||phone.equals("")||postalCode.equals("")){
            Alert empty = new Alert(Alert.AlertType.INFORMATION);
            empty.setTitle("Empty fields");
            empty.setContentText("Please fill all fields!");
            empty.showAndWait();
            return 1;
        }
        else{
            try{
                Integer.parseInt(phone);
                Integer.parseInt(postalCode);
            }
            catch(NumberFormatException e){
                Alert empty = new Alert(Alert.AlertType.INFORMATION);
                empty.setTitle("Empty fields");
                empty.setContentText("Please enter  numbers for phone and postal code!");
                empty.showAndWait();
                return 1;
            }
        }


        LocalDateTime createDate = customer.getCreateDate();
        String createdBy = customer.getCreatedBy();
        String division =  chooseDivision.getSelectionModel().getSelectedItem();
        Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now());
       String lastUpdatedBy = LogInScreen.name;

        int divisionId = 0;
        ResultSet rs = (ResultSet) Select("Select * from first_level_divisions where Division = "+ '"'+division+'"');

        while (rs.next()) {
            divisionId = Integer.parseInt((rs.getString("Division_ID")));

            InsertUpdateDelete("UPDATE customers SET Customer_Name = " + '"' + customerName + '"' + " ,Address = " + '"' + street + '"' +
                    " ,Postal_Code = " + '"' + postalCode + '"' + " ,Phone = " + '"' + phone + '"' + " ,Create_Date = " + '"' + createDate + '"' +
                    " ,Created_By = " + '"' + createdBy + '"' + " ,Last_Update = " + '"' + lastUpdate + '"' + " ,Last_Updated_By = " +
                    '"' + lastUpdatedBy + '"' + " ,Division_ID = " + divisionId + " WHERE Customer_ID = " + customerId);
        }

        Customer modified = new Customer(customerId,customerName,street,phone,postalCode,createDate,createdBy,lastUpdate,lastUpdatedBy,divisionId);
        DataStorage.modifyCustomer(modified);

        Stage stage = (Stage) modifyCustomerButton.getScene().getWindow();
        stage.close();

        return 1;

    }
    /**
     * This method closes the screen when cancel button is clicked
     * @param actionEvent
     */
    public void OnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


}
