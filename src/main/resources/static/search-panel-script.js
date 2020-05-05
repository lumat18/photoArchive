const tagContainer = document.querySelector(".tag-container");

const input = document.querySelector(".tag-input");

const holder = document.querySelector(".tag-holder");
const holder2 = document.querySelector(".tag-holder2");

var tags = [];

function createTag(label) {
  const div = document.createElement("div");
  div.setAttribute("class", "tag");
  const span = document.createElement("span");
  span.innerHTML = label;
  const closeBtn = document.createElement("i");
  closeBtn.setAttribute("class", "material-icons");
  closeBtn.setAttribute("data-item", label);
  closeBtn.innerHTML = "close";

  div.appendChild(span);
  div.appendChild(closeBtn);
  return div;
}

function reset() {
  document.querySelectorAll(".tag").forEach(function(tag) {
    tag.parentElement.removeChild(tag);
  });
}

function addTags() {
  reset();
  tags
    .slice()
    .reverse()
    .forEach(function(tag) {
      const newTag = createTag(tag);
      tagContainer.prepend(newTag);
    });
  input.value = "";
  updateHolder();
}

input.addEventListener("keyup", function(e) {
  if (e.keyCode === 32) {
    tags.push(input.value);
    addTags();
  }
});

document.addEventListener("click", function(e) {
  if (e.target.tagName === "I") {
    console.log(tags);
    removeTag(e);
  }
});

function removeTag(e) {
  const tagLabel = e.target.getAttribute("data-item");
  const index = tags.indexOf(tagLabel);
  tags = [...tags.slice(0, index), ...tags.slice(index + 1)];
  addTags();
  updateHolder();
}

function updateHolder() {
  validateHolder(holder);
  validateHolder(holder2);
}

function validateHolder(holder) {
  if(holder !== null){
    holder.value = "";
    tags.forEach(tag => {
      holder.value += tag;
    });
  }
}

function resizable(el, factor) {
  var int = Number(factor) || 50;
  function resize() {
    el.style.width = (el.value.length + 1) * int + "px";
  }
  var e = "keyup,keypress,focus,blur,change".split(",");
  for (var i in e) el.addEventListener(e[i], resize, false);
  resize();
}
resizable(document.getElementById("search-input-field"), 11);
