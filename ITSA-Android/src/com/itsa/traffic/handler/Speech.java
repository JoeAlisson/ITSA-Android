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
package com.itsa.traffic.handler;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

/**
 * Created on: Jan 07, 2015
 * 
 * @author Alisson Oliveira
 *
 */
public class Speech implements OnInitListener {

	TextToSpeech tts;

	public Speech(Context ctx) {
		tts = new TextToSpeech(ctx, this);
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

}
