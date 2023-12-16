package com.example.layeredarchitecture.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.example.layeredarchitecture.model.ItemDTO;

public interface ItemDao {
      public ArrayList<ItemDTO> getAllItem() throws SQLException, ClassNotFoundException;

    

    public boolean saveItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;
         
    public boolean updateItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;

    
 
    public String generateNewItemCode(String code) throws SQLException, ClassNotFoundException ;
        
    public boolean existItem(String code) throws SQLException, ClassNotFoundException;
    
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException;
     
}
