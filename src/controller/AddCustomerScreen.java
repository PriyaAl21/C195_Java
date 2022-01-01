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
import model.Country;
import model.Customer;
import model.DataStorage;
import model.Division;
import utilities.Crud;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static controller.LogInScreen.name;


/**
 * This class adds new customer to the database and the datastorage lists in the application
 * @author Priya
 */

public class AddCustomerScreen extends Crud implements Initializable {
    public TextField customerNameField;
    public TextField streetNameField;
    public TextField phoneField;
    public TextField postalCodeField;
    public ComboBox<String> chooseCountry;
    public ComboBox<String> chooseDivision;
    public Button addCustomerButton;
    public Button OnCancelAdd;

    public String customerName;
    public String streetName;
    public String phone;
    public String postalCode;
    public LocalDateTime createDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdatedBy;
    public int divisionId;
    public Button cancelButton;

    //public Customer customer;

    /**
     * This method contains code that runs when this page loads
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // customer = new Customer();
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        try {
            ResultSet rs = (ResultSet) Select("select * from countries");
            while (rs.next()) {
                countryNames.add(rs.getString("Country"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        chooseCountry.setItems(countryNames);

        createdBy = name;
        lastUpdatedBy = name;
        createDate= LocalDateTime.now();
        lastUpdate=Timestamp.valueOf(LocalDateTime.now());


    }


    /**
     * This method creates a list of divisions to choose from depending on country chosen
     * @param actionEvent
     * @throws Exception
     */

    public void OnChooseCountry(ActionEvent actionEvent) throws Exception{
       // int id=0;
        ObservableList<String>divisionNames = FXCollections.observableArrayList();
        String one = (String) chooseCountry.getSelectionModel().getSelectedItem();


        ResultSet rs = Select("Select * from first_level_divisions join countries \n" +
                "on countries.Country_ID=first_level_divisions.Country_ID\n" +
                "where countries.Country = "+'"'+one+'"' );
        while (rs.next()) {
            divisionNames.add(rs.getString("Division"));
        }
        chooseDivision.setItems(divisionNames);

    }

    /**
     * This method gets the division id from the database based on the division name chosen in the combo box
     * @param actionEvent
     * @throws SQLException
     */

    public void OnChooseDivision(ActionEvent actionEvent) throws SQLException {
        String one =  chooseDivision.getSelectionModel().getSelectedItem();

        ResultSet rs = (ResultSet) Select("Select * from first_level_divisions where Division = "+ '"'+one+'"');
        while (rs.next()) {
            divisionId = (Integer.parseInt(rs.getString("Division_ID")));

        }


    }

    /**
     * This method creates a new Customer object that is added to the database and the local datastorage lists
     * @param actionEvent
     * @return
     * @throws Exception
     */
    public int OnAddCustomer(ActionEvent actionEvent) throws Exception {
        customerName =  customerNameField.getText();
        streetName =  streetNameField.getText();
        phone =  phoneField.getText();
        postalCode =  postalCodeField.getText();

        if( customerName.equals("")||streetName.equals("")||phone.equals("")||postalCode.equals("")){
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


        System.out.println(divisionId);
        if(chooseCountry.getSelectionModel().getSelectedItem() == null) {
        Alert empty = new Alert(Alert.AlertType.INFORMATION);
        empty.setTitle("Empty fields");
        empty.setContentText("Please choose country and division!");
        empty.showAndWait();
        return 1;
        }
        else if (divisionId==0) {
            Alert empty = new Alert(Alert.AlertType.INFORMATION);
            empty.setTitle("Empty fields");
            empty.setContentText("Please choose division!");
            empty.showAndWait();
            return 1;
        }


        InsertUpdateDelete("Insert into customers(Customer_Name,Address,Postal_Code,Phone,Create_Date,Created_By,Last_Update,Last_Updated_By,Division_ID)"+
                " Values("+'"'+customerName+'"' +","+'"'+streetName+'"'+","+'"'+postalCode+'"'+","+'"'+phone+'"'+","+'"'+createDate+'"'+","+
                '"'+ createdBy+'"'+","+'"'+lastUpdate+'"'+","+'"'+lastUpdatedBy+'"'+","+divisionId+")");

        ResultSet rs = Select("Select * from customers order by CUSTOMER_ID DESC LIMIT 1");
        while (rs.next()) {
            Customer.populate(rs);
        }

        //DataStorage.addCustomer(customer);
        Stage stage = (Stage) addCustomerButton.getScene().getWindow();
        stage.close();

        return 1;

    }

    /**
     * This method closes this page when the cancel button is clicked
     * @param actionEvent
     */
    public void OnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


}
