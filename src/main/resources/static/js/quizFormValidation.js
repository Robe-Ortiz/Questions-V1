function initializeSubmitButton() {
    
    const submitButton = document.getElementById('submitButton');

    function enableSubmitButton() {
        const selectedOption = document.querySelector('input[name="answer"]:checked');
        submitButton.disabled = !selectedOption;
    }
    
    const radioButtons = document.querySelectorAll('input[name="answer"]');
    radioButtons.forEach(radio => {
        radio.addEventListener('change', enableSubmitButton);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    initializeSubmitButton();
});