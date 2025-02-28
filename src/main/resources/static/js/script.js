document.addEventListener("DOMContentLoaded", function() {
    let selectedDate = null;
    const totalTimeElem = document.getElementById("total-time");
    const selectedLecturesElem = document.getElementById("selected-lectures");

    document.querySelectorAll(".calendar-cell").forEach(cell => {
        cell.addEventListener("click", function() {
            document.querySelectorAll(".calendar-cell").forEach(c => c.classList.remove("selected"));
            this.classList.add("selected");
            selectedDate = this.getAttribute("data-date");

            fetch(`/schedule?date=${selectedDate}`)
                .then(response => response.json())
                .then(data => {
                    selectedLecturesElem.innerHTML = "";
                    document.querySelectorAll(".lesson-checkbox").forEach(cb => cb.disabled = false);
                    data.forEach(schedule => {
                        const listItem = document.createElement("li");
                        listItem.textContent = `[${schedule.date}] ${schedule.lesson.name} (${schedule.lesson.duration}분)`;
                        listItem.setAttribute("data-lesson-id", schedule.lesson.id);
                        listItem.setAttribute("data-date", schedule.date);
                        selectedLecturesElem.appendChild(listItem);

                        const lessonCheckbox = document.querySelector(`.lesson-checkbox[data-lesson-id='${schedule.lesson.id}']`);
                        if (lessonCheckbox) {
                            lessonCheckbox.disabled = true;
                        }
                    });
                    updateTotalTime();
                });
        });
    });

    document.querySelectorAll(".lesson-checkbox").forEach(checkbox => {
        checkbox.addEventListener("change", function() {
            if (!selectedDate) {
                alert("날짜를 먼저 선택하세요.");
                this.checked = false;
                return;
            }

            const lessonId = this.getAttribute("data-lesson-id");
            const lessonName = this.nextElementSibling.textContent;

            if (this.checked) {
                fetch(`/schedule`, {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: `date=${selectedDate}&lessonId=${lessonId}`
                }).then(() => {
                    const listItem = document.createElement("li");
                    listItem.textContent = `[${selectedDate}] ${lessonName}`;
                    listItem.setAttribute("data-lesson-id", lessonId);
                    listItem.setAttribute("data-date", selectedDate);
                    selectedLecturesElem.appendChild(listItem);
                    checkbox.disabled = true;
                    updateTotalTime();
                });
            } else {
                fetch(`/schedule?date=${selectedDate}&lessonId=${lessonId}`, {
                    method: "DELETE"
                }).then(() => {
                    document.querySelectorAll(`#selected-lectures li[data-lesson-id='${lessonId}'][data-date='${selectedDate}']`).forEach(el => el.remove());
                    checkbox.disabled = false;
                    checkbox.checked = false;
                    updateTotalTime();
                });
            }
        });
    });

    function updateTotalTime() {
        let totalMinutes = 0;
        document.querySelectorAll("#selected-lectures li").forEach(item => {
            const lessonId = item.getAttribute("data-lesson-id");
            const lessonCheckbox = document.querySelector(`.lesson-checkbox[data-lesson-id='${lessonId}']`);
            totalMinutes += parseInt(lessonCheckbox.getAttribute("data-duration"));
        });
        totalTimeElem.textContent = totalMinutes;
    }
});

document.addEventListener("DOMContentLoaded", function() {
    const calendarBody = document.getElementById("calendar-body");
    const currentMonthText = document.getElementById("current-month");
    const prevMonthBtn = document.getElementById("prev-month");
    const nextMonthBtn = document.getElementById("next-month");

    let currentDate = new Date(); // 현재 날짜 기준

    function renderCalendar(date) {
        calendarBody.innerHTML = ""; // 기존 달력 지우기
        const year = date.getFullYear();
        const month = date.getMonth();

        currentMonthText.textContent = `${year}년 ${month + 1}월`;

        const firstDay = new Date(year, month, 1).getDay(); // 해당 월의 첫 번째 날 요일
        const lastDate = new Date(year, month + 1, 0).getDate(); // 해당 월의 마지막 날짜

        let row = document.createElement("tr");

        // 빈 칸 추가 (첫 요일 전까지)
        for (let i = 0; i < firstDay; i++) {
            row.appendChild(document.createElement("td"));
        }

        // 날짜 추가
        for (let day = 1; day <= lastDate; day++) {
            const cell = document.createElement("td");
            cell.textContent = day;
            cell.setAttribute("data-date", `${year}-${month + 1}-${day}`);
            cell.classList.add("calendar-cell");

            // 날짜 클릭 이벤트 (선택된 날짜 표시)
            cell.addEventListener("click", function() {
                document.querySelectorAll(".calendar-cell").forEach(c => c.classList.remove("selected"));
                this.classList.add("selected");
                console.log("선택한 날짜:", this.getAttribute("data-date")); // 선택한 날짜 출력 (추후 강의 추가 가능)
            });

            row.appendChild(cell);

            // 한 주가 끝나면 새로운 row 추가
            if ((day + firstDay) % 7 === 0 || day === lastDate) {
                calendarBody.appendChild(row);
                row = document.createElement("tr");
            }
        }
    }

    // 이전 달 이동
    prevMonthBtn.addEventListener("click", function() {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendar(currentDate);
    });

    // 다음 달 이동
    nextMonthBtn.addEventListener("click", function() {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendar(currentDate);
    });

    renderCalendar(currentDate); // 초기 렌더링
});
