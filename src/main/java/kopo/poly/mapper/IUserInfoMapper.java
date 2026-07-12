package kopo.poly.mapper;


import kopo.poly.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserInfoMapper {

    int insertUserInfo(UserInfoDTO pDTO) throws Exception;
    // 회원가입

    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;
    // 가입 전 아이디 중복 체크(DB 조회)

    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;
    // 가입 전 이메일 중복 체크(DB)

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getUserId(UserInfoDTO pDTO) throws Exception;


    int updatePassword(UserInfoDTO pDTO) throws Exception;


}
