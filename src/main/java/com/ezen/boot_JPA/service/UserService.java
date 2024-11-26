package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.AuthUserDTO;
import com.ezen.boot_JPA.dto.UserDTO;
import com.ezen.boot_JPA.entity.AuthUser;
import com.ezen.boot_JPA.entity.User;

import java.util.List;

public interface UserService {
    /* convert 작업
     * UserDTO(화면) => User(저장) 변환 */
    default User convertDtoToEntity(UserDTO userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .pwd(userDTO.getPwd())
                .nickName(userDTO.getNickName())
                .lastLogin(userDTO.getLastLogin())
                .build();
    }

    /* UserDTO(화면) => authUser(저장) 변환 */
    default AuthUser convertDtoToAuthEntity(UserDTO userDTO) {
        return AuthUser.builder()
                .email(userDTO.getEmail())
                .auth("ROLE_USER")
                .build();
    }

    /* authUser(entity) => authUserDTO */
    default AuthUserDTO convertEntityToAuthDto(AuthUser authUser){
        return AuthUserDTO.builder()
                .email(authUser.getEmail())
                .auth(authUser.getAuth())
                .build();
    }

    /* user(entity) => userDTO */
    default UserDTO convertEntityToDto(User user, List<AuthUserDTO> authUserDTOList) {
        return UserDTO.builder()
                .email(user.getEmail())
                .pwd(user.getPwd())
                .nickName(user.getNickName())
                .lastLogin(user.getLastLogin())
                .registerAt(user.getRegisterAt())
                .modifyAt(user.getModifyAt())
                .authList(authUserDTOList)
                .build();
    }



    String register(UserDTO userDTO);

    UserDTO selectEmail(String username);

    boolean updateLastLogin(String authEmail);

    List<UserDTO> getList();

    String modify(UserDTO userDTO);

    void remove(String email);
}
