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

function getBasketItemsFromCache(basketId) {
    const basketsString = sessionStorage.getItem('baskets');
    const baskets = JSON.parse(basketsString);
    return {items: baskets[basketId].items, restName: baskets[basketId].restName};
}


async function displayBaskets(baskets) {
    const container = document.getElementById('baskets-container');
    container.innerHTML = ''; // Clear existing content
    if (baskets && baskets.length) {
        baskets.forEach(basket => {
            const basketDiv = document.createElement('div');
            basketDiv.className = 'basket';

            const itemsList = document.createElement('ul');
            let basketData = getBasketItemsFromCache(basket.id);
            basketData.items.forEach(item => {
                const itemLi = document.createElement('li');
                // Directly display item name and quantity
                itemLi.textContent = `${item.menuItem.name} - Quantity: ${item.quantity}`;
                itemsList.appendChild(itemLi);
            });

            basketDiv.appendChild(itemsList);
            container.appendChild(basketDiv);
        });
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    const orderId = sessionStorage.getItem('orderId');
    if (!orderId) {
        alert('No order found.');
        return;
    }
    const baskets = await fetchBasketsByOrder(orderId);
    if (baskets) {
        displayBaskets(baskets);
    }
});