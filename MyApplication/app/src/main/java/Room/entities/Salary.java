package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Salary")
public class Salary {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "SalaryID")
    private int salaryId;

    @ColumnInfo(name = "BasicSalary")
    private Float basicSalary;

    @ColumnInfo(name = "SuplementalPay")
    private Float supplementalPay;

    @ColumnInfo(name = "Coefficient")
    private Float coefficient;

    // Constructor
    public Salary(Float basicSalary, Float supplementalPay, Float coefficient) {
        this.basicSalary = basicSalary;
        this.supplementalPay = supplementalPay;
        this.coefficient = coefficient;
    }

    // Getters and Setters
    public int getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }

    public Float getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Float basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Float getSupplementalPay() {
        return supplementalPay;
    }

    public void setSupplementalPay(Float supplementalPay) {
        this.supplementalPay = supplementalPay;
    }

    public Float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Float coefficient) {
        this.coefficient = coefficient;
    }
}
