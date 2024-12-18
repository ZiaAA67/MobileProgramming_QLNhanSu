package com.example.myapplication.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entities.Employee;

import java.util.List;

@Dao
public interface EmployeeDAO {

    @Insert
    void insert(Employee... employees);

    @Insert
    long insertReturnId(Employee employees);

    @Update
    void update(Employee employee);

    @Delete
    void delete(Employee employee);

    @Query("SELECT * FROM Employee WHERE EmployeeID = :employeeId")
    Employee getById(int employeeId);

    @Query("SELECT * FROM Employee WHERE FullName LIKE :fullName")
    List<Employee> getByName(String fullName);

    // Kiểm tra CCCD
    @Query("SELECT * FROM Employee WHERE IdentityNumber LIKE :identityNumber")
    Employee getByIdentityNumber(String identityNumber);

    // Kiểm tra SDT
    @Query("SELECT * FROM Employee WHERE PhoneNumber LIKE :phoneNumber")
    Employee getByPhoneNumber(String phoneNumber);

    @Query("SELECT * FROM Employee WHERE Email LIKE :email")
    Employee getByEmail(String email);

    @Query("SELECT * FROM Employee WHERE DepartmentID = :departmentId AND Active = 1")
    List<Employee> getByDepartmentId(int departmentId);

    @Query("SELECT * FROM Employee WHERE WorkplaceID = :workplaceId AND Active = 1")
    List<Employee> getByWorkplaceId(int workplaceId);

    @Query("SELECT * FROM Employee WHERE Active = 1")
    List<Employee> getActiveEmployees();

    @Query("SELECT * FROM Employee WHERE Active = 0")
    List<Employee> getInactiveEmployees();

    @Query("SELECT * FROM Employee WHERE IsApprove = 1 AND Active = 1")
    List<Employee> getApproveEmployees();

    @Query("SELECT * FROM Employee WHERE IsApprove = 1 AND Active = 1 AND EmployeeID = :employeeId")
    Employee getApproveEmployeeById(int employeeId);

    @Query("SELECT * FROM Employee WHERE IsApprove = 0 AND Active = 1")
    List<Employee> getDisapproveEmployees();

    @Query("SELECT * FROM Employee WHERE PositionID = :positionId")
    List<Employee> getByPositionId(int positionId);

    @Query("SELECT * FROM Employee WHERE UserID = :userId LIMIT 1")
    Employee getEmployeeByUserId(int userId);

    @Query("SELECT * FROM Employee WHERE FullName LIKE '%' || :name || '%'")
    List<Employee> searchEmployeeName(String name);

    @Query("SELECT COUNT(*) FROM Employee WHERE DepartmentID = :departmentId AND Active = 1")
    long getEmployeeCountByDepartment(long departmentId);

    @Query("SELECT COUNT(*) FROM Employee WHERE WorkplaceID = :workplaceId AND Active = 1")
    long getEmployeeCountByWorkplace(long workplaceId);

    @Query("SELECT COUNT(*) FROM Employee WHERE PositionID = :positionId AND Active = 1")
    long getEmployeeCountByPosition(long positionId);

    //Male = 0, Female = 1, Other = 2
    @Query("SELECT COUNT(*) FROM Employee WHERE Gender = :gender AND Active = 1")
    long getEmployeeCountByGender(long gender);

    @Query("SELECT * FROM Employee WHERE Active = 1 AND UserID IS NULL")
    List<Employee> getEmployeeUnHaveUserAccount();

    @Query("SELECT * FROM Employee")
    List<Employee> getAllEmployees();
}