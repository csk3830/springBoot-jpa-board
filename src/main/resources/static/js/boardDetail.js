console.log('boardDetail.js in');

document.getElementById('listBtn').addEventListener('click',()=>{
    location.href = "/board/list";
});
document.getElementById('modBtn').addEventListener('click', ()=>{
    document.getElementById('title').readOnly=false;
    document.getElementById('content').readOnly=false;
    document.getElementById('writer').readOnly=false;

    // 버튼 생성
    let modBtn = document.createElement("button");
    modBtn.setAttribute("type", "submit");
    modBtn.classList.add("btn", "btn-outline-warning");
    modBtn.innerText="Submit";

    // 추가
    document.getElementById("modForm").appendChild(modBtn);
    document.getElementById("modBtn").remove();
    document.getElementById("delBtn").remove();
})