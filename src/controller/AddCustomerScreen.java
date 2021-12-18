package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.DataStorage;
import model.Division;
import utilities.Crud;
//import controller.CustomerAppointmentScreen;
import utilities.DBQuery;
import utilities.JDBC;

import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static controller.LogInScreen.name;
//import static controller.LogInScreen.userName;

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

//        customer.setCreatedBy(createdBy);
//        customer.setLastUpdatedBy(lastUpdatedBy);
//        customer.setCreateDate(createDate);
//        customer.setLastUpdate(lastUpdate);

    }




    public void OnChooseCountry(ActionEvent actionEvent) throws Exception{
       // int id=0;
        ObservableList<String>divisionNames = FXCollections.observableArrayList();
        String one = (String) chooseCountry.getSelectionModel().getSelectedItem();
        //int countryId = Integer.parseInt(one);
//        ResultSet rs1 = (ResultSet) Select("Select * from countries where Country = "+ '"'+one+'"');
//        while (rs1.next()) {
//            id = Integer.parseInt(rs1.getString("Country_ID"));
//        }
//
//
//       ResultSet rs2 = (ResultSet) Select("Select * from first_level_divisions where Country_ID = "+ id);
//        while (rs2.next()) {
//           divisionNames.add(rs2.getString("Division"));
//        }

        ResultSet rs = Select("Select * from first_level_divisions join countries \n" +
                "on countries.Country_ID=first_level_divisions.Country_ID\n" +
                "where countries.Country = "+'"'+one+'"' );
        while (rs.next()) {
            divisionNames.add(rs.getString("Division"));
        }
        chooseDivision.setItems(divisionNames);

    }

    public void OnChooseDivision(ActionEvent actionEvent) throws SQLException {
        String one =  chooseDivision.getSelectionModel().getSelectedItem();
        ResultSet rs = (ResultSet) Select("Select * from first_level_divisions where Division = "+ '"'+one+'"');
        while (rs.next()) {
            divisionId = (Integer.parseInt(rs.getString("Division_ID")));
            System.out.println(divisionId);
        }

    }
//    public void OnCustomerName(ActionEvent actionEvent) {
//        customerName =  customerNameField.getText();
//        System.out.println(customerNameField.getText());
//
//    }
//
//    public void OnStreetName(ActionEvent actionEvent) {
//        streetName =  streetNameField.getText();
//
//    }
//
//    public void OnPhoneNum(ActionEvent actionEvent) {
//        phone =  phoneField.getText();
//
//    }
//
//    public void OnPostalCode(ActionEvent actionEvent) {
//        postalCode =  postalCodeField.getText();
//
//    }

    public void OnAddCustomer(ActionEvent actionEvent) throws Exception {
        customerName =  customerNameField.getText();
        streetName =  streetNameField.getText();
        phone =  phoneField.getText();
        postalCode =  postalCodeField.getText();


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

    }

    public void OnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


}
