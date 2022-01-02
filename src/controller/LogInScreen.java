package controller;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import utilities.JDBC;
import java.io.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

import static main.Main.getStage;

/**
 * This class contains methods to display a log-in page and validates the inputs for username and password
 * @author Priya
 */
public class LogInScreen implements Initializable {

    @FXML
    private Label signIn;

    @FXML
    private Label pwd;
    @FXML
   private Label usName;
    @FXML
  private TextField userName;
    @FXML
    private TextField password;
    @FXML
    private Button logInButton;
    @FXML
    private Label zoneId;

    public static String name;


    ResourceBundle rb;

    /**
     * This method shows the time zone of user when the page is displayed and sets error messages in English and French
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            this.rb = rb;

        //outputs time zone
        String location = String.valueOf(ZoneId.systemDefault());
        zoneId.setText(rb.getString("time") + "- "+ location );

        //translates to English/ French depending on user settings

         getStage().setTitle(rb.getString("title"));
         signIn.setText(rb.getString("title"));
         usName.setText(rb.getString("first"));
         pwd.setText(rb.getString("second"));
         logInButton.setText(rb.getString("log"));

    }


    /**
     * This method gets the inputs for username and password and validates them for a successful login or sends an alert
     * with appropriate error messages
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */

    public void onLogIn(ActionEvent actionEvent) throws SQLException, IOException {
        String name = userName.getText();
        String pass = password.getText();
        String sqlStatement = "SELECT * FROM users WHERE User_Name =" + '"' + name + '"';

        Statement st = JDBC.getConnection().createStatement();
        st.execute(sqlStatement);

        ResultSet rs = st.getResultSet();


        if (!rs.isBeforeFirst()) {
          if(name.equals((""))) {
              alertError(3,"none"); }
           else alertError(1,name);
        }

        while (rs.next()) {

                try {
                        String user = rs.getString("User_Name");
                        String pwd = rs.getString("Password");

                        if (name.equals(user)) {
                            if (pass.equals(pwd)) {
                                this.name = user;
                                System.out.println("login");
                                addToLog(user,"successful");
                                Stage primaryStage = new Stage();
                                Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentScreen.fxml"));
                                primaryStage.setTitle("Welcome");
                                primaryStage.setScene(new Scene(root, 1200, 700));
                                primaryStage.show();
                                Stage stage = (Stage) logInButton.getScene().getWindow();
                                stage.close();
                                break;
                            }

                            else{
                                if(pass.equals("")) alertError(4, name);
                                else {alertError(2, name);}
                            }
                        } else {
                            System.out.println("Incorrect username/password!");
                            alertError(2, name);
                        }
                } catch (Exception e) {
                    System.out.println("Incorrect username/password!");
                    alertError(2, name);
                    }
                break;

        }
    }
    @FXML
     void alertError(int n, String user) throws IOException {
        Alert some = new Alert(Alert.AlertType.INFORMATION);
        if(n==1) {
            some.setTitle("Not found");
            some.setContentText(rb.getString("error1"));
            addToLog(user, "failed");
        }
        else if(n==2){
            some.setTitle("Error");
            some.setContentText(rb.getString("error2"));
            addToLog(user, "failed");
        }
        else if(n==3){
            some.setTitle("Error");
            some.setContentText(rb.getString("error3"));
        }
        else if(n==4){
            some.setTitle("Error");
            some.setContentText(rb.getString("error4"));
            addToLog(user, "failed");
        }


        some.showAndWait();
    }


    /**
     * This method creates a text file which contains information about each log-in attempts
     * @param user
     * @param loginAttempt
     * @throws IOException
     */
    public void addToLog(String user, String loginAttempt) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        Timestamp ts = Timestamp.valueOf(now);
        String filename = "login_activity.txt";
        PrintWriter outputfile = new PrintWriter(new FileWriter(filename, true));
        outputfile.println("Username - "+ user);
        System.out.println(user);
        System.out.println("Date - "+ today);
        outputfile.println("Date - "+ today);
        outputfile.println("Time - "+ ts);
        outputfile.println("Login attempt "+ loginAttempt+"\n\n");
       // outputfile.flush();
         outputfile.close();
    }
    public void onUserName(ActionEvent actionEvent) {
    }

    public void onPassword(ActionEvent actionEvent) {
    }
}
