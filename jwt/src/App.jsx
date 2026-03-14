import React, { useEffect, useState } from 'react';
import './App.css';
import { BACKENDURL, callApi, setSession } from './lib';

const App = () => {
  const [isSignin, setIsSignin] = useState(true);
  const [signupData, setSignupData] = useState({
    fname: "",
    lname: "",
    mobile: "",
    email: "",
    password: "",
    cpassword: ""
  });
  const [signinData, setSignInData] = useState({
    email: "", 
    password: "",
    captcha: ""
  });
  const [validateSignup, setValidateSignup] = useState({});
  const [validateSignin, setValidateSignin] = useState({});
  const [captchaData, setCaptchaData] = useState({});

  useEffect(()=>{
    //callApi("GET", BACKENDURL + "users/captcha", "", getCaptcha);
    loadCaptcha();
  }, []);

  function loadCaptcha(){
    callApi("GET", BACKENDURL + "users/captcha", "", getCaptcha);
  }

  function getCaptcha(res){
    const data = JSON.parse(res);
    if(data.code != 200)
    {
      alert(data.msg);
      return;
    }
    localStorage.setItem("captcha", data.text);
    setCaptchaData(data);
  }

  function validateSignuppage(){
    let validate = {};
    if(signupData.fname === "") validate.fname = true;
    if(signupData.lname === "") validate.lname = true;
    if(signupData.mobile === "") validate.mobile = true;
    if(signupData.email === "") validate.email = true;
    if(signupData.password === "") validate.password = true;
    if(signupData.cpassword === "" || signupData.password !== signupData.cpassword) validate.cpassword = true;
    setValidateSignup(validate);
    return Object.keys(validate).length === 0;
   }

  function registration(){
    if(!validateSignuppage())
      return;

    let data = JSON.stringify(signupData);
    callApi("POST", BACKENDURL + "users/signup", data, registrationResponse);
  }

  function registrationResponse(res){
    let data = JSON.parse(res);
    alert(data.msg);
  }

  function handleSignupInput(e){
    const {name, value} = e.target;
    setSignupData({...signupData, [name]: value});
  }

  function handleSigninInput(e){
    const {name, value} = e.target;
    setSignInData({...signinData, [name]: value});
  }

  function switchWindow(){
    setIsSignin(isSignin ? false: true);
  }

  function validateSigninPage(){
    let validate = {};
    if(signinData.email === "") validate.email = true;
    if(signinData.password === "") validate.password = true;
    if(signinData.captcha === "" || signinData.captcha != localStorage.getItem("captcha")) validate.captcha = true;
    setValidateSignin(validate);
    return Object.keys(validate).length === 0;
  }

  function signin(){
    if(!validateSigninPage())
      return;
    callApi("POST", BACKENDURL + "users/signin", JSON.stringify(signinData), signinResponse);
  }

  function signinResponse(res){
    let data = JSON.parse(res);
    if(data.code != 200)
    {
      alert(data.msg);
      return;
    }
    //setSession("token", data.token, 1);
    localStorage.setItem("token", data.token);
    window.location.replace("/home");
  }

  return (
    <div className='app'>
      {isSignin === true && 
        <div className='signin'>
          <h3>Sign in with email</h3>
          <input type='text'  className={validateSignin.email ? 'error' : ''} placeholder='Enter Email ID' name="email" value={signinData.email} onChange={(e)=>handleSigninInput(e)} />
          <input type='text'  className={validateSignin.password ? 'error' : ''}  placeholder='Enter Password' name='password' value={signinData.password} onChange={(e)=>handleSigninInput(e)} />
          <div className='captcha'>
            <input type='text' className={validateSignin.captcha ? 'error' : ''} placeholder='Captcha' name='captcha' value={signinData.captcha} onChange={(e)=>handleSigninInput(e)}  />
            <img src={"data:image/png;base64," + captchaData?.image} alt='' onClick={()=>loadCaptcha()} />
          </div>
          <label>Forgot <span>Password?</span></label>
          <button onClick={()=>signin()}>Sign in</button>
          <p>Do not have an account? <label onClick={()=>switchWindow()}>Sign up</label></p>
        </div>
      }

      {isSignin === false &&
        <div className='signup'>
          <h3>Create Account</h3>
          <input type='text' className={validateSignup.fname ? 'error' : ''} placeholder='Enter First Name' name='fname' value={signupData.fname} onChange={(e)=>handleSignupInput(e)} />
          <input type='text' className={validateSignup.lname ? 'error' : ''} placeholder='Enter Last Name' name='lname' value={signupData.lname} onChange={(e)=>handleSignupInput(e)} />
          <input type='text' className={validateSignup.mobile ? 'error' : ''} placeholder='Enter Mobile Number' name="mobile" value={signupData.mobile} onChange={(e)=>handleSignupInput(e)} />
          <input type='text' className={validateSignup.email ? 'error' : ''} placeholder='Enter Email ID' name='email' value={signupData.email} onChange={(e)=>handleSignupInput(e)} />
          <input type='password' className={validateSignup.password ? 'error' : ''} placeholder='Enter Password' name='password' value={signupData.password} onChange={(e)=>handleSignupInput(e)} />
          <input type='password' className={validateSignup.cpassword ? 'error' : ''} placeholder='Enter Confirm Password' name='cpassword' value={signupData.cpassword} onChange={(e)=>handleSignupInput(e)} />
          <button onClick={()=>registration()}>Register</button>
          <p>Already have an account? <label onClick={()=>switchWindow()}>Sign in</label></p>
        </div>
      }
    </div>
  );
}

export default App;
