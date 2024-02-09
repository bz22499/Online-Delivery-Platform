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
    /*document.getElementById('new-firstName').style.display = 'block';
    document.getElementById('new-lastName').style.display = 'block';
    document.getElementById('new-job').style.display = 'block';
    document.getElementById('new-description').style.display = 'block';
    document.getElementById('view-subscription').style.display = 'none';
    document.getElementById('logout').style.display = 'none';*/
    document.getElementById('user-details').style.display = 'none';

    document.getElementById('edit-form').style.display = 'inline-block';

    // Set the current values in the form fields
    document.getElementById('new-firstName').value = document.getElementById('firstName').innerText;
    document.getElementById('new-lastName').value = document.getElementById('lastName').innerText;
    document.getElementById('new-job').value = document.getElementById('job').innerText;
    document.getElementById('new-description').value = document.getElementById('description').innerText;


    // Hide buttons in the user-details div
    document.querySelector('#user-details button').style.display = 'none';
}

function returnToProfile() {
    /*document.getElementById('new-firstName').style.display = 'block';
    document.getElementById('new-lastName').style.display = 'block';
    document.getElementById('new-job').style.display = 'block';
    document.getElementById('new-description').style.display = 'block';
    document.getElementById('view-subscription').style.display = 'inline-block';
    document.getElementById('logout').style.display = 'inline-block';
    //document.getElementById('edit-profile').style.display = 'block';*/
    document.getElementById('user-details').style.display = 'block';

    document.getElementById('edit-form').style.display = 'none';
}

function cancelEdit() {
    returnToProfile();
}

function saveProfile() {
    var newFirstName = document.getElementById('new-firstName').value;
    var newLastName = document.getElementById('new-lastName').value;
    var newJob = document.getElementById('new-job').value;
    var newDescription = document.getElementById('new-description').value;
    var profileData = {
        firstName: newFirstName,
        lastName: newLastName,
        job: newJob,
        description: newDescription,
        profilePicture: document.getElementById('previewImage').src
    };

    // Convert the profileData object to a JSON string
    var profileDataString = JSON.stringify(profileData);

    // Store the JSON string in the local storage
    localStorage.setItem('userProfile', profileDataString);

    document.getElementById('user-details').style.display = 'block';

    document.getElementById('edit-form').style.display = 'none';
    
    // Inform the user that the profile has been saved
    alert("Profile saved!");

    //update the displayed details
    /*document.getElementById('firstName').innerText = newFirstName;
    document.getElementById('lastName').innerText = newLastName;
    document.getElementById('job').innerText = newJob;
    document.getElementById('description').innerText = newDescription;*/

    //returnToProfile();
}