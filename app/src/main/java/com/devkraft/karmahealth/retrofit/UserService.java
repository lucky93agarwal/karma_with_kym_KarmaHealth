package com.devkraft.karmahealth.retrofit;

import com.devkraft.karmahealth.Adapter.ParameterListAPIResponse;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.AddDrugRequest;
import com.devkraft.karmahealth.Model.AddDrugResponse;
import com.devkraft.karmahealth.Model.AddParameterAPIResponse;
import com.devkraft.karmahealth.Model.AddParameterNewAPI;
import com.devkraft.karmahealth.Model.AuthModel;
import com.devkraft.karmahealth.Model.ConfigureDto;
import com.devkraft.karmahealth.Model.DashboardResponse;
import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.DrugListAPIResponse;
import com.devkraft.karmahealth.Model.FCMDataRequest;
import com.devkraft.karmahealth.Model.GetKymUserDetailsRequest;
import com.devkraft.karmahealth.Model.GetParametersListResponse;
import com.devkraft.karmahealth.Model.GetUserAddedSymptomsResponseDTO;
import com.devkraft.karmahealth.Model.GetUserDetailsRequest;
import com.devkraft.karmahealth.Model.GetUserDetailsResponse;
import com.devkraft.karmahealth.Model.LoginNewResponse;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.ParamDto;
import com.devkraft.karmahealth.Model.PatientIdModel;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.RetrofitDrAvailabilityModel;
import com.devkraft.karmahealth.Model.RetrofitPatientIDRequest;
import com.devkraft.karmahealth.Model.RetrofitPrescriptionsModel;
import com.devkraft.karmahealth.Model.SignInRequestModel;
import com.devkraft.karmahealth.Model.SignInResponseModel;
import com.devkraft.karmahealth.Model.SignUpRequestModel;
import com.devkraft.karmahealth.Model.SignUpResponseModel;
import com.devkraft.karmahealth.Model.TrackConfigurationDto;
import com.devkraft.karmahealth.Model.TrackJSONForRequest;
import com.devkraft.karmahealth.Model.ValidateRequest;
import com.devkraft.karmahealth.Model.ValidateResponse;
import com.devkraft.karmahealth.Model.ValidationLoginResponse;
import com.devkraft.karmahealth.Model.VerifyOTPRequest;
import com.devkraft.karmahealth.Model.VerifyOTPResponse;
import com.devkraft.karmahealth.Model.RetrfoitUploadModel;
import com.devkraft.karmahealth.Model.VersionControllModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @Headers("Content-Type: application/json")
    @POST("doctor/availability")
    Call<RetrofitDrAvailabilityModel> availability(@Body RetrofitPatientIDRequest param,
                                                   @HeaderMap Map<String,String> headers);


    @Headers("Content-Type: application/json")
    @POST("prescriptions/list")
    Call<RetrofitPrescriptionsModel> prescriptions(@Body RetrofitPatientIDRequest param,
                                                   @HeaderMap Map<String,String> headers);

    @FormUrlEncoded
    @POST("patient/authenticate")
    Call<AuthModel> authenticate(@Field("patient_id") String patientId,
                                 @Field("token") String token,
                                 @HeaderMap Map<String,String> headers);


//    @GET("api/auth/sendOTP/{mobile}")
//    Call<LoginNewResponse> genrateotp(@Path("mobile") String mobile);

    @GET("{mobile}/AUTOGEN")
    Call<LoginNewResponse> genrateotp(@Path("mobile") String mobile);

    @Headers("Content-Type: application/json")
    @POST("api/drug/{mobile}/addDrug")
    Call<AddDrugResponse> addDrug(@Path("mobile") String mobile,@Body AddDrugRequest param,
                                  @Header("Authorization")String auth);

    @Headers("Content-Type: application/json")
    @GET("api/drug/druglist?")
    Call<List<DrugListAPIResponse>> druglist(@Query("medicineName") String zipCode,
                                             @Header("Authorization")String auth);

    @Headers("Content-Type: application/json")
    @GET("api/parameters/{userId}/getParameterListByUserId?")
    Call<List<ParameterListAPIResponse>> parameterlist(@Path("userId") String userId,@Query("parameter") String zipCode,
                                                       @Header("Authorization")String auth);

    @Headers("Content-Type: application/json")
    @POST("api/parameters/{mobile}/adduserparameter")
    Call<AddParameterAPIResponse> addParameterData(@Path("mobile") String mobile, @Body AddParameterNewAPI param,
                                                   @Header("Authorization")String auth);

    @Headers("Content-Type: application/json")
    @GET("api/parameters/{mobile}/getparameters")
    Call<DiseaseDto> parameterDataList(@Path("mobile") String mobile,
                                       @Header("Authorization")String auth);



    @Headers("Content-Type: application/json")
    @GET("api/parameters/parameter/{id}")
    Call<ParamDto> getParameterData(@Path("id") String id,
                                     @Header("Authorization")String auth);


    @Headers("Content-Type: application/json")
    @GET("api/symptoms/user/{user}?")
    Call<List<GetUserAddedSymptomsResponseDTO>> symptomsDataList(@Path("user") String user, @Query("date") String date,
                                                                 @Header("Authorization")String auth);



    @Headers("Content-Type: application/json")
    @POST("api/parameters/{userid}/{parameterid}/track")
    Call<APIMessageResponse> parameterTrack(@Path("userid") String userid,
                                            @Path("parameterid") String parameterid,
                                            @Body TrackConfigurationDto param,
                                            @Header("Authorization")String auth);



    @Headers("Content-Type: application/json")
    @GET("api/parameters/{userId}/parameter/{parameterId}")
    Call<ConfigureDto> getParameterAPI(@Path("userId") String userId,
                                       @Path("parameterId") String parameterId,
                                       @Header("Authorization")String auth);

    @Headers("Content-Type: application/json")
    @PUT("api/parameters/{userId}/parameter/{parameterId}")
    Call<APIMessageResponse> updateParameterAPI(@Path("userId") String userId,
                                          @Path("parameterId") String parameterId,
                                          @Body ConfigureDto param,
                                                @Header("Authorization")String auth);


    @Headers("Content-Type: application/json")
    @DELETE("api/parameters/{userId}/{parameterId}")
    Call<APIMessageResponse> deleteParameterAPI(@Path("userId") String userId,
                                                @Path("parameterId") String parameterId,
                                                @Header("Authorization")String auth);

    @Headers("Content-Type: application/json")
    @DELETE("api/drug/{userId}/{parameterId}/{type}")
    Call<APIMessageResponse> deleteDrugAPI(@Path("userId") String userId,
                                                @Path("parameterId") String parameterId,
                                                @Path("type") String type,
                                           @Header("Authorization")String auth);


    @Headers("Content-Type: application/json")
    @POST("api/prescription/{kymId}/addAll")
    Call<RetrfoitUploadModel> uploadData(@Path("kymId") String kymId, @Header("Authorization")String auth,@Body RetrofitPrescriptionsModel param);

    @Headers("Content-Type: application/json")
    @GET("api/drug/{kymId}/getdrug")
    Call<DashboardResponse> dashbordAPI(@Path("kymId") String kymId, @Header("Authorization")String auth);

    @POST("api/auth/updatefcm")
    Call<APIMessageResponse> fcmData(@Body FCMDataRequest param);


    @GET("api/auth/app-version/android/{version}")
    Call<VersionControllModel> versionControll(@Path("version") String version);

    @POST("api/auth/validate-phone")
    Call<ValidateResponse> validateData(@Body ValidateRequest param);


    @POST("VERIFY/{kymId}/{otp}")
    Call<VerifyOTPResponse> verifyOTPs(@Path("kymId") String kymId,@Path("otp") String otp);


    @POST("api/auth/signin")
    Call<SignInResponseModel> signin(@Body SignInRequestModel param);


    @POST("api/auth/signup")
    Call<SignUpResponseModel> signup(@Body SignUpRequestModel param);



    @GET("api/auth/validate-patientId/{pId}")
    Call<PatientIdModel> patentIdCheck(@Path("pId") String pId);

    @GET("api/auth/validate-phoneNumber/{mobile}")
    Call<PatientIdModel> mobileNoCheck(@Path("mobile") String mobile);


    @Headers("Content-Type: application/json")
    @POST("api/user/get-user-data")
    Call<GetUserDetailsResponse> getuserDetails(@Body GetUserDetailsRequest param, @Header("Authorization")String auth);



    @Headers("Content-Type: application/json")
    @POST("api/auth/signin")
    Call<LoginResponse> getkymuserDetails(@Body GetKymUserDetailsRequest param, @Header("Authorization")String auth);




    @POST("api/auth/refreshtoken")
    Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest param);







    @FormUrlEncoded
    @POST("patient/validate")
    Call<ValidationLoginResponse> karmaValidation(@Field("patient_id") String patientId,
                                                  @HeaderMap Map<String,String> headers);


}
