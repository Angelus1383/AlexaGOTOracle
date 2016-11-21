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

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Handles the {@link CharacterPlaceIntent} providing a message with last known position of a character.
 * @author Valerio Capozio
 * @author Andrea Capozio
 */
public class CharacterPlaceIntent extends AbstractGOTIntent{
	/**
	 * Service URL
	 */
	private static final String SERVICE_URL = "https://api.got.show";
	/**
	 * URL Path
	 */
	private static final String URI_PATH = "/api/characters/paths/";
	/**
	 * Name of the slot to load.
	 */
	private static final String CHARACTER = "character";
	
	@Override
	public SpeechletResponse handleIntent(Intent intent, Session session) {
		SpeechletResponse response;
		try {
			Slot character = intent.getSlot(CHARACTER);
			LOGGER.info("CharacterPlaceIntent invoked passing character:{} ",character.getValue());
			if(character == null || StringUtils.isEmpty(character.getValue())){
				response = newAskResponse("Are you interested to know the position of which character?", "do you want to know the position of which character?");
			}else{
				JSONObject json = performRemoteGetCall(SERVICE_URL,URI_PATH+character.getValue(), null);
				if(json != null && json.has("message") 
				   && json.getString("message").equalsIgnoreCase("success") && json.has("data") && json.getJSONArray("data").length()>0){
					response = produceResponse(json.getJSONArray("data").getJSONObject(0));
				}else{
					LOGGER.error("A json object null or without message element is returned.");
					response = newTellResponse(COMMUNICATION_ERROR_MESSAGE);
				}
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
			if(json.has("path") && json.getJSONArray("path").length()>0){
				JSONArray pathArray = json.getJSONArray("path");
				JSONObject itemPath = pathArray.getJSONObject(pathArray.length()-1);
				JSONArray itemPathArray = itemPath.getJSONArray("path");
				String position = "an unknown place.";
				for(int i = 0; i < itemPathArray.length(); i++){
					JSONArray innerItemPathArray = itemPathArray.getJSONArray(i);
					for(int j = 0; j<innerItemPathArray.length(); j++){
						if(innerItemPathArray.get(j) instanceof String){
							position = innerItemPathArray.get(j).toString();
						}
					}
				}
				sb.append("The last recorded sighting of ").append(json.getString("name")).append(" occurred in ").append(position).append(".");
			}else{
				sb.append("There is no recorded sightings of ").append(json.getString("name"));
			}
			response = newTellResponse(sb.toString());
		}catch(JSONException e){
			LOGGER.error("Unable to find expected fields in the returned json object.", e);
			response = newTellResponse("Maybe this character is dead in the meanwhile, because I can find him nowhere");
		}
		return response;
	}

}
