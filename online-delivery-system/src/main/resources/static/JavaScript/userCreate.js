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
    }
}else{
    alert("Form was not correctly filled in")
}

function createVendor(){
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

function checkEmailAddressNotUsed(email){
    fetch('/users'+email)
        .then(response => {
            // Check if the response is successful
            if (!response.ok) {
                // Check if the response status is NOT_FOUND (404)
                if (response.status === 404) {
                    //user does not exist so check next
                } else {
                    throw new Error('Network response was not ok');
                }
            }else{
                return false;
            }
        })

    fetch('/vendors'+email)
        .then(response => {
            // Check if the response is successful
            if (!response.ok) {
                // Check if the response status is NOT_FOUND (404)
                if (response.status === 404) {
                    //vendor does not exist so check next
                } else {
                    throw new Error('Network response was not ok');
                }
            }else{
                return false;
            }
        })

    // TODO: Once we have driver controller, check email address for drivers also!!!

    //if it gets here it means all fetch requests had response status not found hence email address is not used
    return true;
}