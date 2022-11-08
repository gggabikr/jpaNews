function idCheckFunction(){
    let passOfUsername;
    //length
    if(inputID.value.length >4 && inputID.value.length <16){
        idLength.classList.add('passChecker')
        idLength.classList.remove('nopassChecker')
        idLength.firstElementChild.classList.remove('fa-times-circle')
        idLength.firstElementChild.classList.add('fa-check-circle')
        passOfUsername = true
    }
    else{
        idLength.classList.remove('passChecker')
        idLength.classList.add('nopassChecker')
        idLength.firstElementChild.classList.add('fa-times-circle')
        idLength.firstElementChild.classList.remove('fa-check-circle')
        passOfUsername = false
    }
    return passOfUsername;
}

function pwCheckFunction(){
    const passOfPassword = [];
    //length
    if(inputPW.value.length >6){
        pwLength.classList.add('passChecker')
        pwLength.classList.remove('nopassChecker')
        pwLength.firstElementChild.classList.remove('fa-times-circle')
        pwLength.firstElementChild.classList.add('fa-check-circle')
        passOfPassword[0] = "OK"
    }
    else{
        pwLength.classList.remove('passChecker')
        pwLength.classList.add('nopassChecker')
        pwLength.firstElementChild.classList.add('fa-times-circle')
        pwLength.firstElementChild.classList.remove('fa-check-circle')
        passOfPassword[0] = "NO"
    }

    //number
    const numbers = /[0-9]/g;
    if(inputPW.value.match(numbers)){
        pwNumber.classList.add('passChecker')
        pwNumber.classList.remove('nopassChecker')
        pwNumber.firstElementChild.classList.remove('fa-times-circle')
        pwNumber.firstElementChild.classList.add('fa-check-circle')
        passOfPassword[1] = "OK"
    }
    else{
        pwNumber.classList.remove('passChecker')
        pwNumber.classList.add('nopassChecker')
        pwNumber.firstElementChild.classList.add('fa-times-circle')
        pwNumber.firstElementChild.classList.remove('fa-check-circle')
        passOfPassword[1] = "NO"
    }

    //case
    const lowercases = /[a-z]/g;
    const uppercases = /[A-Z]/g;
    if(inputPW.value.match(uppercases)&&inputPW.value.match(lowercases)){
        pwCase.classList.add('passChecker')
        pwCase.classList.remove('nopassChecker')
        pwCase.firstElementChild.classList.remove('fa-times-circle')
        pwCase.firstElementChild.classList.add('fa-check-circle')
        passOfPassword[2] = "OK"
    }
    else{
        pwCase.classList.remove('passChecker')
        pwCase.classList.add('nopassChecker')
        pwCase.firstElementChild.classList.add('fa-times-circle')
        pwCase.firstElementChild.classList.remove('fa-check-circle')
        passOfPassword[2] = "NO"
    }

    //special
    const specials = /[!()='?@:;+#`$%_^&*.,/<>-]/;
    if(inputPW.value.match(specials)){
        pwSpecial.classList.add('passChecker')
        pwSpecial.classList.remove('nopassChecker')
        pwSpecial.firstElementChild.classList.remove('fa-times-circle')
        pwSpecial.firstElementChild.classList.add('fa-check-circle')
        passOfPassword[3] = "OK"
    }
    else{
        pwSpecial.classList.remove('passChecker')
        pwSpecial.classList.add('nopassChecker')
        pwSpecial.firstElementChild.classList.add('fa-times-circle')
        pwSpecial.firstElementChild.classList.remove('fa-check-circle')
        passOfPassword[3] = "NO"
    }

    //include the username
    if(!inputPW.value.includes(inputID.value)){
        pwSameasid.classList.add('passChecker')
        pwSameasid.classList.remove('nopassChecker')
        pwSameasid.firstElementChild.classList.remove('fa-times-circle')
        pwSameasid.firstElementChild.classList.add('fa-check-circle')
        passOfPassword[4] = "OK"
    }
    else{
        pwSameasid.classList.remove('passChecker')
        pwSameasid.classList.add('nopassChecker')
        pwSameasid.firstElementChild.classList.add('fa-times-circle')
        pwSameasid.firstElementChild.classList.remove('fa-check-circle')
        passOfPassword[4] ="NO"
    }
    return passOfPassword;
}


const inputID = document.querySelector('#username');
const idChecker = document.querySelector('.id-checker');
const inputPW = document.querySelector('#password');
const pwChecker = document.querySelector('.pw-checker');

const idLength = document.querySelector(".id-length");

const pwLength = document.querySelector(".pw-length");
const pwNumber = document.querySelector(".pw-number");
const pwCase = document.querySelector(".pw-case");
const pwSpecial = document.querySelector(".pw-special");
const pwSameasid = document.querySelector(".pw-sameasid");

//박스클릭시 class중에 noshow없는상태로 바꿈
inputID.addEventListener("focus", event => { idChecker.classList = ('id-checker')
})
//박스밖 클릭시 class에 noshow추가
inputID.addEventListener("blur", event => { idChecker.classList = ('id-checker noshow')
})


inputPW.addEventListener("focus", event => { pwChecker.classList = ('pw-checker')
})
inputPW.addEventListener("blur", event => { pwChecker.classList = ('pw-checker noshow')
})

inputID.addEventListener("keyup",idCheckFunction);

inputPW.addEventListener("keyup", pwCheckFunction);


function checkList(a){
    return a === "OK";
}

function finalCheck(){
    let passOfUsername = idCheckFunction();
    let passOfPassword = pwCheckFunction().every(checkList);
    let password = document.getElementsByName("password")[0].value;
    let confirmPassword = document.getElementsByName("psw-repeat")[0].value;
    let passOfConfirmPassword = password === confirmPassword;

    return passOfUsername === true && passOfPassword === true && passOfConfirmPassword === true;
}
