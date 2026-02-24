import { router, navigateTo } from "./router.js";

window.addEventListener("DOMContentLoaded", router);

window.addEventListener("popstate", router);

document.addEventListener("click", e => {

    const element = e.target.closest("[data-link]");

    if (element) {
        e.preventDefault();
        const path = element.dataset.path
            || new URL(element.href).pathname;

        navigateTo(path);
    }

});