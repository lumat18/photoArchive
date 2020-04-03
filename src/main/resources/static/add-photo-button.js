// const slider = document.querySelector(".slider-switch");
// const fileButton = document.querySelector(".file-button");
// const urlButton = document.querySelector(".url-button");

function clickCorrectButton() {
    if(document.querySelector(".slider-switch").checked){
        console.log("urlButton");
        document.querySelector(".url-button").click();
    }else{
        console.log("fileButton");
        document.querySelector(".file-button").click();
    }
}