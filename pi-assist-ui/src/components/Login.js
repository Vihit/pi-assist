import "./Login.css";
import scrum from "../scrum.svg";
import { useState } from "react";
import { config } from "./config";

function Login(props) {
  const [username, setUsername] = useState("");
  const [pwd, setPwd] = useState("");
  async function loginHandler() {
    let user = { username, pwd };
    fetch(config.apiUrl + "login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(user),
    })
      .then((response) => {
        if (response.ok) return response.json();
        else throw new Error("Login Unsuccessful!");
      })
      .then((actualData) => {
        localStorage.setItem("user", JSON.stringify(actualData));
        props.onLogin();
      });
  }

  return (
    <div className="login-flex-container">
      <div className="scrum-svg">
        <img src={scrum}></img>
      </div>
      <div className="login-screen">
        <div className="login-header">Login</div>
        <div className="form">
          <div className="control">
            <input
              type="text"
              placeholder="username"
              onChange={(e) => setUsername(e.target.value)}
            ></input>
          </div>
          <div className="control">
            <input
              type="password"
              placeholder="password"
              onChange={(e) => setPwd(e.target.value)}
            ></input>
          </div>
          <div className="submit">
            <input type="submit" value="  Go  " onClick={loginHandler}></input>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
