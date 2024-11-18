package com.example.myapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;


@Entity(tableName = "Employee",
        foreignKeys = {
                @ForeignKey(entity = Salary.class, parentColumns = "SalaryID", childColumns = "SalaryID", onDelete = ForeignKey.SET_NULL),
                @ForeignKey(entity = Department.class, parentColumns = "DepartmentID", childColumns = "DepartmentID", onDelete = ForeignKey.SET_NULL),
                @ForeignKey(entity = Position.class, parentColumns = "PositionID", childColumns = "PositionID", onDelete = ForeignKey.SET_NULL),
                @ForeignKey(entity = EducationLevel.class, parentColumns = "EducationID", childColumns = "EducationID", onDelete = ForeignKey.SET_NULL),
                @ForeignKey(entity = User.class, parentColumns = "UserID", childColumns = "UserID", onDelete = ForeignKey.SET_NULL),
                @ForeignKey(entity = Workplace.class, parentColumns = "WorkplaceID", childColumns = "WorkplaceID", onDelete = ForeignKey.SET_NULL)
        }
)
public class Employee {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "EmployeeID")
    private int employeeId;

    @ColumnInfo(name = "FullName")
    private String fullName;

    @ColumnInfo(name = "Gender")
    private int gender;

    @ColumnInfo(name = "Birth")
    private String birth;

    @ColumnInfo(name = "IdentityNumber")
    private String identityNumber;

    @ColumnInfo(name = "Address")
    private String address;

    @ColumnInfo(name = "PhoneNumber")
    private String phoneNumber;

    @ColumnInfo(name = "Email")
    private String email;

    @ColumnInfo(name = "Active")
    private Integer active;

    @ColumnInfo(name = "ImagePath")
    private String imagePath;

    @ColumnInfo(name = "SalaryID")
    private Integer salaryId;

    @ColumnInfo(name = "DepartmentID")
    private Integer departmentId;

    @ColumnInfo(name = "PositionID")
    private Integer positionId;

    @ColumnInfo(name = "EducationID")
    private Integer educationId;

    @ColumnInfo(name = "UserID")
    private Integer userId;

    @ColumnInfo(name = "WorkplaceID")
    private Integer workplaceId;

    // Constructor


    public Employee(String fullName, int gender, String birth, String identityNumber, String address, String phoneNumber, String email, Integer active, String imagePath, Integer salaryId, Integer departmentId, Integer positionId, Integer educationId, Integer userId, Integer workplaceId) {
        this.fullName = fullName;
        this.gender = gender;
        this.birth = birth;
        this.identityNumber = identityNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.active = active;
        this.imagePath = imagePath;
        this.salaryId = salaryId;
        this.departmentId = departmentId;
        this.positionId = positionId;
        this.educationId = educationId;
        this.userId = userId;
        this.workplaceId = workplaceId;
    }

    // Getters and Getters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(Integer salaryId) {
        this.salaryId = salaryId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getEducationId() {
        return educationId;
    }

    public void setEducationId(Integer educationId) {
        this.educationId = educationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(Integer workplaceId) {
        this.workplaceId = workplaceId;
    }
}
