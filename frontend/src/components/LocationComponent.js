import React, { useState, useEffect } from 'react';
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css";
import '/node_modules/primeflex/primeflex.css';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import '../css/CoordinationComponent.css';
import { getEntities, getEntityById, createEntity, deleteEntityById, updateEntityById } from '../Api';


let createLocationBody = {
    "x": 7,
    "y": 7,
    "z": 7,
    "name": "MACAN"  
}

const requestsWithId = [deleteEntityById, getEntityById, updateEntityById];

const LocationComponent = () => {
  const [infoMessage, setInfoMessage] = useState('')
  const [entityId, setEntityId] = useState('')
  const [locations, setLocations] = useState([])
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0) 

  
  const fetchlocations = async (page) => {
    try {
      const data = await getEntities('locations', page)
      setLocations(data.content || [])
      setTotalPages(data.totalPages || 0)
    } catch (error) {
      setInfoMessage(`${error.message}`)
    }
  };

  
  useEffect(() => {
    fetchlocations(currentPage);
  }, [currentPage]); 

  const handleRequest = async (requestFunction, ...args) => {
    setInfoMessage("")
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
        fetchlocations(currentPage)
      } catch (error) {
        setInfoMessage(`${error.message}`)
      }
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
        <Button onClick={() => handleRequest(getEntityById, 'locations', entityId)} label="By id Location" />
        <Button onClick={() => handleRequest(createEntity, 'locations', createLocationBody)} label="Create Location" />
        <Button onClick={() => handleRequest(deleteEntityById, 'locations', entityId)} label="Delete Location" />
        <Button onClick={() => handleRequest(updateEntityById, 'locations', entityId, createLocationBody)} label="Update Location" />
      </div>

      {/* Таблица для отображения координат */}
      <div className="coordination-table-panel">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>X</th>
              <th>Y</th>
              <th>Z</th>
              <th>Name</th>
            </tr>
          </thead>
          <tbody>
            {locations.map((location) => (
              <tr key={location.id}>
                <td>{location.id}</td>
                <td>{location.x}</td>
                <td>{location.y}</td>
                <td>{location.z}</td>
                <td>{location.name}</td>
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
      </div>
    </div>
  );
};

export default LocationComponent;
