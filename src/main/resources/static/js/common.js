const confirmLinks = document.querySelectorAll('[data-link-type]');

for (link of confirmLinks) {
    link.addEventListener("click", function(event) {
        return confirmAction(event, "Are you sure?");
    });
}

function updateOrderHandler(event) {
    if (sessionStorage.getItem('customerId')) {
        return confirmAction(event, "This action will replace your current order, are you sure?");
    }
}

function confirmAction(event, msg) {
    if (!confirm(msg)) {
        event.preventDefault();
        return false;
    }
    return true;
}

function applySortingDirection(event) {
    const sortBy = event.target.id;
    if (sortBy === "sort-desc" || sortBy === "sort-asc") {
        const sortTypes = document.querySelectorAll("#sort-menu a");
        for (sortType of sortTypes) {
            if (sortType.href.search(/[?&]direction=([a-zA-Z]*)/) !== -1) {
                sortType.href = sortType.href.replace(/([?&]direction=)([a-zA-Z]*)/, "$1" + sortBy.split("-")[1]);
            } else {
                sortType.href += "&direction=desc";
            }
        }
    }
}

function cancelOrder() {
        store.clear();
        window.location = "/";
}

const clearParam = window.location.search.match(/[?&]clear([^&]+)=([^&]+)/);

if (clearParam && clearParam.length === 3) {
    const param = clearParam[1].toLowerCase();
    const id = clearParam[2];
    if (param === "customerId".toLowerCase()) {
        const savedCustomerId = sessionStorage.getItem("customerId");
        if (savedCustomerId === id) {
            sessionStorage.clear();
        }
    } else if (param === "productId".toLowerCase()) {
        let map = sessionStorage.getItem("orderProducts");
        if (map) {
            map = JSON.parse(map);
            if (map[id]) {
                delete map[id];
                sessionStorage.setItem("orderProducts", JSON.stringify(map));
            }
        }
    }
}

const savedCustomerId = sessionStorage.getItem("customerId");
if (savedCustomerId) {
    const savedOrderEl = document.getElementById("currentOrder");
    if (savedOrderEl) {
        savedOrderEl.classList.remove("invisible");
        savedOrderEl.classList.add("visible");
        const link = document.querySelector("#currentOrder a");
        if (link) {
            link.setAttribute("href", `/createOrder?customerId=${savedCustomerId}`);
        }
    }
}