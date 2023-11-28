function createItem(){
    let name = document.getElementById("name").value;
    let description = document.getElementById("description").value;
    let price = document.getElementById("price").value

    let valid = true

    if(valid){
        valid = /^(\$|)([1-9]\d{0,2}(\,\d{3})*|([1-9]\d*))(\.\d{2})?$/.test(price)
    }

    if(name.toString() === "" || description.toString() === "" || price.toString() === ""){
        valid = false
    }


    if(valid){
        // Send data to the backend
        fetch('/menuItems', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({name: name, description: description, price: price, id: 1234, vendor: {
                null}})
        })
            .then(response => response.json())
            .then(data => {
                // Handle the response from the backend
                // alert("Menu item submitted")
                // window.location.href = '/vendor'; //refresh page
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }else{
        alert("Form was not correctly filled in")
    }
}
