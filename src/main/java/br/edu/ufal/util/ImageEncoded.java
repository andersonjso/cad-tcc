package br.edu.ufal.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by andersonjso on 3/16/16.
 */
public class ImageEncoded {

    public final String imageEncoded;

    @JsonCreator
    public ImageEncoded(@JsonProperty("imageEncoded") String imageEncoded) {
        this.imageEncoded = imageEncoded;
    }
}
