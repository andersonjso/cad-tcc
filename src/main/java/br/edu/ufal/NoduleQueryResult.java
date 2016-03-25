package br.edu.ufal;

import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by andersonjso on 3/25/16.
 */
public class NoduleQueryResult {

    public final List<BigNodule> bigNodules;
    public final long totalPages;

    @JsonCreator
    public NoduleQueryResult(@JsonProperty("bigNodules") List<BigNodule> bigNodules,
                             @JsonProperty("totalPages") long totalPages) {
        this.bigNodules = bigNodules;
        this.totalPages = totalPages;
    }
}
