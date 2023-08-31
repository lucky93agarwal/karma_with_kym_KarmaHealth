package com.devkraft.karmahealth.Model;

public class TenantConfigurationDTO {
    private Boolean vaccinationFlow;
    private Boolean employeeData;
    private Boolean employeeIdCompulsary;
    private boolean ibduser;

    public Boolean getVaccinationFlow() {
        return vaccinationFlow;
    }

    public void setVaccinationFlow(Boolean vaccinationFlow) {
        this.vaccinationFlow = vaccinationFlow;
    }

    public Boolean getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(Boolean employeeData) {
        this.employeeData = employeeData;
    }

    public Boolean getEmployeeIdCompulsary() {
        return employeeIdCompulsary;
    }

    public void setEmployeeIdCompulsary(Boolean employeeIdCompulsary) {
        this.employeeIdCompulsary = employeeIdCompulsary;
    }

    public boolean getIbduser() {
        return ibduser;
    }

    public void setIbduser(boolean ibduser) {
        this.ibduser = ibduser;
    }
}

