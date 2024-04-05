const searchInput = document.getElementById("search-input");

      searchInput.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
          event.preventDefault();
          performSearch();
        }
      });

      function performSearch() {
        var newPageUrl = "/order";

        // Data to be transferred
        var dataToTransfer = {
          address: searchInput.value,
        };

        var postCode = /^[A-Z]{1,2}[0-9R][0-9A-Z]? ?[0-9][A-Z]{2}$/i;

        if (postCode.test(searchInput.value)) {
          // Append data as query parameters to the URL
          newPageUrl += "?" + new URLSearchParams(dataToTransfer).toString();
          window.location.href = newPageUrl;
        } else {
          alert("Please enter a valid UK postal code.");
        }
      }