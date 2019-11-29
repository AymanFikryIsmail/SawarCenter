package com.hesham.sawar.networkmodule;


import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.PaperPojo;
import com.hesham.sawar.data.model.SubjectPojo;
import com.hesham.sawar.data.model.UserPojo;
import com.hesham.sawar.data.response.AssistantResponse;
import com.hesham.sawar.data.response.CenterResponse;
import com.hesham.sawar.data.response.CheckEmployeeResponse;
import com.hesham.sawar.data.response.CustomResponse;
import com.hesham.sawar.data.response.DepartmentResponse;
import com.hesham.sawar.data.response.DetailsResponse;
import com.hesham.sawar.data.response.FacultyResponse;
import com.hesham.sawar.data.response.ImageResponse;
import com.hesham.sawar.data.response.LoginResponse;
import com.hesham.sawar.data.response.OrderInfoResponse;
import com.hesham.sawar.data.response.OrderResponse;
import com.hesham.sawar.data.response.PaperResponse;
import com.hesham.sawar.data.response.SignUpResponse;
import com.hesham.sawar.data.response.SubjectResponse;
import com.hesham.sawar.data.response.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by ayman on 2019-05-13.
 */

public interface ApiRequest {

    @POST("employee/login")
    Call<LoginResponse> SignIn(@Body UserPojo body);

    @FormUrlEncoded
    @POST("employee/login/check")
    Call<CheckEmployeeResponse> checkForEmployee(
                @Field("id") int  id
    );

    @POST("employee/signup")
    Call<CustomResponse> signup(@Body UserPojo body);

    @FormUrlEncoded
    @POST("employee/update")
    Call<SignUpResponse> updateProfile(
            @Field("assistant_id") int  assistant_id ,
            @Field("name") String  name ,
            @Field("phone") String  phone ,
            @Field("email") String  email
            );


    @POST("cc/update")
    Call<CustomResponse> updateCC(@Body UserPojo body);

    @POST("cc/add")
    Call<UserResponse> addCenter(@Body UserPojo body);
    @Multipart
    @POST("image/")
    Call<ImageResponse> uploadProfileImages(@Part("file\";filename=\"files.jpg\" ") RequestBody file);
//@Part MultipartBody.Part file );
    @POST("cc/all")
    Call<CenterResponse> getAllCenter();

    @GET("faculty/all")
    Call<FacultyResponse> getFaculties();

    @POST("faculty/addtocc")
    Call<CustomResponse> addFacultiesToCenter(@Body FacultyPojo body);

    @POST("faculty/subjects/all")
    Call<SubjectResponse> getAllSubjects(@Body SubjectPojo body);

    @POST("faculty/subjects/filter")
    Call<SubjectResponse> getFilteredSubjects(@Body SubjectPojo body);

    @FormUrlEncoded
    @POST("faculty/departments")
    Call<DepartmentResponse> getAllDepartments(@Field("f_id") int f_id);

    @GET("faculty/paper/get")
    Call<PaperResponse> getPapers(@Query("category_id") int category_id , @Query("sub_id") int subId);


    @POST("faculty/subjects/add")
    Call<CustomResponse> addSubjects(@Body SubjectPojo body);

    @POST("faculty/subjects/rename")
    Call<CustomResponse>renameSubjects(@Body SubjectPojo body);
    @POST("faculty/subjects/remove")
    Call<CustomResponse> removeSubjects(@Body SubjectPojo body);



    @POST("faculty/paper/add")
    Call<CustomResponse> addPaper(@Body PaperPojo body);

    @POST("faculty/paper/rename")
    Call<CustomResponse> renamePaper(@Body PaperPojo body);
    @POST("faculty/paper/remove")
    Call<CustomResponse> removePaper(@Body PaperPojo body);


    @FormUrlEncoded
    @POST("employee/assistant/all")
    Call<AssistantResponse> getAllAssistants(@Field("cc_id") int body);
    @FormUrlEncoded
    @POST("employee/assistant/delete")
    Call<CustomResponse> deleteAssistants(@Field("assistant_id") int body);

    @FormUrlEncoded
    @POST("employee/requests")
    Call<AssistantResponse> getAllAssistantsRequests(@Field("cc_id") int body);

    @FormUrlEncoded
    @POST("employee/accept")
    Call<CustomResponse> acceptAssistants(@Field("assistant_id") int body);

    @FormUrlEncoded
    @POST("employee/reject")
    Call<CustomResponse> rejectAssistants(@Field("assistant_id") int body);



    @GET("univ/all")
    Call<FacultyResponse> getUniversities();

    @FormUrlEncoded
    @POST("cc/home")
    Call<FacultyResponse> getHomeFaculties(@Field("cc_id") int cc_id);

    @GET("cc/problem")
    Call<CustomResponse> postProblem(@Query("hours") int hours ,@Query("cc_id") int cc_id  );




    @GET("order/unready")
    Call<OrderResponse> getUnreadyOrders(@Query("cc_id") int centerid);

    @GET("order/ready")
    Call<OrderResponse> getReadyOrders(@Query("cc_id") int centerid);

    @GET("order/makeready")
    Call<CustomResponse> makeReadyOrder(@Query("order_id") int order_id);


    @GET("order/cancelcc")
    Call<CustomResponse> removeReadyOrder(@Query("order_id") int order_id);


    @GET("order/history")
    Call<OrderResponse> getOrderHistory(@Query("cc_id") int centerid);


    @GET("order/details")
    Call<DetailsResponse> getOrderDetails(@Query("order_id") int order_id);


    @GET("order/info")
    Call<OrderInfoResponse> getOrderInfo(@Query("cc_id") int cc_id);



}





