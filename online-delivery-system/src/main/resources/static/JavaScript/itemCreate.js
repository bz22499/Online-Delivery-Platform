function createItem(){
    let name = document.getElementById("name").value;
    let description = document.getElementById("description").value;
    let price = document.getElementById("price").value
    
    const vendorInfoElement = document.getElementById('vendor-info');
    let vendorId = vendorInfoElement.getAttribute('data-id');

    let valid = true

    if(valid){
        valid = /^(\$|)([1-9]\d{0,2}(\,\d{3})*|([1-9]\d*))(\.\d{2})?$/.test(price)
    }

    if(name.toString() === "" || description.toString() === "" || price.toString() === ""){
        valid = false
    }

    if(valid){
        // Fetch the vendor object from the backend
        fetch(`/vendors/${vendorId}`, {
            method: 'GET'
        })
            .then(response => {
                if(response.ok){
                    return response.json()
                } else {
                    throw new Error('Vendor not found');
                }
            })
            .then(vendor => {
                // Now that we have the vendor object, proceed to create the menu item
                fetch('/menuItems', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        name: name,
                        description: description,
                        price: price,
                        vendor: vendor // Use the fetched vendor object
                    })
                })
                    .then(response => response.json())
                    .then(data => {
                        window.location.href = '/vendor'; //refresh page
                        alert("Menu item submitted");
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
            })
            .catch(error => {
                console.error('Error fetching vendor:', error);
                alert("Failed to fetch vendor details");
            });
    } else {
        alert("Form was not correctly filled in");
    }
}