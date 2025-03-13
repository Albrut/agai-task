function updateDeliveryLabel() {
    let checkbox = document.getElementById("create-delivered");
    let label = document.getElementById("delivery-label");
    label.textContent = checkbox.checked ? "Order already delivered" : "Order not delivered";
}

window.onload = updateDeliveryLabel;

document.getElementById("orderForm").addEventListener("submit", function(e) {
    let valid = true;
    document.querySelectorAll(".quantity-input").forEach(function(input) {
        let stock = parseInt(input.getAttribute("data-stock"), 10);
        let quantity = parseInt(input.value, 10);
        let errorDiv = input.nextElementSibling;
        if(quantity > stock) {
            valid = false;
            errorDiv.style.display = "block";
        } else {
            errorDiv.style.display = "none";
        }
    });
    if (!valid) {
        e.preventDefault();
        alert("Some products have a quantity greater than the available stock. Please correct the values.");
    }
});