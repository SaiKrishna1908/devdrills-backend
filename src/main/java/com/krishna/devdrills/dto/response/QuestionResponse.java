package com.krishna.devdrills.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionResponse {
    public String situation;
    public String expectedAnswer;
    public String notes;
    public List<String> examples;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
}
