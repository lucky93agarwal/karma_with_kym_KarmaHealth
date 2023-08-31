package com.devkraft.karmahealth.Model;

import java.util.List;

public class SubscriptionPlanDTO {

    private Long planId;
    private List<String> planBenefits;
    private String country;
    private String subscriptionCycle;
    private double price;
    private String subscriptionStartDate;
    private String subscriptionRenewsOn;
    private boolean isAutorenewalExpired;
    private String paymentStatus;
    private boolean isAutorenewal;
    private String planName;
    private String current_symbol;
    private String support_email;
    private String subscriptionStatus;
    private String priceId;
    private String planType;
    private String currencyCode;

    public List<String> getPlanBenefits() {
        return planBenefits;
    }

    public void setPlanBenefits(List<String> planBenefits) {
        this.planBenefits = planBenefits;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSubscriptionCycle() {
        return subscriptionCycle;
    }

    public void setSubscriptionCycle(String subscriptionCycle) {
        this.subscriptionCycle = subscriptionCycle;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(String subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public String getSubscriptionRenewsOn() {
        return subscriptionRenewsOn;
    }

    public void setSubscriptionRenewsOn(String subscriptionRenewsOn) {
        this.subscriptionRenewsOn = subscriptionRenewsOn;
    }

    public boolean isAutorenewalExpired() {
        return isAutorenewalExpired;
    }

    public void setAutorenewalExpired(boolean autorenewalExpired) {
        isAutorenewalExpired = autorenewalExpired;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isAutorenewal() {
        return isAutorenewal;
    }

    public void setAutorenewal(boolean autorenewal) {
        isAutorenewal = autorenewal;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getCurrent_symbol() {
        return current_symbol;
    }

    public void setCurrent_symbol(String current_symbol) {
        this.current_symbol = current_symbol;
    }

    public String getSupport_email() {
        return support_email;
    }

    public void setSupport_email(String support_email) {
        this.support_email = support_email;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}

