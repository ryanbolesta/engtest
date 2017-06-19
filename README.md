# Software Engineering API Integration Test

## Goal

Provide a simple web page that allows a user to page through a collection of active user records.

### Assumptions
* Technology choice is whatever platform you're comfortable in, but try to incoorporate Java into the solution.
* No CSS styling necessary, seriously!
* Users per page 5
* Select 3-4 fields from the user's details to display per page
* Dynamic paging controls (i.e. if no previous page is available, no previous link is presented)
* Scalable in terms of not holding the entire user collection in memory, let the API do the work for you.
* API credentials and/or bearer tokens can be hard coded with values obtained out-of-band
* You're familiar with [curl](https://curl.haxx.se) for testing Restful APIs

**Hint**: How much work can the API do for you?

## API Integration Details

### Notes
* The endpoints below will present self-signed SSL certificates, you'll need to either disable SSL validation all together or download and import the certificate into your trusted store.
* You'll be using our development/testing environment, which at times can be unstable.  You can validate the health of the environment [here](https://api.qa.fitpay.ninja/health).

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

More details API documentation can be found [here](https://anypoint.mulesoft.com/apiplatform/fitpay/#/portals/organizations/fd8d2eae-7955-4ec9-b009-b03635fe994b/apis/24399/versions/25936).
