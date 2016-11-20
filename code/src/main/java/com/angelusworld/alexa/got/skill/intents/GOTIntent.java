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
 * Defines a common interface to handle each user intent using the strategy design pattern.
 * @author Valerio Capozio
 * @author Andrea Capozio
 *
 */
public interface GOTIntent {
	
	/**
	 * Handles the user intent performing the business logic of the corresponding task.
	 * @param intent detected from the user request.
	 * @param session of the user.
	 * @return corresponding {@link SpeechletResponse}.
	 * @throws GOTException if something goes wrong.
	 */
	SpeechletResponse handleIntent(final Intent intent, final Session session) throws GOTException;

}
