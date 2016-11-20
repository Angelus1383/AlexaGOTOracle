<p align="center">
  <img src="https://github.com/Angelus1383/AlexaGOTOracle/blob/master/Logo-L.png" width="40%" alt="G.O.T. Oracle Alexa Skill"/> 
</p>

# AlexaGOTOracle
This skill has been submitted to "[The Amazon Alexa API mashup contest](https://www.hackster.io/contests/alexa-api-contest)" hosted by Hackster.io and sponsored by Amazon.

## TEAM/CONTRIBUTORS
* Valerio Capozio
* Andrea Capozio

## SKILL Status on Amazon Store
submitted to Amazon, currently **under acceptance**.

## THINGS
* [Amazon Alexa Alexa Skills Kit](https://developer.amazon.com/alexa-skills-kit)
* [Amazon AWS Lambda Function](http://docs.aws.amazon.com/lambda/latest/dg/welcome.html)
* [Game of Thrones API](https://api.got.show/doc/#api-CharacterLocations-GetByName) 
* [TV MAZE API](http://www.tvmaze.com/api)
* [Game of Thrones Quotes API](https://github.com/wsizoo/game-of-thrones-quotes)

## STORY

### The idea
We are two great fans of the TV show Game of Thrones and often we speak about it, discussing on episodes and characters behavior. So the idea to develop an Alexa Skill, able to search and return information about houses, characters and episodes, has seemed immediately awesome for us. 

To realize this idea we've developed and released an Alexa Skill called **G.O.T. Oracle**. 

In agreement with the mashup requirement of the contest we developed G.O.T. Oracle using different kind of open APIs with the aim to expose a set of heterogeneous intents.

### Skill behaviors
G.O.T. Oracle is able to respond to different kind of questions, invoking different remote services exposed as RESTful API. All the used APIs does not require authentication and are free to use.

G.O.T. Oracle can provide deep information on the required character or house. In addition user can ask to know which is the last known position of a character and to achieve a random quote of the specified character. With respect to the episodes the skill is able to return title and synopsis of each past episode and to return the title and the date of the new episode.

G.O.T. Oracle has several intents to help users to invoke the skill correctly. 
For each remote call to the APIs exists a fallback to manage communication problems or unexpected data format with a funny message.

In addition to the voice output - for some of the exposed intents - G.O.T. Oracle returns also a **card** with formatted data and multimedia contents (text+image).

### Interaction Model
Following the specification provided into the **Custom Skills** section, on the Amazon Developer Portal, we designed the Voice Interface of G.O.T. Oracle. The interaction model of the skill is made by different items:
1. Intent schema.
2. Custom slot types.
3. Sample utterances.

These three items enable Alexa to detect the intent required by the users and provide them the right response. The interaction model has been designed, developed and then configured into the developer portal as shown in the figure below.
![InteractionModel.png](https://github.com/Angelus1383/AlexaGOTOracle/blob/master/schemas/InteractionModel.png)

### Service Logic
All the business logic of the skill is in an **AWS Lambda Function** developed in JAVA™. The architecture of the JAVA artifact is pretty simple, thanks to the interfaces provided by Amazon. Introducing some design patterns, each intent has been arranged to contain all the code necessary to accomplish its aim in respect to the principle of separation of concerns.
The structure of the whole resulting Lambda Function is shown in the following schema.
![AlexaGOTOracleClassDiagram.png](https://github.com/Angelus1383/AlexaGOTOracle/blob/master/schemas/AlexaGOTOracleClassDiagram.png)

#### Example phrases
These are some kind of phrases useful to invoke the intents exposed by G.O.T. Oracle.

* "Alexa, ask G.O.T. ORACLE what characters are available"
* "Alexa, ask G.O.T. ORACLE what houses are available"
* "Alexa, ask G.O.T. ORACLE to get information about Jon Snow"
* "Alexa, ask G.O.T. ORACLE to get a quote of Tyrion Lannister"
* "Alexa, ask G.O.T. ORACLE where is Daenerys Targaryen"
* "Alexa, ask G.O.T. ORACLE to get information about House Martell"
* "Alexa, ask G.O.T. ORACLE to get information on the episode one in the season one"
* "Alexa, ask G.O.T. ORACLE what's happened in the last episode"
* "Alexa, ask G.O.T. ORACLE when will the next episode be released"

## SCHEMATICS

### Service Architecture
The architecture of the G.O.T. Oracle skill is depicted in the following schema.
![AlexaGOTOracle.png](https://github.com/Angelus1383/AlexaGOTOracle/blob/master/schemas/AlexaGOTOracle.png)

In the schema, users can activate the skill using an Amazon Echo/Tap/Dot device, or using other devices combined with the Amazon Voice Service API. The skill analyzes the voice request extracting intent, with slots if any and passes all to the Lambda Function that invokes the right API service, extracts data and prepares the response to return to Alexa in order to provide voice response to the user.

### Voice User Interface Diagram
The VUI diagram of the skill is shown in figure, highlighting correct(dark green) and error(dark red) paths in the call to remote services. The flow shows also, for each kind of intent, which is the API called and examples of textual responses produced and sent to Alexa.
![Alexa GOT ORACLE - VUI.png](https://github.com/Angelus1383/AlexaGOTOracle/blob/master/schemas/Alexa%20GOT%20ORACLE%20-%20VUI.png)

## CATEGORIES
* TV show, entertainment, Game of Thrones. 


