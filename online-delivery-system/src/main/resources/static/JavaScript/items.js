async function fetchItems(vendorId) {
    try {
        const response = await fetch(`/menuItems/vendor/${vendorId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching item data:', error);
    }
}

function populateGrid(pageData) {
    const gridContainer = document.querySelector('.grid-container');
        pageData.forEach((item) => {
            const gridItem = document.createElement('div');
            gridItem.className = 'grid-item';
            gridItem.textContent = item.name;

            const footer = document.createElement('div');
            footer.className = 'grid-item-footer';
            footer.textContent = item.description;
            gridItem.appendChild(footer);

            gridContainer.appendChild(gridItem);
        });
}



async function load(vendorId) {
    const items = await fetchItems(vendorId);
    if (items.length > 0) {
        populateGrid(items)
    }
}


document.addEventListener('DOMContentLoaded', function () {
    const vendorInfoElement = document.getElementById('vendor-info');
    const vendorId = vendorInfoElement.getAttribute('data-id');

    if (vendorId) {
        load(vendorId);
    }
});




