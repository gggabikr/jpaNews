const elements = document.getElementsByClassName('popup_word');


// function contains(selector, text) {
//     const elements = document.querySelectorAll(selector);
//     return Array.prototype.filter.call(elements, function(element){
//         return RegExp(text).test(element.textContent);
//     });
// }
// contains('div', 'sometext'); // find "div" that contain "sometext"
// contains('div', /^sometext/); // find "div" that start with "sometext"
// contains('div', /sometext$/i); // find "div" that end with "sometext", case-insensitive


const mouseOnPopUp = function (event) {
    let byClassName = document.getElementsByClassName("wordDetail");
    let byClassName1 = document.getElementsByClassName("addToListBtn");
    let byClassName2 = document.getElementsByClassName("closeBtn");

    if(byClassName != null){
        for(let ele of byClassName){
            ele.classList.add("noShow");
            ele.parentElement.classList.add("noShow");
        }
    }
    if(byClassName1 != null && byClassName1.length>0){
        byClassName1[0].remove();
    }

    if(byClassName2 != null && byClassName2.length>0){
        byClassName2[0].remove();
    }

    const moreResult = function(event){
        let form = document.createElement('form');
        form.setAttribute('method', 'post');
        form.setAttribute('action', '/searchWord');
        const wordNameInput = document.createElement("input");
        wordNameInput.setAttribute("name", "searchBar");
        wordNameInput.setAttribute("value", event.currentTarget.parentElement.id);
        form.appendChild(wordNameInput);
        form.style.display = 'hidden';
        document.body.appendChild(form)
        form.submit();
    }

    let text = event.currentTarget.classList[0];
    // console.log(text)
    let detailWordInfo = document.getElementById(text);

    const addBtn = document.createElement("button");
    addBtn.setAttribute("type", "button");
    addBtn.setAttribute("class", "addToListBtn btn btn-primary");
    addBtn.textContent = "Add to wordlist";

    const moreResultBtn = document.createElement("button");
    moreResultBtn.setAttribute("type", "button");
    moreResultBtn.setAttribute("class", "btn btn-primary");
    // moreResultBtn.setAttribute("onclick", "searchWordPage();")
    moreResultBtn.textContent = "More result..";
    moreResultBtn.addEventListener("click", moreResult)

    const closeBtn = document.createElement("button");
    closeBtn.setAttribute("class", "closeBtn btn btn-primary");
    closeBtn.textContent = "X";
    closeBtn.addEventListener("click", toggleDetail);

    function toggleDetail() {
        if (detailWordInfo.classList.contains("noShow")) {
            detailWordInfo.classList.remove("noShow");
            detailWordInfo.parentElement.classList.remove("noShow");
            if(detailWordInfo.children[1].hasAttribute("data-wordid")) {
                detailWordInfo.insertAdjacentElement("beforeend", addBtn);
                detailWordInfo.insertAdjacentElement("beforeend", moreResultBtn);
                detailWordInfo.insertAdjacentElement("afterbegin", closeBtn);
            }
        } else {
            detailWordInfo.classList.add("noShow");
            detailWordInfo.parentElement.classList.add("noShow");
            // document.getElementsByClassName("addToListBtn")[0].remove();
            // document.getElementsByClassName("closeBtn")[0].remove();
        }
    }
    toggleDetail();
    // setTimeout(event.currentTarget.classList[0], 1300);
};

for(let i = 0; i < elements.length; i++ ) {
        const current = elements[i];
        // current.addEventListener('mouseover', mouseOnPopUp);
        current.addEventListener('click', mouseOnPopUp);
}