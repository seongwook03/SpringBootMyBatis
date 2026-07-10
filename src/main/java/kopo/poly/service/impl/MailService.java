package kopo.poly.service.impl;

import jakarta.mail.internet.MimeMessage;
import kopo.poly.dto.MailDTO;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class MailService implements IMailService {

    private final JavaMailSender mailSender;
    //JavaMailSender -> 스프링 제공 (메일 발송 객체)

    @Value("${spring.mail.username}")// -> 저 이름의 설정값을 application에서 가지고 옴
    private String fromMail;


    @Override
    public int doSendMail(MailDTO pDTO) {

        log.info("{}.doSendMail start!", this.getClass().getName());


        int res = 1;
        //메일 발송 성공여부 확인용


        //DTO 로부터 데이터 가져오기. null에 의한 에러방지를 위한 if문
        if (pDTO == null) {
            pDTO = new MailDTO();
        }

        String toMail = CmmUtil.nvl(pDTO.getToMail()); // 받는사람
        String title = CmmUtil.nvl(pDTO.getTitle()); // 메일제목
        String contents = CmmUtil.nvl(pDTO.getContents()); // 메일제목

        log.info("toMail : {} / title : {} / contents : {}", toMail, title, contents);


        MimeMessage message = mailSender.createMimeMessage();


        MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");

        try {

            messageHelper.setTo(toMail);
            messageHelper.setFrom(fromMail);
            messageHelper.setSubject(title);
            messageHelper.setText(contents);
            mailSender.send(message);

        } catch (Exception e) {
            res = 0;
            log.info("[ERRER] doSendMail : {}", e);
        }

        log.info("{}.doSendMail end!", this.getClass().getName());
        return res;
    }

}

