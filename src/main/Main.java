package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utilities.JDBC;

import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {

    static Stage stage;

    public Main() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.stage = primaryStage;
        //Locale.setDefault(new Locale("fr", "FR"));
        Locale.setDefault(new Locale("en", "US"));
        ResourceBundle rb = ResourceBundle.getBundle("controller/language_files/rb");
        Parent main = null;
        //Parent root = FXMLLoader.load(getClass().getResource("/view/logInScreen.fxml"));
//        primaryStage.setTitle("Sign-In");
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/logInScreen.fxml"));
//        Parent root = loader.load();
//        controller.LogInScreen mc = loader.getController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/logInScreen.fxml"));
        loader.setResources(rb);
        main = loader.load();

        Scene scene = new Scene(main,800,500);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Stage getStage(){
        return stage;
    }





    public static void main(String[] args) {
        JDBC.makeConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
