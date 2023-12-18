package com.example.layeredarchitecture.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.example.layeredarchitecture.db.DBConnection;
import com.example.layeredarchitecture.model.ItemDTO;
import com.example.layeredarchitecture.model.OrderDetailDTO;

public class OrderDetailDaoImpl implements OrderDetailDao {

    String code;
    String description;
    BigDecimal unitPrice;
    int qty;
    ItemDTO dto=new ItemDTO(code,description,unitPrice,qty);
    @Override
    public boolean savedOrderDetails(List<OrderDetailDTO> orderDetails, String orderId) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        boolean isSaved = false;
        ItemDaoImpl daoimpl = new ItemDaoImpl();


        try {
            connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement stm = connection.prepareStatement("INSERT INTO OrderDetails (oid, itemCode, unitPrice, qty) VALUES (?,?,?,?)");
            connection.setAutoCommit(false);
            for (OrderDetailDTO detail : orderDetails) {
                stm.setString(1, orderId);
                stm.setString(2, detail.getItemCode());
                stm.setBigDecimal(3, detail.getUnitPrice());
                stm.setInt(4, detail.getQty());
            }
            isSaved = stm.executeUpdate() > 0;
            if (isSaved) {
                connection.commit();

            } else {
                connection.rollback();
            }
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }finally{
            connection.setAutoCommit(true);
        }
        return isSaved;
    }
}