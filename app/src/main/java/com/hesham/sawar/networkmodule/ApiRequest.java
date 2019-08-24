package com.hesham.sawar.networkmodule;


import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.PaperPojo;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.AssistantResponse;
import com.hesham.sawar.data.response.DetailsResponse;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.data.response.LoginResponse;
import com.hesham.sawar.data.response.OrderInfoResponse;
import com.hesham.sawar.data.response.OrderResponse;
import com.hesham.sawar.data.response.PaperResponse;
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
    Call<LoginResponse> SignIn(@Body UserPojo body);

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

    @GET("faculty/paper/get")
    Call<PaperResponse> getPapers(@Query("type") String type , @Query("sub_id") int subId);


    @POST("faculty/subjects/add")
    Call<Object> addSubjects(@Body SubjectPojo body);

    @POST("faculty/subjects/rename")
    Call<Object>renameSubjects(@Body SubjectPojo body);
    @POST("faculty/subjects/remove")
    Call<Object> removeSubjects(@Body SubjectPojo body);



    @POST("faculty/paper/add")
    Call<Object> addPaper(@Body PaperPojo body);

    @POST("faculty/paper/rename")
    Call<Object> renamePaper(@Body PaperPojo body);
    @POST("faculty/paper/remove")
    Call<Object> removePaper(@Body PaperPojo body);


    @FormUrlEncoded
    @POST("employee/assistant/all")
    Call<AssistantResponse> getAllAssistants(@Field("cc_id") int body);
    @FormUrlEncoded
    @POST("employee/assistant/delete")
    Call<Object> deleteAssistants(@Field("assistant_id") int body);

    @FormUrlEncoded
    @POST("employee/requests")
    Call<AssistantResponse> getAllAssistantsRequests(@Field("cc_id") int body);

    @FormUrlEncoded
    @POST("employee/accept")
    Call<Object> acceptAssistants(@Field("assistant_id") int body);

    @FormUrlEncoded
    @POST("employee/reject")
    Call<Object> rejectAssistants(@Field("assistant_id") int body);



    @GET("univ/all")
    Call<FacultyResponse> getUniversities();

    @GET("cc/home")
    Call<FacultyResponse> getHomeFaculties(@Query("cc_id") int cc_id);






    @GET("order/unready")
    Call<OrderResponse> getUnreadyOrders(@Query("cc_id") int centerid);

    @GET("order/ready")
    Call<OrderResponse> getReadyOrders(@Query("cc_id") int centerid);

    @GET("order/makeready")
    Call<Object> makeReadyOrder(@Query("order_id") int order_id);


    @GET("order/cancelcc")
    Call<Object> removeReadyOrder(@Query("order_id") int order_id);


    @GET("order/history")
    Call<OrderResponse> getOrderHistory(@Query("cc_id") int centerid);


    @GET("order/details")
    Call<DetailsResponse> getOrderDetails(@Query("order_id") int order_id);


    @GET("order/info")
    Call<OrderInfoResponse> getOrderInfo(@Query("cc_id") int cc_id);



}





