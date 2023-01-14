import { useEffect, useState } from "react";
import { Link, useHistory } from "react-router-dom";
import "./Navbar.css";
import "../fa.css";
import { config } from "./config";

function Navbar(props) {
  const [accountClick, setAccountClick] = useState(false);
  const [pis, setPIs] = useState([]);
  const [selectedPI, setSelectedPI] = useState({});
  const [piUpdated, setPIUpdated] = useState(false);
  const [alert, setAlert] = useState(false);
  const [alertContent, setAlertContent] = useState("");
  let history = useHistory();

  useEffect(() => {
    if (props.isLoggedIn) {
      getPIs();
      setPIUpdated(!piUpdated);
    }
  }, [props.isLoggedIn, selectedPI, pis]);

  function getPIs() {
    fetch(config.apiUrl + "pi/open/", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
      })
      .then((actualData) => {
        localStorage.setItem("PI", JSON.stringify(actualData[0]));
        setPIs(actualData);
      });
  }

  function handlePICreation() {
    let pi = {
      name: "New-PI",
      team: JSON.parse(localStorage.getItem("user")).team,
    };
    fetch(config.apiUrl + "pi/", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
      body: JSON.stringify(pi),
    })
      .then((response) => {
        if (response.ok) {
          setAlert(true);
          setAlertContent("New PI created!");
          setTimeout(() => {
            setAlert(false);
            setAlertContent("");
          }, 1000);
          return response.json();
        }
        throw new Error("New PI already created!");
      })
      .then((actualData) => {
        console.log("PI Created " + actualData);
        localStorage.setItem("PI", JSON.stringify(actualData));
        setPIs((prev) => actualData);
      })
      .catch(function (error) {
        setAlert(true);
        setAlertContent("There is an open PI!");
        setTimeout(() => {
          setAlert(false);
          setAlertContent("");
        }, 1000);
        console.log("New PI already created!", error);
        getPIs();
      });
  }

  function handleAccountClick() {
    setAccountClick(!accountClick);
  }

  function handleLogout() {
    setAccountClick(false);
    props.onLogout();
  }

  function handleMouseOut(e) {
    console.log(e);
  }

  if (props.isLoggedIn) {
    return (
      <div className="navbar">
        <div
          className={
            "team-notification" + (alert ? "" : " team-notification-hidden")
          }
        >
          <div>{alertContent}</div>
        </div>
        <div className="logo">
          <div>PI&nbsp;&nbsp;&nbsp;Assist</div>
        </div>
        <div className="options">
          <div>
            <Link to="/">Home</Link>
          </div>
          <div>
            <Link to="/team">Team</Link>
          </div>
          {JSON.parse(localStorage.getItem("user")).role ===
            "ROLE_Scrum-Master" &&
            pis !== "undefined" &&
            pis.length > 0 && (
              <div className="flex-1">
                <Link to="/pi">PI</Link>
              </div>
            )}

          {props.showPlanningPokerIcon && (
            <div className="flex-1 poker">
              <div className="poker-live">
                <Link to={"/planning-poker/" + props.planningLink}>
                  PlanningPoker
                </Link>
              </div>
              <div className="live flex-1">
                Live
                <div className="live-symbol">
                  <div className="live-symbol-inner"></div>
                </div>
              </div>
            </div>
          )}
        </div>
        <div className="account" onClick={handleAccountClick}>
          <i className="fas fa-user-circle icon-account"></i>
        </div>
        <ul className={"dropdown " + (accountClick ? "show" : "")}>
          <li className="dropdown-item user">
            {JSON.parse(localStorage.getItem("user")).name}
          </li>
          {JSON.parse(localStorage.getItem("user")).role ===
            "ROLE_Scrum-Master" && (
            <li className="dropdown-item" onClick={handlePICreation}>
              Create PI
            </li>
          )}
          {JSON.parse(localStorage.getItem("user")).role ===
            "ROLE_Scrum-Master" && (
            <li className="dropdown-item">
              <a
                href={
                  config.apiUrl +
                  "report/download/" +
                  JSON.parse(localStorage.getItem("user")).team
                }
                target="_blank"
                download
              >
                Download Report
              </a>
            </li>
          )}
          <li className="dropdown-item" onClick={handleLogout}>
            Logout
          </li>
        </ul>
      </div>
    );
  } else {
    return (
      <div className="navbar">
        <div className="logo">
          <div>PI&nbsp;&nbsp;&nbsp;Assist</div>
        </div>
        <div className="options">
          <div></div>
          <div></div>
          <div></div>
        </div>
        <div>
          <div></div>
          <div>&nbsp;</div>
        </div>
      </div>
    );
  }
}

export default Navbar;
