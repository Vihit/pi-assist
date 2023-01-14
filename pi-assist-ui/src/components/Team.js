import { useEffect, useState } from "react";
import "./Team.css";
import { config } from "./config";

function Team() {
  const initialStart =
    localStorage.getItem("PI") !== "undefined"
      ? new Date(JSON.parse(localStorage.getItem("PI")).start)
      : new Date();
  const initialEnd =
    localStorage.getItem("PI") !== "undefined"
      ? new Date(JSON.parse(localStorage.getItem("PI")).end)
      : new Date();
  const [team, setTeam] = useState([]);
  const [start, setStart] = useState(initialStart);
  const [end, setEnd] = useState(initialEnd);
  const [totalDays, setTotalDays] = useState(
    Math.abs(end - start) / (1000 * 3600 * 24)
  );
  const [clickedUser, setClickedUser] = useState("");
  const [piDays, setPIDays] = useState([]);
  const [calendarDays, setCalendarDays] = useState([]);
  const [leaves, setLeaves] = useState([]);
  const [appliedLeaves, setAppliedLeaves] = useState([]);
  const [sameUser, setSameUser] = useState(false);
  const [alert, setAlert] = useState(false);
  const [alertContent, setAlertContent] = useState("");
  const month = [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sept",
    "Oct",
    "Nov",
    "Dec",
  ];
  const dayNames = ["M", "T", "W", "T", "F", "S", "S"];
  useEffect(() => {
    getTeam();
    setTotalDays(Math.abs(end - start) / (1000 * 3600 * 24));
    const days = [];
    for (let i = 0; i <= totalDays; i++) {
      const startDate = new Date(start);
      startDate.setDate(startDate.getDate() + i);
      days.push(startDate);
    }
    setPIDays(days);
    const calendarStart = new Date(start);
    calendarStart.setDate(start.getDate() - start.getDay() + 1);
    const calDays = [];
    const calTotalDays = Math.abs(end - calendarStart) / (1000 * 3600 * 24);
    for (let i = 0; i <= calTotalDays; i++) {
      const startDate = new Date(calendarStart);
      startDate.setDate(startDate.getDate() + i);
      calDays.push(startDate);
    }
    setCalendarDays(calDays);
  }, []);

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

  function addToLeaves(leave) {
    let appdLeaves = [...appliedLeaves];
    const existingLeave = appdLeaves.filter(
      (l) => l.leave === new Date(leave).toISOString().split("T")[0]
    )[0];
    if (existingLeave) {
      console.log("Removing leave");
      appdLeaves = appdLeaves.filter((leave) => leave !== existingLeave);
    } else {
      appdLeaves.push({ leave: new Date(leave).toISOString().split("T")[0] });
    }
    setAppliedLeaves(appdLeaves);
  }

  function fetchLeaves(username) {
    setClickedUser(username);
    if (username === JSON.parse(localStorage.getItem("user")).username) {
      setSameUser(true);
    } else {
      setSameUser(false);
    }
    setLeaves(team.filter((member) => member.username === username)[0].leaves);
    const appdLeaves = [];
    team
      .filter((member) => member.username === username)[0]
      .leaves.forEach((element) => {
        appdLeaves.push(element);
      });
    setAppliedLeaves(appdLeaves);
  }

  function saveLeaves() {
    fetch(config.apiUrl + "leaves/" + clickedUser, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
      body: JSON.stringify(appliedLeaves),
    })
      .then((response) => {
        if (response.ok) {
          console.log("Leaves saved!");
          setAlert(true);
          setAlertContent("Leaves saved!");
          const timeId = setTimeout(() => {
            setAlert(false);
            setAlertContent("");
          }, 1000);
          getTeam();
          return () => {
            clearTimeout(timeId);
          };
        } else {
          throw new Error("Unable to save leaves!");
        }
      })
      .catch(function (error) {
        console.log("Unable to save leaves!", error);
      });
  }

  return (
    <div className="full-container">
      <div
        className={
          "team-notification" + (alert ? "" : " team-notification-hidden")
        }
      >
        <div>{alertContent}</div>
      </div>
      <div
        className={"save-leaves " + (sameUser ? " " : " hide-save-leaves")}
        onClick={() => saveLeaves()}
      >
        Save Leaves
      </div>
      <div className="actual-container">
        <div className="team-container">
          <div className="team-heading">
            {JSON.parse(localStorage.getItem("user")).team}
          </div>
          {team
            .sort((m1, m2) => {
              if (m1.name > m2.name) return 1;
              else if (m1.name < m2.name) return -1;
              else return 0;
            })
            .filter((mbr) => mbr.role === "ROLE_Team-Member")
            .map((val, idx) => (
              <div
                className={
                  "team-member " +
                  (val.username === clickedUser ? " selected-team-member" : "")
                }
                key={idx}
                id={val.username}
                onClick={(e) => fetchLeaves(e.target.id)}
              >
                {val.name}
              </div>
            ))}
        </div>
        <div
          className={
            "calendar-container " + (sameUser ? " active-calendar" : "")
          }
        >
          {dayNames.map((val, idx) => {
            return (
              <div
                key={idx}
                className="day-heading-day unclickable-day day-head"
              >
                {val}
                <div className="day-detail">ABC</div>
              </div>
            );
          })}
          {calendarDays.map((val, idx) => {
            return (
              <div
                className={
                  "day" +
                  "-" +
                  Number(val.getMonth() % 3) +
                  " " +
                  (val.getDay() === 6 || val.getDay() === 0
                    ? " holiday-day"
                    : piDays.filter((day) => day.getTime() === val.getTime())
                        .length > 0
                    ? appliedLeaves.filter(
                        (leave) =>
                          new Date(leave.leave).getMonth() === val.getMonth() &&
                          new Date(leave.leave).getDate() === val.getDate()
                      ).length > 0
                      ? "leave-day "
                      : "pi-day"
                    : "non-pi-day") +
                  (sameUser ? " clickable-day" : " unclickable-day")
                }
                key={idx}
                onClick={() => addToLeaves(val)}
              >
                <div className="day-val">
                  {val.getDate()} <br></br>
                  {month[val.getMonth()]}
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}

export default Team;
