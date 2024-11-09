package com.example.book_ecommerce_api_service.service.impl;

import com.example.book_ecommerce_api_service.components.MailComponents;
import com.example.book_ecommerce_api_service.domain.User;
import com.example.book_ecommerce_api_service.dto.RegisterDto;
import com.example.book_ecommerce_api_service.exception.CustomException;
import com.example.book_ecommerce_api_service.repository.UserRepository;
import com.example.book_ecommerce_api_service.service.UserService;
import com.example.book_ecommerce_api_service.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MailComponents mailComponents;
    private final Map<String, String> emailVerificationMap = new HashMap<>();

    @Async
    @Transactional
    public CompletableFuture<Void> registerUser(RegisterDto.Request request){
        try {
            boolean isExists = userRepository.existsByEmail(request.getEmail());

            if (isExists){
                throw new CustomException(ErrorCode.ALREADY_EXISTS_EMAIL);
            }

            if (!Objects.equals(request.getPassword(), request.getConfirmPassword())){
                throw new CustomException(ErrorCode.UN_MATCH_CONFIRM_PASSWORD);
            }

            if (!request.getPassword().matches(".*[a-zA-Z].*") ||
                    !request.getPassword().matches(".*[0-9].*") ||
                    !request.getPassword().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                throw new CustomException(ErrorCode.NOT_VALID_PATTERN);
            }

            String uuid = UUID.randomUUID().toString();

            User newUser = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                    .address(request.getAddress())
                    .phone(request.getPhone())
                    .type(request.getType())
                    .certification(false)
                    .build();

            userRepository.save(newUser);

            String email = request.getEmail();
            String subject = "회원가입을 축하합니다.";
            String text = "";
            text += "<h3>" + "인증 확인 코드입니다." + "</h3>";
            text += "<h1>" + uuid + "</h1>";
            text += "<h3>" + "감사합니다." + "</h3>";

            mailComponents.sendMail(email, subject, text);

            emailVerificationMap.put(request.getEmail(), uuid);

            return CompletableFuture.completedFuture(null);
//        return RegisterDto.Response.builder()
//                .name(request.getName())
//                .email(request.getEmail())
//                .registerDatetime(LocalDateTime.now())
//                .build();
        } catch (CustomException e) {
            // 예외를 로그로 기록하거나 처리
            return CompletableFuture.failedFuture(e);
        }
    }

    public String getVerificationEmail(String email){
        return emailVerificationMap.getOrDefault(email, "NOT_EXISTS");
    }

    @Transactional
    public boolean checkVerificationUUID(String email, String inputUUID){
        String storedUUID = getVerificationEmail(email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMAIL));

        user.setCertification(true);

        userRepository.save(user);

        return Objects.equals(storedUUID, inputUUID);
    }
}
