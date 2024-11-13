import { getToken } from './components/AuthenticationComponent';
import { urlBack } from './Conf';


export const sendRequest = async (endpoint, method = 'GET', body = null, id = '', modifUrl = '') => {
  const token = await getToken();
  let url = id ? `${urlBack}/api/v1/${endpoint}/${id}` : `http://localhost:8080/api/v1/${endpoint}`;
  if (modifUrl !== ''){
    url = modifUrl
  }
  const options = {
    method,
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: body ? JSON.stringify(body) : null,
  };
  
  try {
    const response = await fetch(url, options);

    if (response.ok) {
      console.log(response)
      if (method === 'DELETE') {
        return "Удалено: id =" + id
      }
      const data = await response.json();
      return data;
    } else {
      const errorData = await response.json();
      throw new Error(`Error ${response.status}: ${errorData.message}`);
    }
  } catch (error) {
    throw new Error(`Fetch error: ${error.message}`);
  }
};
  
// Обновляем функцию getEntities, чтобы она принимала параметр page
export const getEntities = (endpoint, page = 0) => {
  const modifUrl = `${urlBack}/api/v1/${endpoint}?page=${page}&size=5`;
  return sendRequest(endpoint, 'GET', null, '', modifUrl);
};
export const getEntityById = (endpoint, id) => sendRequest(endpoint, 'GET', null, id);
export const createEntity = (endpoint, body) => sendRequest(endpoint, 'POST', body);
export const updateEntityById = (endpoint, id, body) => sendRequest(endpoint, 'PUT', body, id);
export const deleteEntityById = (endpoint, id) => sendRequest(endpoint, 'DELETE', null, id);







// const sendGreaterRatingRequest = async (endpoint, method = 'GET', comparebleRating) => {
//   const token = await getToken()
//   try {
//     const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${comparebleRating}`, {
//       method: method,
//       headers: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${token}`,
//       }
//     });

//     if (response.ok) {
//       const data = await response.json(); // Получаем средний рейтинг
//       setInfoMessage(`Amount: ${data}`);
//     } else {
//       const errorData = await response.json(); 
//       setInfoMessage(`Error ${response.status}: ${errorData.message}`);
//     }
//   } catch (error) {
//     setInfoMessage(`Fetch error: ${error.message}`);
//   }
// };

// const sendSearchFullnameRequest = async (endpoint, method = 'GET', comparebleRating) => {
//   try {
//     const token = await getToken()
//     const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${comparebleRating}`, {
//       method: method,
//       headers: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${token}`,
//       }
//     });

//     if (response.ok) {
//       const data = await response.json();
//       setInfoMessage(JSON.stringify(data, null, 2));;
//     } else {
//       setInfoMessage(`Error ${response.status}: ${response.statusText}`);
//     }
//   } catch (error) {
//     setInfoMessage(`Fetch error: ${error.message}`);
//   }
// };

// const sendDismissStaffOrganization = async (endpoint, method = 'PUT', organizationId) => {
//   try {
//     const token = await getToken()
//     const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${organizationId}`, {
//       method: method,
//       headers: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${token}`,
//       }
//     });

//     if (response.ok) {
//       setInfoMessage(`Dismissed from organization wit id: ${organizationId}`);
//     } else {
//       setInfoMessage(`Error ${response.status}: ${response.statusText}`);
//     }
//   } catch (error) {
//     setInfoMessage(`Fetch error: ${error.message}`);
//   }
// };

// const sendAssignStaffToOrganization = async (endpoint, method = 'PUT', staffId, organizationId) => {
//   try {
//     const token = await getToken()
//     const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${staffId}/${organizationId}`, {
//       method: method,
//       headers: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${token}`,
//       }
//     });

//     if (response.ok) {
//       setInfoMessage(`Assign staff ${staffId} to organization wit id: ${organizationId}`);
//     } else {
//       const errorData = await response.json(); 
//       setInfoMessage(`Error ${response.status}: ${errorData.message}`);
//     }
//   } catch (error) {
//     setInfoMessage(`Fetch error: ${error.message}`);
//   }
// };





















// const sendGetEntityRequest = async (endpoint, method = 'GET') => {
//   try {
//     const token = await getToken()
//     const response = await fetch(`http://localhost:8080/api/v1/${endpoint}`, {
//       method: method,
//       headers: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${token}`,
//       }
      
//     });
    
//     if (response.ok) {
//       const data = await response.json();
//       setInfoMessage(JSON.stringify(data.content, null, 2));
//     } else {
//       const errorData = await response.json(); 
//       setInfoMessage(`Error ${response.status}: ${errorData.message}`);
//     }
//   } catch (error) {
//     setInfoMessage(`Fetch error: ${error.message}`);
//   }
// };

// const sendGetEntityByIdRequest = async (endpoint, method = 'GET', ind) => {
//   try {
//     const token = await getToken()
//     const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${ind}`, {
//       method: method,
//       headers: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${token}`,
//       }
//     });

//     if (response.ok) {
//       const data = await response.json();
//       setInfoMessage(JSON.stringify(data, null, 2));
//     } else {
//       const errorData = await response.json(); 
//       setInfoMessage(`Error ${response.status}: ${errorData.message}`);
//     }
//   } catch (error) {
//     setInfoMessage(`Fetch error: ${error.message}`);
//   }
// };


// const sendСreateEntityRequest = async (endpoint, method = 'POST', body = null) => {
//   try {
//     const token = await getToken()
//     const response = await fetch(`http://localhost:8080/api/v1/${endpoint}`, {
//       method: method,
//       headers: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${token}`,
//       },
//       body: body ? JSON.stringify(body) : null,
//     });

//     if (response.ok) {
//       const data = await response.json();
//       setInfoMessage(JSON.stringify(data, null, 2));
//     } else {
//       const errorData = await response.json(); 
//       setInfoMessage(`Error ${response.status}: ${errorData.message}`);
//     }
//   } catch (error) {
//     setInfoMessage(`Fetch error: ${error.message}`);
//   }
// };


// const sendUpdateEntityByIdRequest = async (endpoint, method = 'PUT', body = null, ind) => {
//   try {
//     const token = await getToken()
//     const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${ind}`, {
//       method: method,
//       headers: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${token}`,
//       },
//       body: body ? JSON.stringify(body) : null,
//     });

//     if (response.ok) {
//       const data = await response.json();
//       setInfoMessage(JSON.stringify(data, null, 2));
//     } else {
//       const errorData = await response.json(); 
//       setInfoMessage(`Error ${response.status}: ${errorData.message}`);
//     }
//   } catch (error) {
//     setInfoMessage(`Fetch error: ${error.message}`);
//   }
// };

// const sendDeleteEntityByIdRequest = async (endpoint, method = 'DELETE', ind) => {
//   try {
//     const token = await getToken()
//     const response = await fetch(`http://localhost:8080/api/v1/${endpoint}/${ind}`, {
//       method: method,
//       headers: {
//         'Content-Type': 'application/json',
//         'Authorization': `Bearer ${token}`,
//       }
//     });

//     if (response.ok) {
//       //const data = await response.json();
//       setInfoMessage(response.status);
//     } else {
//       const errorData = await response.json(); 
//       setInfoMessage(`Error ${response.status}: ${errorData.message}`);
//     }
//   } catch (error) {
//     setInfoMessage(`Fetch error: ${error.message}`);
//   }
// };