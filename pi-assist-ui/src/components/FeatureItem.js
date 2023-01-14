import { useEffect, useState } from "react";
import { Link, useHistory } from "react-router-dom";
import "./FeatureItem.css";

function FeatureItem(props) {
  const [actionItem, setActionItem] = useState([]);
  let history = useHistory();

  const routeChange = () => {
    let path = `/feature`;
    history.push(path, props.feature);
  };

  useEffect(() => {
    let actionItemsFromComments = props.comments.match(/\*\*\*(.*)\*\*\*/g);
    if (actionItemsFromComments != null && actionItemsFromComments.length > 0) {
      setActionItem(
        actionItemsFromComments.map((str) => str.replaceAll("*", ""))
      );
    }
  }, []);

  return (
    <div>
      <div className="feature-item-background" onClick={routeChange}>
        <div className="feature-icon">
          <i className="fa-solid fa-plus"></i>
        </div>
        <div className="feature-link">
          <Link
            to={{
              pathname:
                "https://dbatlas.db.com/jira02/browse/" + props.featureNo,
            }}
            target="_blank"
          >
            {props.featureNo}
          </Link>
        </div>
        <div className="feature-heading">{props.featureHeading}</div>
        <div className="feature-details">
          <div
            className={
              "feature-comment" + (actionItem.length > 0 ? " " : " invisible")
            }
          >
            <i className="fa-solid fa-comment-dots"></i>
            <div className="comment">
              <ul>
                {actionItem.map((val, idx) => {
                  return <li style={{ textAlign: "left" }}>{val}</li>;
                })}
              </ul>
            </div>
          </div>
          <div className="feature-assignee">{props.planningAssignee}</div>
          <div className="feature-points">{props.estimate}</div>
        </div>
      </div>
    </div>
  );
}

export default FeatureItem;
