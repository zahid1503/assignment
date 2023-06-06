package com.samta.assignment.services;


import com.samta.assignment.request.QuestionRequestDto;
import com.samta.assignment.response.NextQuestionDto;
import com.samta.assignment.response.QuestionResponseDto;

public interface IQuizService {

    String  fetchAndStoreQuestionsIntoDB();

    QuestionResponseDto getRandomQuestion();

    NextQuestionDto getCorrectAnswer(QuestionRequestDto request);
}
