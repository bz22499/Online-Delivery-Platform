async function fetchRestaurants(page = 0, size = 17) {
    try {
        const response = await fetch(`http://localhost:8080/vendors?page=${page}&size=${size}`);
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

            // Add the image to the grid item
            gridItem.appendChild(image);

            // Add restaurant name in a footer
            const footer = document.createElement('div');
            footer.className = 'grid-item-footer';
            footer.textContent = restaurant.name;
            gridItem.appendChild(footer);

            // Add click event to navigate to the menu page
            gridItem.addEventListener('click', () => {
                window.location.href = `/menu-page?restaurantId=${restaurant.id}`;
            });

            gridContainer.appendChild(gridItem);
        });
    }
}

let currentPage = 0;
let isLoading = false;

async function loadMore() {
    if (isLoading) return;
    isLoading = true;

    const restaurants = await fetchRestaurants(currentPage, 17);
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

