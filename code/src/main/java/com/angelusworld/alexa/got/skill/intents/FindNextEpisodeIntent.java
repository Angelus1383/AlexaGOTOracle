/**
 * Licensed under the Apache License, Version 2.0 (the "License"). 
 * You may not use this file except in compliance with the License. 
 * A copy of the License is located at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  or in the "license" file accompanying this file. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.angelusworld.alexa.got.skill.intents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Handles the {@link FindNextEpisodeIntent} returning a message with data on the next episode.
 * @author Valerio Capozio
 * @author Andrea Capozio
 */
public class FindNextEpisodeIntent extends AbstractGOTIntent {

	/**
	 * Service URL
	 */
	private static final String SERVICE_URL = "http://api.tvmaze.com/shows/82/episodes";

	@Override
	public SpeechletResponse handleIntent(Intent intent, Session session) {
		SpeechletResponse response;
		try {
			LOGGER.info("FindNextEpisodeIntent invoked.");
			JSONArray json = performRemoteGetCall(SERVICE_URL);
			if(json != null && json.length()>0){
				response = produceResponse(json);
			}else{
				LOGGER.error("A json object null or without message element is returned.");
				response = newTellResponse("I'm not a spoiler.");
			}
		} catch (Exception e) {
			LOGGER.error("Unable to perform intent request.", e);
			response = newTellResponse(COMMUNICATION_ERROR_MESSAGE);
		}
		return response;
	}
	
	/**
	 * Parses the {@link JSONObject} and returns a message with extracted information.
	 * @param json to parse.
	 * @return a message with the extracted information.
	 */
	private SpeechletResponse produceResponse(JSONArray json){
		SpeechletResponse response;
		try{
			StringBuilder sb = new StringBuilder();
			SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd");
			for(int i = json.length()-1; i >= 0; i--){ 
				if(dt.parse(json.getJSONObject(i).getString("airdate")).before(new Date(System.currentTimeMillis()))){
					sb.append(" There is not data about next episode, because the Lord of Light hates spoilers.");
					break;
				}else{
					sb.append("The next episode will be shown on ").append(json.getJSONObject(i).getString("airdate")).append(".");
				}
			}
			response = newTellResponse(sb.toString());
		}catch(JSONException | ParseException e){
			LOGGER.error("Unable to find expected fields in the returned json object.", e);
			response = newTellResponse("I'm not a spoiler.");
		}
		return response;
	}

}
