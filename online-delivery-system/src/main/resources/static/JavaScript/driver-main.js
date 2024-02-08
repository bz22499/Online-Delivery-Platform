document.getElementById("showDetails").addEventListener("click", function() {
    var details = document.getElementById("orderDetails");
    if (details.style.display === "none") {
        details.style.display = "block";
    } else {
        details.style.display = "none";
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