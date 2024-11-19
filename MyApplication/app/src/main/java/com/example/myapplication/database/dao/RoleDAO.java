package com.example.myapplication.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.Role;

import java.util.List;

@Dao
public interface RoleDAO {

    @Insert
    void insert(Role role);

    @Insert
    long insertReturnId(Role role);

    @Update
    void update(Role role);

    @Delete
    void delete(Role role);

    @Query("SELECT * FROM Role")
    List<Role> getAllRoles();

    @Query("SELECT * FROM Role WHERE RoleID = :roleId")
    Role getRoleById(int roleId);

    @Query("SELECT * FROM Role WHERE RoleName = :roleName")
    Role getRoleByName(String roleName);
}
