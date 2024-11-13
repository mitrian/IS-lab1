import React, { useState, useEffect } from 'react';
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css";
import '/node_modules/primeflex/primeflex.css';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import '../css/CoordinationComponent.css';
import { getEntities, getEntityById, createEntity, deleteEntityById, updateEntityById } from '../Api';


let createOrganizationBody = {
    "name": "Haunted",
    "coordinatesId": 6,
    "creationDate": "2023-04-15T14:30:00",
    "officialAddressId": 2,
    "annualTurnover": 77,
    "employeesCount": 88,
    "rating": 99,
    "fullName": "MACAN",
    "type": "PUBLIC",
    "postalAddressId": 6
  }

const requestsWithId = [deleteEntityById, getEntityById, updateEntityById];

const OrganizationComponent = () => {
  const [infoMessage, setInfoMessage] = useState('')
  const [entityId, setEntityId] = useState('')
  const [organization, setOrganization] = useState([])
  const [currentPage, setCurrentPage] = useState(0) 
  const [totalPages, setTotalPages] = useState(0)

  
  const fetchorganization = async (page) => {
    try {
      const data = await getEntities('organization', page)
      setOrganization(data.content || [])
      setTotalPages(data.totalPages || 0)
    } catch (error) {
      setInfoMessage(`${error.message}`)
    }
  };

  
  useEffect(() => {
    fetchorganization(currentPage)
  }, [currentPage])

  const handleRequest = async (requestFunction, ...args) => {
    if (requestsWithId.includes(requestFunction) && !entityId || isNaN(Number(entityId))) {
      setInfoMessage("Введите id");
      return;
    } else {
      try {
        const data = await requestFunction(...args)
        const message = data.content 
          ? JSON.stringify(data.content, null, 2) 
          : JSON.stringify(data, null, 2)
          setInfoMessage(message)

       
        if (requestFunction != getEntityById)
        fetchorganization(currentPage)
      } catch (error) {
        setInfoMessage(`${error.message}`)
      }
    }
  };

  return (
    <div className="coordination-container">

      <div className="coordination-button-panel">
        {/* Поле для ввода ID */}
        <div className="input-panel">
          <label htmlFor="entityId">Enter ID:</label>
          <InputText
            id="entityId"
            value={entityId}
            onChange={(e) => setEntityId(e.target.value)}
            placeholder="Enter the ID"
          />
        </div>

        {/* Кнопки для управления данными */}
        <Button onClick={() => handleRequest(getEntityById, 'organization', entityId)} label="By id Coordination" />
        <Button onClick={() => handleRequest(createEntity, 'organization', createOrganizationBody)} label="Create Coord" />
        <Button onClick={() => handleRequest(deleteEntityById, 'organization', entityId)} label="Delete Coord" />
        <Button onClick={() => handleRequest(updateEntityById, 'organization', entityId, createOrganizationBody)} label="Update Coord" />
      </div>

      {/* Таблица для отображения координат */}
      <div className="coordination-table-panel">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>cord</th>
              <th>cr_date</th>
              <th>off_id</th>
              <th>off_addr</th>
              <th>annualTurnover</th>
              <th>emplCount</th>
              <th>rating</th>
              <th>fullName</th>
              <th>type</th>
              <th>p_addr</th>
            </tr>
          </thead>
          <tbody>
            {organization.map((org) => (
              <tr key={org.id}>
                <td>{org.id}</td>
                <td>{org.name}</td>
                <td>({org.coordinates.x}, {org.coordinates.y})</td>
                <td>{org.creationDate.slice(0, 10)}</td>
                <td>{org.officialAddress.id} </td>
                <td>{org.officialAddress.zipCode}, ({org.officialAddress.location.x}, {org.officialAddress.location.x}, {org.officialAddress.location.z}), {org.officialAddress.location.name}</td>
                <td>{org.annualTurnover}</td>
                <td>{org.employeesCount}</td>
                <td>{org.rating}</td>
                <td>{org.fullName}</td>
                <td>{org.type}</td>
                <td>{org.postalAddress.zipCode}, ({org.postalAddress.location.x}, {org.postalAddress.location.x}, {org.postalAddress.location.z}), {org.postalAddress.location.name}</td>
              </tr>
            ))}
          </tbody>
        </table>

        {/* Кнопки для переключения страниц */}
        <div className="pagination-buttons">
          <Button
            label="Previous"
            icon="pi pi-chevron-left"
            onClick={() => setCurrentPage(currentPage - 1)}
            disabled={currentPage <= 0}
          />
          <Button
            label="Next"
            icon="pi pi-chevron-right"
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={currentPage >= totalPages - 1}
          />
        </div>
        <div className="info-panel">
        <Card>
          <p style={{ wordBreak: 'break-all' }} id="infoPanel">
            {infoMessage}
          </p>
        </Card>
      </div>
      </div>
    </div>
  );
};

export default OrganizationComponent;
