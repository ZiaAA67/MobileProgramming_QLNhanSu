package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Roles")
public class Role {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "RoleID")
    private int roleId;

    @ColumnInfo(name = "RoleName")
    private String roleName;

    @ColumnInfo(name = "Note")
    private String note;

    // Constructor
    public Role(String roleName, String note) {
        this.roleName = roleName;
        this.note = note;
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
}
