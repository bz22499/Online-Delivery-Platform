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
            alert("Failed to update profile 123456");
        });
}

function updateProfilePicture() {
    var input = document.getElementById('imageInput');
    var preview = document.getElementById('previewImage');

    var file = input.files[0];

    if (file) {
        var reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            uploadImage(file)
        };
        reader.readAsDataURL(file);
    }
}

var mimeTypeExtensions = {
    'image/jpeg': '.jpg',
    'image/png': '.png',
    'image/gif': '.gif',
};

function uploadImage(file){
    alert("GOT HERE")
    var formData = new FormData();
    var vendorId = document.getElementById('vendorId').value;

    vendorId = "*"+vendorId+"*"

    var mimeType = file.type;
    var fileExtension = mimeTypeExtensions[mimeType] || '';
    var fileName = vendorId+fileExtension

    formData.append('file', file, fileName);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/upload', true)
    xhr.onload = function() {
        if (xhr.status === 200) {
            alert("WORKED")
            console.log('Upload successful');
        } else {
            alert("FAILED")
            console.error('Upload failed. Status: ' + xhr.status);
        }
    };
    xhr.send(formData);

    updateURLOnDataBase(fileName)
}

function updateURLOnDataBase(fileName){
    var vendorId = document.getElementById('vendorId').value;

    var requestBody = {
        imageUrl: fileName
    };

    fetch(`/vendors/${vendorId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (response.ok) {

            } else {
                throw new Error('Failed to update profile');
            }
        })
        .catch(error => {
            console.error('Error updating profile:', error);
            alert("Failed to update profile 123456");
        });

}