/**
 * Increase or dicrease product items quantity
 */

const addQuantityButton = document.getElementById("btn-increase-qty");
const decreaseQuantityButton = document.getElementById("btn-decrease-qty");
let qtyInput = document.getElementById("quantity-wanted");

const $ = window.document.querySelector.bind(document);

let params = new URLSearchParams(window.location.search);
const productId = params.get("id");

const currentProduct = products.find((p) => {
    if (p.id == productId) {
        return p;
    }
});

let imagesQuantity = 0;
// queries DOM to access children nodes of thumbnail gallery
const galleryThumbnail = $(".swiper.gallery-thumbnail > .swiper-wrapper");

if (galleryThumbnail != null && galleryThumbnail.childElementCount > 1) {
    imagesQuantity = galleryThumbnail.childElementCount;
} else {
    imagesQuantity = "auto";
}

function stockStatus(stock) {

    let config = {
        color: "",
        status: ""
    };

    if (stock == 0) {
        config.status = "No Disponible";
        config.color = "#ff0000";
        return;
    }

    if (stock > 0 && stock < 5) {
        config.status = "Últimas Unidades";
        config.color = "#ff9741ff";
        return;
    }

    config.color = "#4cbb6c";
    config.status = "En Stock";
    return config;
}

let available = $("#product-available");

available.style.color = stockStatus(currentProduct.stock).color;
available.innerHTML = stockStatus(currentProduct.stock).status;

/** Initialize product details */
function renderProductDetails() {

    let name = $("#product-name");
    let sku = $("#product-sku");
    let description = $("#product-description");
    let price = $("#product-current-price");
    let available = $("#product-available");

    name.innerHTML = currentProduct.title;
    sku.innerHTML = "#SKU " + currentProduct.sku;
    description.querySelector("p").innerHTML = currentProduct.short_description;
    price.innerHTML = "$ " + currentProduct.price;

    available.style.color = stockStatus(currentProduct.stock).color;
    available.innerHTML = stockStatus(currentProduct.stock).status;

    setImageGallery(currentProduct);
}


addQuantityButton.onclick = () => {
    qtyInput.value++;
};

decreaseQuantityButton.onclick = () => {

    if (qtyInput.value <= 1) { return }

    qtyInput.value = qtyInput.value - 1;
};

/** Implement SwiperJs product details images gallery */
let productThumbsnailGallery = new Swiper(".gallery-thumbnail", {
    direction: "vertical",
    spaceBetween: 10,
    slidesPerView: imagesQuantity,
    freeMode: true,
    watchSlidesProgress: true
});

let productGallery = new Swiper(".product-gallery", {
    loop: true,
    navigation: {
        prevEl: ".swiper-button-prev",
        nextEl: ".swiper-button-next"
    },
    thumbs: {
        swiper: productThumbsnailGallery
    }
});

// window.document.addEventListener("DOMContentLoaded", renderProductDetails);
