document.getElementById("toggleDetailsBtn").addEventListener("click", function() {
var detailsDiv = document.getElementById("orderDetails");
if (detailsDiv.style.display === "none") {
detailsDiv.style.display = "block";
} else {
detailsDiv.style.display = "none";
}
});

function loadOrders() {
fetch('/api/orders')
.then(response => response.json())
.then(orders => {
    const ordersContainer = document.getElementById('orders');
    ordersContainer.innerHTML = '';
    orders.forEach(order => {
        const orderElement = document.createElement('div');
        orderElement.innerHTML = `Order number:${order.id} - address:${order.address}`;
        ordersContainer.appendChild(orderElement);
    });
});
}
loadOrders();