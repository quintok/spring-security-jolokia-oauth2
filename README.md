Spring Security wrapping Jolokia Servlet
===

This is an example application using spring security to expose jolokia as an OAuth2 Resource Server resource.

OAuth2 is provided by AzureAD

This example does not use spring-boot

Setup
==
1. Register a new app registration in AzureAD
   1. Accounts in this organization directory only
2. In app registration under Expose an API:
   1. Set the application ID URI (default is fine).  Referred to as $APPLICATION_ID_URI from now
   2. Add a Scope
      1. Scope name is `jolokia_access`
      2. Admin consent display name: `Jolokia Access`
      3. Admin consent description: `Jolokia Access`
   3. Add Authorized client application for az cli:
      1. Add `04b07795-8ddb-461a-bbee-02f9e1bf7b46` (AZ Cli client application)
      2. With the scope `$APPLICATION_ID_URI/jolokia_access`
3. In app registration under 'App roles':
   1. Create an app role called:
      1. Display Name: `Jolokia Users`
      2. Allow member types: `Users/Groups`
      3. Value: `JolokiaUsers`
      4. Description: `Allow Jolokia Access`
4. Wait a minute or two for AzureAD to catch up.  This took me about a minute, your mileage may vary.
5. In the enterprise application that is under the same name
   1. In `Users and Groups`:
      1. Add user/group:
         1. Select the user that is you, in my case `Nick Cronin`
         2. Role should be `Jolokia Users` and you should be unable to assign it
            1. If it is not `Jolokia Users` you may have tried this too fast, wait for the sync
6. In Azure CLI:
   1. `az login` again to update your permissions
   2. `az account get-access-token --resource $APPLICATION_ID_URI`
   3. Take the bearer token (`accessToken` in the payload) and run a request like this:
      ```
      GET http://localhost:8080/demo_war/jolokia/list
      Authorization: Bearer $BEARER_TOKEN
      ```