package com.iserhand.inviochallengefinal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CatInterface {
    @GET("v1/breeds")
    Call<String> STRING_CALL(
            @Header("x-api-key") String apiKey,
            @Query("page") int page,
            @Query("limit") int limit
    );
    @GET("v1/breeds/search")
    Call<String> STRING_CALL_SEARCH(
            @Header("x-api-key") String apiKey,
            @Query("q") String searchKey
    );



}
