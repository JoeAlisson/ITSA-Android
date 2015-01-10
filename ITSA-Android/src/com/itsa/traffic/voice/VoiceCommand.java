/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 */
package com.itsa.traffic.voice;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * 
 * Created on: Jan 09, 2015.
 * 
 * @author Alisson Oliveira
 * 
 *
 */
public class VoiceCommand implements OnInitListener, RecognitionListener {

	private TextToSpeech tts;
	private SpeechRecognizer recognizer;
	private VoiceCommandHandler handler;
	

	public VoiceCommand(Context ctx, VoiceCommandHandler handler) {
		this.handler = handler;
		tts = new TextToSpeech(ctx, this);
		recognizer = SpeechRecognizer.createSpeechRecognizer(ctx);
		recognizer.setRecognitionListener(this);
		
	}

	@Override
	public void onInit(int status) {
		if (status != TextToSpeech.ERROR) {
			tts.setLanguage(Locale.getDefault());
		}
	}
	
	public void speak(String text) {
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	public void heard() {
		Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // accept partial results if they come
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,  getClass().getPackage().getName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 15000);
        recognizer.startListening(recognizerIntent);
        
	}
	
	public void stop() {
		recognizer.stopListening();
		recognizer.cancel();
		recognizer.destroy();
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
		// TODO Auto-generated method stub
		Log.i("Recognizer", "on Ready");
	}

	@Override
	public void onBeginningOfSpeech() {
		Log.i("Recognizer", "on Beginning");
		
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onEndOfSpeech() {
		// TODO Auto-generated method stub
		Log.i("Recognizer", "End of");
	}

	@Override
	public void onError(int error) {
		// TODO Auto-generated method stub
		Log.i("Recognizer", "On error " + error);
		handler.onVoiceCommandError(error);
	}

	@Override
	public void onResults(Bundle results) {
		// TODO Auto-generated method stub
		Log.i("Recognizer", "on result");
		List<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		handler.handleCommand(result);
		
	}

	@Override
	public void onPartialResults(Bundle partialResults) {
		// TODO Auto-generated method stub
		Log.i("Recognizer", "On partial");
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
		// TODO Auto-generated method stub
		Log.i("Recognizer", "On event");
	}


}

