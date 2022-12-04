const elements = document.getElementsByClassName('popup_word');


function mouseOnPopUp(e){
    const word = this.getElementById('title')[0].textContent;
        // tel = this.getElementsByClassName('tel')[0].textContent;

    }

    for(let i = 0; i < elements.length; i++ ) {
        const current = elements[i];
        current.addEventListener('mouseover', mouseOnPopUp);
    }