let isLoading = false;
let currentPage = 0;
let vendorId = null;

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

async function populateOrders(ordersData) {
    const gridContainer = document.querySelector('.grid-container');

    if (ordersData && ordersData.content) {
        for (const order of ordersData.content) {
            var matchesVendor = false;

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
                        const totalPrice = menuItem ? (menuItem.price * basketItem.quantity).toFixed(2) : 'N/A';
                        const basketItemInfo = document.createElement('li');
                        basketItemInfo.textContent = `Basket ID: ${basket.id}, Basket Item ID: ${basketItem.id}, Menu Item Name: ${menuItem.name}, Quantity: ${basketItem.quantity}, Price per Item: ${menuItem.price.toFixed(2)}, Total Price: ${totalPrice}`;
                        basketList.appendChild(basketItemInfo);
                        console.log(menuItem.vendor)
                        console.log(menuItem.vendor.email)
                        console.log(vendorId)
                        if(menuItem.vendor.email === vendorId){
                            console.log("yippee, match!")
                            matchesVendor = true
                        }
                    }
                }
                orderItem.appendChild(basketList);
            } else {
                const noBasketInfo = document.createElement('div');
                noBasketInfo.textContent = 'No basket items found for this order.';
                orderItem.appendChild(noBasketInfo);
            }
            const matchesMessage = document.createElement('div');
            if(matchesVendor){
                matchesMessage.textContent = 'This order is for this vendor! yippee!';
            }
            else{
                matchesMessage.textContent = 'NOT FOR THIS VENDOR';
            }
            orderItem.appendChild(matchesMessage);

            // Additional functionality to confirm collection
            const confirmButton = document.createElement('button');
            confirmButton.textContent = 'Confirm Collection';
            confirmButton.addEventListener('click', async () => {
                const confirmation = confirm("Are you sure this order is ready for collection?");
                if (confirmation) {
                    order.status = 'COLLECTION';
                    orderInfo.textContent = `Status: ${order.status}`;
                    // Update order status on the backend
                    try {
                        const response = await fetch(`/orders/${order.id}`, {
                            method: 'PATCH',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({ status: 'COLLECTION' })
                        });
                        if (!response.ok) {
                            throw new Error(`HTTP error! status: ${response.status}`);
                        }
                        console.log("Order status updated to COLLECTION");
                    } catch (error) {
                        console.error('Error updating order status:', error);
                    }
                }
            });
            orderItem.appendChild(confirmButton);

            gridContainer.appendChild(orderItem);
        }
    }
}

// Function to load more orders
async function loadMore() {
    if (isLoading) return;
    isLoading = true;
    const vendorIdElement = document.getElementById('vendorId');
    vendorId = vendorIdElement.value;
    const orders = await fetchOrders(currentPage, 40);
    if (orders && orders.content.length > 0) {
        await populateOrders(orders);
        currentPage++;
    }
    isLoading = false;
}

// Load initial set of orders when the window loads
window.onload = async () => {
    await loadMore();
};

// Load more orders when scrolling near the bottom of the page
window.addEventListener('scroll', () => {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100 && !isLoading) {
        loadMore();
    }
});
