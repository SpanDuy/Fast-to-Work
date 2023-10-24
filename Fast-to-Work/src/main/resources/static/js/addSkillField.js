function addSkillField() {
    var skillRows = document.getElementsByClassName('skill-row');
    var newIndex = skillRows.length;

    var newSkillRow = document.createElement('div');
    newSkillRow.classList.add('skill-row');

    var label = document.createElement('label');
    label.setAttribute('for', 'skills[' + newIndex + ']');
    label.innerText = 'Skill ' + (newIndex + 1) + ':';

    var input = document.createElement('input');
    input.setAttribute('type', 'text');
    input.setAttribute('class', 'form-control');
    input.setAttribute('name', 'skills[' + newIndex + ']');
    input.setAttribute('placeholder', 'Skill ' + (newIndex + 1));

    var error = document.createElement('p');
    error.classList.add('text-danger');
    error.setAttribute('th:if', "${#fields.hasErrors('skills[' + newIndex + ']')}");
    error.setAttribute('th:errors', "*{skills[' + newIndex + ']}");

    newSkillRow.appendChild(label);
    newSkillRow.appendChild(input);
    newSkillRow.appendChild(error);

    var addButton = document.querySelector('.skill-row:last-child button');
    addButton.parentNode.insertBefore(newSkillRow, addButton);
}