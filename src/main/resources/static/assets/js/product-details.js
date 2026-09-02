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

let breadcrumb = $("#breadcrumb-link");
breadcrumb.innerHTML = currentProduct.title;
breadcrumb.setAttribute("href", window.location.href);

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

let imagesQuantity = 0;

function setImageGallery(product) {

    let gallery = $(".swiper.product-gallery").querySelector(".swiper-wrapper");
    let thumbnails = $(".swiper.gallery-thumbnail").querySelector(".swiper-wrapper");

    let images = [];

    if (product.images.length == 0 || product.images == undefined) {
        images = [{ url: product.image_url, alt: product.image_alt }];
    } else {
        images = product.images;

        if (product.image_url != product.images[0].url) {
            images = [{ url: product.image_url, alt: product.image_alt }, ...product.images];
        }
    }

    imagesQuantity = images.length;

    for (let i = 0; images.length > i; i++) {

        let imageSlide = `
            <div class="swiper-slide">
                <img src="${images[i].url}" alt="${images[i].alt}">
            </div>`;

        gallery.innerHTML += imageSlide;
        thumbnails.innerHTML += imageSlide;
    }
}

let available = $("#product-available");

available.style.color = stockStatus(currentProduct.stock).color;
available.innerHTML = stockStatus(currentProduct.stock).status;

setImageGallery(currentProduct);

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
