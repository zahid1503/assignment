package com.samta.assignment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NextQuestionDto {

   @JsonProperty("correct_answer")
   private String correctAnswer;

   @JsonProperty("next_question")
   private QuestionResponseDto nextQuestion;

}
