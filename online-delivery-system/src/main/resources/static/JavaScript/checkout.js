function getOrderIdFromCache(){
    const orderString = sessionStorage.getItem("order");
    const order = JSON.parse(orderString);
    return order.id;
}

// populate page with basket information
async function displayBaskets() {

    const basketsString = sessionStorage.getItem("baskets");
    const baskets = JSON.parse(basketsString);


    const container = document.getElementById("baskets-container");
    container.innerHTML = "";


    if (baskets && baskets.length) {
        let totalCost = 0

        for (const basket of baskets) {

            const restName = document.createElement("div");
            restName.className = "rest-name";
            restName.textContent = basket.restName;
            container.appendChild(restName);

            const table = document.createElement("table");
            table.className = "basket-table";
            const thead = document.createElement("thead");

            const headerRow = document.createElement("tr");
            const itemNameHeader = document.createElement("th");
            itemNameHeader.textContent = "Item Name";
            const quantityHeader = document.createElement("th");
            quantityHeader.textContent = "Quantity";
            headerRow.appendChild(itemNameHeader);
            headerRow.appendChild(quantityHeader);
            thead.appendChild(headerRow);
            table.appendChild(thead);

            const tbody = document.createElement("tbody");

            try {
                for(const basketItem of basket.items){
                    const row = document.createElement("tr");

                    const nameCell = document.createElement("td");
                    nameCell.textContent = basketItem.menuItem.name;
                    row.appendChild(nameCell);

                    const quantityCell = document.createElement("td");
                    quantityCell.textContent = basketItem.quantity;
                    row.appendChild(quantityCell);

                    tbody.appendChild(row);

                    totalCost = totalCost + (basketItem.quantity*basketItem.price);
                }

            } catch (error) {
                console.error("Error populating baskets dropdown:", error);
            }

            table.appendChild(tbody);
            container.appendChild(table);
        }


        const totalCostContainer = document.createElement("div");
        totalCostContainer.className = "total-cost";
        totalCostContainer.textContent = "Total Cost: £" + totalCost.toString();
        container.appendChild(totalCostContainer);
    } else {
        container.textContent = "No baskets found.";
    }
}

async function updateDeliveryAddress() {
    const userEmail = document.getElementById("user-email").value;

    try {
        const response = await fetch(`/addresses/user/${userEmail}`);
        if (response.ok) {
            const addressData = await response.json();
            populateAddressDropdown(addressData);
        } else if (response.status === 404) {
            alert("Address not found for the provided email.");
        } else {
            throw new Error('Failed to fetch address data');
        }
    } catch (error) {
        console.error('Error updating delivery address:', error);
        alert("Failed to fetch address data");
    }
}

function populateAddressDropdown(addressData) {
    const addressSelect = document.getElementById('address-select');
    addressSelect.innerHTML = "";

    if (addressData) {
        const option = document.createElement('option');
        option.value = addressData.id;
        option.textContent = `${addressData.street}, ${addressData.city}, ${addressData.country}`;
        addressSelect.appendChild(option);
    } else {
        console.error('No address data found:', addressData);
        alert("No address found for the provided email.");
    }
}

function updateDeliveryAddressFromDropdown() {
    const selectedAddressId = document.getElementById('address-select').value;
}

document.addEventListener("DOMContentLoaded", async () => {

    await displayBaskets();
    await updateDeliveryAddress();

    const payButton = document.querySelector(".pay-button");
    payButton.addEventListener("click", async () => {
        await updateOrderAddress()
    });

    const backButton = document.getElementById('back-button');
    backButton.addEventListener("click", () => {
        window.history.back();
    });
});

async function updateOrderAddress(){
    const addressSelect = document.getElementById("address-select")
    const addressId = addressSelect.value;
    const addressObj = await fetch(`/addresses/${addressId}`);

    const address = await addressObj.json();

    const orderId = getOrderIdFromCache();

    const updatedOrderData = {
        id: orderId,
        userAddress: address,
        status: "PAID"
    };

    await fetch('/orders/' + orderId, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedOrderData)
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 404) {
                    console.error('Order not found.');
                } else {
                    console.error('Failed to update order:', response.statusText);
                }
                throw new Error('Failed to update order.');
            }
            return response.json();
        })
        .then(updatedOrder => {
            sessionStorage.clear();
            alert("Getting redirected to payment");
        })
        .catch(error => {
            alert("Payment failed");
        });


}
