package br.edu.ufal.repository;

import cbir.isa.NoduleRetrievalPrecisionEvaluation;
import cbir.isa.SimilarNodule;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by andersonjso on 2/8/16.
 */
public class SimilarNoduleRepository {


    public List<SimilarNodule> retrieveSimilarNodules(String path) throws UnknownHostException {
        NoduleRetrievalPrecisionEvaluation noduleRetrievalPrecisionEvaluation =
                new NoduleRetrievalPrecisionEvaluation();

        return noduleRetrievalPrecisionEvaluation.retrieveSimilarNodules(path);
    }
}
