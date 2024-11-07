package com.example.myapplication.Demo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM tblUser")
    List<User> getListUser();

    @Query("SELECT * FROM tblUser WHERE username = :username")
    List<User> checkUser(String username);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM tblUser")
    void deleteAllUser();

    @Query("SELECT * FROM tblUser WHERE username LIKE '%' || :name || '%'")
    List<User> searchUser(String name);
}
