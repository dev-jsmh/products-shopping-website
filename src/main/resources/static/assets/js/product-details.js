/**
 * Increase or dicrease product items quantity
 */

const addQuantityButton = document.getElementById("btn-increase-qty");
const decreaseQuantityButton = document.getElementById("btn-decrease-qty");
let qtyInput = document.getElementById("quantity-wanted");

const $ = window.document.querySelector.bind(document);

let imagesQuantity = 0;
// queries DOM to access children nodes of thumbnail gallery
const galleryThumbnail = $(".swiper.gallery-thumbnail > .swiper-wrapper");

if (galleryThumbnail != null && galleryThumbnail.childElementCount > 1) {
    imagesQuantity = galleryThumbnail.childElementCount;
} else {
    imagesQuantity = "auto";
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
