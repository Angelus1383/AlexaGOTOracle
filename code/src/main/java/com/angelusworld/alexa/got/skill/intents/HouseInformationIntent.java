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
import org.json.JSONException;
import org.json.JSONObject;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Handles the {@link HouseInformationIntent} providing information about the given house.
 * @author Valerio Capozio
 * @author Andrea Capozio
 *
 */
public class HouseInformationIntent extends AbstractGOTIntent {
	/**
	 * Service URL
	 */
	private static final String SERVICE_URL = "https://api.got.show";
	/**
	 * URI Path
	 */
	private static final String URI_PATH = "/api/houses/";
	/**
	 * Name of the slot to load.
	 */
	private static final String HOUSE = "house";

	@Override
	public SpeechletResponse handleIntent(Intent intent, Session session) {
		SpeechletResponse response;
		try {
			LOGGER.info("HouseInformationIntent invoked.");
			Slot house = intent.getSlot(HOUSE);
			String houseValue = house.getValue();
			if(!houseValue.toLowerCase().startsWith("house")){
				houseValue += "House ";
			}
			JSONObject json = performRemoteGetCall(SERVICE_URL,URI_PATH+StringUtils.capitalize(houseValue),null);
			if(json != null && json.has("message") 
			   && json.getString("message").equalsIgnoreCase("success") && json.has("data")){
				response = produceResponse(json.getJSONObject("data"));
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
	private SpeechletResponse produceResponse(JSONObject json){
		SpeechletResponse response;
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("The ").append(json.getString("name")).append(" is a house of Known World");
			if(json.has("region") && !json.isNull("region")){
				sb.append(" in the region of ").append(json.getString("region"));
			}
			sb.append(". ");
			if(json.has("coatOfArms") && !json.isNull("coatOfArms")){
				sb.append("Its coat of arms is ").append(json.getString("coatOfArms")).append(". ");
			}
			if(json.has("words") && !json.isNull("words")){
				sb.append("The official family words is ").append(json.getString("words")).append(". ");
			}
			if(json.has("currentLord") && !json.isNull("currentLord") 
					&& !json.getString("currentLord").equalsIgnoreCase("unknown")){
				sb.append("The current Lord of the ").append(json.getString("name")).append(" is ").append(json.getString("currentLord")).append(". ");
			}
			if(json.has("title") && !json.isNull("title")){
				sb.append("The Lord of the house is known as ").append(json.getString("title")).append(". ");
			}
			if(json.has("imageLink") && !json.isNull("imageLink")){
				response = newTellResponse(sb.toString(), json.getString("name"), SERVICE_URL+json.getString("imageLink"));
			}else{
				response = newTellResponse(sb.toString());
			}
		}catch(JSONException e){
			LOGGER.error("Unable to find expected fields in the returned json object.", e);
			response = newTellResponse("I cannot found information on this house. Maybe J.J. Martin has already destroyed it. Anyway to know the list of supported houses you can say: what houses are available.");
		}
		return response;
	}
	

}
