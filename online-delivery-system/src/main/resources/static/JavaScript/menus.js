let isFetchingBaskets = false;

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

async function preloadBaskets() {
    const orderId = sessionStorage.getItem('orderId');
    if (orderId) {
        const baskets = await fetchBasketsByOrder(orderId);
        sessionStorage.setItem('basketIDs', JSON.stringify(baskets))
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

            // Create image element
            const image = document.createElement('img');
            // Set the image source to the restaurant's image URL or to the default image
            image.src = restaurant.imageUrl || 'images/wix1.png';
            image.alt = restaurant.name;
            image.className = 'restaurant-image';

            // Add image
            gridItem.appendChild(image);

            // Restaurant name footer
            const footer = document.createElement('div');
            footer.className = 'grid-item-footer';
            footer.textContent = restaurant.name;
            gridItem.appendChild(footer);

            const rating = document.createElement('div');
            rating.className = 'rating';
            rating.textContent = restaurant.rating.toFixed(2);
            gridItem.appendChild(rating);


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
    const basketsString = sessionStorage.getItem('baskets');
    const baskets = JSON.parse(basketsString);
    return {items: baskets[basketId].items, restName: baskets[basketId].restName};
}

async function preloadBaskets() {
    isFetchingBaskets = true;
    const orderId = sessionStorage.getItem('orderId');
    if (orderId) {
        try {
            const baskets = await fetchBasketsByOrder(orderId);
            sessionStorage.setItem('basketIDs', JSON.stringify(baskets));
        } finally {
            isFetchingBaskets = false;
            if (document.getElementById('basketsDropdown').style.display === 'block') {
                populateBasketsDropdown();
            }
        }
    } else {
        isFetchingBaskets = false;
    }
}


// populate the dropdown button
async function populateBasketsDropdown() {
    const dropdown = document.getElementById('basketsDropdown'); // given in html (this is the dropdown button)
    dropdown.innerHTML = ''; // had to clear previous items because it grows infinitely otherwise

    const basketsString = sessionStorage.getItem('basketIDs');
    const baskets = JSON.parse(basketsString);

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

        const editButton = document.createElement('button');
        editButton.textContent = 'Edit baskets';
        editButton.className = 'edit-baskets-button';
        editButton.addEventListener('click', function () {
            window.location.href = `/baskets-overview`
        });
        dropdown.appendChild(editButton)

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


window.onload = async () => {
    await loadMore();
    createOrder();
    await preloadBaskets();
}


window.addEventListener('scroll', () => {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100 && !isLoading) {
        loadMore();
    }
});


document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('viewBasketsButton').addEventListener('click', async function(event) {
        event.preventDefault(); // to stop auto-scroll when clicking view basket (from gpt)
        const dropdown = document.getElementById('basketsDropdown');
        if (isFetchingBaskets) {
            dropdown.innerHTML = 'Loading baskets...'
        } else {
            populateBasketsDropdown()
        }
        dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none'; // toggle display
    });
});


