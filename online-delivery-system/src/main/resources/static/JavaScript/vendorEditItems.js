async function fetchItemsForLoggedInVendor(vendorId) {
    try {
        const response = await fetch(`/menuItems/vendor/${vendorId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        alert('Error fetching item data: ', error);
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
        console.error('Error fetching vendor data: ', error);
    }
}

function deleteItemForVendor(itemId){
 
    const payload = {
        delete: true
    }

    fetch(`/menuItems/${itemId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error deleting item. Status: ${response.status}`);
        }else{
            deleteFile("_"+itemId.toString()+"_.jpg");
        }
    })
    .catch(error => {
        alert('Error deleting menu item: ', error);
    });
}

function deleteFile(filename) {
    fetch('/delete?filename=' + encodeURIComponent(filename), {
        method: 'DELETE'
    })
    .catch(error => {
        alert('Error deleting file:', error);
    });
}

async function submitFormClicked(name, price, description,itemID,itemTitle,itemDescription,itemFooter){
    const submitButton = document.getElementById('submit-form-button');
    const cancelButton = document.getElementById('cancel-form-button');

    submitButton.disabled = true;
    cancelButton.disabled = true;
    const vendorInfoElement = document.getElementById('vendor-info');
    const vendorId = vendorInfoElement.getAttribute('data-id');
    const vendor = await fetchVendorFromVendorId(vendorId);

    let valid = /^(\$|)([0-9]\d{0,2}(\,\d{3})*|([0-9]\d*))(\.\d{2})?$/.test(price)

    if(!valid){
        submitButton.disabled = false;
        cancelButton.disabled = false;
        alert("Invalid price.") 
    }else{
        if(price > 10000){
            valid = false
        }
    }

    if(valid){
        if(name.toString() === "" || description.toString() === "" || price.toString() === ""){
            valid = false
            submitButton.disabled = false;
            cancelButton.disabled = false;
            alert("Please complete all the fields.");
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
                    submitButton.disabled = false;
                    cancelButton.disabled = false;
                    return false;
                }
                submitButton.disabled = false;
                cancelButton.disabled = false;
            })
            
        itemTitle.textContent = name;
        itemFooter.textContent = "£"+ price;
        itemDescription.textContent = description;
    }else{
        return false;
    }
    return true;


}

function cancelFormClicked(gridFormContainer,gridItemInfo){
    gridFormContainer.hidden = true;
    gridItemInfo.hidden = false;
}

function populateGrid(pageData) {
    const gridContainer = document.querySelector('.grid-container');
    pageData.forEach((item) => {

        const gridItem = document.createElement('div');
        gridItem.className = 'grid-item';

        const gridItemInfo = document.createElement('div');
        gridItemInfo.className = 'grid-item-info';

        const gridItemContent = document.createElement('div');
        gridItemContent.className = 'grid-item-content';

        const imageHandling = document.createElement('div');
        imageHandling.className = "grid-item-image-box";

        const profileButton = document.createElement('input');
        profileButton.className = 'grid-item-profile-picture';
        profileButton.id = 'imageInput'+item.id;
        profileButton.type = 'file';
        profileButton.accept="image/*";


        imageHandling.appendChild(profileButton);

        const editImageSymbolContainer = document.createElement('label');
        editImageSymbolContainer.htmlFor = "imageInput"+item.id;
        editImageSymbolContainer.className = 'edit-profile-button'

        imageHandling.appendChild(editImageSymbolContainer);
        imageHandling.style.backgroundImage =  "url('images/wix1.png')"

        const editImageSymbol = document.createElement('span');
        editImageSymbol.className = 'material-symbols-outlined'
        editImageSymbol.textContent ="edit";

        editImageSymbolContainer.appendChild(editImageSymbol)

        //check if the image exists
        var ImgUrl = "uploads/" + "_" + item.id + "_.jpg";
        let img = new Image();
        img.onload = function() {
            imageHandling.style.backgroundImage =  "url('"+ImgUrl+"')"
        };
        img.src = ImgUrl;

        gridItemContent.appendChild(imageHandling)

        const gridItemText = document.createElement('div');
        gridItemText.className = 'grid-item-text';


        const gridTitle = document.createElement('div')
        gridTitle.className='grid-item-title'
        gridTitle.textContent = item.name;
        gridItemText.appendChild(gridTitle)

        const description = document.createElement('div');
        description.className = 'grid-item-description';
        description.textContent = item.description;
        gridItemText.appendChild(description);

        const footer = document.createElement('div');
        footer.className = 'grid-item-footer';
        footer.textContent = "£" + item.price.toFixed(2);
        gridItemText.appendChild(footer);

        gridItemContent.appendChild(gridItemText);


        const symbols = document.createElement('div');
        symbols.className = 'symbols-container';

        const editButton = document.createElement('div');
        editButton.className = 'grid-item-edit';

        const editSymbol = document.createElement('span');
        editSymbol.className = 'material-symbols-outlined'
        editSymbol.textContent ="edit";
        editButton.appendChild(editSymbol);
        symbols.appendChild(editButton);

        const deleteButton = document.createElement('div');
        deleteButton.className = 'grid-item-delete'

        const deleteSymbol = document.createElement('span');
        deleteSymbol.className = 'material-symbols-outlined';
        deleteSymbol.textContent ="delete";

        deleteButton.appendChild(deleteSymbol);
        symbols.appendChild(deleteButton);



        gridItemContent.appendChild(symbols)



        gridItemInfo.appendChild(gridItemContent)

        gridItem.appendChild(gridItemInfo);

        const gridFormContainer = document.createElement('div');
        gridFormContainer.className = 'grid-form-container';


        const gridItemForm = document.createElement('form');
        gridItemForm.className = "grid-item-form"
        gridFormContainer.appendChild(gridItemForm);

        const formItemName = document.createElement('input');
        formItemName.value=item.name
        gridItemForm.appendChild(formItemName);

        const formItemDescription = document.createElement('input');
        formItemDescription.value=item.description
        gridItemForm.appendChild(formItemDescription)

        const formItemPrice = document.createElement('input')
        formItemPrice.value=item.price.toFixed(2);
        gridItemForm.appendChild(formItemPrice)

        let currentName = item.name;
        let currentDesc = item.description;
        let currentPrice = item.price;


        const submitForm = document.createElement('button');
        submitForm.disabled = false;
        submitForm.className='stylishbutton';
        submitForm.id = 'submit-form-button'
        submitForm.type = "button"
        submitForm.onclick = async function () {
            let worked = await submitFormClicked(formItemName.value,formItemPrice.value,formItemDescription.value,item.id,gridTitle,description,footer);
            if(!worked){
                formItemName.value = currentName;
                formItemDescription.value = currentDesc;
                formItemPrice.value = currentPrice;
            }else{
                currentName = formItemName.value;
                currentDesc = formItemDescription.value;
                currentPrice = formItemPrice.value;
            }
            cancelFormClicked(gridFormContainer,gridItemInfo);

        }
        submitForm.textContent = "SUBMIT"
        gridItemForm.appendChild(submitForm);

        const cancelForm = document.createElement('button');
        cancelForm.className='stylishbutton';
        cancelForm.id = 'cancel-form-button'
        cancelForm.type = "button"
        cancelForm.onclick = function () {cancelFormClicked(gridFormContainer,gridItemInfo)}
        cancelForm.textContent = "CANCEL";
        gridItemForm.appendChild(cancelForm);

        gridItem.appendChild(gridFormContainer);

        gridContainer.appendChild(gridItem);

        gridFormContainer.hidden=true;

        deleteButton.addEventListener('click', function() {
            const parentNode = gridItem;
            deleteItemForVendor(item.id);
            gridContainer.removeChild(parentNode);
        });

        editButton.addEventListener('click', function (){
            formItemName.value = currentName;
            formItemPrice.value = currentPrice;
            formItemDescription.value = currentDesc;

            gridItemInfo.hidden = true;
            gridFormContainer.hidden=false;
        });


        profileButton.addEventListener('change', function(event) {
            const file = event.target.files[0];
            const reader = new FileReader();

            reader.onload = function(e) {
                updateItemPicture(file,item.id,function(success){
                    if (success) {
                        imageHandling.style.backgroundImage = `url(${e.target.result})`;
                    }
                });
            };

            reader.readAsDataURL(file);
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
        });
    }

    load();
});


var mimeTypeExtensions = {
    'image/jpeg': '.jpg',
    'image/png': '.png',
    'image/gif': '.gif',
};


function updateItemPicture(file, itemID, callback){

    var formData = new FormData();

    itemID= "_"+itemID+"_"

    var mimeType = file.type;
    var fileExtension = mimeTypeExtensions[mimeType] || '';
    var fileName = itemID+fileExtension

    var submitForm = true;

    if(fileExtension !=='.jpg'){
        submitForm = false;
    }

    if(submitForm){
        formData.append('file', file, fileName);
        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/upload', true)
        xhr.onload = function() {
            if (xhr.status === 200) {
                if (callback) {
                    callback(true);
                }
            } else {
                alert("Failed. Ensure file is under 1MB");

                if (callback) {
                    callback(false);
                }
            }
        };
        xhr.send(formData);

    }else{
        alert("Please upload file in jpeg format.");
        if (callback) {
            callback(false);
        }
    }


}