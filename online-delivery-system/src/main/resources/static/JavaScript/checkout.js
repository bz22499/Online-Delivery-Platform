let isFetchingBaskets = false;

// get basket items from cache
function getBasketItemsFromCache() {
    const basketsString = sessionStorage.getItem("baskets");
    const baskets = JSON.parse(basketsString);
    /*return {
        items: baskets[basketId].items,
        restName: baskets[basketId].restName,
    };*/
    return baskets;
}

function getOrderIdFromCache(){
    const orderString = sessionStorage.getItem("order");
    const order = JSON.parse(orderString);
    return order.id;
}

// populate page with basket information
async function displayBaskets() {
    const baskets = getBasketItemsFromCache();
    const container = document.getElementById("baskets-container"); // defined in html
    container.innerHTML = ""; // clear to avoid duplicates
    if (baskets && Object.keys(baskets).length > 0) {
        // Get the keys (basket IDs) and pick the last one
        const basketIds = Object.keys(baskets);
        const lastBasketId = basketIds[basketIds.length - 1];
        const basketData = baskets[lastBasketId];

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

        basketData.items.forEach((item) => {
            const row = document.createElement("tr");

            const nameCell = document.createElement("td");
            nameCell.textContent = item.menuItem.name;
            row.appendChild(nameCell);

            const quantityCell = document.createElement("td");
            quantityCell.textContent = item.quantity;
            row.appendChild(quantityCell);

            tbody.appendChild(row);
        });

        table.appendChild(tbody);
        container.appendChild(table);

        const totalCostContainer = document.createElement("div");
        totalCostContainer.className = "total-cost";
        totalCostContainer.textContent = "Total Cost: $" + calculateTotalCost(basketData.items);
        container.appendChild(totalCostContainer);
    } else {
        container.textContent = "No baskets found.";
    }
}


//Function to calculate the total cost
function calculateTotalCost(items) {
    let totalCost = 0;

    items.forEach((item) => {
        totalCost += item.menuItem.price * item.quantity;
    });

    return totalCost.toFixed(2); //Round to 2 dp
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

// Function to populate the address dropdown with fetched address data
function populateAddressDropdown(addressData) {
    const addressSelect = document.getElementById('address-select');
    addressSelect.innerHTML = ""; // Clear existing options

    if (addressData) {
        const option = document.createElement('option');
        option.value = addressData.id; // Assuming address has an ID
        option.textContent = `${addressData.street}, ${addressData.city}, ${addressData.country}`;
        addressSelect.appendChild(option);
    } else {
        console.error('No address data found:', addressData);
        alert("No address found for the provided email.");
    }
}

// Function to update address dropdown based on selection
function updateDeliveryAddressFromDropdown() {
    const selectedAddressId = document.getElementById('address-select').value;
    // Fetch address details for the selected address ID and populate other fields if needed
}

document.addEventListener("DOMContentLoaded", async () => {

    await displayBaskets();
    await updateDeliveryAddress();

    const payButton = document.querySelector(".pay-button");
    payButton.addEventListener("click", async () => {
        await updateOrderAddress()
        alert("Getting redirected to payment");
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

    //now we have the id of the address we need the id of the order
    const orderId = getOrderIdFromCache();
    
    const updatedOrderData = {
        id: orderId,
        userAddress: address,
        status: "noway"
    };

    alert(orderId);

    await fetch('/orders/' + orderId, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedOrderData)
    })
        .then(response => {
            alert(response.status);
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
            alert("UPDATED ORDER");
        })
        .catch(error => {
            alert("FAILED");
        });


}
