(function () {
    function qs(sel, root = document) {
        return root.querySelector(sel);
    }

    function qsa(sel, root = document) {
        return Array.from(root.querySelectorAll(sel));
    }

    function getSidebar() {
        return qs(".sidebar");
    }

    function getSidebarOverlay() {
        return qs(".sidebar-overlay");
    }

    function openSidebar() {
        const sidebar = getSidebar();
        const overlay = getSidebarOverlay();

        if (sidebar) {
            sidebar.classList.add("open");
        }

        if (overlay) {
            overlay.classList.add("show");
        }
    }

    function closeSidebar() {
        const sidebar = getSidebar();
        const overlay = getSidebarOverlay();

        if (sidebar) {
            sidebar.classList.remove("open");
        }

        if (overlay) {
            overlay.classList.remove("show");
        }
    }

    function toggleSidebar() {
        const sidebar = getSidebar();

        if (!sidebar) {
            return;
        }

        if (sidebar.classList.contains("open")) {
            closeSidebar();
        } else {
            openSidebar();
        }
    }

    function closeModal(modal) {
        if (modal) {
            modal.classList.remove("show");
        }
    }

    document.addEventListener("click", function (e) {
        const toggle = e.target.closest("[data-toggle-sidebar], #sidebarToggle, .mobile-toggle");

        if (toggle) {
            e.preventDefault();
            toggleSidebar();
            return;
        }

        const overlay = e.target.closest(".sidebar-overlay");

        if (overlay) {
            closeSidebar();
            return;
        }

        const sidebarLink = e.target.closest(".sidebar-nav a");

        if (sidebarLink && window.innerWidth <= 992) {
            closeSidebar();
        }

        const close = e.target.closest("[data-close-modal]");

        if (close) {
            closeModal(close.closest(".modal"));
            return;
        }

        const modalBtn = e.target.closest("[data-open-modal]");

        if (modalBtn) {
            e.preventDefault();
            const modalSelector = modalBtn.getAttribute("data-open-modal");
            const modal = qs(modalSelector);

            if (modal) {
                modal.classList.add("show");
            }
            return;
        }

        const print = e.target.closest("[data-print]");

        if (print) {
            e.preventDefault();
            window.print();
        }
    });

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") {
            closeSidebar();
            qsa(".modal.show").forEach(closeModal);
        }
    });

    qsa("[data-table-search]").forEach(function (input) {
        const targetSelector = input.getAttribute("data-table-search");
        const target = qs(targetSelector);

        if (!target) {
            return;
        }

        input.addEventListener("input", function () {
            const keyword = input.value.toLowerCase();

            qsa("tbody tr", target).forEach(function (row) {
                const matched = row.textContent.toLowerCase().includes(keyword);
                row.style.display = matched ? "" : "none";
            });
        });
    });

    window.addEventListener("resize", function () {
        if (window.innerWidth > 992) {
            closeSidebar();
        }
    });
})();
