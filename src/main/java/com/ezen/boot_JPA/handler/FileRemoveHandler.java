package com.ezen.boot_JPA.handler;

import com.ezen.boot_JPA.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class FileRemoveHandler {

    private final String BASE_PATH = "D:\\_myProject\\_java\\_fileUpload\\";

    public boolean deleteFile(FileDTO fileDTO) {
        File delFile = new File(BASE_PATH, fileDTO.getSaveDir());
        boolean isDel = false;
        String delete = fileDTO.getUuid()+"_"+fileDTO.getFileName();

        try {
            File deleteFile = new File(delFile, delete);
            log.info(">>>> deleteFile {}", deleteFile);
            if(deleteFile.exists()) {
                isDel = deleteFile.delete();
            }
            if(fileDTO.getFileType() > 0){
                String deleteThumb = fileDTO.getUuid()+"_th_"+fileDTO.getFileName();
                File deleteThumbFile = new File(delFile, deleteThumb);
                log.info(">>>> deleteThumbFile {}", deleteThumbFile);
                if(deleteThumbFile.exists()) {
                    deleteThumbFile.delete();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDel;
    }

}
