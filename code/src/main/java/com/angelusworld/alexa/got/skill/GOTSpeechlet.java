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
package com.angelusworld.alexa.got.skill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.angelusworld.alexa.got.skill.exception.GOTException;
import com.angelusworld.alexa.got.skill.intents.CancelIntent;
import com.angelusworld.alexa.got.skill.intents.GOTIntent;
import com.angelusworld.alexa.got.skill.intents.HelpIntent;
import com.angelusworld.alexa.got.skill.intents.StopIntent;

/**
 * 
 * The G.O.T Oracle speechlet allows dialogue between Alexa and the backend API.
 * 
 * @author Valerio Capozio
 * @author Andrea Capozio
 *
 */
public class GOTSpeechlet implements Speechlet {
	
	/**
	 * Package prefix of the intent classes.
	 */
	private static final String PACKAGE_PREF = "com.angelusworld.alexa.got.skill.intents." ;
	
	/**
	 * Default logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(GOTSpeechlet.class);

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
    	LOGGER.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
	}

    @Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
    	LOGGER.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		try {
			return produceIntentHandler("WelcomeIntent").handleIntent(null, session);
		} catch (GOTException e) {
			throw new SpeechletException(e.getMessage(), e);
		}
	}

    @Override
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
    	LOGGER.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
    	Intent intent = request.getIntent();
        String intentName = intent.getName();
    	try {
			return produceIntentHandler(intentName).handleIntent(intent, session);
		} catch (GOTException e) {
			throw new SpeechletException(e.getMessage(), e);
		}
	}

    @Override
	public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
    	LOGGER.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
	}
    
    /**
     * Produces an instance of the intent achieved as input. If unable to find the right intent return an instance of {@link HelpIntent}.
     * @param intent name to instantiate the right class.
     * @return the instance of the intent with the given name.
     */
    private GOTIntent produceIntentHandler(String intent){
    	if("AMAZON.HelpIntent".equals(intent)){
    		return new HelpIntent();
    	}else if("AMAZON.StopIntent".equals(intent)){
    		return new StopIntent();
    	}else if("AMAZON.CancelIntent".equals(intent)){
    		return new CancelIntent();
    	}else{
			try {
				Class<? extends GOTIntent> c = Class.forName(PACKAGE_PREF+intent).asSubclass(GOTIntent.class);
		    	return c.newInstance();
			} catch (Exception e) {
				LOGGER.error("Unable to instantiate the class corresponding to the intent"+intent, e);
			}
    	}
    	return new HelpIntent();
    }
    


}
