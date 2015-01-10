/**
 * 
 */
package com.itsa.traffic.voice;

import java.util.List;

/**
 * 
 * Created on: Jan 10, 2015
 * 
 * @author Alisson Oliveira
 *
 */
public interface VoiceCommandHandler {
	
	public void handleCommand(List<String> result);

	public void onVoiceCommandError(int error);

}
