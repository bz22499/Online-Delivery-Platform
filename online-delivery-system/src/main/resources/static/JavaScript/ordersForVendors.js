let isLoading = false;
let currentPage = 0;
let loggedInVendorEmail = ''; // Define a variable to store the logged-in vendor's email

// Fetch orders associated with the currently logged-in vendor
async function fetchOrders(page= 0, size = 10) {
    try {
        const response = await fetch(`/orders?page=${page}&size=${size}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching order data:', error);
    }
}

// Fetch baskets associated with an order
async function fetchBasketsByOrder(orderId) {
    try {
        const response = await fetch(`/baskets/orders/${orderId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const baskets = await response.json();

        // Fetch basket items for each basket
        for (const basket of baskets) {
            const basketItemsResponse = await fetch(`/basketItems/baskets/${basket.id}`);
            if (!basketItemsResponse.ok) {
                throw new Error(`HTTP error! status: ${basketItemsResponse.status}`);
            }
            const basketItems = await basketItemsResponse.json();
            basket.basketItems = basketItems; // Assign fetched basketItems to basket object
        }

        return baskets;
    } catch (error) {
        console.error('Error fetching baskets:', error);
        return null;
    }
}

// Populate orders associated with the currently logged-in vendor
async function populateOrders(ordersData) {
    const gridContainer = document.querySelector('.grid-container');

    if (ordersData && ordersData.content) {
        for (const order of ordersData.content) {
            // Check if the order contains menu items associated with the logged-in vendor
            const hasMenuItemsForVendor = order.baskets.some(basket =>
                basket.basketItems.some(item =>
                    item.menuItem.vendor.email === loggedInVendorEmail
                )
            );

            if (!hasMenuItemsForVendor) {
                continue; // Skip orders that don't have menu items for the logged-in vendor
            }

            const orderItem = document.createElement('div');
            orderItem.className = 'order-item';

            const orderInfo = document.createElement('div');
            orderInfo.textContent = `Order ID: ${order.id}`;
            orderItem.appendChild(orderInfo);

            const basketList = document.createElement('ul');
            basketList.className = 'basket-list';

            for (const basket of order.baskets) {
                for (const basketItem of basket.basketItems) {
                    const menuItem = basketItem.menuItem;
                    if (menuItem.vendor.email !== loggedInVendorEmail) {
                        continue; // Skip menu items not associated with the logged-in vendor
                    }
                    const basketItemInfo = document.createElement('li');
                    const totalPrice = menuItem ? (menuItem.price * basketItem.quantity).toFixed(2) : 'N/A';
                    basketItemInfo.textContent = `Basket ID: ${basket.id}, Basket Item ID: ${basketItem.id}, Menu Item Name: ${menuItem.name}, Quantity: ${basketItem.quantity}, Price per Item: ${menuItem.price.toFixed(2)}, Total Price: ${totalPrice}`;
                    basketList.appendChild(basketItemInfo);
                }
            }

            if (basketList.childNodes.length > 0) {
                orderItem.appendChild(basketList);
            } else {
                const noBasketInfo = document.createElement('div');
                noBasketInfo.textContent = 'No basket items found for this order associated with the logged-in vendor.';
                orderItem.appendChild(noBasketInfo);
            }

            gridContainer.appendChild(orderItem);
        }
    }
}

// Function to load more orders
async function loadMore() {
    if (isLoading) return;
    isLoading = true;
    const orders = await fetchOrders(currentPage, 10);
    if (orders && orders.content.length > 0) {
        await populateOrders(orders);
        currentPage++;
    }
    isLoading = false;
}

// Load initial set of orders when the window loads
window.onload = async () => {
    // Fetch the currently logged-in vendor's email from the hidden div
    loggedInVendorEmail = document.getElementById('loggedInVendorEmail').getAttribute('data-email');
    await loadMore();
};

// Load more orders when scrolling near the bottom of the page
window.addEventListener('scroll', () => {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100 && !isLoading) {
        loadMore();
    }
});

// Toggle visibility of baskets dropdown on button click
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('viewBasketsButton').addEventListener('click', async function(event) {
        event.preventDefault();
        const dropdown = document.getElementById('basketsDropdown');
        dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none';
    });
});
