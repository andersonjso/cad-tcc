package br.edu.ufal.repository;

import org.junit.Test;

import java.net.UnknownHostException;

/**
 * Created by andersonjso on 2/16/16.
 */
public class SimilarNoduleRepositoryTest {
    SimilarNoduleRepository similarNoduleService = new SimilarNoduleRepository();
    @Test
    public void shouldReturnSimilarNodules() throws UnknownHostException {
        similarNoduleService.retrieveSimilarNodules("123");
    }
}
