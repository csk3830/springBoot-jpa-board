package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.UserDTO;
import com.ezen.boot_JPA.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;

@Slf4j
@RequestMapping("/user/*")
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private static final String KAKAO_REST_API_KEY = "ea9c0d226405dacbf737edffbc9299a5";
    private static final String KAKAO_AUTHORIZE_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_REDIRECT_URI = "http://localhost:8089/user/kakao/callback";
    private static final String KAKAO_ACCESS_TOKEN_INFO_URL = "https://kapi.kakao.com/v1/user/access_token_info";


    @GetMapping("/join")
    public void join(){}

    @PostMapping("/join")
    public String join(UserDTO userDTO){
        log.info(">>>> user DTO >>>>>> {}", userDTO);
        //password 암호화
        userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));
        String email = userService.register(userDTO);
        log.info(">>>> email >>>>>> {}", email);
        return (email == null ? "/user/join" : "/index");
    }

    @GetMapping("/login")
    public void Login(@RequestParam(value = "error", required = false) String error,
                      @RequestParam(value = "exception", required = false) String exception,
                      Model model){
        /* 에러와 예외값을 담아 화면으로 전달 */
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        String kakaoLoginPageUrl = KAKAO_AUTHORIZE_URL + "?response_type=code&client_id=" + KAKAO_REST_API_KEY
                + "&redirect_uri=" + KAKAO_REDIRECT_URI;

        model.addAttribute("kakaoLoginPageUrl", kakaoLoginPageUrl);
    }

    @GetMapping("/list")
    public void list(Model model){
        model.addAttribute("userList", userService.getList());
    }

    @GetMapping("/modify")
    public void modify(Principal principal, Model model){
        model.addAttribute("userDTO", userService.selectEmail(principal.getName()));
    }

    @PostMapping("/modify")
    public String modify(UserDTO userDTO){
        if(userDTO.getPwd().length() > 0){
            userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));
        }
        String email = userService.modify(userDTO);
        return "redirect:/";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam("email") String email){
        userService.remove(email);
        return "redirect:/user/logout";
    }

    //////////////////////////
    /* 카카오 소셜 로그인 line */
    /////////////////////////
    @GetMapping("/kakao/callback")
    public String callback(@RequestParam("code") String authorizeCode) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 인가코드 >>>>>>>>>>>>>>>>>> {}", authorizeCode);

        // 인가 코드로 카카오 토큰 요청
        String accessToken = getKakaoAccessToken(authorizeCode);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 액세스 토큰 >>>>>>>>>>>>>>>>>> {}", accessToken);

        // 이후 토큰을 통해 필요한 데이터를 처리
        // 토큰 유효성 검증
        boolean isTokenValid = validateKakaoAccessToken(accessToken);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 토큰 유효성 >>>>>>>>>>>>>>>>>> {}", isTokenValid);

        // 유효성 검사 후 리디렉션
        if (isTokenValid) {
            // 토큰이 유효하면 정상 처리
            return "redirect:/";
        } else {
            // 토큰이 유효하지 않으면 로그아웃 처리하거나 재인증
            return "redirect:/user/logout";
        }
    }

    public String getKakaoAccessToken(String authorizeCode) {
        // MultiValueMap을 사용하여 요청 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_REST_API_KEY);
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("code", authorizeCode);

        // RestTemplate 설정
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // POST 요청 전송
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_TOKEN_URL, HttpMethod.POST, entity, String.class);

        // 응답에서 액세스 토큰 추출
        String accessToken = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            // JSON 응답에서 액세스 토큰을 추출
            String responseBody = response.getBody();
            accessToken = extractKakaoAccessToken(responseBody);
        }

        return accessToken;
    }

    // 카카오액세스 토큰 추출
    public String extractKakaoAccessToken(String responseBody){
        log.info("===========================카카오액세스 토큰 추출 리스폰스바디값 {}", responseBody);
        int start = responseBody.indexOf("\"access_token\":\"") + "\"access_token\":\"".length();
        int end = responseBody.indexOf("\"", start);
        return responseBody.substring(start, end);
    }

    // 카카오 액세스토큰의 유효성 검증 메서드
    private boolean validateKakaoAccessToken(String accessToken){
        // 카카오 액세스 토큰 유효성 검증 요청
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // GET요청 전송
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_ACCESS_TOKEN_INFO_URL, HttpMethod.GET, entity, String.class);

        // 응답 처리
        if(response.getStatusCode() == HttpStatus.OK){
            // 유효한 토큰의 경우 JSON 응답에서 유효성 정보 확인 가능
            String responseBody = response.getBody();
            log.info("토큰 유효성 검증 응답: {}", responseBody);
            return true;
        } else {
            // 유효하지 않은 토큰의 경우
            log.error("토큰 유효성 검증 실패: {}", response.getStatusCode());
            return false;
        }

    }

}
