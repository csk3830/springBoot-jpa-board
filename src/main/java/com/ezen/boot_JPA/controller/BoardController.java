package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.BoardDTO;
import com.ezen.boot_JPA.dto.BoardFileDTO;
import com.ezen.boot_JPA.dto.FileDTO;
import com.ezen.boot_JPA.dto.PagingVO;
import com.ezen.boot_JPA.handler.FileHandler;
import com.ezen.boot_JPA.handler.FileRemoveHandler;
import com.ezen.boot_JPA.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/*")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final FileHandler fileHandler;

    @GetMapping("/register")
    public void register() {}

    // 파일첨부 없이
    /*@PostMapping("/register")
    public String register(BoardDTO boardDTO) {
        log.info(">>> boardDTO >> {}", boardDTO);
        // insert, update, delete   => return 1 row
        // jpa insert, update, delete => return id
        Long bno = boardService.insert(boardDTO);
        log.info(">>> insert >> {}", bno > 0 ? "OK" : "Fail");
        return "redirect:/board/list";
    }*/

    @PostMapping("/register")
    public String register(BoardDTO boardDTO, @RequestParam(name = "files", required = false) MultipartFile[] files) {
        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0){
            // 파일 핸들러 작업
            fileList = fileHandler.uploadFiles(files);
        }
        long bno = boardService.insert(new BoardFileDTO(boardDTO, fileList));
        return "redirect:/board/list";
    }

    /*@GetMapping("/list")
    public void list(Model model) {
        //paging 없는 케이스
        List<BoardDTO> list = boardService.getList();
        model.addAttribute("list", list);
    }*/

    /*@GetMapping("/list")
    public void list(Model model, @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        // 화면에서 들어오는 pageNo = 1 / 0으로 처리가 돼야 한다는 의미
        // 화면에서 들어오는 pageNo = 2 / 1로 처리가 돼야 한다는 의미
        log.info(">>> pageNo >> {}", pageNo);
        pageNo = (pageNo == 0 ? 0 : pageNo - 1);
        log.info(">>> pageNo >> {}", pageNo);
        Page<BoardDTO> list = boardService.getList(pageNo);
        log.info(">>> list >> {}", list.toString());
        log.info(">>> totalCount >> {}", list.getTotalElements());  // 전체 글 수
        log.info(">>> totalPage >> {}", list.getTotalPages());  // 전체 페이지 수 => realEndPage
        log.info(">>> pageNumber >> {}", list.getNumber());  // 현재 페이지 번호 => pageNo
        log.info(">>> pageSize >> {}", list.getSize());  // 한 페이지에 표시되는 길이 => qty
        log.info(">>> next >> {}", list.hasNext());  // next 여부(bool)
        log.info(">>> prev >> {}", list.hasPrevious());  // prev 여부(bool)

        PagingVO pagingVO = new PagingVO(list, pageNo);
        log.info(">>> pagingVO >> {}", pagingVO.toString());
        model.addAttribute("list", list);
        model.addAttribute("pagingVO", pagingVO);
    }*/

    @GetMapping("/list")
    public void list(Model model, 
                     @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                     @RequestParam(value = "type", required = false) String type,
                     @RequestParam(value = "keyword", required = false) String keyword) {
        pageNo = (pageNo == 0 ? 0 : pageNo - 1);
        Page<BoardDTO> list = boardService.getList(pageNo, type, keyword); // type, keyword 추가하여 서비스임플로 보내기
        PagingVO pagingVO = new PagingVO(list, pageNo, type, keyword);
        model.addAttribute("list", list);
        model.addAttribute("pagingVO", pagingVO);
    }

    @GetMapping("/detail")
    public void detail(Model model, @RequestParam("bno") Long bno) {
        //BoardDTO boardDTO = boardService.getDetail(bno);
        boardService.viewsUp(bno);
        BoardFileDTO boardFileDTO = boardService.getDetail(bno);
        model.addAttribute("boardFileDTO", boardFileDTO);
    }

    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, @RequestParam(name = "files", required = false) MultipartFile[] files, RedirectAttributes redirectAttributes) {
        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0){
            // 파일 핸들러 작업
            fileList = fileHandler.uploadFiles(files);
        }
        Long bno = boardService.modify(new BoardFileDTO(boardDTO, fileList));
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/detail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("bno") Long bno) {
        boardService.delete(bno);
        return "redirect:/board/list";
    }

    @ResponseBody
    @DeleteMapping("/file/{uuid}")
    public String fileRemove(@PathVariable("uuid") String uuid){
        FileDTO fileDTO = boardService.getFile(uuid);
        long bno = boardService.fileRemove(uuid);

        FileRemoveHandler fileRemoveHandler = new FileRemoveHandler();
        boolean isDel = fileRemoveHandler.deleteFile(fileDTO);
        return (bno > 0 && isDel) ?  "1" : "0" ;
    }
}
