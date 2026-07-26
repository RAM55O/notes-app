document.addEventListener('click', (event) => {
    const button = event.target.closest('[data-confirm]');
    if (!button) {
        return;
    }

    const message = button.getAttribute('data-confirm') || 'Are you sure?';
    if (!window.confirm(message)) {
        event.preventDefault();
    }
});
