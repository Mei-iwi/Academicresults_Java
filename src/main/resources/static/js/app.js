(function () {
    function qs(sel, root = document) {
        return root.querySelector(sel);
    }

    function qsa(sel, root = document) {
        return Array.from(root.querySelectorAll(sel));
    }

    window.AppUI = {
        toast: function (msg) {
            let area = qs(".toast-area");

            if (!area) {
                area = document.createElement("div");
                area.className = "toast-area";
                document.body.appendChild(area);
            }

            const box = document.createElement("div");
            box.className = "toast-box";
            box.textContent = msg;
            area.appendChild(box);

            setTimeout(function () {
                box.style.opacity = "0";
                box.style.transform = "translateY(8px)";
            }, 2400);

            setTimeout(function () {
                box.remove();
            }, 3000);
        }
    };

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

        const action = e.target.closest("[data-action]");

        if (action) {
            e.preventDefault();

            const type = action.getAttribute("data-action");

            const messages = {
                save: "Đã lưu dữ liệu mẫu trên giao diện.",
                draft: "Đã lưu nháp điểm trên giao diện.",
                publish: "Đã chuyển trạng thái công bố/khóa điểm trên giao diện.",
                lock: "Đã đổi trạng thái khóa/mở tài khoản mẫu.",
                reset: "Đã tạo mật khẩu tạm thời mẫu.",
                delete: "Đã mô phỏng thao tác xóa dữ liệu.",
                print: "Đang mở hộp thoại in."
            };

            if (type === "print") {
                window.print();
                return;
            }

            AppUI.toast(messages[type] || "Đã thực hiện thao tác mẫu.");
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
