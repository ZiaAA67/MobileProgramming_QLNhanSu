package com.example.myapplication.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.User;

import java.util.List;


@Dao
public interface UserDAO {

    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE UserID = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM User WHERE Username = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM User WHERE RoleID = :roleId")
    List<User> getUsersByRoleId(int roleId);
}
