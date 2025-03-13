let currentDeleteId;

function showDeleteModal(orderId) {
    currentDeleteId = orderId.toString();
    document.getElementById('modalContainer').style.display = 'flex';
}

function hideModal() {
    document.getElementById('modalContainer').style.display = 'none';
}

function confirmDelete() {
    const form = document.getElementById('deleteForm');
    form.action = `/admin/orders/delete/${currentDeleteId}`;
    form.submit();
}

document.querySelector('.modal-overlay').addEventListener('click', (e) => {
    if (e.target === e.currentTarget) hideModal();
});

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.btn-delete').forEach(function(button) {
        button.addEventListener('click', function() {
            let orderId = button.getAttribute('data-order-id');
            showDeleteModal(orderId);
        });
    });
});
