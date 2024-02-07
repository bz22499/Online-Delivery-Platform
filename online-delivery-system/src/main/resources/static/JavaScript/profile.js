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
    document.getElementById('firstName').style.display = 'none';
    document.getElementById('lastName').style.display = 'none';
    document.getElementById('job').style.display = 'none';
    document.getElementById('description').style.display = 'none';
    document.getElementById('view-subscription').style.display = 'none';
    document.getElementById('logout').style.display = 'none';

    document.getElementById('edit-form').style.display = 'block';

    // Set the current values in the form fields
    document.getElementById('new-firstName').value = document.getElementById('firstName').innerText;
    document.getElementById('new-lastName').value = document.getElementById('lastName').innerText;
    document.getElementById('new-job').value = document.getElementById('job').innerText;
    document.getElementById('new-description').value = document.getElementById('description').innerText;


    // Hide buttons in the user-details div
    document.querySelector('#user-details button').style.display = 'inline-block';
}

function returnToProfile() {
    document.getElementById('firstName').style.display = 'block';
    document.getElementById('lastName').style.display = 'block';
    document.getElementById('job').style.display = 'block';
    document.getElementById('description').style.display = 'block';
    document.getElementById('view-subscription').style.display = 'inline-block';
    document.getElementById('logout').style.display = 'inline-block';
    //document.getElementById('edit-profile').style.display = 'block';

    document.getElementById('edit-form').style.display = 'none';
}

function cancelEdit() {
    returnToProfile();
}

function saveProfile() {
    var firstName = document.getElementById('new-firstName').value;
    var lastName = document.getElementById('new-lastName').value;
    var job = document.getElementById('new-job').value;
    var description = document.getElementById('new-description').value;
    var profileData = {
        firstName: firstName,
        lastName: lastName,
        job: job,
        description: description,
        profilePicture: document.getElementById('previewImage').src
    };

    // Convert the profileData object to a JSON string
    var profileDataString = JSON.stringify(profileData);

    // Store the JSON string in the local storage
    localStorage.setItem('userProfile', profileDataString);

    // Inform the user that the profile has been saved
    alert("Profile saved!");

    //update the displayed details
    document.getElementById('name').innerText = name;
    document.getElementById('job').innerText = job;
    document.getElementById('description').innerText = description;

    returnToProfile()
}