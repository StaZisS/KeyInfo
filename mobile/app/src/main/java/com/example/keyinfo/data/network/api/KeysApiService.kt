package com.example.keyinfo.data.network.api

import com.example.keyinfo.domain.model.keys.Key
import com.example.keyinfo.domain.model.keys.KeyId
import com.example.keyinfo.domain.model.keys.Transfer
import com.example.keyinfo.domain.model.keys.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface KeysApiService {
    @POST("/api/v1/transfers/requests/{receiver_id}")
    suspend fun createTransfer(
        @Path("receiver_id") id: String, @Body body: KeyId
    ): Response<Unit>

    @GET("/api/v1/keys")
    suspend fun getUserKeys(): Response<ArrayList<Key>>

    @GET("/api/v1/transfers/requests/my")
    suspend fun getMyRequests(
        @Query("status_transfer_request") status: String
    ): Response<ArrayList<Transfer>>

    @GET("/api/v1/transfers/requests/foreign")
    suspend fun getForeignRequests(
        @Query("status_transfer_request") status: String
    ): Response<ArrayList<Transfer>>

    @DELETE("/api/v1/transfers/requests")
    suspend fun deleteMyTransfer(
        @Query("transfer_request_id") id: String
    ): Response<Unit>

    @PATCH("/api/v1/transfers/requests/{transfer_request_id}/accept")
    suspend fun acceptRequest(
        @Path("transfer_request_id") id: String
    ): Response<Unit>

    @PATCH("/api/v1/transfers/requests/{transfer_request_id}/decline")
    suspend fun declineRequest(
        @Path("transfer_request_id") id: String
    ): Response<Unit>

    @GET("/api/v1/users")
    suspend fun getAllUsers(): Response<ArrayList<User>>
}