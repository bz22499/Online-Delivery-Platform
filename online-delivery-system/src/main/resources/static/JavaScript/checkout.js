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

function updateDeliveryAddress(selectedAddress) {
    console.log("Selected address:", selectedAddress);
}

document.addEventListener("DOMContentLoaded", async () => {

    await displayBaskets();

    const payButton = document.querySelector(".pay-button");
    payButton.addEventListener("click", () => {
        alert("Getting redirected to payment");
    });

    const backButton = document.getElementById('back-button');
    backButton.addEventListener("click", () => {
        window.history.back();
    });
});
