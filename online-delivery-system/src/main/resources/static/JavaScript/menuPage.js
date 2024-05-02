let order = getOrderFromCache();

let basket = {
    order: order,
    items: []
};

function findIndex(name, basketsList){
    let i = 0;
    for (let b of basketsList){
        if (b.restName === name){
            return i
        }
        i++;
    }
    return -1;
}


function getSavedBasketIndex(name, basketsList){
    const index = findIndex(name, basketsList);
    if (index === -1){
        return
    } else {
        return index;
    }
}


function getOrderFromCache(){
    const orderString = sessionStorage.getItem("order");
    const order = JSON.parse(orderString);
    return order
}


function getBasketsFromCache(){
    let baskets = [];
    const basketsString = sessionStorage.getItem('baskets');
    if (basketsString) {
        baskets = JSON.parse(basketsString);
    }
    return baskets;
}


function getRestaurantName(){
    const restaurantNameDiv = document.querySelector('.name')
    const restaurantName = restaurantNameDiv.textContent
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


async function hasAddress(email) {
    try {
        const response = await fetch(`/addresses/user/${email}`);
        return response.ok;
    } catch (error) {
        console.error('Error fetching user address:', error);
        return false;
    }
}


// populate grid with given menu items
function populateGrid(pageData) {
    const gridContainer = document.querySelector('.grid-container');
    pageData.forEach((item) => {
        const gridItem = document.createElement('div');
        gridItem.className = 'grid-item';

        const gridData = document.createElement('div');
        gridData.className = 'grid-data';
        gridItem.appendChild(gridData);

        const gridImg = document.createElement('div');
        gridImg.className = 'grid-img';
        gridImg.style.backgroundImage = "url('/images/wix1.png')"
        gridItem.appendChild(gridImg);

        //check if the image exists
        var ImgUrl = "/uploads/" + "_" + item.id + "_.jpg";
        let img = new Image();
        img.onload = function() {
            gridImg.style.backgroundImage =  "url('"+ImgUrl+"')"
        };
        img.src = ImgUrl;


        const gridTitle = document.createElement('div')
        gridTitle.className='grid-item-title'
        gridTitle.textContent = item.name;
        gridData.appendChild(gridTitle)


        const description = document.createElement('div');
        description.className = 'grid-item-description';
        description.textContent = item.description;
        gridData.appendChild(description);

        const footer = document.createElement('div');
        footer.className = 'grid-item-footer';
        footer.textContent = "£"+item.price.toFixed(2);
        gridData.appendChild(footer);

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
        updateSubtotal();
    } else {
        if (!basketItemHtml){
            basketItemHtml = createBasketItemHtml(menuItem); 
            document.querySelector('.basket-items').appendChild(basketItemHtml); 
            basketItemHtml.basketItemData = basketItemData;
        };
        updateItemQuantity(basketItemHtml, qty); 
        updateSubtotal();
    }
}


function createBasketItemHtml(menuItem) {
    const basketItem = document.createElement('div');
    basketItem.className = 'basket-item';
    basketItem.setAttribute('data-id', menuItem.id);

    const itemInfo = document.createElement('div');
    itemInfo.className = 'item-info';

    const itemName = document.createElement('span');
    itemName.textContent = menuItem.name;
    itemInfo.appendChild(itemName);

    const priceDisplay = document.createElement('span');
    priceDisplay.className = 'item-price';
    priceDisplay.textContent = `Total: £${menuItem.price.toFixed(2)}`;
    itemInfo.appendChild(priceDisplay);

    
    basketItem.appendChild(itemInfo);

    const quantityControl = createQuantityControl(basketItem);
    basketItem.appendChild(quantityControl);

    return basketItem;
}


function createQuantityControl(basketItemHtml) {
    const quantityControl = document.createElement('div');
    quantityControl.className = 'quantity-control';

    const decreaseButton = document.createElement('button');
    decreaseButton.innerHTML = '<i class="fa fa-minus"></i>';
    decreaseButton.onclick = () => updateItemQuantity(basketItemHtml, -1);

    const quantityDisplay = document.createElement('span');
    quantityDisplay.className = 'quantity';
    quantityDisplay.textContent = basketItemHtml.basketItemData ? basketItemHtml.basketItemData.quantity: 1;

    const increaseButton = document.createElement('button');
    increaseButton.innerHTML = '<i class="fa fa-plus"></i>';
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
            priceDisplay.textContent = `Total: £${newPrice.toFixed(2)}`;
        }
        updateSubtotal();
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


function cacheBasket(name, basketsList) {
    const index = findIndex(name, basketsList);
    if (index === -1){
        basketsList.push({id: basket.id, items: basket.items, restName: name});
    } else {
        basketsList[index] = {id: basket.id, items: basket.items, restName: name};
    }
    sessionStorage.setItem('baskets', JSON.stringify(basketsList));
    return basketsList
}

function deleteFromCache(name, basketsList) {
    const index = findIndex(name, basketsList);
    if (index === -1) {
        return;
    } else {
        basketsList.splice(index, 1);
    }
    sessionStorage.setItem('baskets', JSON.stringify(basketsList));
    return basketsList
}

function updateSubtotal(){
    const subtotal = document.querySelector('.subtotal');
    let total = 0;
    for (const basketItem of basket.items){
        total = total + (basketItem.quantity * basketItem.price);
    }
    subtotal.textContent = `Subtotal: £${total.toFixed(2)}`;
}

document.addEventListener('DOMContentLoaded', function () {
    const updateBasket = document.getElementById('updateBasketButton');
    const checkoutButton = document.getElementById('checkoutButton');
    const backButton = document.getElementById('back-button');
   
    if(updateBasket && checkoutButton){
        updateBasket.disabled = false;
        checkoutButton.disabled = false;
        backButton.disabled = false;
    }

    const vendorInfoElement = document.getElementById('vendor-info'); 
    const vendorId = vendorInfoElement.getAttribute('data-id');
    if (vendorId) {
        loadVendorItems(vendorId);
    }  

    const restaurantName = getRestaurantName();
    let baskets = getBasketsFromCache();
    const basketResultIndex = getSavedBasketIndex(restaurantName, baskets);
    const basketResult = baskets[basketResultIndex];
    if (basketResult) {
        basket.id = basketResult.id;
        if (basketResult.items.length > 0){
            basket.items = basketResult.items;
        }
        loadSavedBasket();
    }
    updateSubtotal();

    backButton.addEventListener('click', function() {
        window.history.back();
    });
 
    checkoutButton.addEventListener('click', async function () {
        // if(addBasketItem && checkoutButton){
        //     checkoutButton.disabled = true;
        //     addBasketItem.disabled = true;
        // }
        // if (basket.items.length > 0){
        //     try {
        //         basket.id ? basket : await postBasket();
        //         await handleBasketItems();
        //         baskets = cacheBasket(restaurantName, baskets);
        //         if(addBasketItem && checkoutButton){
        //             checkoutButton.disabled = false;
        //             addBasketItem.disabled = false;
        //         }
        //     } catch (error) {
        //         alert("Error creating basket: ", error);
        //         if(addBasketItem && checkoutButton){
        //             checkoutButton.disabled = false;
        //             addBasketItem.disabled = false;
        //         }
        //     }
        // } else {
        //     if (basket.id) {
        //         await handleBasketItems();
        //         await deleteBasket();
        //         baskets = deleteFromCache(restaurantName, baskets);
        //     }
        //     if(addBasketItem && checkoutButton){
        //         checkoutButton.disabled = false;
        //         addBasketItem.disabled = false;
        //     }
        // }
        window.location.href = "/checkout";    
    });


    updateBasket.addEventListener('click', async function () {
        if(updateBasket && checkoutButton){
            checkoutButton.disabled = true;
            updateBasket.disabled = true;
            backButton.disabled = true;
        }
        if (basket.items.length > 0){
            try {
                basket.id ? basket : await postBasket();
                await handleBasketItems();
                baskets = cacheBasket(restaurantName, baskets);
                if(updateBasket && checkoutButton){
                    checkoutButton.disabled = false;
                    updateBasket.disabled = false;
                    backButton.disabled = false;
                }
            } catch (error) {
                alert("Error creating basket: ", error);
                if(updateBasket && checkoutButton){
                    checkoutButton.disabled = false;
                    updateBasket.disabled = false;
                    backButton.disabled = false;
                }
            }
        } else {
            if (basket.id) {
                await handleBasketItems();
                await deleteBasket();
                baskets = deleteFromCache(restaurantName, baskets);
            }
            if(updateBasket && checkoutButton){
                checkoutButton.disabled = false;
                updateBasket.disabled = false;
                backButton.disabled = false;
            }    
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

async function deleteBasket(){
    try {
        const response = await fetch(`/baskets/${basket.id}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error(`HTTP error deleting empty basket. Status: ${response.status}`);
        }
        basket.id = null;
    } catch (error) {
        console.error("Error deleting empty basket: ", error);
    }
}

async function handleBasketItems(){
    const updates = basket.items.map(async (item) => {
        const endpoint = item.id ? `/basketItems/${item.id}` : '/basketItems';
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
    } catch (error) {
        console.error("Error deleting items:", error);
    }
 }