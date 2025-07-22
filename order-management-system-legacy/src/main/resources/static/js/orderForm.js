// Ensure that quantity is always an integer
document.addEventListener('DOMContentLoaded', function() {
    const quantityInput = document.getElementById('quantity');
    
    if (quantityInput) {
        // On input, ensure value is an integer
        quantityInput.addEventListener('input', function(e) {
            const value = e.target.value;
            if (value && value.includes('.')) {
                // Remove decimal part
                e.target.value = parseInt(value, 10);
            }
        });
        
        // On blur, ensure value is valid
        quantityInput.addEventListener('blur', function(e) {
            const value = e.target.value;
            if (value === '' || isNaN(value)) {
                e.target.value = 1; // Default to 1
            } else {
                e.target.value = parseInt(value, 10); // Ensure integer
            }
            
            // Ensure min value (1)
            if (parseInt(e.target.value, 10) < 1) {
                e.target.value = 1;
            }
        });
    }
});
