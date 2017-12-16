package com.lighterletter.www.networking.newschool;

import com.lighterletter.www.networking.model.Issue;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface GitHubService {
    @GET("repos/rails/rails/issues")
    Call<List<Issue>> getIssues();
}
