package com.chefmate.ai;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenAiJsonService {

    public static String cleanJsonResponse(String response, boolean isArray) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray choices = jsonObject.getJSONArray("choices");

        // Extract the "content" string, which should contain the JSON array
        JSONObject choice = choices.getJSONObject(0);
        JSONObject message = choice.getJSONObject("message");
        String content = message.getString("content").trim();


        Log.d("OpenAI API JSON Service", "Raw Content Before Cleanup: " + content);

        // Use a regular expression to capture only the JSON array part within brackets []
        Pattern jsonArrayPattern;
        if(isArray)
            jsonArrayPattern = Pattern.compile("\\[(.*?)\\]", Pattern.DOTALL);
        else
            jsonArrayPattern = Pattern.compile("\\{.*?\\}", Pattern.DOTALL);

        Matcher matcher = jsonArrayPattern.matcher(content);

        if (matcher.find()) {
            content = matcher.group(0);  // This should capture the JSON array portion
        }

          // Step 1: Clean out markdown markers and extra whitespace
        content = content.replaceAll("json|", "").trim();
        content = content.replaceAll("(?<=[\\p{IsHebrew}])\"(?=[\\p{IsHebrew}])", "");

        return content;
    }
}
