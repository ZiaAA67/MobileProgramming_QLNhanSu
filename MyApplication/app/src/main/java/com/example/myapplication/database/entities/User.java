package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;

@Entity(tableName = "User",
        foreignKeys = {
                @ForeignKey(
                        entity = Role.class,
                        parentColumns = "RoleID",
                        childColumns = "RoleID",
                        onDelete = ForeignKey.SET_NULL
                )
        }
)
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "UserID")
    private int userId;

    @ColumnInfo(name = "Username")
    private String username;

    @ColumnInfo(name = "Password")
    private String password;

    @ColumnInfo(name = "CreateDate")
    private String createDate;

    @ColumnInfo(name = "Active")
    private boolean active;

    @ColumnInfo(name = "IsFirstLogin")
    private boolean isFirstLogin;

    @ColumnInfo(name = "RoleID")
    private Integer roleId;


    // Constructor
    public User(String username, String password, String createDate, boolean active, boolean isFirstLogin, Integer roleId) {
        this.username = username;
        this.password = password;
        this.createDate = createDate;
        this.active = active;
        this.isFirstLogin = isFirstLogin;
        this.roleId = roleId;
    }


    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
