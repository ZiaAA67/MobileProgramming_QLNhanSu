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

    @Delete
    void deleteUser(User user);

    @Update
    void updateUser(User user);

    @Query("DELETE FROM tblUser")
    void deleteAllUser();

    @Query("SELECT * FROM tblUser")
    List<User> getListUser();

    @Query("SELECT * FROM tblUser WHERE username LIKE '%' || :kw || '%'")
    List<User> searchUser(String kw);

    @Query("SELECT * FROM tblUser WHERE username = :username")
    List<User> checkUser(String username);
}
