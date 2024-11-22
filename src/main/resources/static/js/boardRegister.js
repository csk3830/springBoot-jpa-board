console.log("boardRegister.js in ")

document.getElementById("trigger").addEventListener('click',()=>{
    document.getElementById('file').click();
});

// 실행파일 막기 / 20MB 이상 막기
const regExp = new RegExp("\.(exe|sh|bat|jar|dll|msi)$");
//const regExpImg = new RegExp("\.(jpg|jpeg|png|bmp|gif)$");
const maxSize = 1024*1024*20;

function fileValidation(fileName, fileSize){
    if(regExp.test(fileName)){
        return 0;
    }else if(fileSize > maxSize){
        return 0;
    }else {
        return 1;
    }
}

document.addEventListener('change',(e)=>{
    if(e.target.id == 'file'){
        const fileObject = document.getElementById('file').files;
        console.log(fileObject);

        document.getElementById('regBtn').disabled = false;

        const fileZone = document.getElementById('fileZone');
        // 이전에 추가한 파일 삭제
        fileZone.innerHTML = "";
        let ul = `<ul class="list-group list-group-flush">`;
        let isOk = 1;   // 여러 파일에 대한 값을 확인 1 * fileValidation
        for(let file of fileObject){
            let valid = fileValidation(file.name, file.size);
            isOk *= valid
            ul += `<li class="list-group-item">`
            ul += `<div class="ms-2 me-auto">`;
            ul += `${valid ? '<div class="fw-bold text-success-emphasis">업로드 가능</div>' : '<div class="fw-bold text-danger-emphasis">업로드 불가능</div>'}`;
            ul += `${file.name}</div>`;
            ul += `<span class="badge text-bg-${ valid ? 'success' : 'danger'} rounded-pill">${file.size}Bytes</span></li>`;
        }
        ul += `</ul>`
        fileZone.innerHTML = ul;

        if(isOk == 0){
            document.getElementById('regBtn').disabled = true;
        }
    }
});
