function createItem(){
    const registerButton = document.getElementById("registerButton");
    registerButton.disabled = true;
    let name = document.getElementById("name").value;
    let description = document.getElementById("description").value;
    let price = document.getElementById("price").value
    
    const vendorInfoElement = document.getElementById('vendor-info');
    let vendorId = vendorInfoElement.getAttribute('data-id');

    let valid = /^(\$|)([0-9]\d{0,2}(\,\d{3})*|([0-9]\d*))(\.\d{2})?$/.test(price)

    if(!valid){
        alert("Invalid price.")
    }else{
        if(price > 10000){
            valid = false
        }
    }

    if(valid){
        if(name.toString() === "" || description.toString() === "" || price.toString() === ""){
            valid = false
            alert("Please complete all the fields.");
        }
    }

    if(valid){
        fetch(`/vendorAddresses/vendor/${vendorId}`, {
            method: 'GET'
        })
            .then(response => {
                if(response.ok){
                    return response.json()
                } else {
                    throw new Error(`Failed to fetch vendor address. Status: ${response.status}`);
                }
            })
            .then(address => {
                fetch(`/vendors/${vendorId}`, {
                    method: 'GET'
                })
                    .then(response => {
                        if(response.ok){
                            return response.json()
                        } else {
                            throw new Error(`Error fetching vendor. Status: ${response.status}`);
                        }
                    })
                    .then(vendor => {
                        fetch('/menuItems', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({
                                name: name,
                                description: description,
                                price: price,
                                vendor: vendor
                            })
                        })
                        .then(response => {
                            if(response.ok){
                                window.location.href = '/vendoradditems';
                            } else {
                                alert(`Error posting basket . Status: ${response.status}`);
                            }
                        })
                        .finally(() => {
                            registerButton.disabled = false;
                        });
                    })
            })
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const registerButton = document.getElementById("registerButton");
    registerButton.disabled = false;
});