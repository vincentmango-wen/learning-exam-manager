package com.example.learning_exam_manager.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    // 一般的な例外の処理
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model) {
        logger.error("予期しないエラーが発生しました", e);
        model.addAttribute("error", "システムエラーが発生しました。しばらく時間をおいて再度お試しください。");
        model.addAttribute("title", "エラー");
        return "error/error";
    }
    
    // リソースが見つからない場合の処理
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException e, Model model) {
        logger.warn("リソースが見つかりません: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        model.addAttribute("title", "404 - ページが見つかりません");
        return "error/404";
    }
    
    // バリデーションエラーの処理
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        logger.warn("不正なリクエスト: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        model.addAttribute("title", "400 - 不正なリクエスト");
        return "error/400";
    }
}