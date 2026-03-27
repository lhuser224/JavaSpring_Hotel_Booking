// booking-logic.js
document.addEventListener('DOMContentLoaded', function() {
    const checkInInput = document.getElementById('checkInDate');
    const checkOutInput = document.getElementById('checkOutDate');

    if (!checkInInput || !checkOutInput) return;

    function setupDateConstraints() {
        const now = new Date();
        let minCheckIn = new Date();
        
        // Logic: Nếu sau 12h trưa, ngày sớm nhất có thể đặt là ngày mai 
        if (now.getHours() >= 12) {
            minCheckIn.setDate(minCheckIn.getDate() + 1);
        }
        
        const minInStr = minCheckIn.toISOString().split('T')[0];
        checkInInput.setAttribute('min', minInStr);
        
        if (!checkInInput.value || checkInInput.value < minInStr) {
            checkInInput.value = minInStr;
        }

        updateMinCheckOut();
    }

    function updateMinCheckOut() {
        let dateIn = new Date(checkInInput.value);
        dateIn.setDate(dateIn.getDate() + 1); // Check-out ít nhất sau 1 ngày 
        const minOutStr = dateIn.toISOString().split('T')[0];
        
        checkOutInput.setAttribute('min', minOutStr);
        if (checkOutInput.value <= checkInInput.value) {
            checkOutInput.value = minOutStr;
        }
    }

    checkInInput.addEventListener('change', updateMinCheckOut);
    setupDateConstraints();
});