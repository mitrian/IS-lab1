import React, { useState } from 'react';
import './App.css';
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css";
import '/node_modules/primeflex/primeflex.css';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { TabView, TabPanel } from 'primereact/tabview';
import AuthenticationComponent from "./components/AuthenticationComponent";
import { useDispatch, useSelector } from 'react-redux';
import { getToken } from './components/AuthenticationComponent';
import CoordinationComponent from './components/CoordinationComponent.js';
import LocationComponent from './components/LocationComponent.js';
import AddressComponent from './components/AddressComponent.js';
import OrganizationComponent from './components/OrganizationComponent.js';
import StaffComponent from './components/StaffComponent.js';
import ExtraComponent from './components/ExtraComponent.js';


const App = () => {
  
return (
  <div >
   
    <TabView>
      <TabPanel header="Auth">
        <AuthenticationComponent /> 
      </TabPanel>
      {/* Вкладка Organization */}
      <TabPanel header="Organization">
        <OrganizationComponent />
      </TabPanel>

      {/* Вкладка Address */}
      <TabPanel header="Address">
        <AddressComponent />
      </TabPanel>

      {/* Вкладка Location */}
      <TabPanel header="Location">
        <LocationComponent />
      </TabPanel>

      {/* Вкладка Coordinates */}
      <TabPanel header="Coordinates">
        <CoordinationComponent />
      </TabPanel>

          
      

      {/* Вкладка Staff */}
      <TabPanel header="Staff">
        <StaffComponent />
      </TabPanel>

      {/* Вкладка Extra */}
      <TabPanel header="Extra">
        {/* <h1>Extra Operations</h1>
        <Button onClick={() => sendAverageRatingRequest('organization/average-rating', 'GET')} label="Average Rating" />
        <Button onClick={() => sendGreaterRatingRequest('organization/count-by-greater-rating', 'GET', 300)} label="Count greater rating" />
        <Button onClick={() => sendSearchFullnameRequest('organization/search-by-fullname-prefix', 'GET', 'MAC')} label="Search fullname" />
        <Button onClick={() => sendDismissStaffOrganization('staff/dismiss', 'PUT', 15)} label="Dismiss staff" />
        <Button onClick={() => sendAssignStaffToOrganization('staff/assign', 'PUT', 1, 15)} label="Assign staff" /> */}
        <ExtraComponent />
      </TabPanel>
    </TabView>

  </div>
);


}


export default App;
