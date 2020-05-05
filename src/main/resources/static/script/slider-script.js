const slider = document.querySelector(".slider-switch");

const urlInput = document.querySelector(".url-input");
const fileInput = document.querySelector(".file-input");

function slide(){
    if(slider.checked){
        urlInput.disabled = false;
        fileInput.disabled = true;
    }else{
        urlInput.disabled = true;
        fileInput.disabled = false;
    }
}