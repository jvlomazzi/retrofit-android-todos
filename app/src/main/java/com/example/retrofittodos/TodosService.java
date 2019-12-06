package com.example.retrofittodos;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface TodosService {
    @POST("todos")
    Call<Todos> post(@Body Todos todos);

    @GET("todos/{id}")
    Call<Todos> get(@Path("id") Integer id);

    @GET("todos")
    Call<List<Todos>> get();

    @PUT("todos/{id}")
    Call<Todos> put(@Path("id") Integer id, @Body Todos todos);

    @PATCH("todos/{id}")
    Call<Todos> patch(@Path("id") Integer id, @Body Todos todos);

    @DELETE("todos/{id}")
    Call<Void> delete(@Path("id") Integer id);
}
