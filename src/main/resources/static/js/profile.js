function updateName() {
    var input = document.querySelector('.name-input');
    var editButton = document.querySelector('.edit-btn');
    var saveButton = document.querySelector('.save-btn');
    
    input.disabled = false;
    input.style.backgroundColor = '#333';  
	
    saveButton.style.display = 'inline-block';
    editButton.style.display = 'none';
}