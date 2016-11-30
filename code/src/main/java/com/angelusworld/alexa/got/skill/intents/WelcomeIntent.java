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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.angelusworld.alexa.got.skill.exception.GOTException;

/**
 * Handles the {@link WelcomeIntent} providing a welcome/introductory message.
 * @author Valerio Capozio
 * @author Andrea Capozio
 *
 */
public class WelcomeIntent extends AbstractGOTIntent{
	/**
	 * Default logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(WelcomeIntent.class);

	@Override
	public SpeechletResponse handleIntent(final Intent intent, final Session session) throws GOTException {
		LOGGER.info("WelcomeIntent invoked.");
		String speechOutput = "Welcome to G.O.T. Fan. "
				   + "I can provide you information about characters and houses of Known World. "
				   + "In addition I can give you information on episodes and better quotes of several characters. ";
		String repromptText =
         "I can provide you information about characters and houses of the Known World "
         + "Here kind of questions you can do: "
         + "Get information about Jon Snow. "
         + "Get information on Lannister. "
         + "Get information on the episode 2 in the season 1. "
         + "For a list of all kind of questions that you can do to me, ask me help. ";

 		return newAskResponse(speechOutput, repromptText);
	}

}
