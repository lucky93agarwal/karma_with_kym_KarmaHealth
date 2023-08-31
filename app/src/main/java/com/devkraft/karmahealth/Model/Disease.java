package com.devkraft.karmahealth.Model;

public class Disease {
    private Long id;
    private String name;
    private String informationLink;
    private Long userDiseaseId;
    private Long createdDate;
    private String organType;

    public Long getUserDiseaseId() {
        return userDiseaseId;
    }

    public void setUserDiseaseId(Long userDiseaseId) {
        this.userDiseaseId = userDiseaseId;
    }

    public String getInformationLink() {
        return informationLink;
    }

    public void setInformationLink(String informationLink) {
        this.informationLink = informationLink;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganType() {
        return organType;
    }

    public void setOrganType(String organType) {
        this.organType = organType;
    }
}
