package com.lighterletter.www.networking.newschool;

import com.lighterletter.www.networking.model.Issue;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface GitHubService {

    @GET("repos/rails/rails/issues")
    Call<List<Issue>> getRailsIssues();

    @GET("users/{username}/received_events")
    Call<ResponseBody> getReceivedEvents(@Path("username") String username);

}
