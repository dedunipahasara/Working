package com.example.layeredarchitecture.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.layeredarchitecture.db.DBConnection;

public class PlaceOrderDaoImpl {

    public String generateOrderId() throws SQLException, ClassNotFoundException{
        
            Connection connection = DBConnection.getDbConnection().getConnection();
            
            ResultSet rst = connection.createStatement().executeQuery("SELECT oid FROM `Orders` ORDER BY oid DESC LIMIT 1;");

           if (rst.next()){
                String id =rst.getString("id");
                int newOrderId=Integer.parseInt(id.replace("O00-",""));
                
                return String.format("OID-%03d", newOrderId);
           }else{
        return "OID-001";
           }
    }

 }


   
    

