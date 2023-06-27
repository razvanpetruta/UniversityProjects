"use strict";

const button = document.querySelector(".sort");
const textarea = document.querySelector("textarea");
const table = document.querySelector("table");

const sortElements = function (e) {
    // e.preventDefault();

    const input = textarea.value.trim();
    const array = input.split(" ").map((a) => Number(a));

    array.sort((a, b) => a - b);

    table.innerHTML = "";

    const numRows = Math.ceil(array.length / 5);
    for (let i = 0; i < numRows; i++) {
        const row = document.createElement("tr");
        for (let j = 0; j < 5; j++) {
            const index = i * 5 + j;
            if (index >= array.length) {
                break;
            }
            const cell = document.createElement("td");
            cell.textContent = String(array[index]);
            row.appendChild(cell);
        }
        table.appendChild(row);
    }
}

button.addEventListener("click", sortElements);