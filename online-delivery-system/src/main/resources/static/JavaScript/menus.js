let isFetchingBaskets = false; // global state to signal when fetching baskets is done
let currentPage = 0;
let isLoading = false;

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


function populateGrid(pageData) { // populates the grid that shows all the restaurants
    const gridContainer = document.querySelector('.grid-container');

    if (pageData && pageData.content) { // if there is pageData and there are >0 items
        pageData.content.forEach((restaurant) => { // for each data object (restaurant)
            const gridItem = document.createElement('div'); // create html div
            gridItem.className = 'grid-item'; // set classname

            // create image element
            const image = document.createElement('img'); // create an image element
            if(restaurant.imageUrl != null){
                image.src = "uploads/" + restaurant.imageUrl
            }else{
                image.src = 'images/wix1.png';
            }

            image.alt = restaurant.name;
            image.className = 'restaurant-image'; // set image class

            // Add image to div (gridItem)
            gridItem.appendChild(image);

            // Restaurant name footer
            const footer = document.createElement('div'); // create div for restaurant name (was located in the foot of the image at first)
            footer.className = 'grid-item-footer';
            footer.textContent = restaurant.name;
            gridItem.appendChild(footer); // add footer to div (gridItem)

            const rating = document.createElement('div'); // create div for rating
            rating.className = 'rating';
            rating.textContent = restaurant.rating.toFixed(2); // set to a double digit float
            gridItem.appendChild(rating); // add to the div (gridItem)


            // Nav to menu page
            gridItem.addEventListener('click', () => { // listener for clicks on gridItem
                if (restaurant.email) { // if email isn't null
                    sessionStorage.setItem('restaurantEmail', restaurant.email);
                    window.location.href = `/${encodeURIComponent(restaurant.email)}/menu-page`; // nav to restaurant page (created using the power of thymeleaf)
                }
                else {
                    console.warn('Restaurant email is undefined:', restaurant);
                }
            });
            gridContainer.appendChild(gridItem); // add the gridItem (the restaurant box we can see in order page) to the grid
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
        total = item.quantity * item.menuItem.price // we can access menuItems directly (since we used these to build basketItems)
    }
    return total
}


function getBasketItemsFromCache(basketId) { // basketItems retrieval from cache
    const basketsString = sessionStorage.getItem('baskets');
    const baskets = JSON.parse(basketsString);
    return {items: baskets[basketId].items, restName: baskets[basketId].restName};
}

async function preloadBaskets() {
    const orderId = sessionStorage.getItem('orderId');
    if (orderId) {
        const baskets = await fetchBasketsByOrder(orderId);
        sessionStorage.setItem('basketIDs', JSON.stringify(baskets)); // add basket IDs to cache
        isFetchingBaskets = false; // ensure the fetching and saving process is marked as done
    } else {
        isFetchingBaskets = false;
    }
}


// populate the dropdown button
async function populateBasketsDropdown() {
    const dropdown = document.getElementById('basketsDropdown'); // given in html (this is the dropdown button)
    dropdown.innerHTML = ''; // have to clear previous items because it grows infinitely otherwise

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
        editButton.textContent = 'View baskets';
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
/*
// Get query parameters from the URL
var urlParams = new URLSearchParams(window.location.search);
// Retrieve data from query parameters
var address = urlParams.get('address');
*/

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
    isFetchingBaskets = true; // set fetching to true from the beginning so users can see lading message naturally
    await loadMore();
    await createOrder();
    await preloadBaskets().then(populateBasketsDropdown); // call preload then populate
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
            dropdown.innerHTML = 'Loading baskets...'; // set as loading in case it's still fetching
        }
        dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none'; // toggle display
    });
});


