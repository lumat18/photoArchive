const tagContainer = document.querySelector(".tag-container");

const input = document.querySelector(".tag-input");

const holder = document.querySelector(".tag-holder");

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
}

input.addEventListener("keyup", function(e) {
  if (e.keyCode === 32) {
    tags.push(input.value);
    addTags();
    holder.value = holder.value + input.value;
    input.value = "";
  }
});

document.addEventListener("click", function(e) {
  if (e.target.tagName === "I") {
    console.log(tags);
    const tagLabel = e.target.getAttribute("data-item");
  }
});
