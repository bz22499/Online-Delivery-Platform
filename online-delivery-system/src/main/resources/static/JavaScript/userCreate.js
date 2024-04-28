async function createCustomer(){
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let password = document.getElementById("password").value
    let email = document.getElementById("email").value
    let checkPassword = document.getElementById("confirm-password").value

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

    if(valid){
        // Send data to the backend
        fetch('/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({firstName: firstName, lastName: lastName, password:password,email:email})
        })
            .then(response => response.json())
            .then(data => {
                // Authentication
                authenticateUser(email, password, false, false);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}

async function createVendor(){
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
            alert("Email address already in use");
        }
    }else{
        alert("Form was not correctly filled in.")
    }


    if(valid){
        // Send data to the backend
        fetch('/vendors', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name:name,password:password,email:email, rating: null, imageUrl: null,
                description: null})
        })
            .then(response => response.json())
            .then(data => {
                // Authentication
                authenticateUser(email, password, true, false)
            })
            .catch(error => {
                console.error('Error:', error);
            });
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
        alert('registration complete')
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
                    window.location.href = '/vendoradditems';
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