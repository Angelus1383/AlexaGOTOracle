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

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.angelusworld.alexa.got.skill.exception.GOTException;

/**
 * Handles the {@link HelpIntent} providing message of explanation about the skill behaviors.
 * @author Valerio Capozio
 * @author Andrea Capozio
 */
public class HelpIntent extends AbstractGOTIntent {

	@Override
	public SpeechletResponse handleIntent(Intent intent, Session session) throws GOTException {
		LOGGER.info("HelpIntent invoked.");
		String speechOutput =
         "I can provide you information about characters, houses or places of the Known World. "
         + "Here kind of questions you can do: "
         + "Get information about Jon Snow. "
         + "What says today Jon Snow. "
         + "Get information on Lannister. "
         + "Which is the last known position of Daenerys Targaryen. "
         + "Get information on the episode 2 in the season 1. "
         + "What's happened in the last episode. "
         + "Finally you can ask the list of supported characters and the list of supported houses. ";
 		return newTellResponse(speechOutput, "G.O.T. Fan Help");
	}

}
