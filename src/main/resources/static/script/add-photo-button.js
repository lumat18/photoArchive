function clickCorrectButton() {
    if(document.querySelector(".slider-switch").checked){
        console.log("urlButton");
        document.querySelector(".url-button").click();
    }else{
        console.log("fileButton");
        document.querySelector(".file-button").click();
    }
}