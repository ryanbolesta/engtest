# Jr. Software Engineering API Integration Test

## Goal

Provide a simple web page that lists pages of active users in table form.

### Assumptions
* Technology choice is whatever platform you're comfortable in
* No styling necessary
* Users per page 5
* Dynamic paging controls (i.e. if no previous page is available, no previous link is presented)
* Clicking username presents a full view of the user's details
** Details must be retrieved from API in a separate call getting the individual user record

## API Integration Details

Authentication is achieved through an oauth2 bearer token, obtained through a client_credentials grant:

```
curl -s --insecure -u CLIENT_ID:CLIENT_SECRET https://auth.qa.fitpay.ninja/oauth/token?grant_type=client_credentials
```

Obtaining a collection of users:

```
curl -s --insecure -H "Authorization: Bearer TOKEN" https://api.qa.fitpay.ninja/users?limit=10
```
