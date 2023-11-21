function createCustomer(){
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value
    var email = document.getElementById("email").value
    var checkPassword = document.getElementById("confirm-password").value

    if(checkPassword.toString() == password.toString()){
        // Send data to the backend
        fetch('/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username:username,password:password,email:email})
        })
            .then(response => response.json())
            .then(data => {
                // Handle the response from the backend
                console.log(data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

}