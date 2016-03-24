package br.edu.ufal.cad;

import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Characteristics;
import br.edu.ufal.util.ImageEncoded;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by andersonjso on 3/23/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoduleDTO {

    private BigNodule bigNodule;
    private List<ImageEncoded> encodedList;

    @JsonCreator
    public NoduleDTO(@JsonProperty("bigNodule") BigNodule bigNodule,
                     @JsonProperty("encodedList") List<ImageEncoded> encodedList) {
        this.bigNodule = bigNodule;
        this.encodedList = encodedList;
    }

    public BigNodule getBigNodule() {
        return bigNodule;
    }

    public List<ImageEncoded> getEncodedList() {
        return encodedList;
    }
}
