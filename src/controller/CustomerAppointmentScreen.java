package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utilities.Crud;

import java.sql.ResultSet;
import java.sql.SQLException;

/**This class creates a report showing customer appointments by month
 * @author Priya
 *
 */
public class CustomerAppointmentScreen extends Crud {
    public Button Jan;
    public Button feb;
    public Button march;
    public Button may;
    public Button june;
    public Button july;
    public Button oct;
    public Button sep;
    public Button nov;
    public Button Aug;
    public Button april;
    public Button dec;
    public TableView<customerAppointment> cusTable;
    public TableColumn cusAptCol;
    public TableColumn countCol;
    public Button close;

    /**
     * This class creates a customerAppointment object
     */

    public static class customerAppointment{
        public String customerName;
        public int count;

        /**
         * This constructor method creates an instance of the customerAppointment class
         * @param customerName
         * @param count
         */
        public customerAppointment(String customerName, int count) {
            this.customerName = customerName;
            this.count = count;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String type) {
            this.customerName = customerName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    /**
     * This method gets the number of appointments for a customer and customer name in a given month
     * @param month
     * @throws SQLException
     */
    public void getCount(int month) throws SQLException {
        String customerName;
        int count;
        ObservableList<customerAppointment>aptTypeItems = FXCollections.observableArrayList();

        ResultSet rs = Select("SELECT customers.Customer_Name, count(*)\n" +
                "FROM appointments\n" +
                "JOIN customers\n" +
                "ON appointments.Customer_ID = customers.Customer_ID\n" +
                "where EXTRACT(month FROM appointments.start)= "+month+ "\n" +
                "group by Customer_Name");
        while (rs.next()) {
            customerName = rs.getString("Customer_Name");
            count = Integer.parseInt(rs.getString("count(*)"));
            customerAppointment aptType = new customerAppointment(customerName, count);
            aptTypeItems.add(aptType);
        }
        cusAptCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("Count"));
        cusTable.setItems(aptTypeItems);
    }


    /**
     * This method gives the number of appointments for a customer in January
     * @param actionEvent
     * @throws SQLException
     */
    public void onJan(ActionEvent actionEvent) throws SQLException {
        getCount(1);

    }

    public void onFeb(ActionEvent actionEvent) throws SQLException {
        getCount(2);
    }
    /**
     * This method gives the number of appointments for a customer in March
     * @param actionEvent
     * @throws SQLException
     */
    public void onMarch(ActionEvent actionEvent) throws SQLException {
        getCount(3);
    }

    public void onMay(ActionEvent actionEvent) throws SQLException {
        getCount(5);
    }

    public void onJune(ActionEvent actionEvent) throws SQLException {
        getCount(6);
    }
    /**
     * This method gives the number of appointments for a customer in July
     * @param actionEvent
     * @throws SQLException
     */
    public void onJuly(ActionEvent actionEvent) throws SQLException {
        getCount(7);
    }

    public void onOct(ActionEvent actionEvent) throws SQLException {
        getCount(10);
    }
    /**
     * This method gives the number of appointments for a customer in September
     * @param actionEvent
     * @throws SQLException
     */
    public void onSep(ActionEvent actionEvent) throws SQLException {
        getCount(9);
    }

    public void onNov(ActionEvent actionEvent) throws SQLException {
        getCount(11);
    }

    public void onAug(ActionEvent actionEvent) throws SQLException {
        getCount(8);
    }
    /**
     * This method gives the number of appointments for a customer for April
     * @param actionEvent
     * @throws SQLException
     */
    public void onApril(ActionEvent actionEvent) throws SQLException {
        getCount(4);
    }

    public void onDec(ActionEvent actionEvent) throws SQLException {
        getCount(12);
    }

    /**
     * This method closes the screen on clicking the close button
     * @param actionEvent
     */
    public void OnClose(ActionEvent actionEvent) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
}
