package com.example.hyuntrace.advice;

import com.example.hyuntrace.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()) // 필드명과 에러 메시지만 저장
        );
        return errors;
    }

    @ExceptionHandler(MemberService.MidExistException.class)
    public ResponseEntity<Map<String, String>> handleMidExistException(MemberService.MidExistException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("오류 :",ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(MemberService.MpwNotMatchException.class)
    public ResponseEntity<Map<String,String>> handleMpwNotMatchException(MemberService.MpwNotMatchException e){
        Map<String,String> result = new HashMap<>();
        result.put("오류",e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception) {
//
//        log.info("handleAccessDeniedException..................");
//
//        Map<String, Object> errors = new HashMap<>();
//        errors.put("message", exception.getMessage());
//
//        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
//    }
}

