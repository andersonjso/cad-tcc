package br.edu.ufal;

import br.edu.ufal.cad.mongodb.tags.Exam;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by andersonjso on 2/23/16.
 */
public class ExamQueryResult {

    public final List<Exam> exams;
    public final long totalPages;

    @JsonCreator
    public ExamQueryResult(@JsonProperty("exams") List<Exam> exams,
                           @JsonProperty("totalPages") long totalPages) {
        this.exams = exams;
        this.totalPages = totalPages;
    }
}
