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
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class QuizService implements IQuizService{

    private static final String API_URL = "https://jservice.io/api/random";
    Integer maxQuestionsListSize =1;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public QuizService(QuestionRepository questionRepository, RestTemplate restTemplate) {
        this.questionRepository = questionRepository;
        this.restTemplate = restTemplate;
    }
    @Override
    public String fetchAndStoreIntoDB(){

        log.debug("fetching questions starts");

        try {
            for (int i = 0; i < maxQuestionsListSize; i++) {
                ResponseEntity<Question[]> questions = restTemplate.getForEntity(API_URL, Question[].class);
                if(questions.getBody() == null){
                    throw new QuestionNotFoundException(ErrorCode.NO_CONTENT, Constants.EMPTY_QUESTION_LIST);
                }
                Question[] questionsEntities = questions.getBody();
                List<Question> questionsList = Arrays.asList(questionsEntities);
                questionRepository.saveAll(questionsList);
            }
            log.debug("fetching questions ends");

            return "Data fetched and stored in the database";
        }catch (Exception ex){
            if(Constants.EMPTY_QUESTION_LIST.equals(ex.getMessage())) {
                throw new QuestionNotFoundException(ErrorCode.NO_CONTENT, Constants.EMPTY_QUESTION_LIST);
            }
            throw new QuestionNotFoundException(ErrorCode.INTERNAL_SERVER_ERROR, Constants.FETCHING_QUESTIONS_FAILED);
        }
    }

    public QuestionResponseDto getRandomQuestion() {

        log.debug("fetching random question starts");

        try {
            Question[] questions = restTemplate.getForObject(API_URL, Question[].class);
            if (questions == null || questions.length == 0) {
                throw new QuestionNotFoundException(ErrorCode.NO_CONTENT, Constants.EMPTY_QUESTION_LIST);
            }

            log.info("Fetched questions: {}", Arrays.toString(questions));
            QuestionResponseDto response = new QuestionResponseDto();

            for (Question question : questions) {
                response.setId(question.getId());
                response.setQuestion(question.getQuestion());
            }
            log.debug("fetching random question ends");
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

        log.debug("fetching random next question and correct answer starts");

        try {

            Question question = questionRepository.findById(request.getId())
                    .orElseThrow(() -> new QuestionNotFoundException(ErrorCode.BAD_REQUEST, Constants.QUESTION_ID_NOT_FOUND));

            NextQuestionDto nextQuestion = new NextQuestionDto();

            QuestionResponseDto response = getRandomQuestion();
            if(response == null){
                throw new QuestionNotFoundException(ErrorCode.NOT_FOUND, Constants.QUESTION_NOT_FOUND);
            }

            nextQuestion.setCorrectAnswer(question.getAnswer());
            nextQuestion.setNextQuestion(response);

            log.debug("fetching random next question and correct answer ends");

            return nextQuestion;
        }catch (Exception exception){
            if(Constants.QUESTION_ID_NOT_FOUND.equals(exception.getMessage())){
                throw new QuestionNotFoundException(ErrorCode.BAD_REQUEST, Constants.QUESTION_ID_NOT_FOUND);
            }else if(Constants.QUESTION_NOT_FOUND.equals(exception.getMessage())){
                throw new QuestionNotFoundException(ErrorCode.NOT_FOUND, Constants.QUESTION_NOT_FOUND);
            }
            throw new QuestionNotFoundException(ErrorCode.INTERNAL_SERVER_ERROR, Constants.CREATING_RESPONSE_DETAILS_FAILED);
        }
    }
}
