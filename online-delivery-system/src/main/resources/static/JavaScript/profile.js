function editProfile() {
    // Clear the password-related input fields
    document.getElementById('current-password').value = '';
    document.getElementById('new-password').value = '';
    document.getElementById('confirm-password').value = '';

    // Hide the initial input fields and edit profile button
    document.getElementById('user-details').style.display = 'none';
    document.getElementById('edit-profile').style.display = 'none';

    // Show the edit form
    document.getElementById('edit-form').style.display = 'block';

    // Set the current values in the form fields
    document.getElementById('new-firstName').value = document.getElementById('firstName').innerText;
    document.getElementById('new-lastName').value = document.getElementById('lastName').innerText;
}

function returnToProfile() {
    // Show the details divs
    document.getElementById('user-details').style.display = 'block';

    document.getElementById('address-details').style.display = 'block';

    // Hide the edit forms
    document.getElementById('edit-form').style.display = 'none';

    document.getElementById('address-fields').style.display = 'none';

    // Show the edit buttons
    document.getElementById('edit-profile').style.display = 'block';

    document.getElementById('edit-address-btn').style.display = 'block';
}

function saveProfile() {
    const saveButton = document.getElementById("saveProfileChanges");
    saveButton.disabled = true;
    const cancelButton = document.getElementById("cancelProfileChanges");
    cancelButton.disabled = true;

    var userId = document.getElementById('userId').value;
    var currentPassword = document.getElementById('current-password').value;
    var firstName = document.getElementById('new-firstName').value;
    var lastName = document.getElementById('new-lastName').value;
    var newPassword = document.getElementById('new-password').value;
    var confirmPassword = document.getElementById('confirm-password').value;

    // Ensure both password fields match
    if (newPassword !== confirmPassword) {
        saveButton.disabled = false;
        cancelButton.disabled = false;
        alert("Passwords do not match.");
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
                saveButton.disabled = false;
                cancelButton.disabled = false;
                
                var firstName = document.getElementById('new-firstName').value;
                var lastName = document.getElementById('new-lastName').value;

                document.getElementById('firstName').innerText = firstName;
                document.getElementById('lastName').innerText = lastName;


                returnToProfile();
            } else if (response.status === 401) {
                // Unauthorized, display invalid password alert
                saveButton.disabled = false;
                cancelButton.disabled = false;
                alert("Invalid password");
            } else {
                saveButton.disabled = false;
                cancelButton.disabled = false;
                throw new Error('Failed to update profile');
            }
        })
        .catch(error => {
            saveButton.disabled = false;
            cancelButton.disabled = false;
            console.error('Error updating profile:', error);
            alert("Failed to update profile");
        });
}

function editAddress() {
    document.getElementById('address-details').style.display = 'none';
    document.getElementById('edit-address-btn').style.display = 'none';
    document.getElementById('address-fields').style.display = 'block';

    document.getElementById('street').value = document.getElementById('current-street').innerText;
    document.getElementById('city').value = document.getElementById('current-city').innerText;
    document.getElementById('country').value = document.getElementById('current-country').innerText;
    document.getElementById('postCode').value = document.getElementById('current-postcode').innerText;
}

function isValidPostcode(postcode) {
    var postcodeRegex = /^[A-Z]{1,2}[0-9R][0-9A-Z]? ?[0-9][A-Z]{2}$/i;
    return postcodeRegex.test(postcode);
}

function saveAddress() {
    const saveButton = document.getElementById("saveAddressChanges");
    saveButton.disabled = true;
    const cancelButton = document.getElementById("cancelAddressChanges");
    cancelButton.disabled = true;
  
    // Get address input data
    var street = document.getElementById('street').value;
    var city = document.getElementById('city').value;
    var postCode = document.getElementById('postCode').value;
    var country = document.getElementById('country').value;
    var email = document.getElementById('userId').value;

    // Check if any of the fields are empty
    if (!street || !city || !postCode || !country) {
        alert("Please fill in all address fields");
        saveButton.disabled = false;
        cancelButton.disabled = false;
        return;
    }

    if (!isValidPostcode(postCode)) {
        alert("Invalid postcode");
        saveButton.disabled = false;
        cancelButton.disabled = false;
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
                        saveButton.disabled = false;
                        cancelButton.disabled = false;
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
                        saveButton.disabled = false;
                        cancelButton.disabled = false;
                        // No address exists, perform a POST request to create a new address
                        return fetch(`/addresses`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(addressData)
                        });
                    } else {
                        saveButton.disabled = false;
                        cancelButton.disabled = false;
                        throw new Error('Failed to fetch address data');
                    }
                })
                .then(response => {
                    if (response.ok) {
                        saveButton.disabled = false;
                        cancelButton.disabled = false;
                        returnToProfile();
                    } else {
                        saveButton.disabled = false;
                        cancelButton.disabled = false;
                        throw new Error('Failed to update address');
                    }
                })
                .catch(error => {
                    saveButton.disabled = false;
                    cancelButton.disabled = false;
                    console.error('Error updating address:', error);
                    alert("Failed to update address");
                });
        })
        .catch(error => {
            saveButton.disabled = false;
            cancelButton.disabled = false;
            alert("Failed to fetch user data: ", error);
        });
}

document.addEventListener('DOMContentLoaded', function () {
    const logoutButton = document.querySelector("#logout");
    logoutButton.addEventListener('click', function() {
        sessionStorage.clear();
        window.location.href='/logout';
    })
})