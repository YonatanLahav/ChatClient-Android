package com.example.whatsapp.api;

import com.example.whatsapp.entities.*;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

public interface WebServiceAPI {

    /**
     * Contact api section.
     */
    @GET("api/contacts")
    Call<List<ApiContact>> GetAllContacts(@Query("username") String username);

    @POST("api/contacts")
    Call<Void> CreateContact(@Body ContactsPostRequest contact, @Query("username") String username);

    @GET("api/contacts/{contactId}")
    Call<ApiContact> GetContact(@Path(value = "contactId", encoded = true) String contactId, @Query("username") String username);

    @PUT("api/contacts/{contactId}")
    Call<Void> EditContact(@Body ContactsPutRequest contact, @Path("contactId") int contactId, @Query("username") String username);

    @DELETE("api/contacts/{contactId}")
    Call<Void> DeleteContact(@Path("contactId") int contactId, @Query("username") String username);

    @POST("api/invitations")
    Call<Void> PostInvitation(@Body ApiInvitation invitation);

    /**
     * Message api section.
     */
    @GET("api/contacts/{contactId}/messages")
    Call<List<ApiMessage>> GetAllContactMessage(@Path("contactId") String contactId, @Query("username") String username);

    @GET("api/contacts/{contactId}/messages/{messageId}")
    Call<ApiMessage> GetMessageById(@Path("contactId") String contactId, @Path("messageId") int messageId, @Query("username") String username);

    @DELETE("api/contacts/{contactId}/messages/{messageId}")
    Call<Void> DeleteMessage(@Path("contactId") String contactId, @Path("messageId") int messageId, @Query("username") String username);

    @POST("api/contacts/{contactId}/messages")
    Call<ApiMessage> CreateMessage(@Body MessagePostRequest content, @Path("contactId") String contactId, @Query("username") String username);

    @PUT("api/contacts/{contactId}/messages/{messageId}")
    Call<Void> EditMessageById(@Path("contactId") String contactId, @Path("messageId") int messageId, @Query("username") String username, @Body MessagePostRequest content);

    @POST("api/transfer")
    Call<Void> PostTransfer(@Body ApiTransfer transfer);

    /**
     * Login and Register section.
     */
    @POST("Login")
    Call<User> Login(@Body LoginPostRequest loginPostRequest);

    @POST("Login/token")
    Call<String> UpdateToken(@Body LoginPostRequest loginPostRequest);

    @POST("Register")
    Call<User> Register(@Body User user);

}