export const BACKENDURL = "http://localhost:8081/";
export function callApi(reqMethod, url, data, responseHandler, jwttoken = "")
{
    let options;
    if(reqMethod === "GET" || reqMethod === "DELETE")
        options = {method: reqMethod, headers:{'Content-Type': 'application/json', 'jwttoken': jwttoken}};
    else
        options = {method: reqMethod, headers:{'Content-Type': 'application/json', 'jwttoken': jwttoken}, body: data};
    fetch(url, options)
        .then((response)=>{
            if(!response.ok)
                throw new Error(response.status + " " + response.statusText);
            return response.text();
        })
        .then((data)=>responseHandler(data))
        .catch((err)=>alert(err));
}

export function setSession(sesName, sesValue, expInDays)
{
     let expires = new Date(new Date().getTime() + (expInDays * 24 * 60 * 60 * 1000));
     document.cookie = `${sesName}=${sesValue};expires=${expires};path=/`;
}