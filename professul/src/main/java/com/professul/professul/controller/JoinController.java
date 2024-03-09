package com.professul.professul.controller;

import com.professul.professul.dto.JoinDTO;
import com.professul.professul.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService=joinService;
    }

    @PostMapping("/join")
    public String joinProcess(JoinDTO joinDTO) throws Exception {
        joinService.joinProcess(joinDTO);
        return "ok";
    }

}
