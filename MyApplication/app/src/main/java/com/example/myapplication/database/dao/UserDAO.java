package com.example.myapplication.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.Department;
import com.example.myapplication.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(User user);

    @Insert
    long insertReturnId(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM User WHERE Active = 1")
    List<User> getActiveUsers();

    @Query("SELECT * FROM User WHERE UserID = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM User WHERE Username = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM User WHERE RoleID = :roleId")
    List<User> getUsersByRoleId(int roleId);

    @Query("SELECT RoleID FROM User WHERE UserID = :userId")
    int getUserByRoleId(int userId);

    @Query("SELECT * FROM User")
    List<User> getListUsers();
}
