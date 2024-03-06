document.addEventListener('DOMContentLoaded', function () {
    const basketSummary = document.querySelector('.basket-summary');
    const totalCost = document.querySelector('.total-cost');
    const payButton = document.querySelector('.pay-button');

    // Function to calculate and display the basket summary and total cost
    function displayBasketSummary() {
        basketSummary.innerHTML = ''; // Clear previous content

        // Iterate through items in the basket
        for (const item of basket.items) {
            const basketItemSummary = document.createElement('div');
            basketItemSummary.textContent = `${item.menuItem.name} x${item.quantity}`;
            basketSummary.appendChild(basketItemSummary);
        }

        // Calculate total cost
        const totalPrice = basket.items.reduce((total, item) => {
            return total + item.menuItem.price * item.quantity;
        }, 0);

        totalCost.textContent = `Total: £${totalPrice.toFixed(2)}`;
    }

    // Display basket summary on page load
    displayBasketSummary();

    // Event listener for the "Pay Now" button
    payButton.addEventListener('click', function () {
        alert('Payment successful! Redirecting to order confirmation page.');

        // Redirect to order confirmation page
        window.location.href = '/order-confirmation';
    });
});