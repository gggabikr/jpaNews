const elements = document.getElementsByClassName('popup_word');


function contains(selector, text) {
    const elements = document.querySelectorAll(selector);
    return Array.prototype.filter.call(elements, function(element){
        return RegExp(text).test(element.textContent);
    });
}
// contains('div', 'sometext'); // find "div" that contain "sometext"
// contains('div', /^sometext/); // find "div" that start with "sometext"
// contains('div', /sometext$/i); // find "div" that end with "sometext", case-insensitive


const mouseOnPopUp = function (event) {
    // const word = document.getElementById('title').childNodes;
    let text = event.currentTarget.classList[0];
    console.log(text)
    let detailWordInfo = document.getElementById(text);
    const addBtn = document.createElement("button");
    // addBtn.createTextNode("Add To WordList");
    // const addBtn = "<button>Add To WordList</button>";

    if(detailWordInfo.classList.contains("noShow")){
        detailWordInfo.classList.remove("noShow");
        detailWordInfo.parentElement.classList.remove("noShow");
        detailWordInfo.insertAdjacentElement("beforeend", addBtn);
    } else{
        detailWordInfo.classList.add("noShow");
        detailWordInfo.parentElement.classList.add("noShow");
    }
    // setTimeout(event.currentTarget.classList[0], 1300);


};

for(let i = 0; i < elements.length; i++ ) {
        const current = elements[i];
        // current.addEventListener('mouseover', mouseOnPopUp);
        current.addEventListener('click', mouseOnPopUp);

}
let popUpTimeOut;

const closePopUp = function (event){
    console.log("closed");
    // event.currentTarget.
}
//
// $(".popup_word").hover(function (){popUpTimeOut = setTimeout(mouseOnPopUp, 1000);},closePopUp)