package com.example.layeredarchitecture.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.example.layeredarchitecture.model.CustomerDTO;

public interface CustomerDao {

    public ArrayList<CustomerDTO> getAllCustomer()throws SQLException, ClassNotFoundException;
       



    public boolean saveCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;
      

    public boolean updateCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException;
     
    

    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
      
    public String generateNewCustomerId(String id) throws SQLException, ClassNotFoundException;

   
    


    public boolean existCustomer(String id) throws SQLException, ClassNotFoundException;




    public CustomerDTO getAllcusromersById(String newValue) throws ClassNotFoundException, SQLException;




   
    
}
