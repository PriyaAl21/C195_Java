package model;

;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

/**
 * This class creates an DataStorage object which stores all customers and appointments
 * It also contains the methods to add,update or delete customers and appointments
 * @author Priya
 */
public class DataStorage {

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    /**
     * This method adds a customer to the list of customers
     *
     * @param customer
     */
    public static void addCustomer(Customer customer) {

       allCustomers.add(customer);

    }

    /**
     * This method adds an appointment to the list of appointments.
     *
     * @param appointment
     */
    public static void addAppointment(Appointment appointment) {
        allAppointments.add(appointment);
    }

    /**
     * This method retrieves all customers from a list of customers
     * @return allCustomers
     * */
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    /**
     * This method retrieves all appointments from a list of appointments
     * @return allAppointments
     * */
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }


    /**
     * This method modifies an existing customer by deleting the old one and creating a new one with same id
     * it also sorts the all customers list
     * @param customer
     */

    public static void modifyCustomer(Customer customer) {
        for(Customer c: allCustomers){
            if(c.getCustomerId()==customer.getCustomerId()){
           allCustomers.remove(c);
           allCustomers.add(customer);
            Comparator<Customer> CustomerComparator = Comparator.comparing(Customer::getCustomerId);
            allCustomers.sort(CustomerComparator);
           break;
            }
        }
    }

    /**
     * This method modifies an existing appointment by deleting the old one and creating a new one with same id
     * it also sorts the all customers list
     * @param apt
     */
    public static void modifyAppointment(Appointment apt) {
        for(Appointment c: allAppointments){
            if(c.getAptId()==apt.getAptId()){
                allAppointments.remove(c);
                allAppointments.add(apt);
                Comparator<Appointment> AptComparator = Comparator.comparing(Appointment::getAptId);
                allAppointments.sort(AptComparator);
                break;
            }
        }
    }

    /**
     * This method deletes a customer and their corresponding appointments
     * @param customer
     */

    public static void deleteCustomer(Customer customer) {
        int cusId = customer.getCustomerId();
        boolean next = false;
        int size = allAppointments.size();
        System.out.println(size);
        for(int i=0;i<=size;i++) {
            System.out.println(i);
            if(i==size){
                System.out.println(i);
                next=true;
               break;
            }
            if (allAppointments.get(i).getCustomerId() == cusId) {
                allAppointments.remove(allAppointments.get(i));
                size--;
                i--;
            }

        }

        if(next == true){
                for (Customer c : allCustomers) {
                    if (c.getCustomerId() == customer.getCustomerId()) {
                        allCustomers.remove(c);

                        break;
                    }
                }
        }

    }


    public static void deleteAppointmentWithCustomer(Customer customer){
        for (Appointment c : allAppointments) {
            if (c.getCustomerId() == customer.getCustomerId()) {
                allAppointments.remove(c);

            }
        }
    }

    /**
     * This method deletes an appointment by its id
     * @param apt
     */

    public static void deleteAppointment(Appointment apt){
        for (Appointment c : allAppointments) {
            if (c.getAptId() == apt.getAptId()) {
                allAppointments.remove(c);
                break;
            }
        }
    }

    public static void clean(){
        for (Customer c : allCustomers) {

            allCustomers.remove(c);


        }
    }


    }



