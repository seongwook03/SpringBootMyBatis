package kopo.poly.service;

import kopo.poly.dto.MailDTO;

public interface IMailService {

    int doSendMail(MailDTO pDTO);
    //매일 전송 기능을 명세하는 인터페이스
    //유지보수를 위해 불필요한 예외확산을 반지, 구현부 책임을 분리함 (throw 구현 X)
}
