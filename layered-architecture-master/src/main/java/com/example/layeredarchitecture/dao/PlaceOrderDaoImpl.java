package com.example.layeredarchitecture.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


import com.example.layeredarchitecture.db.DBConnection;




public class PlaceOrderDaoImpl implements PlaceOrderDao{

     @Override
    public String genarateOrderId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT oid FROM `Orders` ORDER BY oid DESC LIMIT 1;");

        return rst.next() ? String.format("OID-%03d", (Integer.parseInt(rst.getString("oid").replace("OID-", "")) + 1)) : "OID-001";
    }
    @Override
   public boolean existId(String orderId) throws SQLException, ClassNotFoundException {
        boolean isExists=false;
       Connection connection = DBConnection.getDbConnection().getConnection();

       PreparedStatement stm = connection.prepareStatement("SELECT oid FROM `Orders` WHERE oid=?");
       stm.setString(1, orderId);
       if (stm.executeQuery().next()) {

       }
       return isExists;
   }
    @Override
    public boolean saveOrders(String orderId, LocalDate orderDate, String customerId) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        boolean isSaved = false;
     // ArrayList<OrderDetailDTO>  dto =new ArrayList<>();
    
      // OrderDetailDaoImpl detailDao=new OrderDetailDaoImpl();
        try {
            connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement stm = connection.prepareStatement("INSERT INTO `Orders` (oid, date, customerID) VALUES (?,?,?)");
            connection.setAutoCommit(false);
            stm.setString(1, orderId);
            stm.setDate(2, Date.valueOf(orderDate));
            stm.setString(3, customerId);
            isSaved = stm.executeUpdate() > 0;
        
            if (isSaved) {
                connection.commit();
            

            } else {
                connection.rollback();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
        }
      

        return isSaved;
    }
}