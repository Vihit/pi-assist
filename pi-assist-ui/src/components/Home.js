import { useEffect, useState } from "react";
import { config } from "./config";
import FeatureItem from "./FeatureItem";
import "./Home.css";

function Home() {
  const [plannedToggle, setPlannedToggle] = useState(false);
  const [toBePlannedToggle, setToBePlannedToggle] = useState(false);
  const [features, setFeatures] = useState([]);
  const [featureInEditor, setFeatureInEditor] = useState({
    summary: "",
    acceptanceCriteria: "",
    description: "",
    labels: "",
  });
  const [showFeatureEditor, setShowFeatureEditor] = useState(false);
  const [alert, setAlert] = useState(false);
  const [alertContent, setAlertContent] = useState("");

  useEffect(() => {
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
          throw new Error("No Features found");
        }
      })
      .then((actualData) => {
        setFeatures(actualData);
      })
      .catch(function (error) {
        console.log("Some error occurred!", error);
      });
  }, []);

  function togglePlanned() {
    setPlannedToggle(!plannedToggle);
  }

  function toggleToBePlanned() {
    setToBePlannedToggle(!toBePlannedToggle);
  }

  function updateFeatureInEditor(id, value) {
    const fIE = { ...featureInEditor };
    fIE[id] = value;
    setFeatureInEditor(fIE);
  }

  function cancelFeatureEdit() {
    setShowFeatureEditor(false);
    const emptyFeature = {
      summary: "",
      acceptanceCriteria: "",
      description: "",
      labels: "",
    };
    setFeatureInEditor((prev) => emptyFeature);
  }

  function saveFeatureEdit() {
    featureInEditor["team"] = JSON.parse(localStorage.getItem("user")).team;
    featureInEditor["pi"] = JSON.parse(localStorage.getItem("PI")).id;
    console.log(featureInEditor);
    fetch(config.apiUrl + "feature/", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
      body: JSON.stringify(featureInEditor),
    })
      .then((response) => {
        if (response.ok) {
          setAlert(true);
          setAlertContent("Feature added!");
          const timeId = setTimeout(() => {
            setAlert(false);
            setAlertContent("");
          }, 1000);
          return response.json();
        }
        throw new Error("Some error occurred!");
      })
      .then((actualData) => {
        console.log(actualData);
        const staleFeatures = features;
        staleFeatures.push(actualData);
        setFeatures((prev) => staleFeatures);
        cancelFeatureEdit();
      })
      .catch(function (error) {
        console.log("Some error occurred!", error);
      });
  }

  return (
    <div className="home-container">
      <div
        className={
          "team-notification" + (alert ? "" : " team-notification-hidden")
        }
      >
        <div>{alertContent}</div>
      </div>
      <div className="flex-44">
        <div
          className={
            "create-feature-btn " +
            (JSON.parse(localStorage.getItem("user"))["role"] ===
            "ROLE_Product-Owner"
              ? " "
              : " hide-save-leaves")
          }
          onClick={() => setShowFeatureEditor(true)}
        >
          Add Feature
        </div>
      </div>
      <div
        className={"feature-edit " + (showFeatureEditor ? "" : "close-flex ")}
      >
        <div className="story-edit-header">
          <div className="flex-row-title">
            <div className="add-feature">
              <i className="fa-solid fa-add add-feature-icon"></i>
            </div>
            <div className="add-feature-head">New Feature</div>
          </div>
          <div className="story-edit-input">
            <div className="add-feature-label">Title</div>
            <div className="story-edit-ta">
              <textarea
                value={featureInEditor.summary}
                onChange={(e) =>
                  updateFeatureInEditor("summary", e.target.value)
                }
                rows="1"
              ></textarea>
            </div>
          </div>
          <div className="story-edit-input">
            <div className="add-feature-label">Labels</div>
            <div className="story-edit-ta">
              <textarea
                value={featureInEditor.labels}
                onChange={(e) =>
                  updateFeatureInEditor("labels", e.target.value)
                }
                rows="1"
              ></textarea>
            </div>
          </div>
          <div className="story-edit-input">
            <div className="add-feature-label">Description</div>
            <div className="story-edit-ta">
              <textarea
                value={featureInEditor.description}
                onChange={(e) =>
                  updateFeatureInEditor("description", e.target.value)
                }
                rows="5"
              ></textarea>
            </div>
          </div>
          <div className="story-edit-input">
            <div className="add-feature-label">Acceptance Criteria</div>
            <div className="story-edit-ta">
              <textarea
                value={featureInEditor.acceptanceCriteria}
                onChange={(e) =>
                  updateFeatureInEditor("acceptanceCriteria", e.target.value)
                }
                rows="5"
              ></textarea>
            </div>
          </div>
          <div className="flex-row-title">
            <div className="btn-save" onClick={saveFeatureEdit}>
              Save
            </div>
            <div className="btn-cancel" onClick={cancelFeatureEdit}>
              Cancel
            </div>
          </div>
        </div>
      </div>
      <div className="item-header">
        <div
          className="to-do-icon"
          style={{ backgroundColor: "#0052CC" }}
        ></div>
        <div className="item-name">To be Planned</div>
        <div className="drawer">
          <div
            className={
              "drawer-icon " + (toBePlannedToggle ? "animate-drawer" : "")
            }
            style={{ backgroundColor: "#0052CC" }}
          >
            <i
              className="fa-solid fa-circle-chevron-down"
              onClick={toggleToBePlanned}
            ></i>
          </div>
        </div>
      </div>
      <div
        className={"feature-container " + (toBePlannedToggle ? "closed" : "")}
        style={{ borderColor: "#0052CC" }}
      >
        {features.map((val, idx) => {
          if (val.isPlanned === "N")
            return (
              <FeatureItem
                key={val.id}
                feature={val}
                featureNo={val.id}
                featureHeading={val.summary}
                estimate={val.estimate}
                labels={val.labels}
                planningAssignee={val.planningAssignee.charAt(0).toUpperCase()}
                comments={val.discussion}
              ></FeatureItem>
            );
        })}
      </div>
      <div className="item-header">
        <div
          className="to-do-icon"
          style={{ backgroundColor: "#00CC83" }}
        ></div>
        <div className="item-name">Planned</div>
        <div className="drawer">
          <div
            className={"drawer-icon " + (plannedToggle ? "animate-drawer" : "")}
            style={{ backgroundColor: "#00CC83" }}
          >
            <i
              className="fa-solid fa-circle-chevron-down"
              onClick={togglePlanned}
            ></i>
          </div>
        </div>
      </div>
      <div
        className={"feature-container " + (plannedToggle ? "closed" : "")}
        style={{ borderColor: "#00CC83" }}
      >
        {features.map((val, idx) => {
          if (val.isPlanned === "Y")
            return (
              <FeatureItem
                key={val.id}
                feature={val}
                featureNo={val.id}
                featureHeading={val.summary}
                estimate={val.estimate}
                labels={val.labels}
                planningAssignee={val.planningAssignee.charAt(0).toUpperCase()}
                comments={val.discussion}
              ></FeatureItem>
            );
        })}
      </div>
    </div>
  );
}

export default Home;
