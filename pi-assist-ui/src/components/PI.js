import "./PI.css";
import { useEffect, useState } from "react";
import StackedPIStats from "./StackedPIStats";
import { config } from "./config";

function PI() {
  const initialPI =
    localStorage.getItem("PI") !== "undefined"
      ? JSON.parse(localStorage.getItem("PI"))
      : {};
  const [pi, setPI] = useState({
    name: "",
    filter: "",
    start: "",
    end: "",
    spPerDay: "",
    sprints: [{ supportActivities: [{}] }],
  });
  const [alert, setAlert] = useState(false);
  const [alertContent, setAlertContent] = useState("");
  const [team, setTeam] = useState([]);
  const [addNewSupportFlag, setAddNewSupportFlag] = useState(false);
  const [newSupportActivity, setNewSupportActivity] = useState({});
  const [features, setFeatures] = useState([]);
  useEffect(() => {
    setPI((prev) => {
      return initialPI;
    });
    getTeam();
    getFeatures();
  }, []);

  function getFeatures() {
    fetch(
      config.apiUrl +
        "feature/all/" +
        JSON.parse(localStorage.getItem("user")).team,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          "Cache-Control": "no-cache",
          Authorization:
            "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
        },
      }
    )
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("Some error occurred!");
        }
      })
      .then((actualData) => {
        setFeatures(actualData);
      })
      .catch(function (error) {
        console.log("Some error occurred!", error);
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
        setTeam((prev) => {
          return actualData;
        });
      });
  }

  function setPIData(e) {
    const updatedPI = { pi };
    updatedPI.pi[e.target.id] = e.target.value;
    setPI({
      ...pi,
    });
  }
  function savePI() {
    fetch(config.apiUrl + "pi/", {
      method: "PUT",
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
          setAlertContent("PI details saved!");
          setTimeout(() => {
            setAlert(false);
            setAlertContent("");
          }, 1000);
          return response.json();
        }
      })
      .then((actualData) => {
        console.log("PI Updated " + actualData);
        setPI(actualData);
        localStorage.setItem("PI", JSON.stringify(actualData));
      });
  }

  function addSupportActivity() {
    setAddNewSupportFlag(true);
    setNewSupportActivity((prev) => {
      return {
        stream: "",
        type: "",
        storyPoints: "",
        allocated: "",
        sprint: "",
      };
    });
  }

  function deleteSupportActivity(idx) {
    if (idx === 0) {
      setAddNewSupportFlag(false);
      setNewSupportActivity((prev) => {
        return {
          stream: "",
          type: "",
          storyPoints: "",
          allocated: "",
          sprint: "",
        };
      });
    } else {
      fetch(config.apiUrl + "support/" + idx, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization:
            "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
        },
      })
        .then((response) => {
          if (response.ok) {
            setAlert(true);
            setAlertContent("Support Activity deleted!");
            setTimeout(() => {
              setAlert(false);
              setAlertContent("");
            }, 1000);
            return response.status;
          }
          throw new Error("Some error occurred!");
        })
        .then((actualData) => {
          const updatedPI = { ...pi };
          console.log(updatedPI.sprints);
          const updatedSprint = updatedPI.sprints.map((sprint) => {
            sprint.supportActivities = sprint.supportActivities.filter(
              (suppAct) => suppAct.id !== idx
            );
            return sprint;
          });
          setPI(updatedPI);
          console.log(updatedSprint);
        })
        .catch(function (error) {
          console.log("Some error occurred!", error);
        });
    }
  }

  function setNewSupportActivityField(id, value) {
    const currNewSuppAct = { ...newSupportActivity };
    currNewSuppAct[id] = value;
    setNewSupportActivity(currNewSuppAct);
  }

  function saveSupportAct(idx, sprint) {
    if (idx > 0) {
      console.log(idx);
      updateSupportActivity(
        pi.sprints
          .map((sprint) => sprint.supportActivities)
          .flatMap((f) => f)
          .filter((sup) => sup.id === idx)[0],
        sprint
      );
    } else {
      saveNewSupportActivity(newSupportActivity);
    }
  }

  function saveNewSupportActivity(supportActivity) {
    const suppReq = { ...supportActivity };
    suppReq["sprint"] = null;
    fetch(config.apiUrl + "support/" + supportActivity.sprint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
      body: JSON.stringify(suppReq),
    })
      .then((response) => {
        if (response.ok) {
          setAlert(true);
          setAlertContent("Support Activity added!");
          setTimeout(() => {
            setAlert(false);
            setAlertContent("");
          }, 1000);
          return response.json();
        }
        throw new Error("Some error occurred!");
      })
      .then((actualData) => {
        console.log(actualData);
        const updatedPI = { ...pi };
        const updatedSprint = updatedPI.sprints.filter(
          (sprint) => sprint.name === supportActivity.sprint
        );
        console.log(updatedSprint);
        updatedSprint[0].supportActivities.push(actualData);
        setPI(updatedPI);
        console.log(updatedSprint);
        deleteSupportActivity(0);
        setAddNewSupportFlag(false);
      })
      .catch(function (error) {
        console.log("Some error occurred!", error);
      });
  }

  function editSupportActivity(sprintId, id, field, value) {
    const updatedPI = { ...pi };
    const updatedSprint = updatedPI.sprints.filter(
      (sprint) => sprint.id === sprintId
    );
    if (field !== "sprint") {
      const updatedSupp = updatedSprint[0].supportActivities.filter(
        (sup) => sup.id === id
      )[0];
      updatedSupp[field] = value;
      updatedSprint[0].supportActivities
        .filter((sup) => sup.id !== id)
        .push(updatedSupp);
      setPI(updatedPI);
    } else {
      const updatedSupp = updatedSprint[0].supportActivities.filter(
        (sup) => sup.id === Number(id)
      )[0];
      updatedSprint[0].supportActivities =
        updatedSprint[0].supportActivities.filter((supp) => supp.id !== id);
      const newSprint = updatedPI.sprints.filter(
        (sprint) => sprint.id === Number(value)
      );
      console.log(updatedSupp);
      newSprint[0].supportActivities.push(updatedSupp);
      setPI(updatedPI);
    }
    console.log(pi);
  }

  function updateSupportActivity(suppAct, sprint) {
    const suppReq = { ...suppAct };
    fetch(config.apiUrl + "support/" + sprint, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
      body: JSON.stringify(suppReq),
    })
      .then((response) => {
        if (response.ok) {
          setAlert(true);
          setAlertContent("Support Activity updated!");
          setTimeout(() => {
            setAlert(false);
            setAlertContent("");
          }, 1000);
          return response.json();
        }
        throw new Error("Some error occurred!");
      })
      .then((actualData) => {
        console.log(actualData);
      })
      .catch(function (error) {
        console.log("Some error occurred!", error);
      });
  }

  function updateCapacity(id, value) {
    const updatedTeam = [...team];
    const updatedTeamMember = updatedTeam.filter((mbr) => mbr.id === id)[0];
    updatedTeamMember["capacity"] = value;
  }

  function updateUser(id) {
    fetch(config.apiUrl + "user/", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
      body: JSON.stringify(team.filter((mbr) => mbr.id === id)[0]),
    })
      .then((response) => {
        if (response.ok) {
          setAlert(true);
          setAlertContent("User details updated!");
          setTimeout(() => {
            setAlert(false);
            setAlertContent("");
          }, 1000);
          return response.json();
        }
      })
      .then((actualData) => {
        console.log("User details updated " + actualData);
        const updatedTeam = [...team];
        const updatedTeamMembers = updatedTeam.filter((mbr) => mbr.id !== id);
        updatedTeamMembers.push(JSON.parse(actualData));
        setTeam(updatedTeam);
      });
  }

  function calcWeekends(start, end) {
    const startDate = new Date(start);
    const endDate = new Date(end);
    var totalWeekends = 0;
    for (var i = startDate; i <= endDate; i.setDate(i.getDate() + 1)) {
      if (i.getDay() == 0 || i.getDay() == 1) totalWeekends++;
    }
    return totalWeekends;
  }
  return (
    <div>
      <div
        className={
          "team-notification" + (alert ? "" : " team-notification-hidden")
        }
      >
        <div>{alertContent}</div>
      </div>
      <div className="pi-details-container">
        <div className="flex-1">
          <input
            className="small-text"
            type="text"
            placeholder="PI-Name"
            id="name"
            onChange={(e) => setPIData(e)}
            value={pi.name}
            size="10"
          ></input>
        </div>
        <div className="flex-1">
          <input
            className="small-text"
            type="text"
            placeholder="Board Id"
            id="boardId"
            onChange={(e) => setPIData(e)}
            value={pi.boardId}
            size="10"
          ></input>
        </div>
        <div className="flex-1">
          <input
            className="small-text"
            type="text"
            id="filter"
            placeholder="filter-id"
            value={pi.filter}
            onChange={(e) => setPIData(e)}
            size="10"
          ></input>
        </div>
        <div className="flex-1">
          <div className="label-pi">Start</div>
          <input
            type="date"
            placeholder="Start"
            id="start"
            value={pi.start}
            onChange={(e) => setPIData(e)}
          ></input>
        </div>
        <div className="flex-1">
          <div className="label-pi">End</div>
          <input
            type="date"
            placeholder="End"
            id="end"
            value={pi.end}
            onChange={(e) => setPIData(e)}
          ></input>
        </div>
        <div className="flex-1">
          <input
            type="number"
            placeholder="SP/Day"
            id="spPerDay"
            value={pi.spPerDay}
            size="3"
            onChange={(e) => setPIData(e)}
          ></input>
        </div>
        <div className="flex-1">
          <select
            name="Sprint"
            id="closed"
            value={pi.closed}
            onChange={(e) => setPIData(e)}
          >
            <option value="false">Open</option>
            <option value="true">Close</option>
          </select>
        </div>
        <div className="save-btn-pi" onClick={savePI}>
          Save
        </div>
      </div>
      <div className="actual-container-pi">
        <div className="team-container-pi">
          <div className="team-heading">
            {JSON.parse(localStorage.getItem("user")).team}
          </div>
          <div className="team-member-detail">
            <div className={"team-member-pi-head "}>Name</div>
            <div className="team-member-pi-detail-head">
              <input
                type="text"
                defaultValue={"Avail."}
                size="8"
                maxLength="8"
                readOnly
              ></input>
            </div>
            <div className="percent invisible">%</div>
            <div className="team-member-pi-detail-head">
              <input
                type="text"
                defaultValue={"Leaves"}
                size="8"
                maxLength="8"
                readOnly
              ></input>
            </div>

            <div className={"btn-save invisible"}>Save</div>
          </div>
          {team
            .sort((m1, m2) => {
              if (m1.name > m2.name) return 1;
              else if (m1.name < m2.name) return -1;
              else return 0;
            })
            .filter((mbr) => mbr.role === "ROLE_Team-Member")
            .map((val, idx) => (
              <div className="team-member-detail" key={idx}>
                <div className={"team-member-pi "} id={val.username}>
                  {val.name}
                </div>
                <div className="team-member-pi-detail">
                  <input
                    type="number"
                    defaultValue={val.capacity}
                    size="3"
                    maxLength="3"
                    onChange={(e) => updateCapacity(val.id, e.target.value)}
                  ></input>
                </div>
                <div className="percent">%</div>
                <div className="team-member-pi-detail">
                  <input
                    type="number"
                    defaultValue={val.leaves.length}
                    size="3"
                    maxLength="3"
                    readOnly
                  ></input>
                </div>
                <div className={"btn-save"} onClick={() => updateUser(val.id)}>
                  Save
                </div>
              </div>
            ))}
        </div>
        <div className="miscellaneous-container">
          <div className="sprint-container">
            <div className="team-heading">Sprints</div>
            <div className="team-member-detail">
              <div className="sprint-detail-head">
                <input
                  type="text"
                  defaultValue="Sprint"
                  id="name"
                  readOnly
                  disabled
                  size="12"
                ></input>
              </div>
              <div className="sprint-detail-head">
                <input
                  type="text"
                  defaultValue="Start-Date"
                  id="start"
                  readOnly
                  disabled
                  maxLength="12"
                  size="12"
                ></input>
              </div>
              <div className="sprint-detail-head">
                <input
                  type="text"
                  defaultValue="End-Date"
                  id="end"
                  readOnly
                  disabled
                  maxLength="12"
                  size="12"
                ></input>
              </div>
            </div>
            {pi.sprints
              .sort((m1, m2) => {
                if (m1.name > m2.name) return 1;
                else if (m1.name < m2.name) return -1;
                else return 0;
              })
              .map((val, idx) => (
                <div className="team-member-detail" key={idx}>
                  <div className="sprint-detail">
                    <input
                      type="text"
                      defaultValue={val.name}
                      id="name"
                      readOnly
                      disabled
                      size="12"
                    ></input>
                  </div>
                  <div className="sprint-detail">
                    <input
                      type="text"
                      defaultValue={val.start}
                      id="start"
                      readOnly
                      disabled
                      maxLength="12"
                      size="12"
                    ></input>
                  </div>
                  <div className="sprint-detail">
                    <input
                      type="text"
                      defaultValue={val.end}
                      id="end"
                      readOnly
                      disabled
                      maxLength="12"
                      size="12"
                    ></input>
                  </div>
                </div>
              ))}
          </div>
          <div className="support-container">
            <div className="team-heading">Support Activity</div>
            <div className="team-member-detail">
              <div className="team-member-pi-detail-head">
                <input
                  type="text"
                  defaultValue={"Stream"}
                  id="stream"
                  size="8"
                  readOnly
                ></input>
              </div>
              <div className="team-member-pi-detail-head">
                <input
                  type="text"
                  defaultValue={"Type"}
                  id="type"
                  size="10"
                  readOnly
                ></input>
              </div>
              <div className="team-member-pi-detail-head">
                <input
                  type="text"
                  value={"SPs."}
                  id="storyPoints"
                  size="8"
                  maxLength="8"
                  readOnly
                ></input>
              </div>
              <div className="team-member-pi-detail-head">
                <input
                  type="text"
                  value={"Alloc."}
                  id="allocated"
                  size="8"
                  readOnly
                ></input>
              </div>{" "}
              <div className="team-member-pi-detail-head">
                <input type="text" value={"Sprint"} size="10" readOnly></input>
              </div>
              <div className={"btn-save invisible"}>Save</div>
              <div className={"btn-delete-pi invisible"}>
                <i className="fa-solid fa-trash-can"></i>
              </div>
            </div>
            {pi.sprints.map((val, idx) => {
              return val.supportActivities.map((sup, ind) => (
                <div className="team-member-detail" key={ind}>
                  <div className="team-member-pi-detail">
                    <input
                      type="text"
                      value={sup.stream}
                      id="stream"
                      size="8"
                      onChange={(e) =>
                        editSupportActivity(
                          val.id,
                          sup.id,
                          "stream",
                          e.target.value
                        )
                      }
                    ></input>
                  </div>
                  <div className="team-member-pi-detail">
                    <input
                      type="text"
                      value={sup.type}
                      id="type"
                      size="10"
                      onChange={(e) =>
                        editSupportActivity(
                          val.id,
                          sup.id,
                          "type",
                          e.target.value
                        )
                      }
                    ></input>
                  </div>
                  <div className="team-member-pi-detail">
                    <input
                      type="number"
                      value={sup.storyPoints}
                      id="storyPoints"
                      size="3"
                      maxLength="3"
                      onChange={(e) =>
                        editSupportActivity(
                          val.id,
                          sup.id,
                          "storyPoints",
                          e.target.value
                        )
                      }
                    ></input>
                  </div>
                  <div className="team-member-pi-detail">
                    <input
                      type="number"
                      value={sup.allocated}
                      id="allocated"
                      size="3"
                      onChange={(e) =>
                        editSupportActivity(
                          val.id,
                          sup.id,
                          "allocated",
                          e.target.value
                        )
                      }
                    ></input>
                  </div>{" "}
                  <div className="team-member-pi-detail">
                    <select
                      id="sprint-supp"
                      value={val.id}
                      onChange={(e) =>
                        editSupportActivity(
                          val.id,
                          sup.id,
                          "sprint",
                          e.target.value
                        )
                      }
                    >
                      {pi.sprints.map((val, idx) => (
                        <option value={val.id} key={idx}>
                          {val.name}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div
                    className={"btn-save"}
                    onClick={() => saveSupportAct(sup.id, val.name)}
                  >
                    Save
                  </div>
                  <div
                    className={"btn-delete-pi"}
                    onClick={() => deleteSupportActivity(sup.id)}
                  >
                    <i className="fa-solid fa-trash-can"></i>
                  </div>
                </div>
              ));
            })}
            <div
              className={
                "team-member-detail" +
                (addNewSupportFlag ? "" : " hide-add-sprint")
              }
            >
              <div className="team-member-detail">
                <div className="team-member-pi-detail">
                  <input
                    type="text"
                    value={newSupportActivity.stream}
                    id="stream"
                    placeholder="Stream"
                    size="8"
                    onChange={(e) =>
                      setNewSupportActivityField("stream", e.target.value)
                    }
                  ></input>
                </div>
                <div className="team-member-pi-detail">
                  <input
                    type="text"
                    value={newSupportActivity.type}
                    id="type"
                    placeholder="Type"
                    size="10"
                    onChange={(e) =>
                      setNewSupportActivityField("type", e.target.value)
                    }
                  ></input>
                </div>
                <div className="team-member-pi-detail">
                  <input
                    type="number"
                    value={newSupportActivity.storyPoints}
                    id="storyPoints"
                    placeholder="SPs"
                    size="3"
                    onChange={(e) =>
                      setNewSupportActivityField("storyPoints", e.target.value)
                    }
                  ></input>
                </div>
                <div className="team-member-pi-detail">
                  <input
                    type="number"
                    value={newSupportActivity.allocated}
                    id="allocated"
                    placeholder="Alloc %"
                    size="3"
                    maxLength="3"
                    onChange={(e) =>
                      setNewSupportActivityField("allocated", e.target.value)
                    }
                  ></input>
                </div>
                <div className="team-member-pi-detail">
                  <select
                    id="sprint"
                    value={newSupportActivity.sprint}
                    onChange={(e) =>
                      setNewSupportActivityField("sprint", e.target.value)
                    }
                  >
                    <option value=""></option>
                    {pi.sprints.map((val, idx) => (
                      <option value={val.name} key={idx}>
                        {val.name}
                      </option>
                    ))}
                  </select>
                </div>
                <div className={"btn-save"} onClick={() => saveSupportAct(0)}>
                  Save
                </div>
                <div
                  className={"btn-delete-pi"}
                  onClick={() => deleteSupportActivity(0)}
                >
                  <i className="fa-solid fa-trash-can"></i>
                </div>
              </div>
            </div>
            <div className="add-sprint-btn" onClick={addSupportActivity}>
              Add Support Activity
            </div>
          </div>
        </div>
        <div className="chart-container">
          <div className="team-heading">Stats</div>
          {/*<div className="closeds">
            <div className="flex-1 wrapped">
              <StackedPIStats></StackedPIStats>
              <StackedPIStats></StackedPIStats>
              <StackedPIStats></StackedPIStats>
              <StackedPIStats></StackedPIStats>
            </div>
                    </div>*/}
          <div className="team-member-detail">
            <div className="sprint-detail-head">
              <input
                type="text"
                defaultValue={"Sprint"}
                id="name"
                readOnly
                disabled
                size="10"
              ></input>
            </div>
            <div className="sprint-detail-head">
              <input
                type="text"
                defaultValue={"Total"}
                id="total"
                readOnly
                disabled
                maxLength="6"
                size="6"
              ></input>
            </div>
            <div className="sprint-detail-head">
              <input
                type="text"
                defaultValue={"Support"}
                id="support"
                readOnly
                disabled
                maxLength="9"
                size="9"
              ></input>
            </div>
            <div className="sprint-detail-head">
              <input
                type="text"
                defaultValue={"Leaves"}
                id="leaves"
                readOnly
                disabled
                maxLength="7"
                size="7"
              ></input>
            </div>
            <div className="sprint-detail-head">
              <input
                type="text"
                defaultValue={"Forecasted"}
                id="forecasted"
                readOnly
                disabled
                maxLength="8"
                size="8"
              ></input>
            </div>
            <div className="sprint-detail-head">
              <input
                type="text"
                defaultValue={"Planned"}
                id="planned"
                readOnly
                disabled
                maxLength="8"
                size="8"
              ></input>
            </div>
          </div>
          {pi.sprints
            .sort((m1, m2) => {
              if (m1.name > m2.name) return 1;
              else if (m1.name < m2.name) return -1;
              else return 0;
            })
            .map((sprint, idx) => {
              return (
                <div className="team-member-detail">
                  <div className="sprint-detail">
                    <input
                      type="text"
                      value={sprint.name}
                      id="name"
                      readOnly
                      disabled
                      size="10"
                    ></input>
                  </div>
                  <div className="sprint-detail">
                    <input
                      type="text"
                      value={
                        (Math.abs(
                          new Date(sprint.end) - new Date(sprint.start)
                        ) /
                          (1000 * 3600 * 24) -
                          calcWeekends(sprint.start, sprint.end)) *
                        team
                          .filter((mbr) => mbr.role === "ROLE_Team-Member")
                          .map((mbr) => mbr.capacity / 100)
                          .reduce((partialSum, a) => partialSum + a, 0)
                      }
                      id="total"
                      readOnly
                      disabled
                      maxLength="6"
                      size="6"
                    ></input>
                  </div>
                  <div className="sprint-detail">
                    <input
                      type="text"
                      value={sprint.supportActivities
                        .map((supp) => supp.storyPoints * supp.allocated)
                        .reduce((partialSum, a) => partialSum + a, 0)}
                      id="support"
                      readOnly
                      disabled
                      maxLength="9"
                      size="9"
                    ></input>
                  </div>
                  <div className="sprint-detail">
                    <input
                      type="text"
                      value={
                        team
                          .filter((mbr) => mbr.role === "ROLE_Team-Member")
                          .map((mbr) => mbr.leaves)
                          .flatMap((f) => f)
                          .filter(
                            (dt) =>
                              new Date(dt.leave) <= new Date(sprint.end) &&
                              new Date(dt.leave) >= new Date(sprint.start)
                          ).length
                      }
                      id="leaves"
                      readOnly
                      disabled
                      maxLength="7"
                      size="7"
                    ></input>
                  </div>
                  <div className="sprint-detail">
                    <input
                      type="text"
                      value={
                        ((Math.abs(
                          new Date(sprint.end) - new Date(sprint.start)
                        ) /
                          (1000 * 3600 * 24) -
                          calcWeekends(sprint.start, sprint.end)) *
                          team
                            .filter((mbr) => mbr.role === "ROLE_Team-Member")
                            .map((mbr) => mbr.capacity / 100)
                            .reduce((partialSum, a) => partialSum + a, 0) -
                          sprint.supportActivities
                            .map((supp) => supp.storyPoints * supp.allocated)
                            .reduce((partialSum, a) => partialSum + a, 0) -
                          team
                            .filter((mbr) => mbr.role === "ROLE_Team-Member")
                            .map((mbr) => mbr.leaves)
                            .flatMap((f) => f)
                            .filter(
                              (dt) =>
                                new Date(dt.leave) <= new Date(sprint.end) &&
                                new Date(dt.leave) >= new Date(sprint.start)
                            ).length) *
                        pi.spPerDay
                      }
                      id="forecasted"
                      readOnly
                      disabled
                      maxLength="8"
                      size="8"
                    ></input>
                  </div>
                  <div className="sprint-detail">
                    <input
                      type="text"
                      value={features
                        .map((feature) => feature.stories)
                        .flatMap((f) => f)
                        .filter((story) => story.sprint === sprint.name)
                        .map((story) => story.estimate)
                        .reduce((partialSum, a) => partialSum + a, 0)}
                      id="planned"
                      readOnly
                      disabled
                      maxLength="8"
                      size="8"
                    ></input>
                  </div>
                </div>
              );
            })}
        </div>
      </div>
    </div>
  );
}

export default PI;
