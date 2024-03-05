function isValidPostcode(postcode) {
    // Regular expression for UK postcodes
    var postcodeRegex = /^[A-Z]{1,2}[0-9R][0-9A-Z]? ?[0-9][A-Z]{2}$/i;
    return postcodeRegex.test(postcode);
}

function calculateDistance() {
    var postcode1 = document.getElementById("postcode1").value.trim();
    var postcode2 = document.getElementById("postcode2").value.trim();

    // Make sure both postcodes are provided
    if (!postcode1 || !postcode2) {
        alert("Please enter both postcodes.");
        return;
    }

    // Validate postcodes
    if (!isValidPostcode(postcode1) || !isValidPostcode(postcode2)) {
        alert("Please enter valid UK postcodes.");
        return;
    }

    var requestBody = {
        postcode1: postcode1,
        postcode2: postcode2
    };

    // Make API request to calculate distance
    fetch(`/calculate-distance`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById("result").innerText = "Distance between " + postcode1 + " and " + postcode2 + ": " + data + " meters";
        })
        .catch(error => {
            console.error("Error:", error);
            document.getElementById("result").innerText = "Error calculating distance.";
        });
}