package com.example.layeredarchitecture.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.example.layeredarchitecture.db.DBConnection;
import com.example.layeredarchitecture.model.ItemDTO;
import com.example.layeredarchitecture.model.OrderDTO;
import com.example.layeredarchitecture.model.OrderDetailDTO;

import javafx.scene.control.Alert;

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

    public boolean checkOrderExist(String orderId) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT oid FROM `Orders` WHERE oid=?");
        stm.setString(1, orderId);
        /*if order id already exist*/
        if (stm.executeQuery().next()) {
            return false;
        }
        return true;
    }

   
    public boolean saveOrder(OrderDTO orderDTO, List<OrderDetailDTO> orderDetails) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        connection = DBConnection.getDbConnection().getConnection();
        connection.setAutoCommit(false);
        boolean orderSaved = insertOrder(orderDTO);
        if (orderSaved) {
            OrderDetailDaoImpl orderDetailDaoImpl= new OrderDetailDaoImpl();
            boolean orderDetailSaved = orderDetailDaoImpl.insertOrderDetail(orderDetails,orderDTO.getOrderId());
         if (orderDetailSaved){
            ItemDaoImpl itemDaoImpl= new ItemDaoImpl();
             boolean updateItem = itemDaoImpl.updateQuantity(orderDetails);
             if (updateItem){
                 connection.commit();
                 connection.setAutoCommit(true);
                 return true;
             }
             else {
                 connection.rollback();
                 connection.setAutoCommit(true);
                 return false;
             }
         }
        }else {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        return true;
    }



    private boolean insertOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO `Orders` (oid, date, customerID) VALUES (?,?,?)");
        stm.setString(1, orderDTO.getOrderId());
        System.out.println(orderDTO.getOrderDate());
        stm.setDate(2, Date.valueOf(orderDTO.getOrderDate()));
        stm.setString(3, orderDTO.getCustomerId());
        return stm.executeUpdate() > 0;
    }


}

 


   
    

