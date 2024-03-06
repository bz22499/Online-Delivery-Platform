let basket = {
    order: sessionStorage.getItem('orderId'),
    basketId: null,
    items: []
};

// fetch items from vendor
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


// populate grid with given menu items
function populateGrid(pageData) {
    // Make grid
    const gridContainer = document.querySelector('.grid-container');
    pageData.forEach((item) => {
        // Make grid item
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
        footer.className = 'grid-item-footer'; // footer for grid item that displays the price
        footer.textContent = "£"+item.price;
        gridItem.appendChild(footer); // append footer to grid item

        gridItem.addEventListener('click', function() { // make each grid item a button
            addOrUpdateBasketItem(item); // update basket functionality
        });

        gridContainer.appendChild(gridItem); // add every grid item to the grid container
    });
}

function addOrUpdateBasketItem(item) {
    let basketItem = document.querySelector(`.basket-item[data-id='${item.id}']`); // has item id as identifier in html template
    if (!basketItem) { // if not yet in the basket
        basketItem = createBasketItem(item); // create the item to be displayed in basket
        document.querySelector('.basket-items').appendChild(basketItem); // add basket item to html template
        basket.items.push({ // initialise basket item object
            basket: null,
            menuItem: item,
            quantity: 1
        });
    } else { // if in the basket just add one to the quantity
        const basketItemData = basket.items.find(basketItem => basketItem.menuItem.id === item.id);
        updateItemQuantity(basketItem, 1);
    }
}

// basket item creation
function createBasketItem(item) { // makes item a basket item
    const basketItem = document.createElement('div');
    basketItem.className = 'basket-item';
    basketItem.setAttribute('data-id', item.id); // set item id as id of new element in dom

    const itemName = document.createElement('span');
    itemName.textContent = item.name;
    basketItem.appendChild(itemName);

    const priceDisplay = document.createElement('span');
    priceDisplay.className = 'item-price';
    priceDisplay.textContent = `£${item.price.toFixed(2)}`; // display total price (initialised to item.price)
    basketItem.appendChild(priceDisplay);

    basketItem.price = item.price; // store the unit price for calculations

    const quantityControl = createQuantityControl();
    basketItem.appendChild(quantityControl);

    return basketItem;
}


function createQuantityControl() { // the buttons we see when an item is added to a basket
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

function updateItemQuantity(basketItem, change) { // called when item has already been created
    const itemId = parseInt(basketItem.getAttribute('data-id')); // retrieve item id from dom and parse as int
    const basketItemData = basket.items.find(item => item.menuItem.id === itemId); // search basket.items for basket that contains menuItem.id

    // update basketItem (qty)
    if (basketItemData) { // if find succeded to find said basket item
        basketItemData.quantity = Math.max(0, basketItemData.quantity + change); // ensure quantity isn't negative

        // find quantity display
        const quantityDisplay = basketItem.querySelector('.quantity');
        quantityDisplay.textContent = basketItemData.quantity; //update basket item quantity val

        if (basketItemData.quantity === 0) {
            // remove item from displayed basket items by filtering all basket items different to removed item
            basket.items = basket.items.filter(item => item.menuItem.id !== itemId);

            // remove item from the DOM
            basketItem.remove();
        } else {
            // update price display
            const priceDisplay = basketItem.querySelector('.item-price');
            const newPrice = basketItem.price * basketItemData.quantity;
            priceDisplay.textContent = `£${newPrice.toFixed(2)}`;
        }
    }
}

async function load(vendorId) { // get items by vendor (if there is at least 1 item)
    const items = await fetchItems(vendorId);
    if (items.length > 0) {
        populateGrid(items)
    }
}

// cache created baskets to improve speed of dropdown in order page
function basketCache(basket, basketItems) {
    let restaurantNameDiv = document.querySelector('.name')
    let restaurantName = restaurantNameDiv.textContent
    let baskets = sessionStorage.getItem('baskets'); // get baskets from session storage
    // parse baskets if they exist or initialise o.w.
    if (baskets) {
       baskets = JSON.parse(baskets);
    } else {
        baskets = {}; // this is an empty object (like a dict)
    }
    baskets[basket.id] = {items: basketItems, restName: restaurantName}
    console.log(restaurantName)
    sessionStorage.setItem('baskets', JSON.stringify(baskets));
}

document.addEventListener('DOMContentLoaded', function () { // wait until DOM is fully loaded
    const vendorInfoElement = document.getElementById('vendor-info'); // given in html
    const vendorId = vendorInfoElement.getAttribute('data-id'); // specific field in vendor-info
    if (vendorId) {
        load(vendorId);
    }
    const proceedButton = document.querySelector('.proceed-button'); // create basket button and funcionality
    if (proceedButton) { // check if button exists to avoid null reference
        proceedButton.addEventListener('click', async function () { // checks if proceed is clicked
            const response = await fetch("/baskets", { // post to baskets
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(basket)
            }).then(response => {
                if (response.ok) {
                    return response.json()
                }
                else {
                    throw new Error(`HTTP error. Status: ${response.status}`)
                }
            }).then(savedBasket => { // from saved basket, post basketItems
                basketCache(savedBasket, basket.items) // cache the basket and basketItems
                for (let bi of basket.items) {
                    fetch("/basketItems", {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            basket: savedBasket,
                            menuItem: bi.menuItem, // this data is maintained from first load function
                            quantity: bi.quantity
                        })
                    })
                }
                //redirect to check out page
                window.location.href = '/checkout';

            })
                .then(data => alert("Basket submitted"))
                .catch(error => {
                    console.error("Error creating basket: ", error)
                    alert("Failed to create basket")
                })
        });
    }

    const loginButton = document.querySelector('.login-button');
    if (loginButton) {
        loginButton.addEventListener('click', function () {
            window.location.href = "/login";
        });
    }

});

