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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Handles the {@link CharacterQuoteIntent} providing a message containing 
 * a random quote of the specified character.
 * @author Valerio Capozio
 * @author Andrea Capozio
 */
public class CharacterQuoteIntent extends AbstractGOTIntent{
	/**
	 * Service URL.
	 */
	private static final String SERVICE_URL = "https://got-quotes.herokuapp.com";
	/**
	 * Service path URL.
	 */
	private static final String PATH_URL = "/quotes";
	/**
	 * Service queryString.
	 */
	private static final String QUERY = "char=";
	/**
	 * FIELD TO RETRIEVE.
	 */
	private static final String QUOTE = "quote";
	/**
	 * Name of the slot to load.
	 */
	private static final String CHARACTER = "character";
	/**
	 * Normalizes standard name with version used by this API.
	 */
	private static final Map<String, String> translator;
	
	static{
		translator = new HashMap<String,String>();
		translator.put("Tyrion Lannister", "Tyrion");
		translator.put("Cersei Lannister", "Cersei");
		translator.put("Bran Stark", "Bran");
		translator.put("Varys", "Varys");
		translator.put("Jon Snow", "Jon Snow");
		translator.put("Davos Seaworth", "Davos");
		translator.put("Sansa Stark", "Sansa");
		translator.put("Rodrik Stark", "Lord Rodrik");
		translator.put("Samwell Tarly", "Samwell");
		translator.put("Petyr Baelish", "Littlefinger");
		translator.put("Sandor Clegane", "Hound");
		translator.put("Daenerys Targaryen", "Daenerys");
	}

	@Override
	public SpeechletResponse handleIntent(Intent intent, Session session) {
		SpeechletResponse response;
		try {
			LOGGER.info("CharacterQuoteIntent invoked.");
			Slot character = intent.getSlot(CHARACTER);
			String characterName = character.getValue();
			if(translator.containsKey(characterName)){
				characterName = translator.get(character.getValue());
			}
			JSONObject json = performRemoteGetCall(SERVICE_URL, PATH_URL, QUERY+characterName);
			if(json != null && json.has(QUOTE)){
				response = newTellResponse(json.getString(QUOTE));
			}else{
				LOGGER.error("A json object null or without quote element is returned.");
				response = newTellResponse("Hodor!");
			}
		} catch (Exception e) {
			LOGGER.error("Unable to perform intent request.", e);
			response = newTellResponse(COMMUNICATION_ERROR_MESSAGE);
		}
		return response;
	}

}
