package com.example.layeredarchitecture.dao;

import java.sql.SQLException;
import java.util.List;

import com.example.layeredarchitecture.model.OrderDetailDTO;

public interface OrderDetailDao {
    public boolean savedOrderDetails(List<OrderDetailDTO> orderDetails, String orderId) throws SQLException, ClassNotFoundException ;

}
