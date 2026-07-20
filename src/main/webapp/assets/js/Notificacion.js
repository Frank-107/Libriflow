document.addEventListener("DOMContentLoaded", function() {
    const toast = document.querySelector('.libri-toast');

    if (toast) {
        setTimeout(() => {
            toast.classList.add('show');
        }, 100);

        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                toast.remove();
            }, 400);
        }, 3500);
    }
});