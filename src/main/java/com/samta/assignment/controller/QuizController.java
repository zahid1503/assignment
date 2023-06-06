package com.samta.assignment.controller;

import com.samta.assignment.request.QuestionRequestDto;
import com.samta.assignment.response.NextQuestionDto;
import com.samta.assignment.response.QuestionResponseDto;
import com.samta.assignment.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/getQuestions")
    public ResponseEntity<String> fetchAndStoreQuestions() {
        String response =  quizService.fetchAndStoreQuestionsIntoDB();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/play")
    public ResponseEntity<QuestionResponseDto> getQuestion() {
        QuestionResponseDto response = quizService.getRandomQuestion();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/next")
    public ResponseEntity<NextQuestionDto> getNextQuestion(@RequestBody QuestionRequestDto request) {
        NextQuestionDto response = quizService.getCorrectAnswer(request);
        return ResponseEntity.ok().body(response);
    }

}
