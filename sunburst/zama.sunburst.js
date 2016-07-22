/**
 * @author Zakir Magdum.
 */
var zama = zama || {};

zama.Sunburst = function(config, data) {
    this.config = config;
    this.data = data;
    this.config.parent = this.config.parent || "body";
    this.config.width = this.config.width || 800;
    this.config.height = this.config.height || 600;
    this.config.breadCrum = this.config.breadCrum || {}
    this.config.breadCrum.width = this.config.breadCrum.width || 100;
    this.config.breadCrum.height = this.config.breadCrum.height || 20;
    this.config.breadCrum.spacing = this.config.breadCrum.spacing || 3;
    this.config.breadCrum.tail = this.config.breadCrum.tail || 10;
    this.config.colors = this.config.colors || d3.scale.category20();
    this.config.legend = this.config.legend || {};
    this.config.legend.float = this.config.legend.float || 'right';
    this.config.legend.width = this.config.legend.width || 75;
    this.config.legend.height = this.config.legend.height || 20;
    this.config.legend.spacing = this.config.legend.spacing || 3;
    this.config.legend.round = this.config.legend.round || 3;
    this.config.groups = this.config.groups || {};
    this.config.groups.float = this.config.groups.float || 'left';
    this.config.groups.width = this.config.groups.width || 120;
    this.config.groups.height = this.config.groups.height || 20;
    this.config.groups.spacing = this.config.groups.spacing || 3;
    this.config.groups.round = this.config.legend.round || 3;
    this.config.groups.enabledFill = this.config.legend.enabledFill || "#FFEE74";
    this.config.groups.disabledFill = this.config.legend.disabledFill || "#777";
    this.config.groups.round = this.config.legend.round || 3;
    this.config.formatValue = this.config.formatValue || function(d) {return d.toFixed(1);};
    this.config.arcLabelIdPrefix = this.config.arcLabelIdPrefix || Math.floor((Math.random() * 100) + 1);
    this.config.data.hideLeaves = this.config.data.hideLeaves || false;
    this.config.data.idColumn = this.config.data.idColumn || 'id';
    this.config.data.labelThreshold = this.config.data.labelThreshold || 0.3;
    this.groupings = data.groupings;
    this.hdata = this.buildHierarchy(data, config.groupings);
}

zama.Sunburst.prototype.init = function() {
    d3.select(this.config.parent).selectAll("*").remove();
    this.rootSvg = d3.select(this.config.parent).append("svg:svg")
        .attr("width", this.config.width)
        .attr("height", this.config.height);
    this.breadCrumGroup = this.rootSvg.append("svg:g");

    var bcHeight = this.config.breadCrum.height + 3*this.config.breadCrum.spacing;
    var vcHeight = this.config.height - (bcHeight);

    this.vis = this.rootSvg.append("svg:g")
        .attr("width", this.config.width)
        .attr("height", vcHeight)
        .attr("transform", "translate(0," + bcHeight + ")");

    this.graph = this.vis.append("svg:g")
        .attr("transform", "translate(" + this.config.width / 2 + "," + vcHeight / 2 + ")");
    this.radius = Math.min(this.config.width, vcHeight) / 2;
    this.partition = d3.layout.partition()
        .size([2 * Math.PI, this.radius * this.radius])
        .value(function(d) { return d.size; })
        .children(function (d) {return d.values;});
    this.arc = d3.svg.arc()
        .startAngle(function(d) { return d.x; })
        .endAngle(function(d) { return d.x + d.dx; })
        .innerRadius(function(d) { return Math.sqrt(d.y); })
        .outerRadius(function(d) { return Math.sqrt(d.y + d.dy); });
    this.labelArc = d3.svg.arc()
        .startAngle(function(d) { return d.x; })
        .endAngle(function(d) { return d.x + d.dx; })
        .innerRadius(function(d) { return Math.sqrt(d.y+d.dy/2)-6;})
        .outerRadius(function(d) { return Math.sqrt(d.y+d.dy/2)-6;});
}

zama.Sunburst.prototype.render = function() {
    var me = this;
    me.init();
    me.graph.append("svg:circle")
        .attr("r", this.radius)
        .style("opacity", 0);
    var nodes = this.partition.nodes(this.hdata)
        .filter(function(d) {
            return (d.dx > 0.000001); // 0.005 radians = 0.29 degrees
        });
    var legendColors = {};
    nodes.forEach(function(d) {
        d.label = "";
        if (!d.name) {
            legendColors[d.key] = me.config.colors(d.key);
            d.label = d.dx > me.config.data.labelThreshold ? d.key + ' ('+d.value.toFixed(0)+')' : "";
        }
        d.name = d.key
    })

    if (me.config.data.hideLeaves) {
        nodes = nodes.filter(function (d) { return d[me.config.data.idColumn] === undefined})
    }
    
    drawLegend(legendColors, me.config.groupings);

    var g = me.graph.data([this.hdata]).selectAll("path")
        .data(nodes)
        .enter().append("g");

    var path = g.append("svg:path")
        .attr("display", function(d) { return d.depth ? null : "none"; })
        .attr("d", me.arc)
        .attr("fill-rule", "evenodd")
        .style("fill", function(d) { return me.config.colors(d.key); })
        .style("stroke", "#fff")
        .style("opacity", 1)
        .on("mouseover", mouseover);

    var lp = g.append('path')
        .attr('fill', 'none')
        .attr('stroke', 'none')
        .attr('d', me.labelArc)
        .attr('id', function (d, i, j) {
            return 'arc-label' + i + '-' + j;
        });

    var label = g.append('text')
        .attr('text-anchor', 'middle');

    label.append('textPath')
        .attr('startOffset', '25%')
        .attr('xlink:href', function(d, i, j) {
            return '#arc-label' + i + '-' + j;
        })
        .style('fill', '#000')
        .text(function(d) {
            return d.label;
        });

    me.graph.on("mouseleave", mouseleave);

    // Get total size of the tree = value of root node from partition.
    me.totalSize = path.node().__data__.value;

    me.centerText = me.graph.append("svg:g").append("text")
        .attr("x", 0)
        .attr("y", 0)
        .attr('text-anchor', 'middle')
        .style("font-size", "2.5em")
        .text(me.config.formatValue(me.totalSize));

    function mouseover(d) {
       var val = me.config.formatValue(d.value);
        me.centerText.text(val);
        me.breadCrumGroup.style("visibility", "");
        var sequenceArray = getAncestors(d);
        updateBreadcrumbs(sequenceArray, val);
        // Fade all the segments.
        d3.selectAll("path").style("opacity", 0.3);

        // Then highlight only those that are an ancestor of the current segment.
        me.vis.selectAll("path")
            .filter(function(node) {
                return (sequenceArray.indexOf(node) >= 0);
            })
            .style("opacity", 1);
    }

// Restore everything to full opacity when moving off the visualization.
    function mouseleave(d) {
        // Hide the breadcrumb trail
        me.breadCrumGroup.style("visibility", "hidden");
        // Deactivate all segments during transition.
        d3.selectAll("path").on("mouseover", null);
        // Transition each segment to full opacity and then reactivate it.
        d3.selectAll("path")
            .transition()
            .duration(1000)
            .style("opacity", 1)
            .each("end", function() {
                d3.select(this).on("mouseover", mouseover);
            });
        me.centerText.text(me.config.formatValue(me.totalSize));
    }

    function getAncestors(node) {
        var path = [];
        var current = node;
        while (current.parent) {
            path.unshift(current);
            current = current.parent;
        }
        return path;
    }

    function breadcrumbPoints(d, i) {
        var points = [];
        points.push("0,0");
        points.push(me.config.breadCrum.width + ",0");
        points.push(me.config.breadCrum.width + me.config.breadCrum.tail + "," + (me.config.breadCrum.height / 2));
        points.push(me.config.breadCrum.width + "," + me.config.breadCrum.height);
        points.push("0," + me.config.breadCrum.height);
        if (i > 0) { // Leftmost breadcrumb; don't include 6th vertex.
            points.push(me.config.breadCrum.tail + "," + (me.config.breadCrum.height / 2));
        }
        return points.join(" ");
    }

// Update the breadcrumb trail to show the current sequence and percentage.
    function updateBreadcrumbs(nodeArray, valueString) {
        // Data join; key function combines name and depth (= position in sequence).
        var g = me.breadCrumGroup
            .selectAll("g")
            .data(nodeArray, function(d) { return d.name + d.depth; });

        // Add breadcrumb and label for entering nodes.
        var entering = g.enter().append("svg:g");

        entering.append("svg:polygon")
            .attr("points", breadcrumbPoints)
            .style("fill", function(d) { return me.config.colors[d.name]; });

        entering.append("svg:text")
            .attr("x", (me.config.breadCrum.width + me.config.breadCrum.tail) / 2)
            .attr("y", me.config.breadCrum.height / 2)
            .attr("dy", "0.35em")
            .attr("text-anchor", "middle")
            .style("font-weight", 600)
            .style("fill", "#fff")
            .text(function(d) { return d.name; });

        // Set position for entering and updating nodes.
        g.attr("transform", function(d, i) {
            return "translate(" + i * (me.config.breadCrum.width + me.config.breadCrum.spacing) + ", 0)";
        });

        // Remove exiting nodes.
        g.exit().remove();
    }

    function drawLegend(legendColors, groupings) {
        var x = me.config.legend.float === 'right' ? me.config.width - me.config.legend.width : 0;

        var legend = me.vis.append("g").attr("transform", function(d,i) {return "translate(" + x + ",0)";});

        var g = legend.selectAll("g")
            .data(d3.entries(legendColors))
            .enter().append("svg:g")
            .attr("transform", function(d, i) {
                return "translate(0," + i * (me.config.legend.height + me.config.legend.spacing) + ")";
            });

        g.append("svg:rect")
            .attr("rx", me.config.legend.round)
            .attr("ry", me.config.legend.round)
            .attr("width", me.config.legend.width)
            .attr("height", me.config.legend.height)
            .style("fill", function(d) { return d.value; });

        g.append("svg:text")
            .attr("x", me.config.legend.width / 2)
            .attr("y", me.config.legend.height / 2)
            .attr("dy", "0.35em")
            .attr("text-anchor", "middle")
            .text(function(d) { return d.key; });

        var groupings = me.vis.append("g");
        g = groupings.selectAll("g")
            .data(d3.entries(me.config.groupings))
            .enter().append("svg:g")
            .attr("transform", function (d, i) {
                return "translate(0," + i * (me.config.groups.height + me.config.groups.spacing) + ")";
            });
        g.append("svg:rect")
            .attr("rx", me.config.groups.round)
            .attr("ry", me.config.groups.round)
            .attr("width", me.config.groups.width)
            .attr("height", me.config.groups.height)
            .style("fill", function(d) {return d.value.enabled ? me.config.groups.enabledFill : me.config.groups.disabledFill;})
            .on("click", function(d) {
                    console.log("clicked rectangle", d, d3.event.shiftKey, d3.event.altKey);
                    var oldIndex = d.key;
                    var newIndex = oldIndex;
                    if (d3.event.shiftKey && newIndex > 0) {
                        newIndex--;
                    } else if (d3.event.altKey) {
                        me.config.groupings[oldIndex].enabled = !me.config.groupings[oldIndex].enabled;
                        me.hdata = me.buildHierarchy(me.data, me.config.groupings);
                        me.render();
                    } else if (newIndex < (me.config.groupings.length-1)) {
                        newIndex++;
                    }
                    if (oldIndex != newIndex) {
                        var item = me.config.groupings[oldIndex];
                        me.config.groupings.splice(oldIndex, 1);
                        me.config.groupings.splice(newIndex, 0, item);
                        me.hdata = me.buildHierarchy(me.data, me.config.groupings);
                        me.render();
                    }
                });

        g.append("svg:text")
            .attr("x", 10)
            .attr("y", me.config.groups.height / 2)
            .attr("dy", "0.35em")
            .attr("text-anchor", "left")
            .text(function(d) { return d.value.name; });
        var start = (me.config.groupings.length+1) * (me.config.groups.height + me.config.groups.spacing);
        groupings.append("svg:text")
            .attr("text-anchor", "left")
            .style("font-size", 10)
            .append("svg:tspan")
            .attr("x", 10)
            .attr("y", start)
            .text("Click -- move down")
            .append("svg:tspan")
            .attr("x", 10)
            .attr("y", start+20)
            .text("Cmd+Click -- move up")
            .append("svg:tspan")
            .attr("x", 10)
            .attr("y", start+40)
            .text("Alt-Click -- Enable/Disable")
        start += 60;
        g = groupings.append("svg:g")
            .attr("transform", function (d, i) {
                return "translate(0," + start + ")";
            });
        g.append("svg:rect")
            .attr("rx", me.config.groups.round)
            .attr("ry", me.config.groups.round)
            .attr("width", me.config.groups.width)
            .attr("height", me.config.groups.height)
            .style("fill", "#7ff")
            .on("click", function(d) {
                me.config.data.hideLeaves = !me.config.data.hideLeaves;
                me.hdata = me.buildHierarchy(me.data, me.config.groupings);
                me.render();
            });

        g.append("svg:text")
            .attr("x", 10)
            .attr("y", me.config.groups.height / 2)
            .attr("dy", "0.35em")
            .attr("text-anchor", "left")
            .text(me.config.data.hideLeaves ? "Show Leaf" : "Hide Leaf");

    }
}

zama.Sunburst.prototype.buildHierarchy = function(data, groupings) {
    data.forEach(function(d) {
        groupings.forEach(function (grp) {
            d[grp.key] = d[grp.key] || 'Unknown ' + grp.name;
        })
    });
    var nester = d3.nest();
    groupings.filter(function (d) {return d.enabled}).forEach(function (k) {
        nester.key(function (d) {
            return d[k.key];
        })
    })
    var result = nester.entries(data);
    return {"name": "all", "values": result};
}