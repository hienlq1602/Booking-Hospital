package com.ndm.ptit.api;

import com.ndm.ptit.enitities.BaseResponse;
import com.ndm.ptit.enitities.BaseResponse2;
import com.ndm.ptit.enitities.BaseResponse3;
import com.ndm.ptit.enitities.appointment.Appointment;
import com.ndm.ptit.enitities.appointment.DetailAppointment;
import com.ndm.ptit.enitities.booking.Booking;
import com.ndm.ptit.enitities.booking.BookingImage;
import com.ndm.ptit.enitities.doctor.DoctorResponse;
import com.ndm.ptit.enitities.login.LoginRequest;
import com.ndm.ptit.enitities.login.LoginRespone;
import com.ndm.ptit.enitities.login.Patient;
import com.ndm.ptit.enitities.notification.Notification;
import com.ndm.ptit.enitities.notification.ReadResponse;
import com.ndm.ptit.enitities.record.RecordRespone;
import com.ndm.ptit.enitities.services.DoctorService;
import com.ndm.ptit.enitities.services.DoctorServiceResponse;
import com.ndm.ptit.enitities.services.Services;
import com.ndm.ptit.enitities.services.ServicesResponse;
import com.ndm.ptit.enitities.signup.SignUpRequest;
import com.ndm.ptit.enitities.signup.SignUpResponse;
import com.ndm.ptit.enitities.speciality.SpecialityResponse;
import com.ndm.ptit.enitities.treatment.Treatment;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/login")
    Call<LoginRespone> login(@Body LoginRequest loginRequest);

    @POST("api/login")
    Call<SignUpResponse> signup(@Body SignUpRequest signUpRequest);

    @GET("api/specialities")
    Call<BaseResponse<SpecialityResponse>> getAllSpecialities(
            @Header("Authorization") String token
    );

    @GET("api/appointment/")
    Call<BaseResponse<Appointment>> getAllAppointment(
            @Header("Authorization") String token,
            @Query("size") int size,
            @Query("page") int page
    );

    @GET("api/appointment/{id}")
    Call<DetailAppointment> getDetailAppointment(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("api/appointment-records/{id}")
    Call<BaseResponse2<RecordRespone>> getRecordReadByID(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("api/treatments/{id}")
    Call<BaseResponse2<Treatment>> getTreatmentByID(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @PUT("/api/patient/notifications/mark-as-read/{id}")
    Call<ReadResponse> AsReadByID(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @PUT("/api/patient/notifications/mark-as-read-all")
    Call<ReadResponse> allAsRead(
            @Header("Authorization") String token
    );

    @GET("/api/patient/notifications")
    Call<BaseResponse<Notification>> getAllNoti(
            @Header("Authorization") String token
    );

    @GET("/api/patient/booking/{id}")
    Call<BaseResponse2<Booking>> getBookingByID(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @DELETE("/api/patient/booking/{id}")
    Call<BaseResponse3> putBookingCancel(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("/api/booking/photos/{id}")
    Call<BaseResponse<BookingImage>> getBookingImageByID(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("api/services/{id}")
    Call<ServicesResponse> getServiceById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("api/doctors/service/{id}")
    Call<DoctorServiceResponse> getDoctorServiceById(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("api/doctors/{id}")
    Call<BaseResponse2<DoctorResponse>> getDoctorID(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @POST("/api/patient/booking")
    Call<BaseResponse2<Booking>> createBooking(
            @Header("Authorization") String token,
            @Body Map<String, String> body
    );
    @GET("/api/patient/booking")
    Call<BaseResponse<Booking>> bookingReadALl(
            @Header("Authorization") String token
    );


    @Multipart
    @POST("/api/booking/photos")
    Call<ResponseBody> uploadBookingPhoto(
            @Header("Authorization") String token,
            @Part MultipartBody.Part photo,
            @Part("booking_id") RequestBody bookingId
    );

    @GET("/api/patient/profile")
    Call<BaseResponse2<Patient>> readPersionalInformation(
            @Header("Authorization") String token
    );

    @PUT("/api/patient/profile")
    Call<BaseResponse2<Patient>> changeInformation(
            @Header("Authorization") String token,
            @Body Map<String, String> body
    );

    @Multipart
    @PUT("/api/patient/profile/avatar/{id}")
    Call<BaseResponse2<String>> uploadAvatar(
            @Part MultipartBody.Part filename,
            @Header("Authorization") String token,
            @Path("id") int id);


    @GET("/api/doctors")
    Call<BaseResponse<DoctorService>> doctorReadAll(
            @Header("Authorization") String token);

    @GET("/api/specialities")
    Call<BaseResponse<SpecialityResponse>> specialityReadAll(
            @Header("Authorization") String token);

    @GET("/api/services")
    Call<BaseResponse<Services>> serviceReadAll(
            @Header("Authorization") String token);

    @GET("api/specialities/{id}")
    Call<BaseResponse2<SpecialityResponse>> getSpecialityByID(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("api/doctors/speciality/{id}")
    Call<DoctorServiceResponse> getDoctorBySpecial(
            @Header("Authorization") String token,
            @Path("id") int id
    );

}
