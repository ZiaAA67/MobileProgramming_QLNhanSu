package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "Employee_RewardsDiscipline",
        primaryKeys = {"EmployeeID", "RewardsDisciplineID", "Date"},
        foreignKeys = {
                @ForeignKey(entity = Employee.class,
                        parentColumns = "EmployeeID",
                        childColumns = "EmployeeID"),
                @ForeignKey(entity = RewardsDiscipline.class,
                        parentColumns = "RewardsDisciplineID",
                        childColumns = "RewardsDisciplineID")
        }
)
public class Employee_RewardsDiscipline {

    @ColumnInfo(name = "EmployeeID")
    private int employeeId;

    @ColumnInfo(name = "RewardsDisciplineID")
    private int rewardsDisciplineId;

    @ColumnInfo(name = "Date")
    private String date;

    @ColumnInfo(name = "Content")
    private String content;

    @ColumnInfo(name = "Bonus")
    private Float bonus;

    // Constructor
    public Employee_RewardsDiscipline(int employeeId, int rewardsDisciplineId, String date, String content, Float bonus) {
        this.employeeId = employeeId;
        this.rewardsDisciplineId = rewardsDisciplineId;
        this.date = date;
        this.content = content;
        this.bonus = bonus;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getRewardsDisciplineId() {
        return rewardsDisciplineId;
    }

    public void setRewardsDisciplineId(int rewardsDisciplineId) {
        this.rewardsDisciplineId = rewardsDisciplineId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Float getBonus() {
        return bonus;
    }

    public void setBonus(Float bonus) {
        this.bonus = bonus;
    }
}
