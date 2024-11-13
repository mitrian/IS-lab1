export const keyCloakInitOptions = {
    url: 'https://localhost:9443/',
    realm: 'devrealm',
    clientId: 'pub-dev',
    onLoad: 'check-sso', // check-sso | login-required
    KeycloakResponseType: 'code',
  
    // silentCheckSsoRedirectUri: (window.location.origin + "/silent-check-sso.html")
}


export const urlBack = "http://localhost:8080"
  