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

function cancelEditAddress() {
    document.getElementById('address-fields').style.display = 'none';
    document.getElementById('edit-address-btn').style.display = 'block';
}

function editAddress() {
    // Hide the "Edit Address" button
    document.getElementById('edit-address-btn').style.display = 'none';

    // Show the address fields
    document.getElementById('address-fields').style.display = 'block';

    // Fetch current address data and populate the fields
    var email = document.getElementById('userId').value;
    fetch(`/addresses/user/${email}`, {
        method: 'GET'
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 404) {
                // Address not found, do nothing
                return null;
            } else {
                throw new Error('Failed to fetch address data');
            }
        })
        .then(addressData => {
            if (addressData) {
                document.getElementById('street').value = addressData.street;
                document.getElementById('city').value = addressData.city;
                document.getElementById('postCode').value = addressData.postCode;
                document.getElementById('country').value = addressData.country;
            }
        })
        .catch(error => {
            console.error('Error fetching user address:', error);
            alert("Failed to fetch address data");
        });
}

function isValidPostcode(postcode) {
    // Regular expression for UK postcodes
    var postcodeRegex = /^[A-Z]{1,2}[0-9R][0-9A-Z]? ?[0-9][A-Z]{2}$/i;
    return postcodeRegex.test(postcode);
}

function saveAddress() {
    // Get address input data
    var street = document.getElementById('street').value;
    var city = document.getElementById('city').value;
    var postCode = document.getElementById('postCode').value;
    var country = document.getElementById('country').value;
    var email = document.getElementById('userId').value;

    if (!isValidPostcode(postCode)) {
        alert("Invalid postcode");
        return;
    }

    // Make a request to fetch user data
    fetch(`/users/${email}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(user => {
            // Construct the address object with the user data
            var addressData = {
                user: user,
                street: street,
                city: city,
                postCode: postCode,
                country: country,
            };
            // Check if there's an existing address for the user
            fetch(`/addresses/user/${email}`, {
                method: 'GET'
            })
                .then(response => {
                    if (response.ok) {
                        alert("patch");

                        // Add the id to the addressData object (needed because it won't be generated this time; we're patching not posting)
                        return response.json().then(existingAddress => {
                            addressData.id = existingAddress.id;

                            // Perform a PATCH request to update the existing address
                            return fetch(`/addresses/user/${email}`, {
                                method: 'PATCH',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify(addressData)
                            });
                        });
                    } else if (response.status === 404) {
                        alert("post");
                        // No address exists, perform a POST request to create a new address
                        return fetch(`/addresses`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(addressData)
                        });
                    } else {
                        throw new Error('Failed to fetch address data');
                    }
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
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            alert("Failed to fetch user data");
        });
}

