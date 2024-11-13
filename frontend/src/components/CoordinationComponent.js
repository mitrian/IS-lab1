import React, { useState, useEffect } from 'react';
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css";
import '/node_modules/primeflex/primeflex.css';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import '../css/CoordinationComponent.css';
import { getEntities, getEntityById, createEntity, deleteEntityById, updateEntityById } from '../Api';

let createCoordinatesBody = {
  "x": -1,
  "y": 57
}

const requestsWithId = [deleteEntityById, getEntityById, updateEntityById];
const requestsWitbBody = [createEntity, updateEntityById]

const CoordinationComponent = () => {
  const [infoMessage, setInfoMessage] = useState('')
  const [entityId, setEntityId] = useState('')
  const [coordinates, setCoordinates] = useState([])
  const [currentPage, setCurrentPage] = useState(0) 
  const [totalPages, setTotalPages] = useState(0)
  const [xCoordinate, setXCoordinate] = useState('')
  const [yCoordinate, setYCoordinate] = useState('')

  
  const fetchCoordinates = async (page) => {
    try {
      const data = await getEntities('coordinates', page)
      setCoordinates(data.content || [])
      setTotalPages(data.totalPages || 0)
    } catch (error) {
      setInfoMessage(`${error.message}`)
    }
  };

  
  useEffect(() => {
    fetchCoordinates(currentPage)
  }, [currentPage])

  const handleRequest = async (requestFunction, ...args) => {
    if (requestsWithId.includes(requestFunction) && !entityId || isNaN(Number(entityId))) {
      setInfoMessage("Введите id");
      return;
    } else {
      try {
        if (requestsWitbBody.includes(requestFunction)){
          createCoordinatesBody.x = xCoordinate
          createCoordinatesBody.y = yCoordinate
        }

        
        const data = await requestFunction(...args)
        const message = data.content 
          ? JSON.stringify(data.content, null, 2) 
          : JSON.stringify(data, null, 2)
          setInfoMessage(message)

       
        if (requestFunction != getEntityById)
        fetchCoordinates(currentPage)
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
        <Button onClick={() => handleRequest(getEntityById, 'coordinates', entityId)} label="By id Coordination" />
        <Button onClick={() => handleRequest(createEntity, 'coordinates', createCoordinatesBody)} label="Create Coord" />
        <Button onClick={() => handleRequest(deleteEntityById, 'coordinates', entityId)} label="Delete Coord" />
        <Button onClick={() => handleRequest(updateEntityById, 'coordinates', entityId, createCoordinatesBody)} label="Update Coord" />
      </div>

      {/* Таблица для отображения координат */}
      <div className="coordination-table-panel">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>X</th>
              <th>Y</th>
            </tr>
          </thead>
          <tbody>
            {coordinates.map((coord) => (
              <tr key={coord.id}>
                <td>{coord.id}</td>
                <td>{coord.x}</td>
                <td>{coord.y}</td>
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
      <p></p>
      <div className="input-panel">
          <label htmlFor="xCoordinate">Enter X:</label>
          <InputText
            id="xCoordinate"
            value={xCoordinate}
            onChange={(e) => setXCoordinate(e.target.value)}
            placeholder="Enter X"
          />
      {/* </div>
      <div className="input-panel"> */}
          <label htmlFor="yCoordinate">Enter Y:</label>
          <InputText
            id="yCoordinate"
            value={yCoordinate}
            onChange={(e) => setYCoordinate(e.target.value)}
            placeholder="Enter Y"
          />
      </div>
    </div>
  );
};

export default CoordinationComponent;
