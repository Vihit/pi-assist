import * as d3 from "d3";
import { useEffect } from "react";

function StackedPIStats() {
  var groups = [
    {
      name: "Sprint1",
      values: [60, 5, 10, 36, 20],
    },
  ];

  const id = parseInt((Math.random() * 100) | 0);

  useEffect(() => {
    // set up some basic infomation about the chart, height, width and margins
    const width = 125;
    const height = 250;
    const margin = { top: 10, left: 10, bottom: 20, right: 10 };
    const plotHeight = height - (margin.top + margin.bottom);
    const plotWidth = width - (margin.left + margin.right);
    const groupPadding = 1.3; // the gap between each group as a proportion of a single bar

    // we need to know how much horizontal space each group needs
    // this depends on the number of values for that group
    // we also need to know where each group starts int he final chart layout so
    // cumulatively add to their starting point via the currentWidth variable
    // in terms of our 'bar' unit
    let currentWidth = 0;
    groups = groups.map((group) => {
      group.width = group.values.length;
      group.startPosition = currentWidth;
      currentWidth += group.width + groupPadding;
      return group;
    });

    // work out the width needed in terms of bars
    // the padding times the number of gaps + the total number of bars
    const dataXDomain = [
      0,
      (groups.length - 1) * groupPadding + d3.sum(groups, (d) => d.width),
    ];

    // make a scale for the values,
    // hard coding it here, but in reality you probably want the domain to relect
    // the values in the data so that if the data changes so does the scale
    const yScale = d3.scaleLinear().domain([0, 60]).range([0, plotHeight]);

    // ... and for the bars
    const xScale = d3.scaleLinear().domain(dataXDomain).range([0, plotWidth]);

    // Now we're ready to draw the chart!

    //get the SVG and add a group, offset by our defined margins
    const chart = d3
      .select("#chart" + id)
      .append("g")
      .attr(`transform`, `translate(${margin.left},${margin.top})`);

    // for each group in the data add a group on the chart
    const barGroups = chart
      .selectAll("g.bar-group")
      .data(groups)
      .enter()
      .append("g")
      .classed("bar-group", true)
      .attr(
        "transform",
        (group) => `translate(${xScale(group.startPosition)}, 0)`
      ); // postion each bargroup according to its calculated start position and our x-scale

    // for each group...
    barGroups.each(function (group) {
      const barGroup = d3.select(this);

      // add and position a rectangle for each value
      barGroup
        .selectAll("rect")
        .data(group.values)
        .enter()
        .append("rect")
        .attr("width", xScale(1))
        .attr("height", (value) => yScale(value))
        .attr("x", (value, i) => xScale(i))
        .attr("y", (value) => plotHeight - yScale(value))
        .attr("fill", (value, i) => {
          if (i === 0) return d3.schemeSpectral[6][2];
          else if (i === 1) return d3.schemeSpectral[6][1];
          else if (i === 2) return d3.schemeSpectral[6][0];
          else if (i === 3) return d3.schemeSpectral[6][5];
          else if (i === 4) return d3.schemeSpectral[6][4];
        })
        .attr("stroke", (value, i) => {
          if (i === 0) return d3.schemeSpectral[6][2];
          else if (i === 1) return d3.schemeSpectral[6][1];
          else if (i === 2) return d3.schemeSpectral[6][0];
          else if (i === 3) return d3.schemeSpectral[6][5];
          else if (i === 4) return d3.schemeSpectral[6][4];
        });

      //add some labels for the group
      barGroup
        .append("line")
        .attr("x1", 0)
        .attr("x2", xScale(group.width))
        .attr("y1", plotHeight)
        .attr("y2", plotHeight)
        .attr("stroke-width", 2)
        .attr("stroke", "black");

      barGroup
        .append("text")
        .text((group) => group.name)
        .attr("transform", `translate(0,${height - margin.bottom + 4})`);
    });
  });
  return (
    <div>
      <svg id={"chart" + id} width="125" height="250"></svg>
    </div>
  );
}

export default StackedPIStats;
