import React, { useEffect, useState } from 'react';
import { BACKENDURL, callApi } from './lib';

const Home = () => {
    const [fullname, setfullName] = useState("");

    useEffect(()=>{
        const token = localStorage.getItem("token");
        if(!token)
            window.location.replace("/");

        callApi("GET", BACKENDURL + "users/uinfo", "", getUserFullName, token);
    },[]);

    function getUserFullName(res){
        const data = JSON.parse(res);
        if(data.code != 200)
        {
            alert(data.msg);
            return;
        }
        setfullName(data.fullname);
    }

    return (
        <div>
            <h2>Welcome {fullname}!</h2>
        </div>
    );
}

export default Home;
