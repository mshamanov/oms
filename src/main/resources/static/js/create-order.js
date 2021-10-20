const store = sessionStorage;
const customerId = store.getItem("customerId");

if (+customerId !== currentCustomerId) {
    store.clear();
    store.setItem("customerId", currentCustomerId);
}

let orderProducts = sessionStorage.getItem("orderProducts");

if (orderProducts) {
    orderProducts = JSON.parse(orderProducts);
    const counters = document.querySelectorAll(".product-counter");
    counters.forEach(counter => {
        const split = counter.id.split("-");
        if (split.length === 3) {
            const productId = split[2];
            if (orderProducts[productId]) {
                counter.setAttribute("value", Math.abs(orderProducts[productId].amount));
            }
        }
    });

    updateCart();
}

function handleCounter(event) {
    const type = event.type;
    const action = event.target;

    if ((type === "click" && !action.id.includes("product-dash") && !action.id.includes("product-plus"))
    || (type === "keyup" && !action.id.includes("product-counter"))) {
        return;
    }

    const split = action.id.split("-");

    if (split.length !== 3) {
        return;
    }

    const productId = split[2];
    const amountEl = document.getElementById(`product-counter-${productId}`);
    const productPriceEl = document.getElementById(`product-price-${productId}`);

    if (amountEl) {
        const price = +productPriceEl.dataset.rawPrice;
        let amount = Math.abs(+amountEl.value);

        if (action.id.includes("product-dash")) {
            if (amount === 0) {
                return;
            }
            amount--;
        } else if (action.id.includes("product-plus")) {
            amount++;
        }

        amountEl.value = amount;
        amountEl.setAttribute("value", amount);
        updateStorage(productId, price, amount);
        updateCart();
    }
}

function updateCart() {
    let map = store.getItem("orderProducts");
    if (map) {
        map = JSON.parse(map);
        const sum = Object.values(map)
                          .filter(p => p.amount > 0 && p.totalPrice > 0)
                          .reduce((acc, val) => acc + val.totalPrice, 0);
        const cartTotalEl = document.getElementById("cart-total");
        cartTotalEl.innerText = sum.toFixed(2);
    }
}

function updateStorage(productId, price, amount) {
    let map = store.getItem("orderProducts");

    if (!map) {
        map = {};
    } else {
        map = JSON.parse(map);
    }

    if (map[productId]) {
        const currProduct = map[productId];
        if (amount === 0) {
            delete map[productId];
        } else {
            map[productId] = {amount, totalPrice: price * amount};
        }
    } else {
        map[productId] = {amount, totalPrice: price * amount};
    }

    store.setItem("orderProducts", JSON.stringify(map));
}

function cancelOrder(event) {
        store.clear();
        window.location = "/";
}

function submitOrderForm(event) {
    // event.preventDefault();
    const sendForm = event.target;
    let map = store.getItem("orderProducts");

    if (map) {
        map = JSON.parse(map);
        Object.keys(map).forEach((k, idx) => {
            const inputId = idx;
            const amount = map[k].amount;
            const productIdEl = createInput(`orderItems[${inputId}].product.id`, k);
            const productAmountEl = createInput(`orderItems[${inputId}].amount`, amount);
            sendForm.append(productIdEl, productAmountEl);
        });
    }

    sendForm.submit();

    function createInput(name, value) {
        const el = document.createElement("input");
        el.type = "hidden";
        el.name = name;
        el.value = value;
        return el;
    }
}

