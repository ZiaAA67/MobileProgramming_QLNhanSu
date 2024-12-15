package com.example.myapplication.MainApp.SalaryManager;

public class ExportItemSalary {

    private int employeeId;
    private String fullname;
    private String monthYear;
    private String total;
    private String rewardDisciplineName;
    private String getRewardDisciplineMoney;
    private String basicSalary;
    private String allowance;
    private String coefficient;
    private String overTime;
    private String overTimeMoney;
    private String tax;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRewardDisciplineName() {
        return rewardDisciplineName;
    }

    public void setRewardDisciplineName(String rewardDisciplineName) {
        this.rewardDisciplineName = rewardDisciplineName;
    }

    public String getGetRewardDisciplineMoney() {
        return getRewardDisciplineMoney;
    }

    public void setGetRewardDisciplineMoney(String getRewardDisciplineMoney) {
        this.getRewardDisciplineMoney = getRewardDisciplineMoney;
    }

    public String getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(String basicSalary) {
        this.basicSalary = basicSalary;
    }

    public String getAllowance() {
        return allowance;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    public String getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(String coefficient) {
        this.coefficient = coefficient;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getOverTimeMoney() {
        return overTimeMoney;
    }

    public void setOverTimeMoney(String overTimeMoney) {
        this.overTimeMoney = overTimeMoney;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public ExportItemSalary(int employeeId, String fullname, String monthYear, String total, String rewardDisciplineName, String getRewardDisciplineMoney, String basicSalary, String allowance, String coefficient, String overTime, String overTimeMoney, String tax) {
        this.employeeId = employeeId;
        this.fullname = fullname;
        this.monthYear = monthYear;
        this.total = total;
        this.getRewardDisciplineMoney = getRewardDisciplineMoney;
        this.basicSalary = basicSalary;
        this.allowance = allowance;
        this.coefficient = coefficient;
        this.overTime = overTime;
        this.overTimeMoney = overTimeMoney;
        this.tax = tax;
    }

    public ExportItemSalary() {
    }
}
