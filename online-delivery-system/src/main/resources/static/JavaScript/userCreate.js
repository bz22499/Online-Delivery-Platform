function isValidPostcode(postcode) {
    // Regular expression for UK postcodes
    var postcodeRegex = /^[A-Z]{1,2}[0-9R][0-9A-Z]? ?[0-9][A-Z]{2}$/i;
    return postcodeRegex.test(postcode);
}

async function createCustomer(){
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let password = document.getElementById("password").value
    let email = document.getElementById("email").value
    let street = document.getElementById("street").value;
    let city = document.getElementById("city").value;
    let postCode = document.getElementById("postCode").value;
    let country = document.getElementById("country").value;
    let checkPassword = document.getElementById("confirm-password").value

    // Validate address fields
    if (!firstName || !lastName || !password || !email || !street || !city || !postCode || !country) {
        alert("Please fill in all fields.");
        return;
    }

    // Validate postcode
    if (!isValidPostcode(postCode)) {
        alert("Please enter a valid UK postcode.");
        return;
    }

    let valid = checkPassword === password

    if(valid){
        valid = /\S+@\S+\.\S+/.test(email)
    }

    if(firstName.toString() === "" || lastName.toString() === "" || password.toString() === "" || checkPassword.toString() === ""){
        valid = false
    }

    if(valid){
        valid = await checkEmailAddressNotUsed(email);
        if(!valid){
            alert("email address already in use");
        }
    }else{
        alert("Form was not correctly filled in")
    }

    try {
        // create the user
        const userResponse = await fetch('/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                firstName: firstName,
                lastName: lastName,
                password: password,
                email: email
            })
        });
        const userData = await userResponse.json();

        // create the address associated with the user
        const addressResponse = await fetch('/addresses', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                user: { email: email },
                street: street,
                city: city,
                postCode: postCode,
                country: country
            })
        });
        const addressData = await addressResponse.json();

        // Once both user and address are created, authenticate the user
        authenticateUser(email, password, false, false);
    } catch (error) {
        console.error('Error:', error);
    }
}

async function createVendor(){
    let name = document.getElementById("username").value;
    let password = document.getElementById("password").value
    let email = document.getElementById("email").value
    let street = document.getElementById("street").value;
    let city = document.getElementById("city").value;
    let postCode = document.getElementById("postCode").value;
    let country = document.getElementById("country").value;
    let checkPassword = document.getElementById("confirm-password").value

    // Validate address fields
    if (!name || !password || !email || !street || !city || !postCode || !country) {
        alert("Please fill in all fields.");
        return;
    }

    // Validate postcode
    if (!isValidPostcode(postCode)) {
        alert("Please enter a valid UK postcode.");
        return;
    }

    let valid = checkPassword === password
    if(valid){
        valid = /\S+@\S+\.\S+/.test(email)
    }

    if(name.toString() === "" || password.toString() === "" || checkPassword.toString() === ""){
        valid = false
    }


    if(valid){
        valid = await checkEmailAddressNotUsed(email);
        if(!valid){
            alert("email address already in use");
        }
    }else{
        alert("Form was not correctly filled in")
    }

    try {
        // create the vendor
        const userResponse = await fetch('/vendors', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: name,
                password: password,
                email: email
            })
        });
        const vendorData = await userResponse.json();

        // create the address associated with the user
        const addressResponse = await fetch('/vendorAddresses', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                vendor: { email: email },
                street: street,
                city: city,
                postCode: postCode,
                country: country
            })
        });
        const vendorAddressData = await addressResponse.json();

        // Once both user and address are created, authenticate the user
        authenticateUser(email, password, true, false);
    } catch (error) {
        console.error('Error:', error);
    }
}

async function createDriver(){
    let name = document.getElementById("username").value;
    let password = document.getElementById("password").value
    let email = document.getElementById("email").value
    let checkPassword = document.getElementById("confirm-password").value

    let valid = checkPassword === password

    if(valid){
        valid = /\S+@\S+\.\S+/.test(email)
    }

    if(name.toString() === "" || password.toString() === "" || checkPassword.toString() === ""){
        valid = false
    }

    if(valid){
        valid = await checkEmailAddressNotUsed(email);
        if(!valid){
            alert("email address already in use");
        }
    } else {
        alert("Form was not correctly filled in")
    }

    if(valid){
        // Send data to the backend
        fetch('/drivers', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({name:name, password:password,email:email})
        })
            .then(response => response.json())
            .then(data => {
                // Authentication
                authenticateUser(email, password, false, true);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}

function authenticateUser(email, password, isVendor, isDriver) {
    fetch('/login?username=' + encodeURIComponent(email) + '&password=' + encodeURIComponent(password), {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({})
    })
        .then(response => {
            if(response.ok) {
                // Redirect the user after successful authentication
                if(isVendor) {
                    window.location.href = '/vendor';
                } else if (isDriver) {
                    window.location.href = '/driverMain';
                } else {
                    window.location.href = '/home';
                }
            } else {
                // Handle authentication failure (will never happen lets be honest)
                alert("Authentication failed. Please try again.");
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

async function checkEmailAddressNotUsed(email) {
    let valid = true;

    try {
        const response1 = await fetch('users/' + email);
        if (!response1.ok) {
            if (response1.status !== 404) {
                throw new Error('Network response was not ok');
            }
        } else {
            valid = false;
        }

        const response2 = await fetch('vendors/' + email);
        if (!response2.ok) {
            if (response2.status !== 404) {
                throw new Error('Network response was not ok');
            }
        } else {
            valid = false;
        }

        // TODO: Once we have driver controller, check email address for drivers also!!!

        //if it gets here it means all fetch requests had response status not found hence email address is not used
        return valid;
    } catch (error) {
        console.error('Error:', error);
        return valid;
    }
}