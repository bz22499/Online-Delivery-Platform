async function fetchRestaurants(page =0, size = 18) {
    try {
        const response = await fetch(`/vendors?page=${page}&size=${size}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching restaurant data:', error);
    }
}

async function createOrder() {
    const existingOrder = sessionStorage.getItem('orderId') // check if order has already been created for the session. If so, don't proceed with order creation
    if (existingOrder) {
        return;
    }
    try {
        const response = await fetch(`/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const order = await response.json();
        sessionStorage.setItem('orderId', order.id);
    } catch (error) {
        console.log('Error creating order: ', error)
    }

}

// Luke's bit
async function fetchAllRestaurants(){
    try {
        const response = await fetch(`/vendors`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching restaurant data:', error);
    }
}

function populateGrid(pageData) {
    const gridContainer = document.querySelector('.grid-container');

    if (pageData && pageData.content) {
        pageData.content.forEach((restaurant) => {
            const gridItem = document.createElement('div');
            gridItem.className = 'grid-item';
            gridItem.textContent = restaurant.name;

            // Create image element
            const image = document.createElement('img');
            // Set the image source to the restaurant's image URL or to the default image
            image.src = restaurant.imageUrl || 'images/logo_ld.jpeg';
            image.alt = restaurant.name;
            image.className = 'restaurant-image';

            // Add image
            gridItem.appendChild(image);

            // Restaurant name footer
            const footer = document.createElement('div');
            footer.className = 'grid-item-footer';
            footer.textContent = restaurant.name;
            gridItem.appendChild(footer);

            // Nav to menu page
            gridItem.addEventListener('click', () => {
                if (restaurant.email) {
                    window.location.href = `/${encodeURIComponent(restaurant.email)}/menu-page`;
                }
                else {
                    console.warn('Restaurant email is undefined:', restaurant);
                }
            });
            gridContainer.appendChild(gridItem);
        });

    }

}

// get all the baskets for current order ID
async function fetchBasketsByOrder(orderId) {
    try {
        const response = await fetch(`/baskets/orders/${orderId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching baskets:', error);
        return null;
    }
}

function calculateTotal(items) {
    let total = 0;
    for (const item of items) {
        total = item.quantity * item.menuItem.price
    }
    return total
}

function getBasketItemsFromCache(basketId) {
    const basketsString = sessionStorage.getItem('baskets')
    const baskets = JSON.parse(basketsString)
    return {items: baskets[basketId].items, restName: baskets[basketId].restName};
}


// populate the dropdown button
async function populateBasketsDropdown(baskets) {
    const dropdown = document.getElementById('basketsDropdown'); // given in html (this is the dropdown button)
    if (baskets && baskets.length) { // check initialisation
        for (const basket of baskets) { // display info for all baskets in cache (all created baskets)
            try {
                let basketData = getBasketItemsFromCache(basket.id);
                let totalPrice= calculateTotal(basketData.items);
                const basketElement = document.createElement('div');
                basketElement.textContent = `Restaurant: ${basketData.restName}, Total: £${totalPrice.toFixed(2)}`;
                dropdown.appendChild(basketElement);
            } catch (error) {
                console.error('Error populating baskets dropdown:', error);
            }
        }

    } else {
        dropdown.textContent = 'No active baskets';
    }
}

// Luke's bit
// Get query parameters from the URL
var urlParams = new URLSearchParams(window.location.search);
// Retrieve data from query parameters
var address = urlParams.get('address');

let currentPage = 0;
let isLoading = false;

async function loadMore() {

    if (isLoading) return;
    isLoading = true;

    const restaurants = await fetchRestaurants(currentPage,18);
    if (restaurants && restaurants.content.length > 0) {
        populateGrid(restaurants);
        currentPage++;
    }

    isLoading = false;
}

window.onload = loadMore();
createOrder();
window.addEventListener('scroll', () => {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100 && !isLoading) {
        loadMore();
    }
});

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('viewBasketsButton').addEventListener('click', async function() {
        const orderId = sessionStorage.getItem('orderId');
        if (!orderId) {
            alert('No order found.');
            return;
        }
        const baskets = await fetchBasketsByOrder(orderId);
        if (baskets) {
            populateBasketsDropdown(baskets);
        }
        const dropdown = document.getElementById('basketsDropdown');
        dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none'; // toggle display
    });
});




