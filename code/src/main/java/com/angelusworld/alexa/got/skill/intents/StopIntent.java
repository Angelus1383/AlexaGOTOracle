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
 * Handles the {@link StopIntent} providing an exiting message.
 * @author Valerio Capozio
 * @author Andrea Capozio
 */
public class StopIntent extends AbstractGOTIntent {

	@Override
	public SpeechletResponse handleIntent(Intent intent, Session session) throws GOTException {
		LOGGER.info("StopIntent invoked.");
		return newTellResponse("Dothras check");//Be cool and ride well(Goodby) http://leganerd.com/2014/05/29/got-dothraki-phrasebook/
	}

}
