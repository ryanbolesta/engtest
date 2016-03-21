# Jr. Software Engineering API Integration Test

## Goal

Provide a simple web page that allows a user to page through a collection of active user records.

### Assumptions
* Technology choice is whatever platform you're comfortable in
* No styling necessary, seriously!
* Users per page 5
* Dynamic paging controls (i.e. if no previous page is available, no previous link is presented)
* Clicking username presents a full view of the user's details
  * Details must be retrieved from API in a separate call getting the individual user record

**Hint**: How much work can the API do for you?

## API Integration Details

Authentication is achieved through an oauth2 bearer token, obtained through a client_credentials grant:

```
curl -s --insecure -u CLIENT_ID:CLIENT_SECRET https://auth.qa.fitpay.ninja/oauth/token?grant_type=client_credentials
```

Obtaining a collection of users:

```
curl -s --insecure -H "Authorization: Bearer TOKEN" https://api.qa.fitpay.ninja/users?limit=10
```
