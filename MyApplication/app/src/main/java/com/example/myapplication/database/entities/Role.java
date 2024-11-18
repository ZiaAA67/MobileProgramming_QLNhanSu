package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Role")
public class Role {

    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "RoleID")
    private int roleId;

    @ColumnInfo(name = "RoleName")
    private String roleName;

    @ColumnInfo(name = "Description")
    private String description;

    // Constructor
    public Role(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    // Getters and Setters
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
