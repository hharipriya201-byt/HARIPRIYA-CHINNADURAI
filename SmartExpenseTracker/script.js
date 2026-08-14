const form = document.getElementById("transactionForm");
const descriptionInput = document.getElementById("description");
const amountInput = document.getElementById("amount");
const typeInput = document.getElementById("type");
const categoryInput = document.getElementById("category");
const transactionList = document.getElementById("transactionList");
const emptyState = document.getElementById("emptyState");

let transactions = JSON.parse(localStorage.getItem("transactions")) || [];

function formatCurrency(value) {
  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR"
  }).format(value);
}

function saveTransactions() {
  localStorage.setItem("transactions", JSON.stringify(transactions));
}

function render() {
  transactionList.innerHTML = "";

  let income = 0;
  let expense = 0;

  transactions.forEach((transaction) => {
    if (transaction.type === "income") {
      income += transaction.amount;
    } else {
      expense += transaction.amount;
    }

    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${escapeHtml(transaction.description)}</td>
      <td>${escapeHtml(transaction.category)}</td>
      <td><span class="badge ${transaction.type}">${transaction.type}</span></td>
      <td>${formatCurrency(transaction.amount)}</td>
      <td><button class="delete" onclick="deleteTransaction('${transaction.id}')">Delete</button></td>
    `;
    transactionList.appendChild(row);
  });

  document.getElementById("income").textContent = formatCurrency(income);
  document.getElementById("expense").textContent = formatCurrency(expense);
  document.getElementById("balance").textContent = formatCurrency(income - expense);

  emptyState.style.display = transactions.length ? "none" : "block";
  document.getElementById("transactionTable").style.display =
    transactions.length ? "table" : "none";
}

function escapeHtml(value) {
  return value.replace(/[&<>"']/g, (char) => ({
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#039;"
  }[char]));
}

form.addEventListener("submit", (event) => {
  event.preventDefault();

  const description = descriptionInput.value.trim();
  const amount = Number(amountInput.value);

  if (!description || amount <= 0) return;

  transactions.unshift({
    id: crypto.randomUUID(),
    description,
    amount,
    type: typeInput.value,
    category: categoryInput.value
  });

  saveTransactions();
  render();
  form.reset();
});

function deleteTransaction(id) {
  transactions = transactions.filter((transaction) => transaction.id !== id);
  saveTransactions();
  render();
}

document.getElementById("clearAll").addEventListener("click", () => {
  if (!transactions.length) return;

  if (confirm("Delete all transactions?")) {
    transactions = [];
    saveTransactions();
    render();
  }
});

render();
