package com.lighterletter.www.networking.oldschool;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lighterletter.www.networking.R;
import com.lighterletter.www.networking.model.Issue;
import com.lighterletter.www.networking.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    private final String url = "https://api.github.com/repos/rails/rails/issues";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new NetworkTask().execute(url);
    }


    public class NetworkTask extends AsyncTask<String, Void, Integer> {
        private List<Issue> githubIssues = new ArrayList<>();

        @Override
        protected Integer doInBackground(String... strings) {
            InputStream inputStream = null;
            HttpsURLConnection urlConnection = null;
            Integer result = 0;

            try {

                URL url = new URL(strings[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {

                    Log.v("okay", statusCode + "");
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    parseResult(response);
                    result = 1; // Data!

                } else {
                    result = 0; //"No data :(";
                }

            } catch (Exception e) {
                Log.d("doInBackground", e.getLocalizedMessage());
            }
            return result; //defaults to no data
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 1) {
                //success! set the main adapter
                Log.d(TAG, "onPostExecute: " + githubIssues.size());

            } else {
                Log.e("issuePostExecute", "Failed to fetch data!");
            }

        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            inputStream.close();
            return result;
        }

        private void parseResult(String result) {

            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    Issue githubIssue = new Issue();
                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String comments_url = jsonobject.getString("comments_url");
                    githubIssue.setComments_url(comments_url);

                    String title = jsonobject.getString("title");
                    githubIssue.setTitle(title);

                    JSONObject user = jsonobject.getJSONObject("user");
                    String login = user.getString("login"); //username
                    User userObj = new User();
                    userObj.setLogin(login);
                    githubIssue.setUser(userObj);

                    String body = jsonobject.getString("body");
                    githubIssue.setBody(body);
                    githubIssues.add(githubIssue);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
