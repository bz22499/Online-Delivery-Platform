function createCustomer(){
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
    }else{
        alert("Form was not correctly filled in")
    }
}

function createVendor(){
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value
    let email = document.getElementById("email").value
    let checkPassword = document.getElementById("confirm-password").value
    let valid = checkPassword === password
    if(valid){
        valid = /\S+@\S+\.\S+/.test(email)
    }

    if(username.toString() === "" || password.toString() === "" || checkPassword.toString() === ""){
        valid = false
    }


    if(valid){
        // Send data to the backend
        fetch('/vendors/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username:username,password:password,email:email})
        })
            .then(response => response.json())
            .then(data => {
                // Handle the response from the backend
                window.location.href = '/home';
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }else{
        alert("Form was not correctly filled in")
    }
}