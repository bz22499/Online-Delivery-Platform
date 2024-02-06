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
   fetch(`/orders`, {
       method: 'POST',
       headers: {
           'Content-Type': 'application/json'
       }
   })
       .then(response => console.log(response.json()))
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

window.addEventListener('scroll', () => {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100 && !isLoading) {
        loadMore();
    }
});

window.onload = loadMore();
window.onload = createOrder()


