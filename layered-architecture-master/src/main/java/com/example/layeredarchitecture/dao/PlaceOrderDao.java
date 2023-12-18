package com.example.layeredarchitecture.dao;

import java.sql.SQLException;
import java.time.LocalDate;

public interface PlaceOrderDao {

    boolean existId(String orderId) throws SQLException, ClassNotFoundException;

    boolean saveOrders(String orderId, LocalDate orderDate, String customerId) throws SQLException, ClassNotFoundException;
    String genarateOrderId() throws SQLException, ClassNotFoundException ;
}
