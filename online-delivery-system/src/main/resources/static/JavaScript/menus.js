async function fetchRestaurants(page = 0, size = 10) {
    try {
        const response = await fetch(`http://localhost:8080/vendors?page=${page}&size=${size}`); // Adjust the URL to your API endpoint
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
    gridContainer.innerHTML = ''; // Clear existing content

    if (pageData && pageData.content) {
        pageData.content.forEach((restaurant) => {
            const gridItem = document.createElement('div');
            gridItem.className = 'grid-item';
            gridItem.textContent = restaurant.name; // Assuming 'name' is a property of restaurant

            // Add click event to navigate to the menu page
            gridItem.addEventListener('click', () => {
                window.location.href = `/menu-page?restaurantId=${restaurant.id}`; // Adjust URL as needed
            });

            gridContainer.appendChild(gridItem);
        });
    }
}

let currentPage = 1;

async function initPage() {
    const restaurants = await fetchRestaurants(currentPage);
    if (restaurants) {
        populateGrid(restaurants);
    }

    document.getElementById('prev-page').addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            updatePage();
        }
    });

    document.getElementById('next-page').addEventListener('click', () => {
        currentPage++;
        updatePage();
    });
}

async function updatePage() {
    const restaurants = await fetchRestaurants(currentPage - 1); // Adjust for zero-based page index
    if (restaurants) {
        populateGrid(restaurants);
        document.getElementById('current-page').textContent = currentPage; // Display is 1-based
    }
}

window.onload = initPage;

