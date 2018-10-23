# Software Engineering API Integration Test

## Goal

In order to increase the efficiency of a client using the FitPay API, we need a new composite
API resource created that will decrease the number of network calls being made.

### Technology Assumptions
* Technology choice is whatever platform you're comfortable in
  * If you chose a java based solution, a very basic spring-boot application is included.
* API credentials and/or bearer tokens can be hard coded with values obtained out-of-band
* You're familiar with [curl](https://curl.haxx.se) for testing Restful APIs
* Your solution can ideally be launched with a single command, if not instructions MUST be provided so we can test your new API
  * Example `mvn spring-boot:run`

### Overview

API clients typically need to make three separate network calls to obtain all the information necessary for a robust end consumer experience:

```
GET https://api.qa.fitpay.ninja/users/:userId
GET https://api.qa.fitpay.ninja/users/:userId/devices
GET https://api.qa.fitpay.ninja/users/:userId/creditCards
```

We need a new API resource that composes these 3 separate calls into a single JSON structure so our API clients don't need to make 3 network calls anymore:

```
GET http://localhost:8080/compositeUsers/:userId
```

* Given the `userId` as part of the URL path, retrieve the underlying API resources from the FitPay API
* The `Content-Type` returned MUST be `application/json`
* The JSON returned MUST at least include the following elements
  * `userId`
  * `creditCardId`
  * `state` for both devices and creditCards
  * `deviceId`
* The `devices` and `creditCards` MUST support lists of 0-n values
* The API SHOULD support two optional filters
  * `creditCardState` - Filter credit cards by `state` if query parameter specified
  * `deviceState` - Filter device by `state` if query parameter is specified

Example Requests:
```
GET http://localhost:8080/compositeUsers/:userId?creditCardState=ACTIVE
GET http://localhost:8080/compositeUsers/:userId?deviceState=INITIALIZED
GET http://localhost:8080/compositeUsers/:userId?creditCardState=ERROR&deviceState=FAILED_INITILIZATION
```

## API Integration Details

### Notes
* The endpoint will present self-signed SSL certificates, you'll need to either disable SSL validation all together or download and import the certificate into your trusted store.
* You'll be using our development/testing environment, which at times can be unstable.  You can validate the health of the environment [here](https://api.qa.fitpay.ninja/health).
* Review the JSON returned by the API to see if there are hints and useful things that can assist in making further API invocations

### API Authentication

Authentication is achieved through an oauth2 bearer token, obtained through a client_credentials grant utilizing the credentials issued to you from FitPay:

```
curl -s --insecure -u CLIENT_ID:CLIENT_SECRET https://auth.qa.fitpay.ninja/oauth/token?grant_type=client_credentials
```

### Getting Users

Using the `access_token` retrieved above, obtaining a collection of users:

```
curl -s --insecure -H "Authorization: Bearer TOKEN" https://api.qa.fitpay.ninja/users?limit=10
```

### Getting an Individual User

Using the `access_token` retrieved above, obtaining an individual user:

```
curl -s --insecure -H "Authorization: Bearer TOKEN" https://api.qa.fitpay.ninja/users/:userId
```

### Getting an Individual User's Devices

Using the `access_token` retrieved above, obtaining a collection of a user's devices:

```
curl -s --insecure -H "Authorization: Bearer TOKEN" https://api.qa.fitpay.ninja/users/:userId/devices
```

### Getting an Individual User's Credit Cards

Using the `access_token` retrieved above, obtaining a collection of a users's credit cards:

```
curl -s --insecure -H "Authorization: Bearer TOKEN" https://api.qa.fitpay.ninja/users/:userId/creditCards
```

More details API documentation can be found [here](https://anypoint.mulesoft.com/apiplatform/fitpay/#/portals/organizations/fd8d2eae-7955-4ec9-b009-b03635fe994b/apis/24399/versions/25936).
