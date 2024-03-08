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

async function fetchVendorFromVendorId(vendorId){
    try {
        const response = await fetch(`/vendors/${vendorId}`);
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
    // Define the info/piece of body that will be sent to back end (payload)
    const payload = {
        delete: true
    }

    fetch('/menuItems/' + itemId.toString(), {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(data => {
        })
        .catch(error => {
            // Handle errors that occur during the request
            alert('There was a problem with your fetch operation:' + error.toString());
        });
}

async function submitFormClicked(name, price, description,itemID,itemTitle,itemDescription,itemFooter){
    const vendorInfoElement = document.getElementById('vendor-info');
    const vendorId = vendorInfoElement.getAttribute('data-id');
    const vendor = await fetchVendorFromVendorId(vendorId);

    let valid = /^(\$|)([0-9]\d{0,2}(\,\d{3})*|([0-9]\d*))(\.\d{2})?$/.test(price)

    if(!valid){
        alert("Price was not in correct format") //if it is not valid explain why
    }else{
        if(price > 10000){
            valid = false
            alert("Price of item exceeded maximum price of 10000")
        }
    }

    if(valid){
        if(name.toString() === "" || description.toString() === "" || price.toString() === ""){
            valid = false
            alert("A field was left empty");
        }
    }

    const menuItemDTO = {
        name: name,
        description: description,
        price: price,
        vendor: vendor
    };

    if(valid){
        fetch('menuItems/' + itemID, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(menuItemDTO)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });

        itemTitle.textContent = name;
        itemFooter.textContent = "£"+ price;
        itemDescription.textContent = description;
    }else{
        alert("FORM INCORRECTLY FILLED OUT")
    }



}

function cancelFormClicked(gridItemForm,gridItemContent){
    gridItemForm.hidden = true;
    gridItemContent.hidden = false;
}

function populateGrid(pageData) {
    const gridContainer = document.querySelector('.grid-container');
    pageData.forEach((item) => {
        const gridItem = document.createElement('div');
        gridItem.className = 'grid-item';

        //content contains the content of each menu item
        const gridItemContent = document.createElement('div');


        const gridTitle = document.createElement('div')
        gridTitle.className='grid-item-title'
        gridTitle.textContent = item.name;
        gridItemContent.appendChild(gridTitle)

        const description = document.createElement('div');
        description.className = 'grid-item-description';
        description.textContent = item.description;
        gridItemContent.appendChild(description);

        const footer = document.createElement('div');
        footer.className = 'grid-item-footer';
        footer.textContent = "£" + item.price.toFixed(2);
        gridItemContent.appendChild(footer);

        const deleteButton = document.createElement('div');
        deleteButton.className = 'grid-item-delete'
        gridItemContent.appendChild(deleteButton);

        const editButton = document.createElement('div');
        editButton.className = 'grid-item-edit';
        gridItemContent.appendChild(editButton);

        gridItem.appendChild(gridItemContent);

        //elements inside the form will be fields that can be changed upon clicking the edit button
        const gridItemForm = document.createElement('form');
        gridItemForm.className = "grid-item-form"

        const formItemName = document.createElement('input');
        formItemName.value=item.name
        gridItemForm.appendChild(formItemName);

        const formItemDescription = document.createElement('input');
        formItemDescription.value=item.description
        gridItemForm.appendChild(formItemDescription)

        const formItemPrice = document.createElement('input')
        formItemPrice.value=item.price.toFixed(2);
        gridItemForm.appendChild(formItemPrice)

        const submitForm = document.createElement('button');
        submitForm.id = 'submit-form-button'
        submitForm.type = "button"
        submitForm.onclick = async function () {await submitFormClicked(formItemName.value,formItemPrice.value,formItemDescription.value,item.id,gridTitle,description,footer); cancelFormClicked(gridItemForm,gridItemContent)}
        submitForm.textContent = "SUBMIT"
        gridItemForm.appendChild(submitForm);

        const cancelForm = document.createElement('button');
        cancelForm.id = 'cancel-form-button'
        cancelForm.type = "button"
        cancelForm.onclick = function () {cancelFormClicked(gridItemForm,gridItemContent)}
        cancelForm.textContent = "CANCEL";
        gridItemForm.appendChild(cancelForm);


        gridItem.appendChild(gridItemForm)

        gridContainer.appendChild(gridItem);

        gridItemForm.hidden=true;

        deleteButton.addEventListener('click', function() {
            const parentNode = gridItem;
            deleteItemForVendor(item.id);
            gridContainer.removeChild(parentNode);
        });

        editButton.addEventListener('click', function (){
            gridItemContent.hidden = true;
            gridItemForm.hidden=false;
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