package com.devkraft.karmahealth.Model;

public class LoginResponse {

   /* private UserResponseDTO userDTO;
    private String accessToken;
    private String refreshToken;
    private boolean isDrugPresent;
    private boolean isOnBoardingFlow;
    private UserToken userToken;*/

    private UserDto userDTO;
    private String accessToken;
    private String refreshToken;
    private String onBoardingToken;
    private boolean drugPresent;
    private boolean onBoardingFlow;
    //    private String onBoardingFlow;
    private String employerCode;
    private boolean isNewUser;

    UserToken userToken;

    public UserDto getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDto userDTO) {
        this.userDTO = userDTO;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOnBoardingToken() {
        return onBoardingToken;
    }

    public void setOnBoardingToken(String onBoardingToken) {
        this.onBoardingToken = onBoardingToken;
    }

    public boolean isDrugPresent() {
        return drugPresent;
    }

    public void setDrugPresent(boolean drugPresent) {
        this.drugPresent = drugPresent;
    }

    public boolean isOnBoardingFlow() {
        return onBoardingFlow;
    }

    public void setOnBoardingFlow(boolean onBoardingFlow) {
        this.onBoardingFlow = onBoardingFlow;
    }

    public String getEmployerCode() {
        return employerCode;
    }

    public void setEmployerCode(String employerCode) {
        this.employerCode = employerCode;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }

    public UserToken getUserToken() {
        return userToken;
    }

    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
    }
}

