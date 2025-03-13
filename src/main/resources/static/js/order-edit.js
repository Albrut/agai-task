let newRowIndex = document.querySelectorAll("#orderItemsTable tbody tr").length;

function addProductFromModal(button) {
    let prodId = button.getAttribute("data-prod-id");
    let prodPrice = button.getAttribute("data-prod-price");
    let prodName = button.getAttribute("data-prod-name");
    let newRowHtml = `
      <tr>
        <td>
          <input type="text" name="orderItems[${newRowIndex}].productId" class="form-control" value="${prodId}" readonly>
        </td>
        <td>
          <input type="number" name="orderItems[${newRowIndex}].quantity" class="form-control" value="1" min="1">
        </td>
        <td>
          <input type="text" name="orderItems[${newRowIndex}].price" class="form-control" value="${prodPrice}" readonly>
        </td>
        <td>
          <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Delete</button>
        </td>
      </tr>
    `;
    document.querySelector("#orderItemsTable tbody").insertAdjacentHTML("beforeend", newRowHtml);
    newRowIndex++;
    hideAddProductModal();
}


function removeRow(button) {
    let row = button.closest('tr');
    row.remove();
}

function hideAddProductModal() {
    $('#addProductModal').modal('hide');
}