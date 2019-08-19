package com.hesham.sawar.networkmodule;


import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.AssistantResponse;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.data.response.SubjectResponse;
import com.hesham.sawar.data.response.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ayman on 2019-05-13.
 */

public interface ApiRequest {

    @POST("employee/login")
    Call<UserResponse> SignIn(@Body UserPojo body);

    @POST("employee/signup")
    Call<Object> signup(@Body UserPojo body);
    @POST("cc/add")
    Call<UserResponse> addCenter(@Body UserPojo body);

    @POST("cc/all")
    Call<UserResponse> getAllCenter();

    @GET("faculty/all")
    Call<FacultyResponse> getFaculties();

    @POST("faculty/addtocc")
    Call<Object> addFacultiesToCenter(@Body FacultyPojo body);

    @POST("faculty/subjects/all")
    Call<SubjectResponse> getAllSubjects(@Body SubjectPojo body);



    @FormUrlEncoded
    @POST("employee/assistant/all")
    Call<AssistantResponse> getAllAssistants(@Field("cc_id") int body);
    @FormUrlEncoded
    @POST("employee/assistant/delete")
    Call<AssistantResponse> deleteAssistants(@Field("assistant_id ") int body);

    @FormUrlEncoded
    @POST("employee/requests")
    Call<AssistantResponse> getAllAssistantsRequests(@Field("cc_id") int body);

    @FormUrlEncoded
    @POST("employee/accept")
    Call<AssistantResponse> acceptAssistants(@Field("assistant_id ") int body);

    @FormUrlEncoded
    @POST("employee/reject")
    Call<AssistantResponse> rejectAssistants(@Field("assistant_id ") int body);



    @GET("univ/all")
    Call<FacultyResponse> getUniversities();

    @GET("cc/home")
    Call<FacultyResponse> getHomeFaculties(@Query("cc_id") int cc_id);
}





