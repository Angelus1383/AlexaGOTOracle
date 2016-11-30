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
 * Handles the {@link CharacterInformationIntent} providing information about the given character.
 * @author Valerio Capozio
 * @author Andrea Capozio
 *
 */
public class CharacterInformationIntent extends AbstractGOTIntent {
	/**
	 * Service URL
	 */
	private static final String SERVICE_URL = "https://api.got.show";
	/**
	 * URI Path
	 */
	private static final String URI_PATH = "/api/characters/";
	/**
	 * Name of the slot to load.
	 */
	private static final String CHARACTER = "character";

	@Override
	public SpeechletResponse handleIntent(Intent intent, Session session){
		SpeechletResponse response;
		try {
			Slot character = intent.getSlot(CHARACTER);
			LOGGER.info("CharacterInformationIntent invoked passing character:{} ",character.getValue());
			if(character == null || StringUtils.isEmpty(character.getValue())){
				response = newAskResponse("Are you interested to know more information on which character?", "On which character would you like have information?");
			}else{
				JSONObject json = performRemoteGetCall(SERVICE_URL,URI_PATH+character.getValue(),null);
				if(json != null && json.has("message") 
				   && json.getString("message").equalsIgnoreCase("success") && json.has("data")){
					response = produceResponse(json.getJSONObject("data"));
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
			sb.append(json.getString("name")).append(" is a ");
			if(json.has("male")){
				sb.append(json.getBoolean("male")? "male":"female");
			}
			sb.append(" character");
			if(json.has("house")){
				String house = json.getString("house");
				if(house.toLowerCase().startsWith("house") || house.toLowerCase().endsWith("house")){
					sb.append(" belonging to the ").append(house);
				}else{
					sb.append(" belonging to the house of ").append(house);
				}
			}
			if(json.has("spouse")){
				sb.append(", married with ").append(json.getString("spouse"));
			}
			sb.append(". ");
			if(json.has("titles")){
				JSONArray titles = json.getJSONArray("titles");
				if(titles.length()>0){
					sb.append(json.getString("name")).append(" is known also as: ");
					for(int i = 0; i < titles.length(); i++){
						sb.append(titles.getString(i));
						if(i == titles.length()-1){
							sb.append(". ");
						}else{
							sb.append(", ");
						}
					}
				}
			}
			if(!json.has("dateOfDeath")){
				sb.append("At the moment ").append(json.getString("name")).append(" is still alive.");
			}else{
				sb.append(json.getString("name")).append("is dead.");
			}
			if(json.has("imageLink") && !json.isNull("imageLink")){
				response = newTellResponseWithImage(sb.toString(), json.getString("name"), SERVICE_URL+json.getString("imageLink"));
			}else{
				response = newTellResponse(sb.toString());
			}
		}catch(JSONException e){
			LOGGER.error("Unable to find expected fields in the returned json object.", e);
			response = newTellResponse("I cannot found information on this character. Maybe J.J. Martin has already killed this character. Anyway to know the list of supported characters you can say: what characters are available.");
		}
		return response;
	}
	
	

}
