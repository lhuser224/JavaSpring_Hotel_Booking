// static/js/common-date.js
function applyDateConstraints(inId, outId) {
    const inInput = document.getElementById(inId);
    const outInput = document.getElementById(outId);
    if (!inInput || !outInput) return;

    const now = new Date();
    if (now.getHours() >= 12) now.setDate(now.getDate() + 1);
    const minStr = now.toISOString().split('T')[0];

    inInput.setAttribute('min', minStr);
    inInput.addEventListener('change', () => {
        let nextDay = new Date(inInput.value);
        nextDay.setDate(nextDay.getDate() + 1);
        outInput.setAttribute('min', nextDay.toISOString().split('T')[0]);
    });
}