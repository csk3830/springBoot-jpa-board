package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.MessageDTO;
import com.ezen.boot_JPA.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/message/*")
@Slf4j
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String register(MessageDTO messageDTO){
        log.info(">>> messageDTO >> {}", messageDTO);
        Long mno = messageService.insert(messageDTO);
        log.info(">>>> message insert >>> {}", mno > 0 ? "OK" : "Fail");
        return "redirect:/message/list";
    }

    @GetMapping("/list")
    public void list(Principal principal, Model model){
        model.addAttribute("messageDTO", messageService.selectEmail(principal.getName()));
    }


}
