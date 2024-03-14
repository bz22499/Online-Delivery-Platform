function createItem(){
    let name = document.getElementById("name").value;
    let description = document.getElementById("description").value;
    let price = document.getElementById("price").value
    
    const vendorInfoElement = document.getElementById('vendor-info');
    let vendorId = vendorInfoElement.getAttribute('data-id');

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

    if(valid){
        // Fetch the vendor's address
        fetch(`/vendorAddresses/vendor/${vendorId}`, {
            method: 'GET'
        })
            .then(response => {
                if(response.ok){
                    return response.json()
                } else if(response.status === 404){
                    throw new Error('Address not found for vendor');
                } else {
                    throw new Error('Failed to fetch vendor address');
                }
            })
            .then(address => {
                // Proceed to fetch the vendor object if the address exists
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
            })
            .catch(error => {
                console.error('Error fetching address:', error);
                alert("Please provide an address in your profile");
            });
    }
}