async function fetchItems(vendorId) {
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

function populateGrid(pageData) {
    const gridContainer = document.querySelector('.grid-container');
    const basketItems = document.querySelector('.basket-items');
    pageData.forEach((item) => {
            const gridItem = document.createElement('div');
            gridItem.className = 'grid-item';
            gridItem.textContent = item.name;

            const footer = document.createElement('div');
            footer.className = 'grid-item-footer';
            footer.textContent = item.price;
            gridItem.appendChild(footer);


        gridItem.addEventListener('click', function() {
            addOrUpdateBasketItem(item);
        });

            gridContainer.appendChild(gridItem);
        });
}

function addOrUpdateBasketItem(item) {
    let basketItem = document.querySelector(`.basket-item[data-id='${item.id}']`);
    if (!basketItem) {
        basketItem = createBasketItem(item);
        document.querySelector('.basket-items').appendChild(basketItem);
    } else {
        updateItemQuantity(basketItem, 1);
    }
}

function createBasketItem(item) {
    const basketItem = document.createElement('div');
    basketItem.className = 'basket-item';
    basketItem.setAttribute('data-id', item.id);

    const itemName = document.createElement('span');
    itemName.textContent = item.name;
    basketItem.appendChild(itemName);

    const priceDisplay = document.createElement('span');
    priceDisplay.className = 'item-price';
    priceDisplay.textContent = `£${item.price.toFixed(2)}`; // Assuming price is a number
    basketItem.appendChild(priceDisplay);

    basketItem.price = item.price; // Store the unit price for future calculations

    const quantityControl = createQuantityControl();
    basketItem.appendChild(quantityControl);

    return basketItem;
}

function createQuantityControl() {
    const quantityControl = document.createElement('div');
    quantityControl.className = 'quantity-control';

    const decreaseButton = document.createElement('button');
    decreaseButton.textContent = '-';
    decreaseButton.onclick = () => updateItemQuantity(quantityControl.parentElement, -1);

    const quantityDisplay = document.createElement('span');
    quantityDisplay.className = 'quantity';
    quantityDisplay.textContent = 1;

    const increaseButton = document.createElement('button');
    increaseButton.textContent = '+';
    increaseButton.onclick = () => updateItemQuantity(quantityControl.parentElement, 1);

    quantityControl.appendChild(decreaseButton);
    quantityControl.appendChild(quantityDisplay);
    quantityControl.appendChild(increaseButton);

    return quantityControl;
}

function updateItemQuantity(basketItem, change) {
    const quantityDisplay = basketItem.querySelector('.quantity');
    let quantity = parseInt(quantityDisplay.textContent);
    quantity = Math.max(0, quantity + change);
    quantityDisplay.textContent = quantity;

    // Update price display
    const priceDisplay = basketItem.querySelector('.item-price');
    const newPrice = basketItem.price * quantity;
    priceDisplay.textContent = `£${newPrice.toFixed(2)}`;

    if (quantity === 0) {
        basketItem.remove();
    }
}

async function load(vendorId) {
    const items = await fetchItems(vendorId);
    if (items.length > 0) {
        populateGrid(items)
    }
}


document.addEventListener('DOMContentLoaded', function () {
    const vendorInfoElement = document.getElementById('vendor-info');
    const vendorId = vendorInfoElement.getAttribute('data-id');
    if (vendorId) {
        load(vendorId);
    }
    const proceedButton = document.querySelector('.proceed-button');
    if (proceedButton) { // Check if button exists to avoid null reference errors
        proceedButton.addEventListener('click', function() {
            console.log("Button clicked")
            // window.location.href = '/checkout'; // Navigate to checkout page
        });
    }
});








