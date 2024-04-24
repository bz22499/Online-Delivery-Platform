let restaurantName;
let order = getOrderFromCache();
let baskets;

let basket = {
    order: order,
    items: []
};

function findIndex(name){
    let i = 0;
    for (let b of baskets){
        if (b.restName === name){
            return i
        }
        i++;
    }
    return -1;
}



function getSavedBasket(){
    const index = findIndex(restaurantName);
    if (index === -1){
        return
    } else {
        return baskets[index];
    }
}

function getOrderFromCache(){
    let orderString = sessionStorage.getItem("order");
    let order = JSON.parse(orderString);
    return order
}

function getBasketsFromCache(){
    let basketsString = sessionStorage.getItem('baskets');
    if (basketsString) {
        baskets = JSON.parse(basketsString);
    } else {
        baskets = []; 
    }
    return baskets;
}

function getRestaurantName(){
    let restaurantNameDiv = document.querySelector('.name')
    let restaurantName = restaurantNameDiv.textContent
    return restaurantName;
}

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

// Function to check if the user has an address
async function hasAddress(email) {
    try {
        const response = await fetch(`/addresses/user/${email}`);
        return response.ok; // Returns true if the address exists, false otherwise
    } catch (error) {
        console.error('Error fetching user address:', error);
        return false; // Return false in case of any error
    }
}

// populate grid with given menu items
function populateGrid(pageData) {
    const gridContainer = document.querySelector('.grid-container');
    pageData.forEach((item) => {
        const gridItem = document.createElement('div');
        gridItem.className = 'grid-item';

        const gridTitle = document.createElement('div')
        gridTitle.className='grid-item-title'
        gridTitle.textContent = item.name;
        gridItem.appendChild(gridTitle)


        const description = document.createElement('div');
        description.className = 'grid-item-description';
        description.textContent = item.description;
        gridItem.appendChild(description);

        const footer = document.createElement('div');
        footer.className = 'grid-item-footer';
        footer.textContent = "£"+item.price;
        gridItem.appendChild(footer);

        gridItem.addEventListener('click', function() {
            addOrUpdateBasketItem(item);
        });

        gridContainer.appendChild(gridItem);
    });
}

function addOrUpdateBasketItem(menuItem, qty=1) {
    let basketItemData = basket.items.find(item => item.menuItem.id === menuItem.id);
    let basketItemHtml = document.querySelector(`.basket-item[data-id='${menuItem.id}']`);

    if (!basketItemData) {
        if (!basketItemHtml){
            basketItemHtml = createBasketItemHtml(menuItem); 
            document.querySelector('.basket-items').appendChild(basketItemHtml); 
        };
        basketItemData = {
            id: null,
            menuItem: menuItem,
            quantity: qty,
            price: menuItem.price
        };
        basket.items.push(basketItemData);
        basketItemHtml.basketItemData = basketItemData;
    } else {
        if (!basketItemHtml){
            basketItemHtml = createBasketItemHtml(menuItem); 
            document.querySelector('.basket-items').appendChild(basketItemHtml); 
            basketItemHtml.basketItemData = basketItemData;
        };
        updateItemQuantity(basketItemHtml, qty);
    }
}

function createBasketItemHtml(menuItem) {
    const basketItem = document.createElement('div');
    basketItem.className = 'basket-item';
    basketItem.setAttribute('data-id', menuItem.id);

    const itemName = document.createElement('span');
    itemName.textContent = menuItem.name;
    basketItem.appendChild(itemName);

    const priceDisplay = document.createElement('span');
    priceDisplay.className = 'item-price';
    priceDisplay.textContent = `£${menuItem.price.toFixed(2)}`;
    basketItem.appendChild(priceDisplay);

    const quantityControl = createQuantityControl(basketItem);
    basketItem.appendChild(quantityControl);

    return basketItem;
}


function createQuantityControl(basketItemHtml) {
    const quantityControl = document.createElement('div');
    quantityControl.className = 'quantity-control';

    const decreaseButton = document.createElement('button');
    decreaseButton.textContent = '-';
    decreaseButton.onclick = () => updateItemQuantity(basketItemHtml, -1);

    const quantityDisplay = document.createElement('span');
    quantityDisplay.className = 'quantity';
    quantityDisplay.textContent = basketItemHtml.basketItemData ? basketItemHtml.basketItemData.quantity: 1;

    const increaseButton = document.createElement('button');
    increaseButton.textContent = '+';
    increaseButton.onclick = () => updateItemQuantity(basketItemHtml, 1);

    quantityControl.appendChild(decreaseButton);
    quantityControl.appendChild(quantityDisplay);
    quantityControl.appendChild(increaseButton);

    return quantityControl;
}

function updateItemQuantity(basketItemHtml, change) {
    if (basketItemHtml && basketItemHtml.basketItemData) {
        const basketItemData = basketItemHtml.basketItemData;
        basketItemData.quantity = Math.max(0, basketItemData.quantity + change); 

        const quantityDisplay = basketItemHtml.querySelector('.quantity');
        quantityDisplay.textContent = basketItemData.quantity;

        if (basketItemData.quantity === 0) {
            basket.items = basket.items.filter(item => item.menuItem.id !== basketItemData.menuItem.id);
            basketItemHtml.remove();
        } else {
            const priceDisplay = basketItemHtml.querySelector('.item-price');
            const newPrice = basketItemData.price * basketItemData.quantity;
            priceDisplay.textContent = `£${newPrice.toFixed(2)}`;
        }
    }
}

async function loadVendorItems(vendorId) {
    const items = await fetchItems(vendorId);
    if (items.length > 0) {
        populateGrid(items)
    }
}

function loadSavedBasket(){
    for (bItem of basket.items){
        addOrUpdateBasketItem(bItem.menuItem, 0);
    }
}

function cacheBasket() {
    const index = findIndex(restaurantName, baskets);
    if (index === -1){
        baskets.push({id: basket.id, items: basket.items, restName: restaurantName})
    } else {
        baskets[index] = {id: basket.id, items: basket.items, restName: restaurantName};
    }
    sessionStorage.setItem('baskets', JSON.stringify(baskets));
}

document.addEventListener('DOMContentLoaded', function () {
    const vendorInfoElement = document.getElementById('vendor-info'); 
    const vendorId = vendorInfoElement.getAttribute('data-id');
    restaurantName = getRestaurantName();
    baskets = getBasketsFromCache();
    const basketResult = getSavedBasket();
    if (basketResult) {
        basket.id = basketResult.id;
        if (basketResult.items){
            basket.items = basketResult.items;
        }
        loadSavedBasket();
    }
    if (vendorId) {
        loadVendorItems(vendorId);
    }  

    const checkoutButton = document.querySelector('.checkout-button'); 
    checkoutButton.addEventListener('click', async function () {
        try {
            basket.id ? basket : await postBasket();
            await handleBasketItems();
            cacheBasket();
            window.location.href = "/checkout";
        } catch (error) {
            alert("Error creating basket: " + error)
        }
    });

    const addBasketItem = document.querySelector('.update-basket-button');
    addBasketItem.addEventListener('click', async function () {
        try {
            basket.id ? basket : await postBasket();
            await handleBasketItems();
            cacheBasket();
        } catch (error) {
            alert("Error creating basket: " + error)
        }
    });
});

async function postBasket(){
    const response = await fetch("/baskets", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            order: basket.order
        }),
    });
    if (!response.ok) {
        throw new Error(`HTTP error posting basket. Status: ${response.status}`);
    }
    const responseBasket = await response.json();
    basket.id = responseBasket.id;
}

async function handleBasketItems(){
    const updates = basket.items.map(async (item) => {
        const endpoint = item.id ? `/basketItems/${basket.id}` : '/basketItems';
        const method =  item.id ? 'PATCH' : 'POST';
        const bItemResponse = await fetch(endpoint, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                basket: basket,
                menuItem: item.menuItem,
                quantity: item.quantity
            }), 
        });
        if (!bItemResponse.ok){
            throw new Error(`HTTP error posting item. Status: ${bItemResponse.status}`)
        }
        return await bItemResponse.json();
    });

    const updatedItems = await Promise.all(updates);
    basket.items = updatedItems.map((item, index) => ({
        ...basket.items[index],
        ...item,
    }))

    try {
        const response = await fetch(`/basketItems/baskets/${basket.id}`);
        if (!response.ok) {
            throw new Error(`HTTP error fetching items for deletion. Status: ${response.status}`);
        }
        const previousItems = await response.json();
        const localItemIds = new Set(basket.items.map(item => item.id));
        const itemsToDelete = previousItems.filter(item => !localItemIds.has(item.id));
        const deletePromises = itemsToDelete.map(item =>
            fetch(`/basketItems/${item.id}`, {
                method: 'DELETE'
            })
        );

        await Promise.all(deletePromises);
        console.log("Items successfully deleted from the server.");
    } catch (error) {
        console.error("Error deleting items:", error);
    }
 }
