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

/**
 * This is the main class which renders the application
 */
public class Main extends Application {

    static Stage stage;

    public Main() {
    }

    /**
     * This method navigates to the Log-in screen
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.stage = primaryStage;

        //uncomment following line to test changing to French language setting
        //Locale.setDefault(new Locale("fr", "FR"));

        Locale.setDefault(new Locale("en", "US"));
        ResourceBundle rb = ResourceBundle.getBundle("controller/language_files/rb");
        Parent main = null;
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


    /**
     * This method makes the connection with database on starting the application and disconnects when it is closed
     * @param args
     */

    public static void main(String[] args) {
        JDBC.makeConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
