package com.chefmate.ai;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for processing and cleaning JSON responses from the OpenAI API.
 * This class provides methods to extract and clean specific JSON data from OpenAI's responses.
 */
public class OpenAiJsonService {

    /**
     * Cleans and extracts the JSON content from an OpenAI API response. Depending on the structure of the response,
     * it either extracts a JSON array or a JSON object while removing unwanted characters or formatting.
     *
     * @param response The raw JSON response string received from OpenAI.
     * @param isArray  Specifies whether the expected content is a JSON array. If true, extracts array data;
     *                 otherwise, extracts JSON object data.
     * @return A cleaned JSON string representing either a JSON array or object.
     * @throws JSONException if the input response cannot be parsed as valid JSON.
     */
    public static String cleanJsonResponse(String response, boolean isArray) throws JSONException {
        Log.d("OpenAiJsonService", "Before cleanup: " + response);
        // Parse the response string into a JSONObject
        JSONObject jsonObject = new JSONObject(response);

        // Extract the "choices" array from the JSON response
        JSONArray choices = jsonObject.getJSONArray("choices");

        // Extract the first "choice" object and its "message" content
        JSONObject choice = choices.getJSONObject(0);
        JSONObject message = choice.getJSONObject("message");
        String content = message.getString("content").trim();

        // Define a regular expression pattern to capture the JSON array or object portion
        Pattern jsonArrayPattern;
        if (isArray) {
            jsonArrayPattern = Pattern.compile("\\[(.*?)\\]", Pattern.DOTALL); // For JSON arrays
        } else {
            jsonArrayPattern = Pattern.compile("\\{.*?\\}", Pattern.DOTALL); // For JSON objects
        }

        // Match the pattern within the content
        Matcher matcher = jsonArrayPattern.matcher(content);
        if (matcher.find()) {
            content = matcher.group(0); // Capture the JSON portion
        }

        // Clean out unwanted markdown markers and unnecessary characters
        content = content.replaceAll("json|", "").trim(); // Remove "json|" markers
        content = content.replaceAll("(?<=[\\p{IsHebrew}])\"(?=[\\p{IsHebrew}])", ""); // Remove quotes in Hebrew text

        Log.d("OpenAiJsonService", "After cleanup: " + content);

        return content;
    }
}
