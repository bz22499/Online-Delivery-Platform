let isFetchingBaskets = false;
let currentPage = 0;
let isLoading = false;

async function fetchRestaurants(page = 0, size = 18) {
  try {
    const response = await fetch(`/vendors?page=${page}&size=${size}`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching restaurant data:", error);
  }
}

async function createOrder() {
  const existingOrder = sessionStorage.getItem("order");
  if (existingOrder) {
    return;
  }
  try {
    const response = await fetch(`/orders`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        userAddress: null,
        status: "PENDING",
      }),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const order = await response.json();
    sessionStorage.setItem("order", JSON.stringify(order));
  } catch (error) {
    console.error("Error creating order: ", error);
  }
}

async function fetchAllRestaurants() {
  try {
    const response = await fetch(`/vendors`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching restaurant data:", error);
  }
}

function populateGrid(pageData) {
  const gridContainer = document.querySelector(".grid-container");

  if (pageData && pageData.content) {
    pageData.content.forEach((restaurant) => {
      const gridItem = document.createElement("div");
      gridItem.className = "grid-item";

      const image = document.createElement("img");
      if (restaurant.imageUrl != null) {
        image.src = "uploads/" + restaurant.imageUrl;
      } else {
        image.src = "images/wix1.png";
      }

      image.alt = restaurant.name;
      image.className = "restaurant-image";

      gridItem.appendChild(image);

      const footer = document.createElement("div");
      footer.className = "grid-item-footer";
      footer.textContent = restaurant.name;
      gridItem.appendChild(footer);

      const rating = document.createElement("div");
      rating.className = "rating";
      rating.textContent = restaurant.rating.toFixed(2);
      gridItem.appendChild(rating);

      // Nav to menu page
      gridItem.addEventListener("click", () => {
        if (restaurant.email) {
          window.location.href = `/${encodeURIComponent(restaurant.email)}/menuPage`;
        } else {
          console.warn("Restaurant email is undefined:", restaurant);
        }
      });
      gridContainer.appendChild(gridItem);
    });
  }
}

async function fetchBasketsByOrder(orderId) {
  try {
    const response = await fetch(`/baskets/orders/${orderId}`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("Error fetching baskets:", error);
    return null;
  }
}

function calculateTotal(items) {
  let total = 0;
  for (const item of items) {
    total = total + (item.quantity * item.menuItem.price);
  }
  return total;
}

async function preloadBaskets() {
  const orderString = sessionStorage.getItem("order");
  const order = JSON.parse(orderString);
  const orderId = order.id;
  if (orderId) {
    const baskets = await fetchBasketsByOrder(orderId);
    sessionStorage.setItem("basketIDs", JSON.stringify(baskets));
    isFetchingBaskets = false
  } else {
    isFetchingBaskets = false;
  }
}

async function populateBasketsDropdown() {
  const dropdown = document.getElementById("basketsDropdown");
  dropdown.innerHTML = "";

  const basketsString = sessionStorage.getItem("baskets");
  const baskets = JSON.parse(basketsString);

  if (baskets && baskets.length) {
    for (const basket of baskets) {
      try {
        const totalPrice = calculateTotal(basket.items);
        const basketElement = document.createElement("div");
        basketElement.className = "basketDiv";
        basketElement.textContent = `Restaurant: ${basket.restName}, Total: £${totalPrice.toFixed(2)}`;
        dropdown.appendChild(basketElement);
      } catch (error) {
        console.error("Error populating baskets dropdown:", error);
      }
    }

    const checkoutButtonDiv = document.createElement("div");
    checkoutButtonDiv.className = "checkout-button";
    const checkoutButton = document.createElement("p");
    const checkoutButtonTarget = document.createElement("a");
    checkoutButtonTarget.textContent = "Checkout";
    checkoutButtonTarget.target = "_parent";
    checkoutButtonTarget.href = "/checkout";
    checkoutButton.appendChild(checkoutButtonTarget);
    checkoutButtonDiv.appendChild(checkoutButton);
    dropdown.appendChild(checkoutButtonDiv);

  } else {
    dropdown.textContent = "No active baskets";
  }
}

async function loadMore() {
  if (isLoading) return;
  isLoading = true;
  const restaurants = await fetchRestaurants(currentPage, 18);
  if (restaurants && restaurants.content.length > 0) {
    populateGrid(restaurants);
    currentPage++;
  }
  isLoading = false;
}

window.onload = async () => {
  isFetchingBaskets = true;
  await loadMore();
  await createOrder();
  await preloadBaskets().then(populateBasketsDropdown);
};

window.addEventListener("scroll", () => {
  if (
    window.innerHeight + window.scrollY >= document.body.offsetHeight - 100 &&
    !isLoading
  ) {
    loadMore();
  }
});

document.addEventListener("DOMContentLoaded", function () {
  document
    .getElementById("viewBasketsButton")
    .addEventListener("click", async function (event) {
      event.preventDefault();
      const dropdown = document.getElementById("basketsDropdown");
      if (isFetchingBaskets) {
        dropdown.innerHTML = "Loading baskets...";
      } 
      dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
      if (dropdown.style.display === "block") {
        const rect = viewBasketsButton.getBoundingClientRect();

        dropdown.style.top = `${rect.bottom + window.scrollY}px`;
        dropdown.style.left = `${rect.left + window.scrollX - 30}px`;
      }
    });
});
