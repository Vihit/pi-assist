import "./FeatureSection.css";

function FeatureSection() {
  return (
    <div>
      <div className="item-header">
        <div className="to-do-icon"></div>
        <div className="item-name">To be planned</div>
        <div className="drawer">
          <div className="drawer-icon">
            <i className="fa-solid fa-circle-chevron-down"></i>
          </div>
        </div>
      </div>
      <div className="feature-container"></div>
    </div>
  );
}

export default FeatureSection;
