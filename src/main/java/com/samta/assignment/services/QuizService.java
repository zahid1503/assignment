package com.samta.assignment.services;

import com.samta.assignment.entities.Question;
import com.samta.assignment.exceptions.ErrorCode;
import com.samta.assignment.exceptions.QuestionNotFoundException;
import com.samta.assignment.repository.QuestionRepository;
import com.samta.assignment.request.QuestionRequestDto;
import com.samta.assignment.response.NextQuestionDto;
import com.samta.assignment.response.QuestionResponseDto;
import com.samta.assignment.utilities.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class QuizService implements IQuizService{

    private static final String API_URL = "https://jservice.io/api/random";
    Integer questionListSize =1;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public QuizService(QuestionRepository questionRepository, RestTemplate restTemplate) {
        this.questionRepository = questionRepository;
        this.restTemplate = restTemplate;
    }
    @Override
    public String fetchAndStoreQuestionsIntoDB(){

        log.debug("fetching questions starts");

        try {
            for (int i = 0; i < questionListSize; i++) {

                Question[] questions = restTemplate.getForObject(API_URL, Question[].class);
                if(questions.equals(null)){
                    throw new QuestionNotFoundException(ErrorCode.NO_CONTENT, Constants.EMPTY_QUESTION_LIST);
                }

                List<Question> questionList = Arrays.asList(questions);
                questionRepository.saveAll(questionList);
            }
            return "Data fetched and stored in the database";
        }catch (Exception ex){
            if(Constants.EMPTY_QUESTION_LIST.equals(ex.getMessage())) {
                throw new QuestionNotFoundException(ErrorCode.NO_CONTENT, Constants.EMPTY_QUESTION_LIST);
            }
            throw new QuestionNotFoundException(ErrorCode.INTERNAL_SERVER_ERROR, Constants.FETCHING_QUESTIONS_FAILED);
        }
    }

    public QuestionResponseDto getRandomQuestion() {

        try {
            Question[] questions = restTemplate.getForObject(API_URL, Question[].class);
            if(questions.equals(null)) {
                throw new QuestionNotFoundException(ErrorCode.NO_CONTENT, Constants.EMPTY_QUESTION_LIST);
            }
                log.info("fetching questions starts {}", questions);
            QuestionResponseDto response = new QuestionResponseDto();

            for (Question question : questions) {
                response.setId(question.getId());
                response.setQuestion(question.getQuestion());
            }
            return response;
        }catch (Exception exception){
            if(Constants.EMPTY_QUESTION_LIST.equals(exception.getMessage())) {
                throw new QuestionNotFoundException(ErrorCode.NO_CONTENT, Constants.EMPTY_QUESTION_LIST);
            }
            throw new QuestionNotFoundException(ErrorCode.INTERNAL_SERVER_ERROR, Constants.FETCHING_QUESTIONS_FAILED);
        }
    }

    @Override
    public NextQuestionDto getCorrectAnswer(QuestionRequestDto request) {

        try {
            Question question = questionRepository.findById(request.getId())
                    .orElseThrow(() -> new QuestionNotFoundException(ErrorCode.BAD_REQUEST, Constants.QUESTION_ID_NOT_FOUND));

            NextQuestionDto nextQuestion = new NextQuestionDto();

            QuestionResponseDto response = getRandomQuestion();
            if(response.equals(null)){
                throw new QuestionNotFoundException(ErrorCode.BAD_REQUEST, Constants.QUESTION_NOT_FOUND);
            }

            nextQuestion.setCorrectAnswer(question.getAnswer());
            nextQuestion.setNextQuestion(response);

            return nextQuestion;
        }catch (Exception exception){
            if(Constants.QUESTION_ID_NOT_FOUND.equals(exception.getMessage())){
                throw new QuestionNotFoundException(ErrorCode.BAD_REQUEST, Constants.QUESTION_ID_NOT_FOUND);
            }else if(Constants.QUESTION_NOT_FOUND.equals(exception.getMessage())){
                throw new QuestionNotFoundException(ErrorCode.BAD_REQUEST, Constants.QUESTION_NOT_FOUND);
            }
            throw new QuestionNotFoundException(ErrorCode.INTERNAL_SERVER_ERROR, Constants.CREATING_RESPONSE_DETAILS_FAILED);
        }
    }
}
