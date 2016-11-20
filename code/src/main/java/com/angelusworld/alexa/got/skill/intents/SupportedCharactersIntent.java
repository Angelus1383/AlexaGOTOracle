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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Handles the {@link SupportedCharactersIntent} returning the list of the supported characters.
 * @author Valerio Capozio
 * @author Andrea Capozio
 */
public class SupportedCharactersIntent extends AbstractGOTIntent{

	@Override
	public SpeechletResponse handleIntent(Intent intent, Session session)  {
		SpeechletResponse response;
		try {
			LOGGER.info("SupportedCharactersIntent invoked.");
			try(BufferedReader br = new BufferedReader(new InputStreamReader(SupportedCharactersIntent.class.getClassLoader().getResourceAsStream("speechAssets/customSlotTypes/LIST_OF_CHARACTERS")))){
				StringBuilder sb = new StringBuilder();
				String line = null;
				while((line=br.readLine())!= null){
					sb.append(line).append("; ");
				}
				response = newAskResponse("The list of supported characters is very long. To the complete list, you can see the card on the Alexa app. The most required are: Daenerys Targaryen, Tyrion Lannister and Jon Snow. ", 
						"So, which character would you like information for?", 
						"List of Supported Characters", 
						sb.toString());
			}
		} catch (Exception e) {
			LOGGER.error("Unable to perform intent request.", e);
			response = newTellResponse(COMMUNICATION_ERROR_MESSAGE);
		}
		return response;
	}

}
