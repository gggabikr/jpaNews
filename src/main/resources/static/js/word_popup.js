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

function toggleAddToWordListForm(){
    // let wordId = event.target.value;
    // console.log("wordId: "+ wordId);
    if (document.getElementById("formLocation").classList.contains('noShow')){
        document.getElementById("formLocation").classList.remove('noShow');
    } else{
        document.getElementById("formLocation").classList.add('noShow');
    }
}

function addToList(){
    let checkedWords = document.querySelectorAll('input[name=wordCheck]:checked');
    for(let i = 0; i<checkedWords.length; i++){
        console.log(checkedWords[i].value);
    }
    toggleAddToWordListForm();
}

const addFormCloseBtn = document.getElementById("addFormCloseBtn");
if(addFormCloseBtn){
    addFormCloseBtn.addEventListener("click", toggleAddToWordListForm);
}

const mouseOnPopUp = function (event) {
    let byClassName = document.getElementsByClassName("wordDetail");
    let byClassName1 = document.getElementsByClassName("addToListBtn");
    let byClassName2 = document.getElementsByClassName("closeBtn");
    let byClassName3 = document.getElementsByClassName("moreResultBtn");

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

    if(byClassName3 != null && byClassName3.length>0){
        byClassName3[0].remove();
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
    addBtn.addEventListener("click", addToList);
    addBtn.textContent = "Add to wordlist";

    const moreResultBtn = document.createElement("button");
    moreResultBtn.setAttribute("type", "button");
    moreResultBtn.setAttribute("class", "moreResultBtn btn btn-primary");
    // moreResultBtn.setAttribute("onclick", "searchWordPage();")
    moreResultBtn.textContent = "More result..";
    moreResultBtn.addEventListener("click", moreResult)

    const closeBtn = document.createElement("button");
    closeBtn.setAttribute("class", "closeBtn btn btn-primary");
    closeBtn.textContent = "X";
    closeBtn.addEventListener("click", toggleDetail);

    const btn = document.getElementsByClassName("selectBtn");
    const form = document.getElementById("AddWordToWordlistForm");
    for (let i = 0; i < btn.length; i++) {
        btn[i].addEventListener("click", function(){
            const selectedList = document.getElementById("listSelect").value;
            console.log(selectedList);
            let checkedWords = document.querySelectorAll('input[name=wordCheck]:checked');
            // console.log(checkedWords);
            if(checkedWords.length <=1){
                form.action = '/addWordToList/' + checkedWords[0].value;
                console.log(form.action);
                form.submit();
            }else {
                form.action = '/addWordToList/'
                for(let i=0; i<checkedWords.length; i++){
                    form.action += checkedWords[i].value;
                    form.action += "D";
                }
                console.log("There are multiple words selected.")
                console.log(form.action);
                form.submit();
            }
        });
    }

    function toggleDetail() {
        if (detailWordInfo.classList.contains("noShow")) {
            detailWordInfo.classList.remove("noShow");
            detailWordInfo.parentElement.classList.remove("noShow");
            detailWordInfo.insertAdjacentElement("afterbegin", closeBtn);
            if(detailWordInfo.children[2].hasAttribute("data-wordid")) {
                if(document.getElementById("formLocation")){
                    detailWordInfo.insertAdjacentElement("beforeend", addBtn);
                }
                detailWordInfo.insertAdjacentElement("beforeend", moreResultBtn);
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