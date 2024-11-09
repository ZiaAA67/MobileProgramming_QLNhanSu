package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "Employees",
        foreignKeys = {
                @ForeignKey(entity = Salary.class, parentColumns = "SalaryID", childColumns = "SalaryID"),
                @ForeignKey(entity = Department.class, parentColumns = "DepartmentID", childColumns = "DepartmentID"),
                @ForeignKey(entity = Position.class, parentColumns = "PositionID", childColumns = "PositionID"),
                @ForeignKey(entity = EducationLevel.class, parentColumns = "EducationID", childColumns = "EducationID")
        }
)
public class Employee {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "EmployeeID")
    private int employeeId;

    @ColumnInfo(name = "FullName")
    private String fullName;

    @ColumnInfo(name = "Sex")
    private int sex;

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

    @ColumnInfo(name = "Status")
    private Integer status;

    @ColumnInfo(name = "JoinDate")
    private String joinDate;

    @ColumnInfo(name = "Image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    @ColumnInfo(name = "SalaryID")
    private Integer salaryId;

    @ColumnInfo(name = "DepartmentID")
    private Integer departmentId;

    @ColumnInfo(name = "PositionID")
    private Integer positionId;

    @ColumnInfo(name = "SectionID")
    private Integer sectionId;

    @ColumnInfo(name = "EducationID")
    private Integer educationId;

    @ColumnInfo(name = "Note")
    private String note;

    // Constructor, Getters, and Setters
    public Employee(String fullName, int sex, String birth, String identityNumber, String address, String phoneNumber, String email, Integer status, String joinDate, byte[] image, Integer salaryId, Integer departmentId, Integer positionId, Integer sectionId, Integer educationId, String note) {
        this.fullName = fullName;
        this.sex = sex;
        this.birth = birth;
        this.identityNumber = identityNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.status = status;
        this.joinDate = joinDate;
        this.image = image;
        this.salaryId = salaryId;
        this.departmentId = departmentId;
        this.positionId = positionId;
        this.sectionId = sectionId;
        this.educationId = educationId;
        this.note = note;
    }

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getEducationId() {
        return educationId;
    }

    public void setEducationId(Integer educationId) {
        this.educationId = educationId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
