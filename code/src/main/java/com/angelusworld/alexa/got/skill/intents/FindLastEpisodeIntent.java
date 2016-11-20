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
import org.jsoup.Jsoup;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Handles the {@link FindLastEpisodeIntent} returning a message with data on the last delivered episode.
 * @author Valerio Capozio
 * @author Andrea Capozio
 */
public class FindLastEpisodeIntent extends AbstractGOTIntent {

	/**
	 * Service URL
	 */
	private static final String SERVICE_URL = "http://api.tvmaze.com/shows/82/episodes";

	@Override
	public SpeechletResponse handleIntent(Intent intent, Session session){
		SpeechletResponse response;
		try {
			LOGGER.info("FindLastEpisodeIntent invoked.");
			JSONArray json = performRemoteGetCall(SERVICE_URL);
			if(json != null && json.length()>0){
				response = produceResponse(json);
			}else{
				LOGGER.error("A json object null or without message element is returned.");
				response = newTellResponse(COMMUNICATION_ERROR_MESSAGE);
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
					sb.append("The title of the last episode is: ").append(json.getJSONObject(i).getString("name")).append(". ")
					.append("The synopsis of the episode is the following. ").append(Jsoup.parseBodyFragment(json.getJSONObject(i).getString("summary")).text());
					break;
				}
			}
			response = newTellResponse(sb.toString());
		}catch(JSONException | ParseException e){
			LOGGER.error("Unable to find expected fields in the returned json object.", e);
			response = newTellResponse("The Lord of light does not want show you this now.");
		}
		return response;
	}

}
