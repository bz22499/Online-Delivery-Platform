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
    document.getElementById('user-details').style.display = 'none';
    document.getElementById('edit-form').style.display = 'block';

    // Set the current values in the form fields
    document.getElementById('new-firstName').value = document.getElementById('firstName').innerText;
    document.getElementById('new-lastName').value = document.getElementById('lastName').innerText;
    document.getElementById('new-job').value = document.getElementById('job').innerText;
    document.getElementById('new-description').value = document.getElementById('description').innerText;

    // Hide buttons in the user-details div
    document.querySelector('#user-details button').style.display = 'none';
}

function returnToProfile() {
    document.getElementById('user-details').style.display = 'block';
    document.getElementById('edit-form').style.display = 'none';
}

function cancelEdit() {
    returnToProfile();
}

function saveProfile() {
    // Update profile information with the edited values
    document.getElementById('firstName').innerText = document.getElementById('new-firstName').value;
    document.getElementById('lastName').innerText = document.getElementById('new-lastName').value;
    document.getElementById('job').innerText = document.getElementById('new-job').value;
    document.getElementById('description').innerText = document.getElementById('new-description').value;

    // Show profile section and hide edit form
    document.getElementById('user-details').style.display = 'block';
    document.getElementById('edit-form').style.display = 'none';

    // Inform the user that the profile has been saved
    alert("Profile saved!");
}