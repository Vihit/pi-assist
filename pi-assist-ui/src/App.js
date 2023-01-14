import logo from "./logo.svg";
import "./App.css";
import { useEffect, useState } from "react";
import { Route, useHistory } from "react-router-dom";
import { config } from "./components/config";
import FeatureDetail from "./components/FeatureDetail";
import Home from "./components/Home";
import Navbar from "./components/Navbar";
import Team from "./components/Team";
import PI from "./components/PI";
import PlanningPoker from "./components/PlanningPoker";
import Login from "./components/Login";

function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [openPlanningPoker, setOpenPlanningPoker] = useState(false);
  const [planningItemId, setPlanningItemId] = useState(0);
  const [planningItemName, setPlanningItemName] = useState("");

  const history = useHistory();

  useEffect(() => {
    if (loggedIn) regularCheck();
  }, [loggedIn]);

  function regularCheck() {
    console.log("Regular Check");
    setTimeout(regularCheck, 5000);
    checkOpenPPItem();
  }
  function checkOpenPPItem() {
    fetch(
      config.apiUrl +
        "planning-poker/open/" +
        JSON.parse(localStorage.getItem("user")).team,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization:
            "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
        },
      }
    )
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("No Items being estimated");
        }
      })
      .then((actualData) => {
        if (actualData) {
          setPlanningItemId(actualData.key);
          setPlanningItemName(actualData.summary);
          setOpenPlanningPoker(true);
        }
      })
      .catch(function (error) {
        setOpenPlanningPoker(false);
      });
  }

  function loginHandler() {
    setLoggedIn(true);
    history.push("/");
  }

  function logoutHandler() {
    localStorage.clear();
    setLoggedIn(false);
  }

  function planningPokerHandler() {
    setOpenPlanningPoker(!openPlanningPoker);
  }

  return (
    <div className="App">
      <Navbar
        isLoggedIn={loggedIn}
        onLogout={logoutHandler}
        showPlanningPokerIcon={openPlanningPoker}
        planningLink={"id=" + planningItemId + "&name=" + planningItemName}
      ></Navbar>
      {!loggedIn ? <Login onLogin={loginHandler}></Login> : null}
      {loggedIn && (
        <div>
          <Route exact path="/" component={Home}></Route>
          <Route exact path="/feature">
            <FeatureDetail
              onPlanningPokerClick={planningPokerHandler}
            ></FeatureDetail>
          </Route>
          <Route exact path="/team">
            <Team></Team>
          </Route>
          <Route exact path="/pi">
            <PI></PI>
          </Route>
          <Route exact path="/planning-poker/:story">
            <PlanningPoker
              onPlanningPokerClick={planningPokerHandler}
            ></PlanningPoker>
          </Route>
        </div>
      )}
    </div>
  );
}

export default App;
