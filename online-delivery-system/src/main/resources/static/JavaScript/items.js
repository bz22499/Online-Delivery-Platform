async function fetchItems() {
    try {
        const response = await fetch(`http://localhost:8080/menuItems`);
        if (!response.ok) {
            alert("ERRROR")
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        alert(data)
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
            gridContainer.appendChild(gridItem);
        });
}



async function load() {
    const items = await fetchItems();
    alert(items)
    alert(items.content)
    if (items.length > 0) {
        populateGrid(items)
    }
}


document.addEventListener('DOMContentLoaded', function () {
    load()
});




