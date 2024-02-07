async function fetchItemsForLoggedInVendor(vendorId) {
    try {
        const response = await fetch(`/menuItems/vendor/${vendorId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching item data:', error);
    }
}

function deleteItemForVendor(itemId){
    fetch('/menuItems/' + itemId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(data => {
            // Optionally handle the data returned from the server
            alert("delete successful")
        })
        .catch(error => {
            // Handle errors that occur during the request
            alert('There was a problem with your fetch operation:' + error.toString());
        });
}


function populateGrid(pageData) {
    const gridContainer = document.querySelector('.grid-container');
    pageData.forEach((item) => {
        const gridItem = document.createElement('div');
        gridItem.className = 'grid-item';
        gridItem.textContent = item.name;

        const footer = document.createElement('div');
        footer.className = 'grid-item-footer';
        footer.textContent = item.price;
        gridItem.appendChild(footer);

        const deleteButton = document.createElement('div');
        deleteButton.className = 'grid-item-delete';
        gridItem.appendChild(deleteButton);

        gridContainer.appendChild(gridItem);


        deleteButton.addEventListener('click', function() {
            // Your JavaScript function here
            const parentNode = deleteButton.parentNode;
            const itemId = parentNode.id;

            alert(item.id);
            deleteItemForVendor(item.id);


            // Call any JavaScript function or perform any action you want here
        });

    });
}

async function load() {
    const vendorInfoElement = document.getElementById('vendor-info');
    const vendorId = vendorInfoElement.getAttribute('data-id');

    if (vendorId) {
        const items = await fetchItemsForLoggedInVendor(vendorId);
        if (items.length > 0) {
            populateGrid(items);
        }
    }
}

document.addEventListener('DOMContentLoaded', async function () {
    const proceedButton = document.querySelector('.proceed-button');

    if (proceedButton) {
        proceedButton.addEventListener('click', function() {
            console.log("Button clicked");
            // window.location.href = '/checkout'; // Navigate to checkout page
        });
    }

    load();
});