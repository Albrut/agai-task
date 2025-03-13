document.addEventListener("DOMContentLoaded", function() {
    const orderForm = document.getElementById("orderForm");
    const submitButton = orderForm.querySelector("button[type='submit']");
    const checkboxes = orderForm.querySelectorAll("input[type='checkbox'][name^='orderItemsRequests']");

    const messageDiv = document.createElement("div");
    messageDiv.style.color = "red";
    messageDiv.style.marginBottom = "10px";
    orderForm.insertBefore(messageDiv, orderForm.firstChild);

    if (checkboxes.length === 0) {
        submitButton.disabled = true;
        messageDiv.textContent = "No available products. Please create a product.";
    } else {
        messageDiv.textContent = "";
    }

    function updateSubmitState() {
        let isAnySelected = Array.from(checkboxes).some(chk => chk.checked);
        submitButton.disabled = !isAnySelected;
    }

    if (checkboxes.length > 0) {
        updateSubmitState();
        checkboxes.forEach(checkbox => {
            checkbox.addEventListener("change", updateSubmitState);
        });
    }

    orderForm.addEventListener("submit", function(e) {
        if (checkboxes.length === 0) {
            e.preventDefault();
            alert("No available products. Please create a product before placing an order.");
            return false;
        }
        let hasSelectedProduct = Array.from(checkboxes).some(chk => chk.checked);
        if (!hasSelectedProduct) {
            e.preventDefault();
            alert("Please select at least one product before creating an order.");
            return false;
        }

        let valid = true;
        const quantityInputs = orderForm.querySelectorAll(".quantity-input");
        quantityInputs.forEach(function(input) {
            const stock = parseInt(input.getAttribute("data-stock"), 10);
            const quantity = parseInt(input.value, 10);
            const errorDiv = input.nextElementSibling;
            if (quantity > stock) {
                valid = false;
                errorDiv.style.display = "block";
            } else {
                errorDiv.style.display = "none";
            }
        });
        if (!valid) {
            e.preventDefault();
            alert("Some products have a quantity greater than the available stock. Please correct the values.");
            return false;
        }
    });
});
