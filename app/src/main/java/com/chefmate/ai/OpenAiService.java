package com.chefmate.ai;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

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

/**
 * Abstract base class that handles communication with the OpenAI API.
 * This class provides functionality to send prompts to OpenAI and process responses asynchronously.
 * Activities extending this class must implement the {@link #handleOpenAiResponse(String)} method to handle API responses.
 */
public abstract class OpenAiService extends BaseActivity {

    private final String url = "https://api.openai.com/v1/chat/completions"; // OpenAI API endpoint
    private final String apiKey = "<Add here your API Key>"; // API key for authentication

    /**
     * Sends a request to the OpenAI API with the given prompt.
     *
     * @param prompt The input prompt to send to OpenAI.
     */
    public void callOpenAI(String prompt) {
        OkHttpClient client = new OkHttpClient();

        // Define the request body as JSON
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{ \"model\": \"gpt-4o-mini\", \"messages\": [{ \"role\" : \"user\", \"content\":\"" + prompt + "\"}], \"temperature\" : 0.7}";
        RequestBody body = RequestBody.create(json, JSON);

        // Build the HTTP request
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        // Log details of the request
        Log.i("OpenAI Request", "URL: " + url);
        Log.i("OpenAI Request", "Body JSON: " + json);

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                Log.e("OpenAiService", "Failed to connect: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        // Read and construct the response body
                        StringBuilder responseBody = new StringBuilder();
                        if (response.body() == null) {
                            Log.d("Response", "The body is null");
                        } else {
                            InputStream inputStream = response.body().byteStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                responseBody.append(line);
                            }
                            reader.close();

                            // Log the raw response
                            Log.d("OpenAI API", "Raw Response: " + responseBody.toString());
                        }

                        // Pass the response to the abstract handler method
                        handleOpenAiResponse(responseBody.toString());
                    } else {
                        // Log API request failure
                        Log.e("OpenAI Error", "Request failed: " + response.code() + " " + response.message() +
                                " " + (response.networkResponse() != null ? response.networkResponse().message() : ""));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } finally {
                    response.close();
                }
            }
        });
    }

    /**
     * Abstract method to handle the response from the OpenAI API.
     * Subclasses must implement this method to define how the response should be processed.
     *
     * @param response The raw response string received from OpenAI.
     * @throws JSONException if the response cannot be parsed as JSON.
     */
    protected abstract void handleOpenAiResponse(String response) throws JSONException;
}
