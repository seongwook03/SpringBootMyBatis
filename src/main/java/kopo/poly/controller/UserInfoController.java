package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/user")
@Controller
public class UserInfoController {

    private final IUserInfoService userInfoService;

    @GetMapping(value = "userRegForm")
    public String userRegForm() {

        log.info("{}.user/userRegForm", this.getClass().getName());

        return "/user/userRegForm";
    }

    @ResponseBody
    @PostMapping(value = "getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {

        log.info("{}.getUserIdExists Start!", this.getClass().getName());

        String userId = CmmUtil.nvl(request.getParameter("userId"));

        log.info("userId : {}", userId);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info("{}.getUserIdExists End!", this.getClass().getName());

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "getEmailExists")
    public UserInfoDTO getEmailExists(HttpServletRequest request) throws Exception {

        log.info("{}.getEmailExists Start!", this.getClass().getName());

        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("email : {}", email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info("{}.getEmailExists End!", this.getClass().getName());

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request) {

        log.info("{}.insertUserInfo start!", this.getClass().getName());

        int res = 0;
        String msg = "";
        MsgDTO dto;


        UserInfoDTO pDTO;

        try {


            String userId = CmmUtil.nvl(request.getParameter("userId")); //아이디
            String userName = CmmUtil.nvl(request.getParameter("userName")); //이름
            String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호
            String email = CmmUtil.nvl(request.getParameter("email")); //이메일
            String addr1 = CmmUtil.nvl(request.getParameter("addr1")); //주소
            String addr2 = CmmUtil.nvl(request.getParameter("addr2")); //상세주소

            log.info("userId : " + userId);
            log.info("userName : " + userName);
            log.info("password : " + password);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);
            pDTO.setUserName(userName);


            pDTO.setPassword(EncryptUtil.encHashSHA256(password));


            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            res = userInfoService.insertUserInfo(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "회원가입되었습니다.";


            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";

            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";
            }

        } catch (Exception e) {

            msg = "실패하였습니다. : " + e;
            log.info(e.toString());

        } finally {

            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info("{}.insertUserInfo End!", this.getClass().getName());
        }

        return dto;
    }

    /**
     * 로그인을 위한 입력 화면으로 이동
     */
    @GetMapping(value = "login")
    public String login() {
        log.info("{}.login Start!", this.getClass().getName());

        log.info("{}.login End!", this.getClass().getName());

        return "user/login";
    }

    /**
     * 로그인 처리 및 결과 알려주는 화면으로 이동
     */
    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) {

        log.info("{}.loginProc Start!", this.getClass().getName());

        int res = 0; //로그인 처리 결과를 저장할 변수 (로그인 성공 : 1, 아이디, 비밀번호 불일치로인한 실패 : 0, 시스템 에러 : 2)
        String msg = ""; //로그인 결과에 대한 메시지를 전달할 변수
        MsgDTO dto; // 결과 메시지 구조

        //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
        UserInfoDTO pDTO;

        try {

            String userId = CmmUtil.nvl(request.getParameter("userId")); //아이디
            String password = CmmUtil.nvl(request.getParameter("password")); //비밀번호

            log.info("userId : {} / password : {}", userId, password);

            //웹(회원정보 입력화면)에서 받는 정보를 저장할 변수를 메모리에 올리기
            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId);

            //비밀번호는 절대로 복호화되지 않도록 해시 알고리즘으로 암호화함
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기 위한 userInfoService 호출하기
            UserInfoDTO rDTO = userInfoService.getLogin(pDTO);

            /*
             * 로그인을 성공했다면, 회원아이디 정보를 session에 저장함
             *
             * 세션은 톰켓(was)의 메모리에 존재하며, 웹사이트에 접속한 사람(연결된 객체) 마다 메모리에 값을 올린다.
             *
             * 예) 톰켓에 100명의 사용자가 로그인했다면, 사용자 각각 회원아이디를 메모리에 저장하며,
             *     메모리에 저장된 객체의 수는 100개이다.
             *     따라서 과도한 세션은 톰켓의 메모리 부하를 발생시켜 서버가 다운되는 현상이 있을 수 있기때문에,
             *     최소한으로 사용하는 것을 권장한다.
             *
             * 스프링에서 세션을 사용하기 위해서는 함수명의 파라미터에 HttpSession session 존재해야 한다.
             * 세션은 톰켓의 메모리에 저장되기 때문에 url 마다 전달하는게 필요하지 않고,
             * 그냥 메모리에서 부르면 되기 때문에 jsp, controller에서 쉽게 불러서 쓸수 있다.
             * */
            if (!CmmUtil.nvl(rDTO.getUserId()).isEmpty()) { //로그인 성공

                res = 1;
                /*
                 * 세션에 회원아이디 저장하기, 추후 로그인여부를 체크하기 위해 세션에 값이 존재하는지 체크한다.
                 * 일반적으로 세션에 저장되는 키는 대문자로 입력하며, 앞에 SS를 붙인다.
                 *
                 * Session 단어에서 SS를 가져온 것이다.
                 */
                msg = "로그인이 성공했습니다.";

                session.setAttribute("SS_USER_ID", userId);
                session.setAttribute("SS_USER_NAME", CmmUtil.nvl(rDTO.getUserName()));

            } else {
                msg = "아이디와 비밀번호가 올바르지 않습니다.";

            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "시스템 문제로 로그인이 실패했습니다.";
            res = 2;
            log.info(e.toString());

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info("{}.loginProc End!", this.getClass().getName());
        }

        return dto;
    }

    /**
     * 로그인 성공 페이지 이동
     */
    @GetMapping(value = "loginResult")
    public String loginSuccess() {
        log.info("{}.user/loginResult Start!", this.getClass().getName());

        log.info("{}.user/loginResult End!", this.getClass().getName());

        return "user/loginResult";
    }

    /**
     * 아아디 찾기 화면
     */
    @GetMapping(value = "searchUserId")
    public String searchUserId() {
        log.info("{}.user/searchUserId Start!", this.getClass().getName());

        log.info("{}.user/searchUserId End!", this.getClass().getName());

        return "user/searchUserId";
    }

    /**
     * 아아디 찾기 로직 수행
     */
    @PostMapping(value = "searchUserIdProc")
    public String searchUserIdProc(HttpServletRequest request, ModelMap model) throws Exception {
        log.info("{}.searchUserIdProc Start!", this.getClass().getName());

        /*
         * ##################################################################
         *          웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장!!
         *
         *    무조건 웹으로 받은 정보는 DTO에 저장하기 위해 임시로 String 변수에 저장함
         * ##################################################################
         */

        String userName = CmmUtil.nvl(request.getParameter("userName")); // 이름
        String email = CmmUtil.nvl(request.getParameter("email")); // 이메일

        /*
         * ##################################################################
         *   반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함
         *                    반드시 작성할 것
         * ##################################################################
         */
        log.info("userName : {} /email : {}", userName, email);

        /*
         * ##################################################################
         *          웹(회원정보 입력화면)에서 받는 정보를 DTO에 저장하기!!
         *
         *          무조건 웹으로 받은 정보는 DTO에 저장해야 한다고 이해하길 권함
         * ##################################################################
         */

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserName(userName);
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchUserIdOrPasswordProc(pDTO))
                .orElseGet(UserInfoDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info("{}.searchUserIdProc End!", this.getClass().getName());

        return "user/searchUserIdResult";
    }

}
