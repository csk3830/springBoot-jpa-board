package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.CommentDTO;
import com.ezen.boot_JPA.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment/*")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post")
    @ResponseBody
    public String post(@RequestBody CommentDTO commentDTO) {
        long cno = commentService.post(commentDTO);
        return cno > 0 ? "1" : "0";
    }

    @GetMapping("/list/{bno}/{page}")
    @ResponseBody
    public Page<CommentDTO> list(@PathVariable("bno") long bno, @PathVariable("page") int page){
        // page = 0 부터
        // 1page => 0  / 2page => 1
        page = (page==0 ? 0 : page - 1);
        // List<CommentDTO> list = commentService.getList(bno, page);

        Page<CommentDTO> list = commentService.getList(bno,page);
        log.info(">>>> list >> {}", list);
        return list;
    }

    @ResponseBody
    @DeleteMapping("/remove/{cno}")
    public String remove(@PathVariable("cno") long cno){
        long isOk = commentService.remove(cno);
        return isOk == 0 ? "1" : "0";
    }

    @PutMapping("/update")
    @ResponseBody
    public String update(@RequestBody CommentDTO commentDTO){
        long cno = commentService.update(commentDTO);
        return cno > 0 ? "1" : "0";
    }

}
