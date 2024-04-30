let isLoading = false;
let currentPage = 0;
let driverId = null;
let orders = []

async function fetchOrders() {
    try {
        const response = await fetch('/orders/status/COLLECTION');
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
        const fetchItemsPromises = baskets.map(basket =>
            fetch(`/basketItems/baskets/${basket.id}`).then(async response => {
                if (!response.ok) {
                    throw new Error(`HTTP error. Status: ${response.status}`);
                }
                const basketItems = await response.json();
                basket.basketItems = basketItems;
                return basket;
            })
        );
        return await Promise.all(fetchItemsPromises);
    } catch (error) {
        console.error('Error fetching baskets:', error);
        return null;
    }
}

async function preLoadData(){
    list = []
    const storedData = sessionStorage.getItem('orders');

    const orders = await fetchOrders();
    const promises = orders.map(async (order) => {
        const baskets = await fetchBasketsByOrder(order.id);
        let vendorAddress = null;

        if (baskets.length > 0 && baskets[0].basketItems.length > 0) {
            vendorAddress = await fetchVendorAddress(baskets[0].basketItems[0].menuItem.vendor.email);
        }

        return {
            order: order,
            baskets: baskets,
            vendorAddress: vendorAddress 
        };
    });

    list = await Promise.all(promises);
    console.log("Finished preloading");
    return list;
}

async function fetchVendorAddress(vendorEmail) {
    try {
        const response = await fetch(`/vendorAddresses/vendor/${vendorEmail}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching vendor address:', error);
        return null;
    }
}

async function fetchDistance(postcode1, postcode2) {
    try {
        const response = await fetch('/calculate-distance', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ postcode1, postcode2 })
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const distance = await response.json();
        return distance;
    } catch (error) {
        console.error('Error fetching distance:', error);
        return null;
    }
}

async function populateOrders(ordersData) {
    console.log("populating");
    const gridContainer = document.querySelector('.grid-container');
    console.log(orders);
    for (const object of orders){
        const orderItem = document.createElement('div');
        orderItem.className = 'order-item';
        const vendorAddress = object.vendorAddress

        const userPostcode = object.order.userAddress ? object.order.userAddress.postCode : null;
        const userAddressDisplay = userPostcode ? `Street: ${object.order.userAddress.street}, Postcode: ${userPostcode}` : 'N/A';
        const orderInfo = document.createElement('div');
        const orderId = document.createElement("p");
        orderId.textContent = `Order ID: ${object.order.id}`
        const userAddressHtml = document.createElement("p");
        userAddressHtml.textContent = `User Address: ${userAddressDisplay}`;
        orderInfo.appendChild(orderId);
        orderInfo.appendChild(userAddressHtml);
        orderItem.appendChild(orderInfo);

        if (object.baskets && object.baskets.length > 0) {
            const basketList = document.createElement('ul');
            basketList.className = 'basket-list';
            for (const basket of object.baskets) {
                const basketItems = basket.basketItems;
                for (const basketItem of basketItems) {
                    const menuItem = basketItem.menuItem;
                    const vendorPostcode = vendorAddress ? vendorAddress.postCode : null;
                    const vendorAddressDetails = vendorPostcode ? `Vendor Street: ${vendorAddress.street}, Vendor Postcode: ${vendorPostcode}` : 'N/A';
                    const totalPrice = menuItem ? (menuItem.price * basketItem.quantity).toFixed(2) : 'N/A';
                    const basketItemInfo = document.createElement('li');
                    basketItemInfo.textContent = `Menu Item Name: ${menuItem.name}, Quantity: ${basketItem.quantity}, Vendor Address: ${vendorAddressDetails}`;
                    basketList.appendChild(basketItemInfo);
                }
            }
            orderItem.appendChild(basketList);
            gridContainer.appendChild(orderItem);
        } else {
            console.log('No basket items found for this order.');
        }

        const confirmButton = document.createElement('button');
        confirmButton.textContent = 'Confirm Collection';
        confirmButton.addEventListener('click', async () => {
            const confirmation = confirm("Are you sure you want to collect this order?");
            if (confirmation) {
                order.status = driverId;
                const response = await fetch(`/orders/${order.id}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(order)
                });
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const responseData = await response.json();
                console.log('Response from PATCH request:', responseData);
                console.log("Order status updated to driver id");
                const orderInfoElement = document.getElementById(`order-${order.id}-info`);
                if (orderInfoElement) {
                    orderInfoElement.textContent = `Status: ${order.status}`;
                }
            }
        });
        orderItem.appendChild(confirmButton);
    }
    console.log("populated");
}

async function loadMore() {
    console.log("loadmore");
    if (isLoading) return;
    isLoading = true;
    const driverIdElement = document.getElementById('driverId');
    driverId = driverIdElement.value;
    if (orders && orders.length > 0) {
        console.log("hit load");
        await populateOrders(orders);
    }
    isLoading = false;
}

window.onload = async () => {
    orders = await preLoadData();
    await loadMore();
};

// window.addEventListener('scroll', () => {
//     if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100 && !isLoading) {
//         loadMore();
//     }
// });
