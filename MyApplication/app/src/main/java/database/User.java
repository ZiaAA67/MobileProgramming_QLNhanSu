package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users",
        foreignKeys = {
                @ForeignKey(entity = Employee.class, parentColumns = "EmployeeID", childColumns = "EmployeeID"),
                @ForeignKey(entity = Role.class, parentColumns = "RoleID", childColumns = "RoleID")
        }
)
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "UsersID")
    private int usersId;

    @ColumnInfo(name = "EmployeeID")
    private Integer employeeId;

    @ColumnInfo(name = "RoleID")
    private Integer roleId;

    @ColumnInfo(name = "Username")
    private String username;

    @ColumnInfo(name = "Password")
    private String password;

    @ColumnInfo(name = "Status")
    private Integer status;

    @ColumnInfo(name = "CreateDate")
    private String createDate;

    // Constructor
    public User(Integer employeeId, Integer roleId, String username, String password, Integer status, String createDate) {
        this.employeeId = employeeId;
        this.roleId = roleId;
        this.username = username;
        this.password = password;
        this.status = status;
        this.createDate = createDate;
    }

    // Getters and Setters
    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
