document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('orderForm');
    if (!form) return;

    const messagesDiv = document.getElementById('formMessages');
    const checkboxes = form.querySelectorAll('.product-checkbox');
    const quantityInputs = form.querySelectorAll('.quantity-input');
    const submitBtn = form.querySelector('button[type="submit"]');
    const isAdmin = /*[[${#authorization.expression('hasRole(''ROLE_ADMIN'')')}]]*/ false;

    updateSubmitState();

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateSubmitState);
    });

    quantityInputs.forEach(input => {
        input.addEventListener('input', validateQuantity);
    });

    form.addEventListener('submit', handleSubmit);

    function updateSubmitState() {
        const anyChecked = Array.from(checkboxes).some(checkbox => checkbox.checked);
        submitBtn.disabled = !anyChecked;
    }

    function validateQuantity(e) {
        const input = e.target;
        const stock = parseInt(input.getAttribute('data-stock'), 10);
        const errorDiv = input.nextElementSibling;

        if (input.value > stock) {
            errorDiv.style.display = 'block';
            input.classList.add('is-invalid');
        } else {
            errorDiv.style.display = 'none';
            input.classList.remove('is-invalid');
        }
    }

    function handleSubmit(e) {
        e.preventDefault();
        messagesDiv.style.display = 'none';

        const selectedProducts = Array.from(checkboxes).filter(checkbox => checkbox.checked);
        if (selectedProducts.length === 0) {
            showMessage('Please select at least one product');
            return;
        }

        let valid = true;
        quantityInputs.forEach(input => {
            const stock = parseInt(input.getAttribute('data-stock'), 10);
            if (input.value > stock) {
                valid = false;
                input.classList.add('is-invalid');
                input.nextElementSibling.style.display = 'block';
            }
        });

        if (!valid) {
            showMessage('Some quantities exceed available stock');
            return;
        }

        form.submit();
    }

    function showMessage(message) {
        messagesDiv.textContent = message;
        messagesDiv.style.display = 'block';
        window.scrollTo(0, 0);
    }
});