package com.example.trueclub_assignment;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterFace {
    @GET("/")
    Call<ApiResponse> getResponse(@Query("name") String name);
}
