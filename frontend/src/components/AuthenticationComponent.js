import React, { useState } from 'react';
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css";
import '/node_modules/primeflex/primeflex.css';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import Keycloak from 'keycloak-js';
import {keyCloakInitOptions} from '../Conf';


let initOptions = keyCloakInitOptions

let kc = new Keycloak(initOptions);

kc.init({
    onLoad: initOptions.onLoad,
    KeycloakResponseType: 'code',
    silentCheckSsoRedirectUri: window.location.origin + "/silent-check-sso.html", checkLoginIframe: false,
    redirectUri: 'http://localhost:3000/',
    pkceMethod: 'S256'
    }).then((auth) => {
    if (!auth) {
        window.location.reload();
    } else {
        console.info("Authenticated");
        console.log('auth', auth)
        console.log('Keycloak', kc)
        kc.onTokenExpired = () => {
        console.log('token expired')
        }
    }
    }, () => {
    console.error("Authenticated Failed");
});

export const getToken = async () => {
    try {
      await kc.updateToken(30); // обновление токена за 30 секунд до истечения
      return kc.token;
    } catch (error) {
      console.error('Failed to refresh token, logging out:', error);
      kc.logout();
    }
  };
  


const AuthenticationComponent = () => {

  const [authMessage, setInfoMessage] = useState('');


//   const dispatch = useDispatch()
//   const cash = useSelector(state => state.cash)

//   const addCash = () => {
//     dispatch({type: "ADD_CASH", payload: 1})
//     console.log(cash)
//   }
  

  return (
        <div className="grid">
         
            <div className='col-8'>
                <Card>
                    <p style={{ wordBreak: 'break-all' }} id='authPanel'>
                    {authMessage}
                    </p>
                </Card>
            </div>
  
            <div className="col">
                <Button onClick={() => { setInfoMessage(kc.authenticated ? 'Authenticated: TRUE' : 'Authenticated: FALSE') }} className="m-1" label='Is Authenticated' />
                <Button onClick={() => { (kc.login())}} className='m-1' label='Login' severity="success" />
                {/* <Button onClick={() => { setInfoMessage(kc.token) }} className="m-1" label='Show Access Token' severity="info" /> */}
                {/* <Button onClick={() => { setInfoMessage(JSON.stringify(kc.tokenParsed)) }} className="m-1" label='Show Parsed Access token' severity="info" /> */}
                {/* <Button onClick={() => { setInfoMessage(kc.isTokenExpired(5).toString()) }} className="m-1" label='Check Token expired' severity="warning" /> */}
                {/* <Button onClick={() => { kc.updateToken(10).then((refreshed)=>{ setInfoMessage('Token Refreshed: ' + refreshed.toString()) }, (e)=>{setInfoMessage('Refresh Error')}) }} className="m-1" label='Update Token (if about to expire)' />   */}
                <Button onClick={() => { kc.logout({ redirectUri: 'http://localhost:3000/' }) }} className="m-1" label='Logout' severity="danger" />
                <Button onClick={() => console.log(kc.token)} />
                {/* <Button onClick={() => addCash()} label='REDUX' /> */}
            </div>
        </div>
  );
}

export default AuthenticationComponent;