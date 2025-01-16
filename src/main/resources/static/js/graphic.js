var categoryLabels = categories.map(function(category) {
    return category;
});

var ctx = document.getElementById('myDoughnutChart').getContext('2d');

var myDoughnutChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
        labels: categoryLabels,  
        datasets: [{
            label: 'Number of Questions',
            data: questionCounts,  
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)', 
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)', 
                'rgba(75, 192, 192, 0.2)', 
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 99, 71, 0.2)', 
                'rgba(0, 255, 255, 0.2)', 
                'rgba(255, 165, 0, 0.2)', 
                'rgba(34, 193, 195, 0.2)', 
                'rgba(253, 187, 45, 0.2)', 
                'rgba(255, 20, 147, 0.2)', 
                'rgba(255, 105, 180, 0.2)', 
                'rgba(60, 179, 113, 0.2)', 
                'rgba(255, 215, 0, 0.2)', 
                'rgba(238, 130, 238, 0.2)', 
                'rgba(72, 61, 139, 0.2)', 
                'rgba(255, 69, 0, 0.2)' 
            ], 
            borderColor: [
                'rgba(255, 99, 132, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 99, 71, 1)', 
                'rgba(0, 255, 255, 1)', 
                'rgba(255, 165, 0, 1)', 
                'rgba(34, 193, 195, 1)', 
                'rgba(253, 187, 45, 1)', 
                'rgba(255, 20, 147, 1)', 
                'rgba(255, 105, 180, 1)', 
                'rgba(60, 179, 113, 1)', 
                'rgba(255, 215, 0, 1)', 
                'rgba(238, 130, 238, 1)', 
                'rgba(72, 61, 139, 1)', 
                'rgba(255, 69, 0, 1)' 
            ],
            borderWidth: 1
        }]
    },
	options: {
	    responsive: true,
	    maintainAspectRatio: false,
	    plugins: {
	        legend: {
	            position: 'top',
	        },
	        tooltip: {
	            callbacks: {
					title: function() {
					    return ''; 
					},
	                label: function(tooltipItem) {
	                    return tooltipItem.label + ': ' + tooltipItem.raw; 
	                }
	            }
	        }
	    }
	}
});
