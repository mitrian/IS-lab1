import React, { useState, useEffect } from 'react';
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css";
import '/node_modules/primeflex/primeflex.css';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import '../css/CoordinationComponent.css';
import { getEntities, getEntityById, createEntity, deleteEntityById, updateEntityById } from '../Api';
import { useDispatch } from 'react-redux';

let createAddressBody = {
    "zipCode" : "DEMO",
    "townId" : 3
}

const requestsWithId = [deleteEntityById, getEntityById, updateEntityById];

const AddressComponent = () => {
  const [infoMessage, setInfoMessage] = useState('')
  const [entityId, setEntityId] = useState('')
  const [address, setAddress] = useState([])
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0) 

  
  const fetchaddress = async (page) => {
    try {
      const data = await getEntities('address', page)
      setAddress(data.content || [])
      setTotalPages(data.totalPages || 0)
    } catch (error) {
      setInfoMessage(`${error.message}`)
    }
  };

  
  useEffect(() => {
    fetchaddress(currentPage);
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
        fetchaddress(currentPage)
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
        <Button onClick={() => handleRequest(getEntityById, 'address', entityId)} label="By id address" />
        <Button onClick={() => handleRequest(createEntity, 'address', createAddressBody)} label="Create address" />
        <Button onClick={() => handleRequest(deleteEntityById, 'address', entityId)} label="Delete address" />
        <Button onClick={() => handleRequest(updateEntityById, 'address', entityId, createAddressBody)} label="Update address" />
      </div>

      {/* Таблица для отображения координат */}
      <div className="coordination-table-panel">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>zipCode</th>
              <th>loc_id</th>
              <th>loc_x</th>
              <th>loc_y</th>
              <th>loc_z</th>
              <th>loc_name</th>
            </tr>
          </thead>
          <tbody>
            {address.map((address) => (
              <tr key={address.id}>
                <td>{address.id}</td>
                <td>{address.zipCode}</td>
                <td>{address.location.id}</td>
                <td>{address.location.x}</td>
                <td>{address.location.y}</td>
                <td>{address.location.z}</td>
                <td>{address.location.name}</td>
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

export default AddressComponent;
