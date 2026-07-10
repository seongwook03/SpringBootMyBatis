package kopo.poly.service.impl;


import kopo.poly.dto.MailDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.mapper.IUserInfoMapper;
import kopo.poly.service.IMailService;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService{

    private final IUserInfoMapper userInfoMapper;

    private final IMailService mailService;


    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {
        log.info("{}.getUserIdExists Start!", this.getClass().getName());

        UserInfoDTO rDTO = userInfoMapper.getUserIdExists(pDTO);

        log.info("{}.getUserIdExists End!", this.getClass().getName());

        return rDTO;
    }

    @Override
    public UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception {

        log.info("{}.emailAuth Start!", this.getClass().getName());

        UserInfoDTO rDTO = Optional.ofNullable(userInfoMapper.getEmailExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info("rDTO : {}", rDTO);

        if (CmmUtil.nvl(rDTO.getExistsYn()).equals("N")) {
            int authNumber = ThreadLocalRandom.current().nextInt(10000, 1000000);

            log.info("authNumber : {}", authNumber);

            MailDTO dto = new MailDTO();

            dto.setTitle("이메일 중복 확인 인증번호 발송 메일");
            dto.setContents("인증번호는 " + authNumber + " 입니다.");
            dto.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            mailService.doSendMail(dto);

            dto = null;

            rDTO.setAuthNumber(authNumber);

        }

        log.info("{}.emailAuth End!", this.getClass().getName());

        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info("{}.insertUserInfo Start!", this.getClass().getName());

        int res;

        int success = userInfoMapper.insertUserInfo(pDTO);

        if (success > 0){
            res = 1;

            MailDTO mDTO = new MailDTO();

            mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            mDTO.setTitle("회원가입을 축하드립니다.");


            mDTO.setContents(CmmUtil.nvl(pDTO.getUserName()) + "님의 회원가입을 진심으로 축하드립니다.");


            mailService.doSendMail(mDTO);
        } else {
        res = 0;

    }

    log.info("{}.insertUserInfo End!", this.getClass().getName());

    return res;
    }

    @Override
    public UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception {

        log.info("{}.getLogin Start!", this.getClass().getName());

        // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기 위한 mapper 호출하기
        // userInfoMapper.getUserLoginCheck(pDTO) 함수 실행 결과가 NUll 발생하면, UserInfoDTO 메모리에 올리기
        UserInfoDTO rDTO = Optional.ofNullable(userInfoMapper.getLogin(pDTO)).orElseGet(UserInfoDTO::new);

        /*
         * userInfoMapper로 부터 SELECT 쿼리의 결과로 회원아이디를 받아왔다면, 로그인 성공!!
         *
         * DTO의 변수에 값이 있는지 확인하기 처리속도 측면에서 가장 좋은 방법은 변수의 길이를 가져오는 것이다.
         * 따라서  .length() 함수를 통해 회원아이디의 글자수를 가져와 0보다 큰지 비교한다.
         * 0보다 크다면, 글자가 존재하는 것이기 때문에 값이 존재한다.
         */
        if (!CmmUtil.nvl(rDTO.getUserId()).isEmpty()) {

            MailDTO mDTO = new MailDTO();

            //아이디, 패스워드 일치하는지 체크하는 쿼리에서 이메일 값 받아오기(아직 암호화되어 넘어오기 때문에 복호화 수행함)
            mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(rDTO.getEmail())));

            mDTO.setTitle("로그인 알림!"); //제목

            //메일 내용에 가입자 이름넣어서 내용 발송
            mDTO.setContents(DateUtil.getDateTime("yyyy.MM.dd hh:mm:ss") + "에 "
                    + CmmUtil.nvl(rDTO.getUserName()) + "님이 로그인하였습니다.");

            //회원 가입이 성공했기 때문에 메일을 발송함
            mailService.doSendMail(mDTO);

        }

        log.info("{}.getLogin End!", this.getClass().getName());

        return rDTO;
    }

    @Override
    public UserInfoDTO getUserId(UserInfoDTO pDTO) throws Exception {
        return null;
    }

    @Override
    public UserInfoDTO searchUserIdOrPasswordProc(UserInfoDTO pDTO) throws Exception {

        log.info("{}.searchUserIdOrPasswordProc Start!", this.getClass().getName());

        UserInfoDTO rDTO = userInfoMapper.getUserId(pDTO);

        log.info("{}.searchUserIdOrPasswordProc End!", this.getClass().getName());

        return rDTO;
    }




}
