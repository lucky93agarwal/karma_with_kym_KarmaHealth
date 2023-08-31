package com.devkraft.karmahealth.Model;

import java.util.Objects;

public class UserDto {

    private Long id;
    private String email;
    private String name;
    private Long dateOfBirth;
    private String gender;
    private Long ethnicityId;
    private String ethnicityName;
    private Long parent;
    private String relationship;
    private boolean resetPasswordRequired;
    private boolean premium;
    private boolean emailConfirm;
    private TenantConfigurationDTO tenantConfigurationDTO;
    private EnterpriseDTO enterpriseDTO;
    private boolean medshadowUser;
    private boolean profileSetup;
    private String location;
    private String avatarName;
    private String ipReverseLookupLocation;
    private String userLocale;
    private long tenantId;
    private boolean premiumByLocation;
    private String userType;
    private boolean displayPDF;
    private String registrationDate;
    private String countryCode;
    private String country;
    private String trialExpiryDate;
    private SubscriptionPlanDTO subscriptionPlanDTO;


    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public SubscriptionPlanDTO getSubscriptionPlanDTO() {
        return subscriptionPlanDTO;
    }

    public void setSubscriptionPlanDTO(SubscriptionPlanDTO subscriptionPlanDTO) {
        this.subscriptionPlanDTO = subscriptionPlanDTO;
    }

    public TenantConfigurationDTO getTenantConfigurationDTO() {
        return tenantConfigurationDTO;
    }

    public void setTenantConfigurationDTO(TenantConfigurationDTO tenantConfigurationDTO) {
        this.tenantConfigurationDTO = tenantConfigurationDTO;
    }

    public boolean isPremiumByLocation() {
        return premiumByLocation;
    }

    public void setPremiumByLocation(boolean premiumByLocation) {
        this.premiumByLocation = premiumByLocation;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(String userLocale) {
        this.userLocale = userLocale;
    }

    public String getIpReverseLookupLocation() {
        return ipReverseLookupLocation;
    }

    public void setIpReverseLookupLocation(String ipReverseLookupLocation) {
        this.ipReverseLookupLocation = ipReverseLookupLocation;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isProfileSetup() {
        return profileSetup;
    }

    public void setProfileSetup(boolean profileSetup) {
        this.profileSetup = profileSetup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isMedshadowUser() {
        return medshadowUser;
    }

    public void setMedshadowUser(boolean medshadowUser) {
        this.medshadowUser = medshadowUser;
    }

    public EnterpriseDTO getEnterpriseDTO() {
        return enterpriseDTO;
    }

    public void setEnterpriseDTO(EnterpriseDTO enterpriseDTO) {
        this.enterpriseDTO = enterpriseDTO;
    }

    public boolean isEmailConfirm() {
        return emailConfirm;
    }

    public void setEmailConfirm(boolean emailConfirm) {
        this.emailConfirm = emailConfirm;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getEthnicityId() {
        return ethnicityId;
    }

    public void setEthnicityId(Long ethnicityId) {
        this.ethnicityId = ethnicityId;
    }

    public String getEthnicityName() {
        return ethnicityName;
    }

    public void setEthnicityName(String ethnicityName) {
        this.ethnicityName = ethnicityName;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public boolean isResetPasswordRequired() {
        return resetPasswordRequired;
    }

    public void setResetPasswordRequired(boolean resetPasswordRequired) {
        this.resetPasswordRequired = resetPasswordRequired;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean getDisplayPDF() {
        return displayPDF;
    }

    public void setDisplayPDF(boolean displayPDF) {
        this.displayPDF = displayPDF;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTrialExpiryDate() {
        return trialExpiryDate;
    }

    public void setTrialExpiryDate(String trialExpiryDate) {
        this.trialExpiryDate = trialExpiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return resetPasswordRequired == userDto.resetPasswordRequired &&
                premium == userDto.premium &&
                emailConfirm == userDto.emailConfirm &&
                medshadowUser == userDto.medshadowUser &&
                profileSetup == userDto.profileSetup &&
                Objects.equals(id, userDto.id) &&
                Objects.equals(email, userDto.email) &&
                Objects.equals(name, userDto.name) &&
                Objects.equals(dateOfBirth, userDto.dateOfBirth) &&
                Objects.equals(gender, userDto.gender) &&
                Objects.equals(ethnicityId, userDto.ethnicityId) &&
                Objects.equals(ethnicityName, userDto.ethnicityName) &&
                Objects.equals(parent, userDto.parent) &&
                Objects.equals(relationship, userDto.relationship) &&
                Objects.equals(enterpriseDTO, userDto.enterpriseDTO) &&
                Objects.equals(tenantConfigurationDTO, userDto.tenantConfigurationDTO) &&
                Objects.equals(location, userDto.location) &&
                Objects.equals(userType, userDto.userType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, dateOfBirth, gender, ethnicityId, ethnicityName, parent, relationship, resetPasswordRequired, premium, emailConfirm, enterpriseDTO, tenantConfigurationDTO, medshadowUser, profileSetup, location, userType);
    }
}
