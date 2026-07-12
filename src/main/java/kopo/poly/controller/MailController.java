package kopo.poly.controller;


import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.dto.MailDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/mail")
@Controller
public class MailController {

    private final IMailService mailService; // 메일 발송 위한 서비스 객체 사용

    @GetMapping(value = "mailForm")//베일 발송하기 폼
    public String mailForm(){

        log.info("{}.mailForm Start!", this.getClass().getName());

        return "mail/mailForm";

    }

    @ResponseBody
    @PostMapping(value = "sendMail")
    public MsgDTO sendMail(HttpServletRequest request){

        log.info("{}.sendMail Start!", this.getClass().getName());

        String msg; // 발송 결과 메시지

        // 월 URL로부터 전달받는 값
        String toMail = CmmUtil.nvl(request.getParameter("toMail")); // 받는사람
        String title = CmmUtil.nvl(request.getParameter("title")); // 제목
        String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

        log.info("toMail : {} / title : {} / contents : {}", toMail, title, contents);

        MailDTO pDTO = new MailDTO(); // 메일 발송할 정보 넣기위한 dto객체 생성

        pDTO.setToMail(toMail); // 3개모두 dto에 저장
        pDTO.setTitle(title);
        pDTO.setContents(contents);

        int res = mailService.doSendMail(pDTO); // 발송

        if (res == 1){ // 성공
            msg = "메일 발송하였습니다.";

        } else {
            msg = "메일 발송 실패하였습니다.";
        }

        log.info(msg);

        MsgDTO dto = new MsgDTO(); // 결과 메시지 전달
        dto.setMsg(msg);

        log.info("{}.sendMail End", this.getClass().getName());

        return dto;

    }
}
