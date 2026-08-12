document.addEventListener("DOMContentLoaded", function () {
    const toasts = document.querySelectorAll('.libri-toast');

    toasts.forEach((toast) => {
        setTimeout(() => {
            toast.classList.add('show');
        }, 100);

        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                toast.remove();
            }, 400);
        }, 3500);
    });
});