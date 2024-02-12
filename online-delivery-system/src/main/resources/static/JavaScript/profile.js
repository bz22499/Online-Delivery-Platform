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
    // Get the current user's ID from the hidden input field
    var userId = document.getElementById('userId').value;

    // Get the updated first and last names from the input fields
    var firstName = document.getElementById('new-firstName').value;
    var lastName = document.getElementById('new-lastName').value;

    // Make a request to update the user's profile
    fetch(`/users/${userId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstName: firstName,
            lastName: lastName
            // Add other fields if needed
        })
    })
        .then(response => {
            if (response.ok) {
                // Profile update successful
                alert("Profile updated successfully");
                // Update displayed details if needed
                document.querySelector('.firstName').innerText = firstName;
                document.querySelector('.lastName').innerText = lastName;
                // Return to profile view
                returnToProfile();
            } else {
                // Handle profile update failure
                throw new Error('Failed to update profile');
            }
        })
        .catch(error => {
            console.error('Error updating profile:', error);
            alert("Failed to update profile");
        });
}