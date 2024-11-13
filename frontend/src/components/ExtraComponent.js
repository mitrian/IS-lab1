import React, { useState } from 'react';
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css";
import '/node_modules/primeflex/primeflex.css';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import '../css/CoordinationComponent.css';
import { getToken } from './AuthenticationComponent';


const ExtraComponent = () => {
  const [infoMessage, setInfoMessage] = useState('');
  const [entityId, setEntityId] = useState('');
  const [comparebleRating, setComparebleRating] = useState('');
  const [organizationId, setOrganizationId] = useState('');
  const [staffId, setStaffId] = useState('');


  const sendAverageRatingRequest = async (endpoint, method = 'GET') => {
    try {
      const token = await getToken()
      const response = await fetch(`http://localhost:8080/api/v1/${endpoint}`, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });
  
      if (response.ok) {
        const data = await response.json(); // Получаем средний рейтинг
        const roundedRating = data.toFixed(3)
        setInfoMessage(`Average Rating: ${roundedRating}`);
      } else {
        const errorData = await response.json();
        setInfoMessage(`Error ${response.status}: ${errorData.message}`);
      }
    } catch (error) {
      setInfoMessage(`Fetch error: ${error.message}`);
    }
  };

  const sendGreaterRatingRequest = async (endpoint, method = 'GET', comparebleRating) => {
    const token = await getToken()
    try {
      const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${comparebleRating}`, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });

      if (response.ok) {
        const data = await response.json(); // Получаем средний рейтинг
        setInfoMessage(`Amount: ${data}`);
      } else {
        const errorData = await response.json(); 
        setInfoMessage(`Error ${response.status}: ${errorData.message}`);
      }
    } catch (error) {
      setInfoMessage(`Fetch error: ${error.message}`);
    }
  };

  const sendSearchFullnameRequest = async (endpoint, method = 'GET', comparebleRating) => {
    try {
      const token = await getToken()
      const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${comparebleRating}`, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });

      if (response.ok) {
        const data = await response.json();
        setInfoMessage(JSON.stringify(data, null, 2));;
      } else {
        const errorData = await response.json();
        setInfoMessage(`Error ${response.status}: ${errorData.message}`);
      }
    } catch (error) {
      setInfoMessage(`Fetch error: ${error.message}`);
    }
  };

  const sendDismissStaffOrganization = async (endpoint, method = 'PUT', organizationId) => {
    try {
      const token = await getToken()
      console.log('f')
      const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${organizationId}`, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });

      if (response.ok) {
        setInfoMessage(`Dismissed from organization wit id: ${organizationId}`);
      } else {
        const errorData = await response.json();
        setInfoMessage(`Error ${response.status}: ${errorData.message}`);
      }
    } catch (error) {
      setInfoMessage(`Fetch error: ${error.message}`);
    }
  };

  const sendAssignStaffToOrganization = async (endpoint, method = 'PUT', staffId, organizationId) => {
    try {
      const token = await getToken()
      const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${staffId}/${organizationId}`, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      });

      if (response.ok) {
        setInfoMessage(`Assign staff ${staffId} to organization wit id: ${organizationId}`);
      } else {
        const errorData = await response.json(); 
        setInfoMessage(`Error ${response.status}: ${errorData.message}`);
      }
    } catch (error) {
      setInfoMessage(`Fetch error: ${error.message}`);
    }
};



  
  return (
    <div className="coordination-container">
      <div className="info-panel">
        <Card>
          <p style={{ wordBreak: 'break-all' }} id="infoPanel">
            {infoMessage}
          </p>
        </Card>
      </div>

      <div className="coordination-button-panel">
        {/* Поля для ввода данных */}
        <div className="input-panel">
          <label htmlFor="entityId">Entity ID:</label>
          <InputText
            id="entityId"
            value={entityId}
            onChange={(e) => setEntityId(e.target.value)}
            placeholder="Введите ID сущности"
          />

          <label htmlFor="comparebleRating">Comparable Rating:</label>
          <InputText
            id="comparebleRating"
            value={comparebleRating}
            onChange={(e) => setComparebleRating(e.target.value)}
            placeholder="Введите рейтинг"
          />

          <label htmlFor="organizationId">Organization ID:</label>
          <InputText
            id="organizationId"
            value={organizationId}
            onChange={(e) => setOrganizationId(e.target.value)}
            placeholder="Введите ID организации"
          />

          <label htmlFor="staffId">Staff ID:</label>
          <InputText
            id="staffId"
            value={staffId}
            onChange={(e) => setStaffId(e.target.value)}
            placeholder="Введите ID сотрудника"
          />
        </div>

        {/* Кнопки для вызова функций */}
        <Button onClick={() => sendAverageRatingRequest('organization/average-rating', 'GET')} label='Average rating' />
        <Button onClick={() => sendGreaterRatingRequest('organization/count-by-greater-rating', 'GET', comparebleRating)} label="Rating greater then" />
        <Button onClick={() => sendSearchFullnameRequest('organization/search-by-fullname-prefix', 'GET', 'MAC')} label="Find by name" />
        <Button onClick={() => sendDismissStaffOrganization('staff/dismiss', 'PUT', organizationId)} label="Dismiss staff" />
        <Button onClick={() => sendAssignStaffToOrganization('staff/assign', 'PUT', staffId, organizationId)} label="Assign staff" />
      </div>
    </div>
  );
};

export default ExtraComponent;
