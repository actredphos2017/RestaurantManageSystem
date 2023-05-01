document.addEventListener("DOMContentLoaded", function () {
    let panels = JSON.parse(localStorage.getItem("openedCategories"));
    let collapses = JSON.parse(localStorage.getItem("showingCollapses"));
    localStorage.removeItem("openedCategories");
    localStorage.removeItem("showingCollapses");

    if (panels) {
        panels.forEach((id) => {
            document.getElementById(id).classList.remove("collapsed");
        });
    }

    if (collapses) {
        collapses.forEach((id) => {
            document.getElementById(id).classList.add("show");
        });
    }
});

function memoryReload() {
    let panels = document.querySelectorAll(".accordion-button");
    let collapses = document.querySelectorAll(".accordion-collapse");
    let openedCategories = [];
    let showingCollapses = [];

    panels.forEach((item) => {
        if (!item.classList.contains("collapsed"))
            openedCategories.push(item.id.toString());
    });

    collapses.forEach((item) => {
        if (item.classList.contains("show"))
            showingCollapses.push(item.id.toString());
    })

    localStorage.setItem("openedCategories", JSON.stringify(openedCategories));
    localStorage.setItem("showingCollapses", JSON.stringify(showingCollapses));

    location.reload();
}

let authCode = document.getElementById("auth_code").textContent;
let id = document.getElementById("restaurant_id").textContent;

let addDishTargetCategoryIndex = 1;

async function pushEditRequest(taskName, data) {
    const response = await fetch("/rest/edit_menu", {
        method: "POST",
        headers:
            {
                "auth_code": authCode,
                "id": id,
                "task_name": taskName,
                "data": data
            }
    });
    if (response.ok) {
        let taskResponse = JSON.parse((await response.text()).toString());
        if (taskResponse.ok) return true;
        else {
            alert(taskResponse.reason);
            return false;
        }
    } else {
        alert("请求失败！");
        return false;
    }
}

async function pushSetPictureRequest(targetDish, dishName, formBody) {
    const response = await fetch("/rest/upload_dish_pic", {
        method: "POST",
        headers:
            {
                "auth_code": authCode,
                "id": id,
                "target_dish": targetDish,
                "dish_name": dishName
            },
        body: formBody
    });
    if (response.ok) {
        let taskResponse = JSON.parse((await response.text()).toString());
        if (taskResponse.ok) return true;
        else {
            alert(taskResponse.reason);
            return false;
        }
    } else {
        alert("请求失败！");
        return false;
    }
}

document.querySelectorAll(".add-dish-btn").forEach(
    function (button) {
        button.addEventListener("click", function () {
            addDishTargetCategoryIndex = parseInt(button.getAttribute("category-index"));
            document.getElementById("dish-category-select").value = addDishTargetCategoryIndex;
        });
    }
)

document.querySelectorAll('.del-dish-btn').forEach(
    function (button) {
        button.addEventListener("click", async function () {
            if (confirm("你是否真的要删除菜品 " + button.closest(".card-body").querySelector("h5").textContent + " 吗？")) {
                let attribute = button.getAttribute("dish-index");
                await pushEditRequest("delete_dish", attribute);
                memoryReload();
            }
        });
    });

document.querySelectorAll('.dish-mv-up-btn').forEach(
    function (button) {
        button.addEventListener("click", async function () {
            let attribute = button.getAttribute("dish-index");
            await pushEditRequest("move_up_dish", attribute);
            memoryReload();
        });
    });

let diyCategoryIndex = 1;
let diyOptionIndex = 1;

let diyLists = document.getElementById("diy-questions");

document.getElementById("add-diy-btn").addEventListener("click", function () {

    let newDiyBtnID = "add-diy-class-btn" + diyCategoryIndex;
    let optionAddTargetID = "add-diy-class-place" + diyCategoryIndex;
    let diyRemoveBtnID = "remove-diy-class-btn" + diyCategoryIndex;
    let thisDiyID = "diy-class" + diyCategoryIndex;
    diyCategoryIndex = diyCategoryIndex + 1;

    let newChoice = document.createElement("div");
    newChoice.classList.add("card", "m-2", "p-3", "diy-item");
    newChoice.id = thisDiyID;

    newChoice.innerHTML = `<div class="input-group mb-3 diy-name-input-group">
                                <span class="input-group-text" id="dish-diy-name-input">分类名</span>
                                <input type="text" class="form-control diy-name-input" aria-label="分类名" id="dish-diy-name"
                                       aria-describedby="dish-diy-name-input" placeholder="做法、加料等">
                            </div>
                            <div class="form-check single-choice-check-group">
                                <input class="form-check-input single-choice-checkbox" type="checkbox" value="" id="single-choice">
                                <label class="form-check-label" for="single-choice">
                                    单选分类
                                </label>
                            </div>

                            <div class="diy-option-list" id="` + optionAddTargetID + `"></div>

                            <div class="row my-2">
                                <div class="col-auto">
                                    <button class="btn btn-primary btn-sm" id="` + newDiyBtnID + `">添加选项</button>
                                </div>
                                <div class="col-auto">
                                    <button class="btn btn-danger btn-sm" id="` + diyRemoveBtnID + `">删除分类</button>
                                </div>
                            </div>`;

    diyLists.appendChild(newChoice);

    document.getElementById(diyRemoveBtnID).addEventListener("click", function () {
        document.getElementById(thisDiyID).remove();
    });

    let newOptionBtn = document.getElementById(newDiyBtnID);
    newOptionBtn.addEventListener("click", function () {
        let removeOptionBtnID = "add-diy-option-btn" + diyOptionIndex;
        let thisOptionID = "diy-option-tag" + diyOptionIndex;

        diyOptionIndex = diyOptionIndex + 1;

        let optionList = document.getElementById(optionAddTargetID);

        let newOption = document.createElement("div");
        newOption.classList.add("row", "m-3", "diy-option-item");
        newOption.id = thisOptionID;
        newOption.innerHTML = `<div class="col-auto option-info">
                                    <div class="input-group">
                                        <span class="input-group-text">选项名</span>
                                        <input type="text" class="form-control diy-option-name" placeholder="中辣、加葱花等"
                                               aria-label="Username">
                                        <span class="input-group-text">价格差</span>
                                        <input type="text" class="form-control diy-option-pd" placeholder="正数为升价，负数为降价"
                                               aria-label="Server">
                                    </div>
                                </div>
                                <div class="col-auto">
                                    <button class="btn btn-danger" id="` + removeOptionBtnID + `">D</button>
                                </div>`;

        optionList.appendChild(newOption);

        document.getElementById(removeOptionBtnID).addEventListener("click", function () {
            document.getElementById(thisOptionID).remove();
        });
    });


});

document.getElementById("submit-new-dish-btn").addEventListener("click", async function () {

    let dishName = document.getElementById("dish-name").value;

    let newDish = {
        "bp": parseFloat(document.getElementById("dish-bp").value),
        "commit": document.getElementById("dish-commit").value,
        "name": dishName,
        "pic": "",
        "diy": []
    };

    let diyGroup = diyLists.querySelectorAll(".diy-item");

    diyGroup.forEach(function (item) {
        let newDiyItem = {
            "name": item.querySelector(".diy-name-input-group > .diy-name-input").value,
            "sc": item.querySelector(".single-choice-check-group > .single-choice-checkbox").checked,
            "values": []
        };

        let diyOptionGroup = item.querySelectorAll(".diy-option-list > .diy-option-item > .option-info");

        diyOptionGroup.forEach(function (option) {
            let newOption = {
                "value": option.querySelector(".diy-option-name").value,
                "pd": parseFloat(option.querySelector(".diy-option-pd").value),
                "commit": ""
            };

            newDiyItem.values.push(newOption);
        });

        newDish.diy.push(newDiyItem);

    });

    let targetCategoryIndex = document.getElementById("dish-category-select").value;
    let newDishIndex = document.getElementById("category" + targetCategoryIndex).querySelector(".dishes-list").children.length;

    let data = targetCategoryIndex + "|" + JSON.stringify(newDish);

    await pushEditRequest("add_dish", data);

    let dishPicValue = document.getElementById("dish-pic");
    if (dishPicValue.files.length > 0) {
        let formData = new FormData();
        formData.append("picture", dishPicValue.files[0]);
        await pushSetPictureRequest(targetCategoryIndex + "," + newDishIndex, dishName, formData);
    }

    memoryReload();

});