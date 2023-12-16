package com.example.layeredarchitecture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.layeredarchitecture.db.DBConnection;
import com.example.layeredarchitecture.model.ItemDTO;
import com.example.layeredarchitecture.view.tdm.ItemTM;

public class ItemDaoImpl implements ItemDao {
    @Override

    public ArrayList<ItemDTO> getAllItem() throws SQLException, ClassNotFoundException{
     Connection connection = DBConnection.getDbConnection().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Item");
            ArrayList<ItemDTO> getAllItem= new ArrayList<>();
            while(rst.next()){
                 ItemDTO itemDto=new ItemDTO(rst.getString("code"),rst.getString("description"),rst.getBigDecimal("unitPrice"),rst.getInt("qtyOnHand"));
                 getAllItem.add(itemDto);
            }
            return getAllItem;
    } 
@Override
    public boolean saveItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException{
         Connection connection = DBConnection.getDbConnection().getConnection();
                PreparedStatement pstm = connection.prepareStatement("INSERT INTO Item (code, description, unitPrice, qtyOnHand) VALUES (?,?,?,?)");
                pstm.setString(1, itemDTO.getCode());
                pstm.setString(2, itemDTO.getDescription());
                pstm.setBigDecimal(3, itemDTO.getUnitPrice());
                pstm.setInt(4, itemDTO.getQtyOnHand());
                boolean isSaved =pstm.executeUpdate()>0;
                return isSaved;
    
    }
    @Override

    public boolean updateItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE Item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?");
        pstm.setString(1, itemDTO.getDescription());
        pstm.setBigDecimal(2,itemDTO.getUnitPrice());
        pstm.setInt(3, itemDTO.getQtyOnHand());
        pstm.setString(4, itemDTO.getCode());
       boolean isUpdate= pstm.executeUpdate()>0;
       return isUpdate;
    }
    @Override
 
    public String generateNewItemCode(String code) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        ResultSet rst = connection.createStatement().executeQuery("SELECT code FROM Item ORDER BY code DESC LIMIT 1;");
        if (rst.next()) {
            code = rst.getString("code");
            int newItemId = Integer.parseInt(code.replace("I00-", "")) + 1;
            return String.format("I00-%03d", newItemId);
        } else {
            return "I00-001";
        }
    }
@Override
    public boolean existItem(String code) throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT id FROM Item WHERE code=?");
        pstm.setString(1, code);
        return pstm.executeQuery().next();
    }
    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Item WHERE code=?");
        pstm.setString(1,code);
       boolean isDeleted= pstm.executeUpdate()>0;
       return isDeleted;
    }
}
