# Client Credential Grant spring mvc test

Story: As an unauthenticated user, I want to retrieve statistical computations via an authenticated
REST API service.


## Installation
This project requires JDK 8 and maven for building and testing it. 

Install Java on linux using this command:

    sudo apt-get install openjdk-8-jdk
    
Install maven    
    
    sudo apt-get install maven
    
    
##Get the source code

From unix terminal:
    
    git clone https://github.com/antoniosignore/client_credentials.git    
    
## Build the WAR

    mvn clean package
    
The resulting test.war file in under :

    /target/test.war    

## Design

The test requires the implementation of the client credential grant defined in Oauth specifications 

The simplest of all of the OAuth 2.0 grants, this grant is suitable for machine-to-machine authentication where a specific 
user’s permission to access data is not required.


The exercise requested a simple proxy REST endpoint that would internally access by client_credential grant (Oauth2.0) to
a secure /api/v1.0/risk endpoint which would return the passed data with some statistics attached.

To keep in simple I have chosen the simplest off all statistics:

Users passes an integer i, the risk engine returns (i+1)    :-) 

I have defined 2 REST end points:

A secured one:

    POST /api/v1.0/risk

An open one:

    POST /proxy

The user flow is the following:

1. User calls :

    curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" http://localhost:8080/test/proxy --data '{"value":1}'

The code in the rest servlet associated to the mapping "/proxy" executes:

    a. a machine to machine rest call http://localhost:8080/test/oauth/token to retrive an Oauth2.0 token 
    b. Using the token in an Authorization: Bearer <token>, access the /proxy endpoing passing the ValueDTO
    3. The /risk engine computes the statistic (very simple in this example (i+1)) and return back to the client Rest caller

### unit test code coverage 

    ProxyRestController	100% (1/1)	100% (1/1)	85% (6/7)
    RiskRestController	100% (1/1)	100% (1/1)	85% (6/7)

# CURL test

In order to validate the solution you can run the following curls.

To be able to execute the CURLs you need to start the web application. There are several ways to do it:

a. Copy the target/test.war into your application server of choice (tomcat or jetty or whatever...)

b. if you import the project in IntellJ just click in the maven tomcat7 plugin the goal:

    tomcat7:run-war

### POST /api/v1.0/risk

    curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" http://localhost:8080/test/api/v1.0/risk
    
#### expected response

Expected result is an unauthorized response being the /api/v1.0 endpoint protected:     
    
    {
    	"error": "unauthorized",
    	"error_description": "An Authentication object was not found in the SecurityContext"
    }

### POST test/oauth/token?grant_type=client_credentials

    curl -X POST -H "Accept: application/json" -H "Authorization: Basic Y29kaW5nX3Rlc3Q6YndabTVYQzZIVGxyM2ZjZHpSbkQ="  http://localhost:8080/test/oauth/token?grant_type=client_credentials
    

The Authorization header contains the base64 encoding of the client/secret pair (coding_test:bwZm5XC6HTlr3fcdzRnD)
    
    String plainClientCredentials = "coding_test:bwZm5XC6HTlr3fcdzRnD";
    String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
    
   
#### expected response

    {
    	"access_token": "1e674a1c-7a70-4d93-b815-a2e1aa3b028a",   <----- token to be added as bearer to the /api/risk call
    	"token_type": "bearer",                                                                                         
    	"expires_in": 81,                                                                                               
    	"scope": "read write trust some_scope"                                                                          
    }                                                                                                                  
    
### GET /api/v1.0/risk   (bearer)

    curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -H "Authorization: Bearer 1e674a1c-7a70-4d93-b815-a2e1aa3b028a" http://localhost:8080/test/api/v1.0/risk --data '{"value":1}' 


#### expected response
 
This time we expect the input value and the calculated statistics (given 1, we expect 1+1 =2) 
    
    {"value":1,"stat":2}
    
    
### GET /test   (bearer)

Finally this is the actual final test with the user calling the /proxy endpoint


#### expected response

    curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" http://localhost:8080/test/proxy --data '{"value":1}' 
    
    {"value":1,"stat":2}
    
    
# DISCLAIMER

Being just a test it is by no means code considered of production quality. 

There are some harcoded values (localhost:port address, no logs), no fancy UI, no fancy risk statistics. 

The goal was just to try to demostrate the use of the client_credential grant as required in the test.
        
        