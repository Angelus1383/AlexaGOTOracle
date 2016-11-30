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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Image;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.StandardCard;
import com.angelusworld.alexa.got.skill.exception.GOTException;

/**
 * Defines common behaviors of the {@link GOTIntent} objects.
 * @author Valerio Capozio
 * @author Andrea Capozio
 *
 */
public abstract class AbstractGOTIntent implements GOTIntent{
	/**
	 * Default message to handle communication error with backend API.
	 */
	protected final static String COMMUNICATION_ERROR_MESSAGE = "Ops this is embarrassing, but i received no response from Varys little birds.";
	
	protected final static Logger LOGGER = LoggerFactory.getLogger(AbstractGOTIntent.class); 
	
	/**
	 * Performs a get request on the specified URL and returns the achieved {@link JSONObject}.
	 * @param url to invoke.
	 * @param path to pass with the url.
	 * @return the {@link JSONObject} returned by the invoked url or null in case of error.
	 * @throws GOTException if something goes wrong.
	 */
	protected JSONObject performRemoteGetCall(String url,String path, String query) throws GOTException {
		try{
			HttpClient httpClient = httpClientTrustingAllSSLCerts();
			HttpGet httpGetRequest;
			if(query != null){
				httpGetRequest = new HttpGet(new URIBuilder(url).setPath(path).setCustomQuery(query).build());
			}else if(path != null){
				httpGetRequest = new HttpGet(new URIBuilder(url).setPath(path).build());
			}else{
				httpGetRequest = new HttpGet(url);
			}
			HttpResponse httpResponse = httpClient.execute(httpGetRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					StringBuilder sb = new StringBuilder();
					try (BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()))) {
						String line = "";
						while ((line = rd.readLine()) != null) {
							sb.append(line);
						}
						return new JSONObject(sb.toString());
					}
				}else{
					throw new GOTException("An empty entity is returned by the remote service.");
				}
			} else {
				throw new GOTException("A "+httpResponse.getStatusLine().getStatusCode()+"HTTP status is returned by the remote service.");
			}
		}catch(Exception e){
			throw new GOTException(e.getMessage(), e);
		}
	}
	
	/**
	 * Performs a get request on the specified URL and returns the achieved {@link JSONObject}.
	 * @param url to invoke.
	 * @return the {@link JSONObject} returned by the invoked url or null in case of error.
	 * @throws GOTException if something goes wrong.
	 */
	protected JSONArray performRemoteGetCall(String url) throws GOTException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGetRequest = new HttpGet(url);
		try{
			HttpResponse httpResponse = httpClient.execute(httpGetRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					StringBuilder sb = new StringBuilder();
					try (BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()))) {
						String line = "";
						while ((line = rd.readLine()) != null) {
							sb.append(line);
						}
						return new JSONArray(sb.toString());
					}
				}else{
					throw new GOTException("An empty entity is returned by the remote service.");
				}
			} else {
				throw new GOTException("A HTTP status different from 200 is returned by the remote service.");
			}
		}catch(Exception e){
			throw new GOTException(e.getMessage(), e);
		}
	}

    /**
     * Wrapper for creating the Ask response from the input strings with
     * plain text output and reprompt speeches.
     *
     * @param stringOutput
     *            the output to be spoken
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @return SpeechletResponse the speechlet response
     */
    protected SpeechletResponse newAskResponse(String stringOutput, String repromptText) {
        return newAskResponse(stringOutput, false, repromptText, false);
    }

    /**
     * Wrapper for creating the Ask response from the input strings with
     * plain text output, reprompt speeches and a card.
     *
     * @param stringOutput
     *            the output to be spoken
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @param card
     * 			  the cardTitle to set in the card of the companion app.
     * @return SpeechletResponse the speechlet response
     */
    protected SpeechletResponse newAskResponse(String stringOutput, String repromptText, String cardTitle) {
    	return newAskResponse(stringOutput, repromptText, cardTitle, null);
    }

    /**
     * Wrapper for creating the Ask response from the input strings with
     * plain text output, reprompt speeches and a card.
     *
     * @param stringOutput
     *            the output to be spoken
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @param cardTitle
     * 			  the cardTitle to set in the card of the companion app.
     * @param cardText
     * 			  the cardText to set in the card of the companion app.
     * @return SpeechletResponse the speechlet response
     */
    protected SpeechletResponse newAskResponse(String stringOutput, String repromptText, String cardTitle, String cardText) {
    	OutputSpeech outputSpeech, repromptOutputSpeech;
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
            repromptOutputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
        SimpleCard card = new SimpleCard();
        card.setTitle(cardTitle);
        if(cardText==null){
        	card.setContent(stringOutput);
        }else{
        	card.setContent(cardText);
        }
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt, card);
    }

    /**
     * Wrapper for creating the Ask response from the input strings.
     *
     * @param stringOutput
     *            the output to be spoken
     * @param isOutputSsml
     *            whether the output text is of type SSML
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @param isRepromptSsml
     *            whether the reprompt text is of type SSML
     * @return SpeechletResponse the speechlet response
     */
    protected SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
            String repromptText, boolean isRepromptSsml) {
        OutputSpeech outputSpeech, repromptOutputSpeech;
        if (isOutputSsml) {
            outputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
        } else {
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
        }

        if (isRepromptSsml) {
            repromptOutputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) repromptOutputSpeech).setSsml(stringOutput);
        } else {
            repromptOutputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
        }

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }
    
    /**
     * Wrapper for creating the Tell response from the input strings with
     * plain text output.
     * 
     * @param stringOutput the output to be spoken.
     * @return SpeechletResponse the speechlet response.
     */
    protected SpeechletResponse newTellResponse(String stringOutput){
    	return newTellResponse(stringOutput, null);
    }
    

    /**
     * Wrapper for creating the Tell response from the input strings with
     * plain text output and a card with the given cardTitle.
     * 
     * @param stringOutput the output to be spoken.
     * @param cardTitle the title of the card to send to the companion app.
     * @return SpeechletResponse the speechlet response.
     */
    protected SpeechletResponse newTellResponse(String stringOutput, String cardTitle){
    	return newTellResponse(stringOutput, cardTitle, null);
    }

    /**
     * Wrapper for creating the Tell response from the input strings with
     * plain text output and a card with the given cardTitle.
     * 
     * @param stringOutput the output to be spoken.
     * @param cardTitle the title of the card to send to the companion app.
     * @param cardImage the image to show into the card.
     * @return SpeechletResponse the speechlet response.
     */
    protected SpeechletResponse newTellResponseWithImage(String stringOutput, String cardTitle, String cardImage){
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(stringOutput);
        SpeechletResponse response;
    	if(StringUtils.isEmpty(cardTitle)){
    		response = SpeechletResponse.newTellResponse(outputSpeech);
    	}else if(StringUtils.isEmpty(cardImage)){
    		SimpleCard card = new SimpleCard();
    		card.setTitle(cardTitle);
    		card.setContent(stringOutput);
    		response = SpeechletResponse.newTellResponse(outputSpeech, card);
    	}else{
    		// Create the Simple card content.
            StandardCard card = new StandardCard();
            card.setTitle(cardTitle);
            Image img = new Image();
            img.setSmallImageUrl(cardImage);
            card.setImage(img);
            card.setText(stringOutput);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
    	}

		return response;
    }

    /**
     * Wrapper for creating the Tell response from the input strings with
     * plain text output and a card with the given cardTitle.
     * 
     * @param stringOutput the output to be spoken.
     * @param cardTitle the title of the card to send to the companion app.
     * @param cardText the text to show into the card.
     * @return SpeechletResponse the speechlet response.
     */
    protected SpeechletResponse newTellResponse(String stringOutput, String cardTitle, String cardText){
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(stringOutput);
        SpeechletResponse response;
    	if(StringUtils.isEmpty(cardTitle)){
    		response = SpeechletResponse.newTellResponse(outputSpeech);
    	}else if(StringUtils.isEmpty(cardText)){
    		SimpleCard card = new SimpleCard();
    		card.setTitle(cardTitle);
    		card.setContent(stringOutput);
    		response = SpeechletResponse.newTellResponse(outputSpeech, card);
    	}else{
    		SimpleCard card = new SimpleCard();
            card.setTitle(cardTitle);
            card.setContent(cardText);
            response = SpeechletResponse.newTellResponse(outputSpeech, card);
    	}

		return response;
    }

	@SuppressWarnings("deprecation")
	private HttpClient httpClientTrustingAllSSLCerts() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, getTrustingManager(), new java.security.SecureRandom());
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sc,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(socketFactory).build();
        return httpClient;
    }

    private TrustManager[] getTrustingManager() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // Do nothing
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // Do nothing
            }

        } };
        return trustAllCerts;
    }
	
}
