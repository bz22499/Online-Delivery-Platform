const orders = [
    { id: 1, customer: "Jia", address: "BS1 5JA", items: "Burger" },
    { id: 2, customer: "hao", address: "BS8 1JR", items: "Pizza" },
    // more orders
];

function displayOrders() {
    const ordersContainer = document.getElementById('orders');
    ordersContainer.innerHTML = '';
    orders.forEach(order => {
        const orderDiv = document.createElement('div');
        orderDiv.className = 'order';
        orderDiv.innerHTML = `
            <h3>Order #${order.id}</h3>
            <p><strong>Customer:</strong> ${order.customer}</p>
            <p><strong>Address:</strong> ${order.address}</p>
            <p><strong>Order detail:</strong> ${order.items}</p>
            <button onclick="acceptOrder(${order.id})">Pick up</button>
        `;
        ordersContainer.appendChild(orderDiv);
    });
}

function acceptOrder(orderId) {
    console.log("accept order", orderId);
}

document.addEventListener('DOMContentLoaded', displayOrders);
