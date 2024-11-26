console.log('boardDetail.js in');

document.getElementById('listBtn').addEventListener('click',()=>{
    location.href = "/board/list";
});

if(document.getElementById('modBtn')){
    document.getElementById('modBtn').addEventListener('click', ()=>{
        document.getElementById('title').readOnly=false;
        document.getElementById('content').readOnly=false;
        document.getElementById('writer').readOnly=false;

        // 버튼 생성
        let modBtn = document.createElement("button");
        modBtn.setAttribute("type", "submit");
        modBtn.setAttribute("id", "regBtn");
        modBtn.classList.add("btn", "btn-outline-warning");
        modBtn.innerText="저장";

        // 추가
        document.getElementById("modForm").appendChild(modBtn);
        document.getElementById("modBtn").remove();
        document.getElementById("delBtn").remove();

        // file-x   fileUpload 버튼 disabled = false
        document.getElementById('trigger').style.display='inline-block';

        let fileDelBtn = document.querySelectorAll(".file-x");
        console.log(fileDelBtn);
        for(let delBtn of fileDelBtn){
            delBtn.style.display = 'inline-block';
        }
    });
}

document.addEventListener('click', (e)=>{
    if(e.target.classList.contains('file-x')){
        let uuid = e.target.dataset.uuid;
        fileRemoveToServer(uuid).then(result =>{
            if(result > 0  ){
                alert('파일삭제성공');
                e.target.closest('li').remove();
            }
        })
    }
});

async function fileRemoveToServer(uuid) {
    try {
        const url = '/board/file/'+uuid;
        const config={
            method:'delete'
        }
        const resp = await fetch(url, config);
        const result = await resp.text();
        return result;
    } catch (error) {
        console.log(error);
    }
}