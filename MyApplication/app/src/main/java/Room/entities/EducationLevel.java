package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "EducationLevel")
public class EducationLevel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "EducationID")
    private int educationId;

    @ColumnInfo(name = "EducationLevelName")
    private String educationLevelName;

    @ColumnInfo(name = "Major")
    private String major;


    // Constructor
    public EducationLevel(String educationLevelName, String major) {
        this.educationLevelName = educationLevelName;
        this.major = major;
    }

    // Getters and Setters
    public int getEducationId() {
        return educationId;
    }

    public void setEducationId(int educationId) {
        this.educationId = educationId;
    }

    public String getEducationLevelName() {
        return educationLevelName;
    }

    public void setEducationLevelName(String educationLevelName) {
        this.educationLevelName = educationLevelName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
