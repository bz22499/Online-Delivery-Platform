function updateProfilePicture() {
    var input = document.getElementById('imageInput');
    var preview = document.getElementById('previewImage');

    var file = input.files[0];

    if (file) {
        var reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
}

function editProfile() {

    // Clear the password-related input fields
    document.getElementById('current-password').value = '';
    document.getElementById('new-password').value = '';
    document.getElementById('confirm-password').value = '';

    document.querySelector('.firstName').style.display = 'none';
    document.querySelector('.lastName').style.display = 'none';
    document.getElementById('view-subscription').style.display = 'none';
    document.getElementById('logout').style.display = 'none';

    document.getElementById('edit-form').style.display = 'block';

    // Set the current values in the form fields
    document.getElementById('new-firstName').value = document.querySelector('.firstName').innerText;
    document.getElementById('new-lastName').value = document.querySelector('.lastName').innerText;

    // Hide buttons in the user-details div
    document.querySelector('#user-details button.edit-profile').style.display = 'none'; // edited this line as well
}

function returnToProfile() {
    document.querySelector('.firstName').style.display = 'block';
    document.querySelector('.lastName').style.display = 'block';
    document.getElementById('view-subscription').style.display = 'inline-block';
    document.getElementById('logout').style.display = 'inline-block';
    //document.getElementById('edit-profile').style.display = 'block';

    document.getElementById('edit-form').style.display = 'none';

    // Show the edit profile button again
    document.querySelector('#user-details button.edit-profile').style.display = 'inline-block';
}

function cancelEdit() {
    returnToProfile();
}

function saveProfile() {
    var userId = document.getElementById('userId').value;
    var currentPassword = document.getElementById('current-password').value;
    var firstName = document.getElementById('new-firstName').value;
    var lastName = document.getElementById('new-lastName').value;
    var newPassword = document.getElementById('new-password').value;
    var confirmPassword = document.getElementById('confirm-password').value;

    // Ensure both password fields match
    if (newPassword !== confirmPassword) {
        alert("Passwords do not match");
        return;
    }

    var requestBody = {
        firstName: firstName,
        lastName: lastName,
        currentPassword: currentPassword
    };

    // Only include the password fields if they are not empty and not whitespace
    if (newPassword.trim() !== '' && confirmPassword.trim() !== '') {
        requestBody.newPassword = newPassword;
    }

    // Make a request to update the user's profile including the password
    fetch(`/users/${userId}/profile`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (response.ok) {
                alert("Profile updated successfully");
                document.querySelector('.firstName').innerText = firstName;
                document.querySelector('.lastName').innerText = lastName;
                returnToProfile();
            } else if (response.status === 401) {
                // Unauthorized, display invalid password alert
                alert("Invalid password");
            } else {
                throw new Error('Failed to update profile');
            }
        })
        .catch(error => {
            console.error('Error updating profile:', error);
            alert("Failed to update profile");
        });
}

function editAddress() {
    // Hide the "Edit Address" button
    document.getElementById('edit-address-btn').style.display = 'none';

    // Show the address fields
    document.getElementById('address-fields').style.display = 'block';

    // Fetch current address data and populate the fields
    var email = document.getElementById('user-email').innerText; // Assuming the user's email is stored in an element with ID 'user-email'
    fetch(`/addresses/user/${email}`)
        .then(response => response.json())
        .then(data => {
            if (data) {
                document.getElementById('street').value = data.street;
                document.getElementById('city').value = data.city;
                document.getElementById('postCode').value = data.postCode;
                document.getElementById('country').value = data.country;
            }
        })
        .catch(error => {
            console.error('Error fetching user address:', error);
        });
}

function saveAddress() {
    // Get address input data
    var street = document.getElementById('street').value;
    var city = document.getElementById('city').value;
    var postCode = document.getElementById('postCode').value;
    var country = document.getElementById('country').value;

    // Get the user's email (assuming it's available in the DOM)
    var email = document.getElementById('user-email').innerText;

    // Make a request to fetch user data
    fetch(`/users/${email}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(user => {
            // construct the address object with the user data
            var addressData = {
                street: street,
                city: city,
                postCode: postCode,
                country: country,
                user: user  // Include the entire user object
            };

            // Make a request to save/update the user address
            return fetch(`/addresses`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(addressData)
            });
        })
        .then(response => {
            if (response.ok) {
                alert("Address updated successfully");
                // Hide the address fields
                document.getElementById('address-fields').style.display = 'none';
                // Show the "Edit Address" button
                document.getElementById('edit-address-btn').style.display = 'block';
            } else {
                throw new Error('Failed to update address');
            }
        })
        .catch(error => {
            console.error('Error updating address:', error);
            alert("Failed to update address");
        });
}
