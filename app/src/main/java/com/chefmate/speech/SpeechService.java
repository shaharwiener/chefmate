package com.chefmate.speech;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechService {
    private TextToSpeech textToSpeech;

    public SpeechService(Context context){
        // Initialize Text-to-Speech
        this.textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Set language to Hebrew (or any other supported language)
                int result = this.textToSpeech.setLanguage(new Locale("he", "IL")); // Hebrew
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                  //  Toast.makeText(this, "Hebrew language not supported", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("TTS", "Initialization failed");
            }
        });
    }

    public void speak(Object obj) {
        if (textToSpeech != null && obj != null) {
            StringBuilder optionsText = new StringBuilder();
            if(obj instanceof ArrayList){
                ArrayList objArrayList = (ArrayList)obj;
                for (int i = 0; i < objArrayList.size() && i < 4; i++) {
                    optionsText.append("Option ").append(i + 1).append(": ").append(objArrayList.get(i)).append(". ");
                }
            }
            this. textToSpeech.speak(optionsText.toString(), TextToSpeech.QUEUE_FLUSH, null, "optionsTTS");
        }
    }

    public void stop(){
        this.textToSpeech.stop();
    }

    public void destroy(){
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
