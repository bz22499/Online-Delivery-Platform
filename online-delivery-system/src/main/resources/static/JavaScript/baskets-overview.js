let isFetchingBaskets = false;

// get all the baskets for current order ID
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
// get basket items from cache
function getBasketItemsFromCache(basketId) {
  const basketsString = sessionStorage.getItem("baskets");
  const baskets = JSON.parse(basketsString);
  return {
    items: baskets[basketId].items,
    restName: baskets[basketId].restName,
  };
}

// populate page with basket information
async function displayBaskets(baskets) {
  const container = document.getElementById("baskets-container"); // defined in html
  container.innerHTML = ""; // clear to avoid duplicates
  if (baskets && baskets.length) {
    // check if baskets exist
    baskets.forEach((basket) => {
      const section = document.createElement("section"); // create a section for each basket
      section.className = "basket-section";

      const heading = document.createElement("h2");
      let basketData = getBasketItemsFromCache(basket.id);
      heading.textContent = basketData.restName;
      section.appendChild(heading);

      // create a table for basket items
      const table = document.createElement("table");
      table.className = "basket-table";
      const thead = document.createElement("thead");
      const headerRow = document.createElement("tr");
      const itemNameHeader = document.createElement("th");
      itemNameHeader.textContent = "Item Name";
      const quantityHeader = document.createElement("th");
      quantityHeader.textContent = "Quantity";
      headerRow.appendChild(itemNameHeader);
      headerRow.appendChild(quantityHeader);
      thead.appendChild(headerRow);
      table.appendChild(thead);

      const tbody = document.createElement("tbody");

      basketData.items.forEach((item) => {
        const row = document.createElement("tr");

        const nameCell = document.createElement("td");
        nameCell.textContent = item.menuItem.name;
        row.appendChild(nameCell);

        const quantityCell = document.createElement("td");
        quantityCell.textContent = item.quantity;
        row.appendChild(quantityCell);

        tbody.appendChild(row);
      });

      table.appendChild(tbody);
      section.appendChild(table);
      container.appendChild(section);
    });
  } else {
    container.textContent = "No baskets found.";
  }
}

document.addEventListener("DOMContentLoaded", async () => {
  const orderId = sessionStorage.getItem("orderId");
  if (!orderId) {
    alert("No order found.");
    return;
  }
  const baskets = await fetchBasketsByOrder(orderId);
  if (baskets) {
    displayBaskets(baskets);
  }
});
