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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

/**
 * The handler of the AWS Lambda function.
 * 
 * @author Valerio Capozio
 * @author Andrea Capozio
 */
public class GOTSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

	/**
	 * Set of identifiers to perform security check on caller identity.
	 * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
     * Alexa Skill
	 */
	private static final Set<String> supportedApplicationIds;
	/**
	 * Constant to AlexaApplicationId environment property.
	 * Values into the property can be comma separated. 
	 */
	private static final String ALEXA_APP_ID = "ALEXA_APP_ID";
	
    static {
        supportedApplicationIds = new HashSet<String>();
        if(!StringUtils.isEmpty(System.getenv(ALEXA_APP_ID))){
        	if(System.getenv(ALEXA_APP_ID).contains(",")){
        		supportedApplicationIds.addAll(Arrays.asList(
        				StringUtils.split(System.getenv(ALEXA_APP_ID), ",")));
        	}else{
        		supportedApplicationIds.add(System.getenv(ALEXA_APP_ID));
        	}
        }
    }

	/**
	 * Constructs an {@link GOTSpeechletRequestStreamHandler} initializing it with default parameters. 
	 * 
	 */
    public GOTSpeechletRequestStreamHandler() {
        super(new GOTSpeechlet(), supportedApplicationIds);
    }

	/**
	 * Constructs an {@link GOTSpeechletRequestStreamHandler} starting from the given parameters. 
	 * 
     * @param speechlet
     *            the {@code Speechlet} that handles the requests
     * @param supportedApplicationIds
     *            a {@code Set} of supported {@code ApplicationId}s used to validate that the
     *            requests are intended for your service
	 */
	public GOTSpeechletRequestStreamHandler(Speechlet speechlet, Set<String> supportedApplicationIds) {
		super(speechlet, supportedApplicationIds);
	}

}
