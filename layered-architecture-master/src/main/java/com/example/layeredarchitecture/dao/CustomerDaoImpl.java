package com.example.layeredarchitecture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.layeredarchitecture.db.DBConnection;
import com.example.layeredarchitecture.model.CustomerDTO;

public class CustomerDaoImpl implements CustomerDao {

@Override
    public ArrayList<CustomerDTO> getAllCustomer()throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getDbConnection().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Customer");
            ArrayList<CustomerDTO> getAllCustomer=new ArrayList<>();
            while(rst.next()){
                 CustomerDTO customerDto=new CustomerDTO(rst.getString("id"),
                 rst.getString("name"),rst.getString("address"));
                 getAllCustomer.add(customerDto);
            }
            return getAllCustomer;
        }


@Override
      public boolean saveCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getDbConnection().getConnection();
                PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer (id,name, address) VALUES (?,?,?)");
                pstm.setString(1, customerDTO.getId());
                pstm.setString(2, customerDTO.getName());
                pstm.setString(3, customerDTO.getAddress());
                boolean isSaved =pstm.executeUpdate()>0;
               return isSaved;
                


                }
@Override
    public boolean updateCustomer(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("UPDATE Customer SET name=?, address=? WHERE id=?");
        pstm.setString(1, customerDTO.getName());
        pstm.setString(2, customerDTO.getAddress());
        pstm.setString(3, customerDTO.getId());
        boolean isUpdate=pstm.executeUpdate()>0;
        return isUpdate;
    }
@Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Customer WHERE id=?");
        pstm.setString(1,id);
       boolean isDeleted= pstm.executeUpdate()>0;
       return isDeleted;
    }
@Override
    public String generateNewCustomerId(String id) throws SQLException, ClassNotFoundException{
        
            Connection connection = DBConnection.getDbConnection().getConnection();
           
            ResultSet rst = connection.createStatement().executeQuery("SELECT id FROM Customer ORDER BY id DESC LIMIT 1;");
            if (rst.next()) {
                 id = rst.getString("id");
                int newCustomerId = Integer.parseInt(id.replace("C00-", "")) + 1;
                return String.format("C00-%03d", newCustomerId);
            } else {
                return "C00-001";
            }
    }

@Override
    public boolean existCustomer(String id) throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT id FROM Customer WHERE id=?");
        pstm.setString(1, id);
        return pstm.executeQuery().next();
    }
}
