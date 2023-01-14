import { useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import "./PlanningPoker.css";
import { over } from "stompjs";
import SockJS from "sockjs-client";
import { config } from "./config";

var stompClient = null;
var availableInterval = null;
function PlanningPoker(props) {
  const [revealEstimates, setRevealEstimates] = useState(false);
  const [team, setTeam] = useState([]);
  const [estimate, setEstimate] = useState(0);
  const { story } = useParams();
  let history = useHistory();
  const user = JSON.parse(localStorage.getItem("user"))["username"];
  const [onlineUsers, setOnlineUsers] = useState([]);
  const [estimateFromUsers, setEstimatesFromUsers] = useState([]);
  const [pokerPlanningItem, setPokerPlanningItem] = useState({});

  useEffect(() => {
    console.log("Main hook called");
    const sock = new SockJS(config.apiUrl + "pi-assist-server/");
    stompClient = over(sock);
    stompClient.connect({}, onConnected, onError);
    getTeam();
    getPokerPlanningItem();
    availableInterval = setInterval(() => {
      if (stompClient) {
        let onlineMessage = { username: user, online: true };
        stompClient.send(
          "/planning-poker/available",
          {},
          JSON.stringify(onlineMessage)
        );
        setPokerPlanningItem((pokerPlanningItem) => {
          stompClient.send("/planning-poker/get", {}, pokerPlanningItem.id);
          return pokerPlanningItem;
        });
      }
    }, 1000);
    return function cleanup() {
      let onlineMessage = { username: user, online: false };
      stompClient.send(
        "/planning-poker/available",
        {},
        JSON.stringify(onlineMessage)
      );
      clearInterval(availableInterval);
      stompClient.disconnect();
      console.log("Socket Connection disconnected!");
    };
  }, []);

  function getPokerPlanningItem() {
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
        }
      })
      .then((actualData) => {
        setPokerPlanningItem(actualData);
      });
  }

  function onConnected() {
    const users = [...onlineUsers];
    users.push(user);
    setOnlineUsers(users);
    stompClient.subscribe("/topic/estimates", onEstimateReceive);
    stompClient.subscribe("/topic/online", onlineUsersHeartbeat);
  }

  function onlineUsersHeartbeat(payload) {
    const userInMessage = JSON.parse(payload.body).username;
    const online = JSON.parse(payload.body).online;
    if (online) {
      setOnlineUsers((onlineUserArr) => {
        const existingArr = [...onlineUserArr];
        if (existingArr.includes(userInMessage)) {
          return [...onlineUserArr];
        } else return [...onlineUserArr, userInMessage];
      });
    } else {
      console.log("Removing from online users");
      const onlineMap = [...onlineUsers];
      setOnlineUsers(onlineMap.filter((user) => user !== userInMessage));
    }
  }
  function onEstimateReceive(payload) {
    if (payload.body === "end") {
      props.onPlanningPokerClick();
      history.goBack();
    } else if (payload.body === "reveal") {
      setRevealEstimates(true);
    } else {
      console.log(payload);
      const estimates = JSON.parse(payload.body);
      setEstimatesFromUsers((prevEstimates) => {
        return estimates;
      });
    }
  }

  function onError(err) {
    console.log(err);
  }

  function reveal() {
    stompClient.send("/planning-poker/end", {}, "reveal");
    setRevealEstimates(true);
  }

  function clickedEstimate(e) {
    setEstimate(e.target.id);
    if (stompClient) {
      let estimateMessage = {
        storyId: story.split("&name=")[0].split("id=")[1],
        name: user,
        estimate: e.target.id,
        team: JSON.parse(localStorage.getItem("user")).team,
      };
      stompClient.send(
        "/planning-poker/estimateMessage",
        {},
        JSON.stringify(estimateMessage)
      );
    }
  }

  function endPoker() {
    stompClient.send("/planning-poker/end", {}, "end");
    props.onPlanningPokerClick();
    closeItemForEstimation();
  }

  function closeItemForEstimation() {
    let item = {
      key: story.split("&name=")[0].split("id=")[1],
      closed: true,
      team: JSON.parse(localStorage.getItem("user")).team,
    };
    fetch(config.apiUrl + "planning-poker/", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
      body: JSON.stringify(item),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
        throw new Error("No such item being estimated!");
      })
      .then((actualData) => {
        console.log("Item closed for estimation!" + actualData);
        // history.goBack();
      })
      .catch(function (error) {
        console.log("No such item being estimated!", error);
      });
  }

  function getTeam() {
    const team = JSON.parse(localStorage.getItem("user")).team;
    fetch(config.apiUrl + "users/" + team, {
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
        setTeam(actualData);
      });
  }

  return (
    <div className="full-container">
      <div className="actual-container">
        <div className="team-container">
          <div className="team-heading">
            {JSON.parse(localStorage.getItem("user")).team}
          </div>
          {team
            .filter((mbr) => mbr.role === "ROLE_Team-Member")
            .map((val, idx) => (
              <div
                className={
                  "team-member flex-1 " +
                  (estimateFromUsers.filter(
                    (est) => est.username === val.username
                  ).length > 0
                    ? " green"
                    : "")
                }
                key={idx}
                id={val.username}
              >
                <div className={"member "}>{val.name}</div>
                <div className="live-member">
                  <i
                    className={
                      "fa-solid fa-circle-check " +
                      (onlineUsers.includes(val.username)
                        ? "online"
                        : "offline")
                    }
                  ></i>
                </div>
              </div>
            ))}
        </div>
        <div className="planning-container">
          <div className="story-planning-title">
            <div className="edit-add-story">
              <i className="fa-solid fa-bookmark edit-add-story-icon"></i>
            </div>
            <div>{pokerPlanningItem.summary}</div>
          </div>
          <div
            className={
              "label-estimate" +
              (estimate === 0 &&
              !estimateFromUsers
                .map((est, idx) => est.username)
                .includes(user) &&
              JSON.parse(localStorage.getItem("user")).role ===
                "ROLE_Team-Member"
                ? " "
                : " closeds")
            }
          >
            Choose your estimate!
          </div>
          <div
            className={
              "story-estimate-cards " +
              (estimate === 0 &&
              !estimateFromUsers
                .map((est, idx) => est.username)
                .includes(user) &&
              JSON.parse(localStorage.getItem("user")).role ===
                "ROLE_Team-Member"
                ? " "
                : " closeds")
            }
          >
            <div className="estimate-card" id="1" onClick={clickedEstimate}>
              1
            </div>
            <div className="estimate-card" id="2" onClick={clickedEstimate}>
              2
            </div>
            <div className="estimate-card" id="3" onClick={clickedEstimate}>
              3
            </div>
            <div className="estimate-card" id="4" onClick={clickedEstimate}>
              4
            </div>
            <div className="estimate-card" id="5" onClick={clickedEstimate}>
              5
            </div>
            <div className="estimate-card" id="6" onClick={clickedEstimate}>
              6
            </div>
            <div className="estimate-card" id="7" onClick={clickedEstimate}>
              7
            </div>
            <div className="estimate-card" id="8" onClick={clickedEstimate}>
              8
            </div>
            <div className="estimate-card" id="9" onClick={clickedEstimate}>
              9
            </div>
            <div className="estimate-card" id="10" onClick={clickedEstimate}>
              10
            </div>
          </div>
          <div className="label-estimate">Team estimates!</div>
          <div className="story-estimate-cards">
            {estimateFromUsers.map((est, idx) => {
              return (
                <div
                  className={
                    "estimate-card" +
                    (revealEstimates ? " unfolded" : " folded")
                  }
                >
                  <div>{est.estimate}</div>
                  <div className="estimator">
                    {
                      team.filter(
                        (member) => member.username === est.username
                      )[0].name
                    }
                  </div>
                </div>
              );
            })}
          </div>
          <div
            className={
              "buttons " +
              (JSON.parse(localStorage.getItem("user")).role ===
              "ROLE_Scrum-Master"
                ? ""
                : "closeds")
            }
          >
            <div className={"poker-button"} onClick={reveal}>
              Reveal
            </div>
            <div className="poker-button" onClick={endPoker}>
              End Poker
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default PlanningPoker;
