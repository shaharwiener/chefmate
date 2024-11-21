package com.chefmate.speech;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A service for handling text-to-speech (TTS) functionality in the application.
 * The `SpeechService` provides methods to initialize TTS, speak provided text or options,
 * stop ongoing speech, and release TTS resources.
 */
public class SpeechService {
    private TextToSpeech textToSpeech;

    /**
     * Initializes the Text-to-Speech engine and sets the language to Hebrew (Israel).
     *
     * @param context The context of the calling activity or application.
     */
    public SpeechService(Context context) {
        this.textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Set language to Hebrew
                int result = this.textToSpeech.setLanguage(new Locale("he", "IL"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Hebrew language not supported or missing data");
                }
            } else {
                Log.e("TTS", "Initialization failed");
            }
        });
    }

    /**
     * Speaks the provided object, which can be a list of options. The method constructs
     * speech text from the object and speaks it using the TTS engine.
     *
     * @param obj The object to be spoken. If it is an `ArrayList`, the service reads up to
     *            four items as options.
     */
    public void speak(Object obj) {
        if (textToSpeech != null && obj != null) {
            StringBuilder optionsText = new StringBuilder();

            // Handle ArrayList input for speaking multiple options
            if (obj instanceof ArrayList) {
                ArrayList<?> objArrayList = (ArrayList<?>) obj;
                for (int i = 0; i < objArrayList.size() && i < 4; i++) {
                    optionsText.append("Option ").append(i + 1).append(": ").append(objArrayList.get(i)).append(". ");
                }
            }

            // Speak the constructed text
            this.textToSpeech.speak(optionsText.toString(), TextToSpeech.QUEUE_FLUSH, null, "optionsTTS");
        }
    }

    /**
     * Stops any ongoing speech output.
     */
    public void stop() {
        if (textToSpeech != null) {
            this.textToSpeech.stop();
        }
    }

    /**
     * Releases the resources used by the Text-to-Speech engine.
     * Should be called when the service is no longer needed to prevent resource leaks.
     */
    public void destroy() {
        if (textToSpeech != null) {
            this.textToSpeech.stop();
            this.textToSpeech.shutdown();
        }
    }
}
