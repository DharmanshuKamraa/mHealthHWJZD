/* Project specific Javascript goes here. */
var ctx = $("#myChart").get(0).getContext("2d");
var data = {
	labels: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
	datasets: [
		{
			label: "Here",
			fillColor: "rgba(220,220,220,0.2)",
			strokeColor: "rgba(220,220,220,1)",
			pointColor: "rgba(220,220,220,1)",
			pointStrokeColor: "#fff",
			pointHighlightFill: "#fff",
			pointHighlightStroke: "rgba(220,220,220,1)",
			data: [65, 59, 80, 81, 56, 55, 40]
		},
		// {
		// 	label: "My Second dataset",
		// 	fillColor: "rgba(151,187,205,0.2)",
		// 	strokeColor: "rgba(151,187,205,1)",
		// 	pointColor: "rgba(151,187,205,1)",
		// 	pointStrokeColor: "#fff",
		// 	pointHighlightFill: "#fff",
		// 	pointHighlightStroke: "rgba(151,187,205,1)",
		// 	data: [28, 48, 40, 19, 86, 27, 90]
		// } ,
		//         {
		//             label: "My Second dataset",
		//             fillColor: "rgba(11,17,25,0.5)",
		//             strokeColor: "rgba(11,17,25,0.8)",
		//             highlightFill: "rgba(11,17,25,0.75)",
		//             highlightStroke: "rgba(11,17,25,1)",
		//             data: [38, 18, 30, 9, 16, 87, 10]
		//         } ,
	]
};


var options = {

    ///Boolean - Whether grid lines are shown across the chart
    scaleShowGridLines : true,

    //String - Colour of the grid lines
    scaleGridLineColor : "rgba(0,0,0,.05)",

    //Number - Width of the grid lines
    scaleGridLineWidth : 1,

    //Boolean - Whether to show horizontal lines (except X axis)
    scaleShowHorizontalLines: true,

    //Boolean - Whether to show vertical lines (except Y axis)
    scaleShowVerticalLines: true,

    //Boolean - Whether the line is curved between points
    bezierCurve : true,

    //Number - Tension of the bezier curve between points
    bezierCurveTension : 0.4,

    //Boolean - Whether to show a dot for each point
    pointDot : true,

    //Number - Radius of each point dot in pixels
    pointDotRadius : 4,

    //Number - Pixel width of point dot stroke
    pointDotStrokeWidth : 1,

    //Number - amount extra to add to the radius to cater for hit detection outside the drawn point
    pointHitDetectionRadius : 20,

    //Boolean - Whether to show a stroke for datasets
    datasetStroke : true,

    //Number - Pixel width of dataset stroke
    datasetStrokeWidth : 2,

    //Boolean - Whether to fill the dataset with a colour
    datasetFill : true,

    //String - A legend template
    legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].strokeColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"

};
var myLineChart = new Chart(ctx).Line(data , options);

/*
	Polar Chart Starts here.
*/
polarctx = $("#top5ItemsChart").get(0).getContext("2d")
var polarData = [
    {
        value: 300,
        color:"#F7464A",
        highlight: "#FF5A5E",
        label: "Tomatoes"
    },
    {
        value: 50,
        color: "#46BFBD",
        highlight: "#5AD3D1",
        label: "Vegetables"
    },
    {
        value: 100,
        color: "#FDB45C",
        highlight: "#FFC870",
        label: "Chicken"
    },
    {
        value: 40,
        color: "#949FB1",
        highlight: "#A8B3C5",
        label: "Dairy"
    },
    {
        value: 120,
        color: "#4D5360",
        highlight: "#616774",
        label: "Eggs"
    }

];
var polarChart  = new Chart(polarctx).PolarArea(polarData);

/*
	Bar chart starts here.
*/
var bardata = {
    labels: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
    datasets: [
        {
            label: "My First dataset",
            fillColor: "rgba(220,220,220,0.5)",
            strokeColor: "rgba(220,220,220,0.8)",
            highlightFill: "rgba(220,220,220,0.75)",
            highlightStroke: "rgba(220,220,220,1)",
            data: [65, 59, 80, 81, 56, 55, 40]
        },
        {
            label: "My Second dataset",
            fillColor: "rgba(151,187,205,0.5)",
            strokeColor: "rgba(151,187,205,0.8)",
            highlightFill: "rgba(151,187,205,0.75)",
            highlightStroke: "rgba(151,187,205,1)",
            data: [28, 48, 40, 19, 86, 27, 90]
        } ,
        // {
        //     label: "My Second dataset",
        //     fillColor: "#627",
        //     strokeColor: "#627",
        //     highlightFill: "rgba(151,187,205,0.75)",
        //     highlightStroke: "rgba(151,187,205,1)",
        //     data: [18, 48, 20, 9, 56, 17, 34]
        // } ,
        // {
        //     label: "My Second dataset",
        //     fillColor: "#350",
        //     strokeColor: "#350",
        //     highlightFill: "rgba(151,187,205,0.75)",
        //     highlightStroke: "rgba(151,187,205,1)",
        //     data: [45, 15, 21, 10, 26, 47, 70]
        // } ,
        // {
        //     label: "My Second dataset",
        //     fillColor: "#7ff",
        //     strokeColor: "#7ff",
        //     highlightFill: "rgba(151,187,205,0.75)",
        //     highlightStroke: "rgba(151,187,205,1)",
        //     data: [10, 9, 17, 15, 23, 77, 90]
        // } ,
    ]
};
barctx = $("#barChart").get(0).getContext("2d")
var myBarChart = new Chart(barctx).Bar(bardata);

/*
	Pie chart starts here
*/

var piedata = [
    {
        value: 300,
        color:"#F7464A",
        highlight: "#FF5A5E",
        label: "Hanover"
    },
    {
        value: 50,
        color: "#46BFBD",
        highlight: "#5AD3D1",
        label: "Salem"
    },
    {
        value: 100,
        color: "#FDB45C",
        highlight: "#FFC870",
        label: "Lebanon"
    }
]

piectx = $("#pieChart").get(0).getContext("2d")
var myBarChart = new Chart(piectx).Pie(piedata);
