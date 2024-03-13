function editProfile() {

    // Clear the password-related input fields
    document.getElementById('current-password').value = '';
    document.getElementById('new-password').value = '';
    document.getElementById('confirm-password').value = '';

    document.querySelector('.name').style.display = 'none';
    document.querySelector('.description').style.display = 'none';
    document.getElementById('logout').style.display = 'none';

    document.getElementById('edit-form').style.display = 'block';

    // Set the current values in the form fields
    document.getElementById('new-name').value = document.querySelector('.name').innerText;
    document.getElementById('new-description').value = document.querySelector('.description').innerText;

    // Hide buttons in the user-details div
    document.querySelector('#user-details button.edit-profile').style.display = 'none';
}

function returnToProfile() {
    document.querySelector('.name').style.display = 'block';
    document.querySelector('.description').style.display = 'block';
    document.getElementById('logout').style.display = 'inline-block';

    document.getElementById('edit-form').style.display = 'none';

    // Show the edit profile button again
    document.querySelector('#user-details button.edit-profile').style.display = 'inline-block';
}

function cancelEdit() {
    returnToProfile();
}

function saveProfile() {
    var vendorId = document.getElementById('vendorId').value;
    var currentPassword = document.getElementById('current-password').value;
    var name = document.getElementById('new-name').value;
    var description = document.getElementById('new-description').value;
    var newPassword = document.getElementById('new-password').value;
    var confirmPassword = document.getElementById('confirm-password').value;

    // Ensure both password fields match
    if (newPassword !== confirmPassword) {
        alert("Passwords do not match");
        return;
    }

    var requestBody = {
        name: name,
        description: description,
        currentPassword: currentPassword
    };

    // Only include the password fields if they are not empty and not whitespace
    if (newPassword.trim() !== '' && confirmPassword.trim() !== '') {
        requestBody.newPassword = newPassword;
    }

    // Make a request to update the user's profile including the password
    fetch(`/vendors/${vendorId}/vendorProfile`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (response.ok) {
                alert("Profile updated successfully");
                document.querySelector('.name').innerText = name;
                document.querySelector('.description').innerText = description;
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
    var email = document.getElementById('vendorId').value;
    fetch(`/vendorAddresses/vendor/${email}`, {
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
            // Check if addressData is null before accessing its properties
            if (addressData) {
                document.getElementById('street').value = addressData.street;
                document.getElementById('city').value = addressData.city;
                document.getElementById('postCode').value = addressData.postCode;
                document.getElementById('country').value = addressData.country;
            }
        })
        .catch(error => {
            console.error('Error fetching vendor address:', error);
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
    var email = document.getElementById('vendorId').value;

    // Check if any of the fields are empty
    if (!street || !city || !postCode || !country) {
        alert("Please fill in all address fields");
        return;
    }

    if (!isValidPostcode(postCode)) {
        alert("Invalid postcode");
        return;
    }

    // Make a request to fetch vendor data
    fetch(`/vendors/${email}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(vendor => {
            // Construct the address object with the vendor data
            var addressData = {
                vendor: vendor,
                street: street,
                city: city,
                postCode: postCode,
                country: country,
            };
            // Check if there's an existing address for the vendor
            fetch(`/vendorAddresses/vendor/${email}`, {
                method: 'GET'
            })
                .then(response => {
                    if (response.ok) {

                        // Add the id to the addressData object (needed because it won't be generated this time; we're patching not posting)
                        return response.json().then(existingAddress => {
                            addressData.id = existingAddress.id;

                            // Perform a PATCH request to update the existing address
                            return fetch(`/vendorAddresses/vendor/${email}`, {
                                method: 'PATCH',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify(addressData)
                            });
                        });
                    } else if (response.status === 404) {
                        // No address exists, perform a POST request to create a new address
                        return fetch(`/vendorAddresses`, {
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
            console.error('Error fetching vendor data:', error);
            alert("Failed to fetch vendor data");
        });
}