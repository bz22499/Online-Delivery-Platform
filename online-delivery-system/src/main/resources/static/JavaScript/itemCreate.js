function createCustomer(){
    let name = document.getElementById("name").value;
    let description = document.getElementById("description").value;
    let price = document.getElementById("price").value

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
