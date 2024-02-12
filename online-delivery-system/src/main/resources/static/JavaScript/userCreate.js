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
                // Handle the response from the backend
                window.location.href = '/home';
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
            alert("email address already in use");
        }
    }else{
        alert("Form was not correctly filled in")
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
                // Handle the response from the backend
                window.location.href = '/home';
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
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