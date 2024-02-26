package com.threlease.base.functions.auth;

import com.threlease.base.functions.maps.Data;
import com.threlease.base.utils.Hash;
import com.threlease.base.utils.Session;
import com.threlease.base.utils.responses.BasicResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final Data data;
    private final String ip = "127.0.0.1";

    @Autowired
    public AuthController(Data data) {
        this.data = data;
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestParam("id") String id
    ) {
        Optional<Session> data_value = data.get(id);

        if (data_value.isEmpty() ||
                data_value.get().created_At.getTime() + (1000 * 60 * 60 * 24 * 24) < new Date(System.currentTimeMillis()).getTime()
        ) {
            BasicResponse response = BasicResponse.builder()
                    .success(false)
                    .message(Optional.of("올바르지 않은 세션 입니다."))
                    .data(Optional.empty())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        BasicResponse response = BasicResponse.builder()
                .success(true)
                .message(Optional.empty())
                .data(Optional.of(data_value))
                .build();

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
        HttpServletRequest request,
        @RequestAttribute String name,
        @RequestAttribute String student_id
    ) {
        String ipAddress = request.getRemoteAddr();

        if (!Objects.equals(ipAddress, ip)) {
            BasicResponse response = BasicResponse.builder()
                    .success(false)
                    .message(Optional.of("해당 서비스 (세션)을 발급하실려면 경소고 코딩관 내에서 사용하셔야 합니다."))
                    .data(Optional.empty())
                    .build();

            return ResponseEntity.status(401).body(response);
        }

        Session session = Session.builder()
                .name(name)
                .student_id(student_id)
                .created_At(new Date(System.currentTimeMillis()))
                .build();

        Hash hash = new Hash();

        String session_hash = hash.generateSHA512(Arrays.toString(session.toString().getBytes()));

        boolean success = data.put(session_hash, session);

        BasicResponse response = BasicResponse.builder()
                .success(success)
                .message(success ? Optional.empty() : Optional.of("예상치 못한 에러가 발생 했습니다. 관리자에게 문의해주세요."))
                .data(success ? Optional.of(session_hash) : Optional.empty())
                .build();

        return ResponseEntity.status(success ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(response);
    }
}
