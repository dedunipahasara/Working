package com.example.layeredarchitecture.controller;

import com.example.layeredarchitecture.dao.CustomerDao;
import com.example.layeredarchitecture.dao.CustomerDaoImpl;

import com.example.layeredarchitecture.model.CustomerDTO;
import com.example.layeredarchitecture.view.tdm.CustomerTM;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;



public class ManageCustomersFormController {
    public AnchorPane root;
    public TextField txtCustomerName;
    public TextField txtCustomerId;
    public JFXButton btnDelete;
    public JFXButton btnSave;
    public TextField txtCustomerAddress;
    public TableView<CustomerTM> tblCustomers;
    public JFXButton btnAddNewCustomer;
    CustomerDao customerDaoImpl =new CustomerDaoImpl();
    
    public void initialize() {
        tblCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        initUI();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtCustomerId.setText(newValue.getId());
                txtCustomerName.setText(newValue.getName());
                txtCustomerAddress.setText(newValue.getAddress());

                txtCustomerId.setDisable(false);
                txtCustomerName.setDisable(false);
                txtCustomerAddress.setDisable(false);
            }
        });

        txtCustomerAddress.setOnAction(event -> btnSave.fire());
        loadAllCustomers();
    }

    private void loadAllCustomers() {
        tblCustomers.getItems().clear();
        /*Get all customers*/
         try {
              
              ArrayList<CustomerDTO> allCustomer=customerDaoImpl.getAllCustomer();
              for(CustomerDTO c:allCustomer){
                tblCustomers.getItems().
                add(new CustomerTM(c.getId(),c.getName(),c.getAddress()));
              }


           /*  Connection connection = DBConnection.getDbConnection().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Customer");

            while (rst.next()) {
                tblCustomers.getItems().add(new CustomerTM(rst.getString("id"), rst.getString("name"), rst.getString("address")));
            }*/
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }

    private void initUI() {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerId.setDisable(true);
        txtCustomerName.setDisable(true);
        txtCustomerAddress.setDisable(true);
        txtCustomerId.setEditable(false);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/com/example/layeredarchitecture/main-form.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(() -> primaryStage.sizeToScene());
    }

    public void btnAddNew_OnAction(ActionEvent actionEvent) {
        txtCustomerId.setDisable(false);
        txtCustomerName.setDisable(false);
        txtCustomerAddress.setDisable(false);
        txtCustomerId.clear();
        txtCustomerId.setText(generateNewId());
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerName.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblCustomers.getSelectionModel().clearSelection();
    }


    public void btnSave_OnAction(ActionEvent actionEvent) {
        String id = txtCustomerId.getText();
        String name = txtCustomerName.getText();
        String address = txtCustomerAddress.getText();

        if (!name.matches("[A-Za-z ]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid name").show();
            txtCustomerName.requestFocus();
            return;
        } else if (!address.matches(".{3,}")) {
            new Alert(Alert.AlertType.ERROR, "Address should be at least 3 characters long").show();
            txtCustomerAddress.requestFocus();
            return;
        }

        if (btnSave.getText().equalsIgnoreCase("save")) {
            /*Save Customer*/
            try {
                if (existCustomer(id)) {
                    new Alert(Alert.AlertType.ERROR, id + " already exists").show();
                }
                
                CustomerDTO customerDTO= new CustomerDTO(id,name,address);
                boolean isSaved=customerDaoImpl.saveCustomer(customerDTO);
                
  
                if(isSaved){
                      tblCustomers.getItems().add(new CustomerTM(id, name, address));
                }
            } catch (SQLException e) { 
                new Alert(Alert.AlertType.ERROR, "Failed to save the customer " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            /*Update customer*/
            try {
                if (!existCustomer(id)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
                }
                  CustomerDao customerDaoImpl =new CustomerDaoImpl();
                CustomerDTO customerDTO= new CustomerDTO(id,name,address);
                boolean isUpdate=customerDaoImpl.updateCustomer(customerDTO);

                if(isUpdate){
                      System.out.println("updated");
                }
                
               
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the customer " + id + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            CustomerTM selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
            selectedCustomer.setName(name);
            selectedCustomer.setAddress(address);
            tblCustomers.refresh();
        }

        btnAddNewCustomer.fire();
    }


    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
       
        boolean isExist =customerDaoImpl.existCustomer(id);
        return isExist;
    }
 

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        /*Delete Customer*/
        String id = tblCustomers.getSelectionModel().getSelectedItem().getId();
        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }

            

                boolean isDeleted=customerDaoImpl.deleteCustomer(id);
                
           if(isDeleted){
              tblCustomers.getItems().remove(tblCustomers.getSelectionModel().getSelectedItem());
            tblCustomers.getSelectionModel().clearSelection();
            initUI();
           }

           

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String generateNewId() {
        
            
        String id = null;
        try{
            
            id= customerDaoImpl.generateNewCustomerId(id);
        
            
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new id " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

            return id;

    

    }

   /*  private String getLastCustomerId() {
        List<CustomerTM> tempCustomersList = new ArrayList<>(tblCustomers.getItems());
        Collections.sort(tempCustomersList);
        return tempCustomersList.get(tempCustomersList.size() - 1).getId();
    }*/

    
       
}
   


