import React, { useState, useEffect } from 'react';
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css";
import '/node_modules/primeflex/primeflex.css';
import { Button } from 'primereact/button';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import '../css/CoordinationComponent.css';
import { getEntities, getEntityById, createEntity, deleteEntityById, updateEntityById } from '../Api';


let createStaffBody = {
    "name" : "testik",
    "organizationId" : 17
}

const requestsWithId = [deleteEntityById, getEntityById, updateEntityById];

const StaffComponent = () => {
  const [infoMessage, setInfoMessage] = useState('')
  const [entityId, setEntityId] = useState('')
  const [staff, setStaff] = useState([])
  const [currentPage, setCurrentPage] = useState(0) 
  const [totalPages, setTotalPages] = useState(0)
  console.log(staff)

  
  const fetchstaff = async (page) => {
    try {
      const data = await getEntities('staff', page)
      setStaff(data.content || [])
      setTotalPages(data.totalPages || 0)
    } catch (error) {
      setInfoMessage(`${error.message}`)
    }
  };

  
  useEffect(() => {
    fetchstaff(currentPage)
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
        fetchstaff(currentPage)
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
        <Button onClick={() => handleRequest(getEntityById, 'staff', entityId)} label="By id Coordination" />
        <Button onClick={() => handleRequest(createEntity, 'staff', createStaffBody)} label="Create Coord" />
        <Button onClick={() => handleRequest(deleteEntityById, 'staff', entityId)} label="Delete Coord" />
        <Button onClick={() => handleRequest(updateEntityById, 'staff', entityId, createStaffBody)} label="Update Coord" />
      </div>

      {/* Таблица для отображения координат */}
      <div className="coordination-table-panel">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>org_id</th>

            </tr>
          </thead>
          <tbody>
            {staff.map((st) => ( 
              // console.log(st)
              <tr key={st.id}>
                <td>{st.id}</td>
                <td>{st.name}</td>
                <td>{st.organization ? st.organization.id : 'Нет данных'}</td>
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

export default StaffComponent;
