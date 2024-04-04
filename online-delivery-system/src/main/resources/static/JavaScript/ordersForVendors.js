let isLoading = false;
let currentPage = 0;
let loggedInVendorId = ''; // Define a variable to store the logged-in vendor's id

async function fetchOrders(page = 0, size = 10) {
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

async function fetchBasketsByOrder(orderId) {
    try {
        const response = await fetch(`/baskets/orders/${orderId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const baskets = await response.json();
        return baskets;
    } catch (error) {
        console.error('Error fetching baskets:', error);
        return null;
    }
}

async function populateOrders(ordersData) {
    const gridContainer = document.querySelector('.grid-container');

    if (ordersData && ordersData.content) {
        for (const order of ordersData.content) {
            let orderContainsVendorItems = false;

            const orderItem = document.createElement('div');
            orderItem.className = 'order-item';

            const orderInfo = document.createElement('div');
            orderInfo.textContent = `Order ID: ${order.id}`;
            orderItem.appendChild(orderInfo);

            // Fetch baskets associated with the order
            const baskets = await fetchBasketsByOrder(order.id);
            if (baskets && baskets.length > 0) {
                const basketList = document.createElement('ul');
                basketList.className = 'basket-list';
                for (const basket of baskets) {
                    const basketItems = basket.basketItems;
                    for (const basketItem of basketItems) {
                        const menuItem = basketItem.menuItem;
                        if (menuItem.vendor.id === loggedInVendorId) {
                            orderContainsVendorItems = true;
                            const basketItemInfo = document.createElement('li');
                            const totalPrice = menuItem ? (menuItem.price * basketItem.quantity).toFixed(2) : 'N/A';
                            basketItemInfo.textContent = `Basket ID: ${basket.id}, Basket Item ID: ${basketItem.id}, Menu Item Name: ${menuItem.name}, Quantity: ${basketItem.quantity}, Price per Item: ${menuItem.price.toFixed(2)}, Total Price: ${totalPrice}`;
                            basketList.appendChild(basketItemInfo);
                        }
                    }
                }
                if (orderContainsVendorItems) {
                    orderItem.appendChild(basketList);
                    gridContainer.appendChild(orderItem);
                }
            }
            console.log('Order ID:', order.id, 'Contains Vendor Items:', orderContainsVendorItems);
        }
    }
}


// Function to load more orders
async function loadMore() {
    if (isLoading) return;
    isLoading = true;
    const orders = await fetchOrders(currentPage, 30);
    if (orders && orders.content.length > 0) {
        await populateOrders(orders);
        currentPage++;
    }
    isLoading = false;
}

// Load initial set of orders when the window loads
window.onload = async () => {
    // Fetch the currently logged-in vendor's id from the hidden div
    loggedInVendorId = document.getElementById('loggedInVendorEmail').getAttribute('data-email');
    await loadMore();
};

// Load more orders when scrolling near the bottom of the page
window.addEventListener('scroll', () => {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100 && !isLoading) {
        loadMore();
    }
});
