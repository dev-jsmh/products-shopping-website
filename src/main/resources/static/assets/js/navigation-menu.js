/** JS for navigation bar menu. This menu collapses
 * into a hamburger menu on mobile screens and
 * slides in from the left when users click the menu
 * button 
 * 
 * */

document.addEventListener("DOMContentLoaded", () => {
    // --- LÓGICA DEL SIDE-NAV GENERAL ---
    const openMenuBtn = document.getElementById("openSideNavbarMenu");
    const closeMenuBtn = document.getElementById("closeSideNavbarMenu");
    const sideNav = document.getElementById("sideNavBar");
    const overlay = document.getElementById("menuOverlay");

    const openNav = () => {
        sideNav.classList.add("active");
        overlay.classList.add("active");
        document.body.classList.toggle("no-scroll");
        openMenuBtn.classList.add("active");
    };

    const closeNav = () => {
        sideNav.classList.remove("active");
        overlay.classList.remove("active");
        document.body.classList.toggle("no-scroll");
        openMenuBtn.classList.remove("active");
    };

    // Close menu when a link is clicked (optional but good for UX)
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', closeNav);
    });

    openMenuBtn.addEventListener("click", openNav);
    closeMenuBtn.addEventListener("click", closeNav);
    overlay.addEventListener("click", closeNav);

    // --- LÓGICA DEL ACORDEÓN (SUB-MENÚS) ---
    const navToggles = document.querySelectorAll(".nav-toggle");

    navToggles.forEach(toggle => {
        toggle.addEventListener("click", function (e) {
            e.preventDefault(); // Previene comportamientos por defecto

            // 1. Girar la flecha (toggle clase 'active' en el botón)
            this.classList.toggle("active");

            // 2. Encontrar el sub-menú directamente debajo de este botón
            const subMenu = this.nextElementSibling;

            // 3. Abrir o cerrar el sub-menú
            if (subMenu && subMenu.classList.contains("sub-menu")) {
                subMenu.classList.toggle("open");
            }
        });
    });
});