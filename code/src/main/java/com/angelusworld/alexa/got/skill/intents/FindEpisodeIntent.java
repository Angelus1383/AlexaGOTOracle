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

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Handles the {@link FindEpisodeIntent} returning a message with data on the specified episode.
 * @author Valerio Capozio
 * @author Andrea Capozio
 */
public class FindEpisodeIntent extends AbstractGOTIntent {

	/**
	 * Service URL
	 */
	private static final String SERVICE_URL = "http://api.tvmaze.com/shows/82/episodebynumber?";
	/**
	 * Name of the slot to load.
	 */
	private static final String EPISODE = "episode";
	/**
	 * Name of the slot to load.
	 */
	private static final String SEASON = "season";	

	@Override
	public SpeechletResponse handleIntent(Intent intent, Session session) {
		SpeechletResponse response;
		try {
			Slot episode = intent.getSlot(EPISODE);
			Slot season = intent.getSlot(SEASON);
			LOGGER.info("FindEpisodeIntent invoked with episode:{} and season:{}",episode.getValue(),season.getValue());
			JSONObject json = performRemoteGetCall(SERVICE_URL+"season="+season.getValue()+"&number="+episode.getValue(),null,null);
			if(json != null && json.has("id")){
				response = produceResponse(json);
			}else{
				LOGGER.error("A json object null or without message element is returned.");
				response = newTellResponse("I cannot find this episode, seems like this doesn't exist. Are you sure to be a Game of Thrones real fan?");
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
	private SpeechletResponse produceResponse(JSONObject json){
		SpeechletResponse response;
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("The title of the episode ").append(json.getInt("number")).append(" in the season ")
			.append(json.getInt("season")).append(" is ").append(json.getString("name")).append(". ");
			sb.append("The synopsis of the episode is the following. ").append(Jsoup.parseBodyFragment(json.getString("summary")).text());
			response = newTellResponse(sb.toString());
		}catch(JSONException e){
			LOGGER.error("Unable to find expected fields in the returned json object.", e);
			response = newTellResponse("The Lord of light does not want show you this now.");
		}
		return response;
	}
}
