package com.chefmate.ai;

import android.util.Log;

import com.chefmate.BaseActivity;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class OpenAiService extends BaseActivity {
    final String url = "https://api.openai.com/v1/chat/completions";
    final String apiKey = "<Add here your API key>";

    public void callOpenAI(String prompt){
        // OpenAI API endpoint and key

        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{ \"model\": \"gpt-4o-mini\", \"messages\": [{ \"role\" : \"user\", \"content\":\"" + prompt + "\"}], \"temperature\" : 0.7}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();
        Log.i("OpenAI Request","URL: " + url);
        Log.i("OpenAI Request","body json: " + json);
        Log.i("OpenAI Request","Request: " + request);
        // Execute API request in a background thread
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Network Test", "Failed to connect: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {

                        StringBuilder responseBody = new StringBuilder();
                        if(response.body() == null)
                            Log.d("Response","The body is null");
                        else {
                            InputStream inputStream = response.body().byteStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                responseBody.append(line);
                            }
                            reader.close();
                            // Print the response body to Logcat
                            Log.d("OpenAI API", "Raw Response: " + responseBody.toString());
                        }

                        handleOpenAiResponse(responseBody.toString());
                    } else {
                        Log.e("OpenAI Error", "Request failed: " + response.code() + " " + response.message() + " " + response.networkResponse().message());
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } finally {
                    response.close();
                }
            }
        });
    }

    protected abstract void handleOpenAiResponse(String response) throws JSONException;
}
