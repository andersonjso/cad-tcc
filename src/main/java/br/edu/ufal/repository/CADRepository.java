package br.edu.ufal.repository;



import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.cad.cbir.isa.NoduleRetrievalPrecisionEvaluation;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import br.edu.ufal.util.MongoUtils;
import org.jongo.MongoCursor;

import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by andersonjso on 2/8/16.
 */
public class CADRepository {

    private final int QUANTITY = 10;

    public List<SimilarNodule> retrieveSimilarNodules(String examPath, String noduleId) throws UnknownHostException {
        Exam exam = MongoUtils.exams().findOne("{path: {$regex: #}}", examPath + ".*").as(Exam.class);

        Optional<BigNodule> nodule = exam.getReadingSession().getBignodule().stream()
                .filter(bigNodule -> bigNodule.getNoduleID().equals(noduleId)).findFirst();

        double[] textureAttributes = nodule.get().getTextureAttributes();

        NoduleRetrievalPrecisionEvaluation NRPEval =
                new NoduleRetrievalPrecisionEvaluation();

        return NRPEval.retrieveSimilarNodules(textureAttributes);
    }

    public List<Exam> listExams() {
        MongoCursor<Exam> examsCursor = MongoUtils.exams().find().as(Exam.class);

        List<Exam> exams = StreamSupport.stream(examsCursor.spliterator(), false)
                .filter(exam -> exam.getReadingSession().getBignodule().size() > 0)
                .collect(Collectors.toList());

//        long totalExams = MongoUtils.exams().count();
//        long totalPages = (long) (Math.ceil(totalExams / (double) QUANTITY));

        return exams;
    }

    public List<BigNodule> retrieveBigNodulesFromExam(String examPath) {
        Exam exam = retrieveExamByPath(examPath);

        return exam.getReadingSession().getBignodule();
    }

    public Exam retrieveExamByPath(String examPath) {
        return MongoUtils.exams().findOne("{path: {$regex: #}}", examPath + ".*").as(Exam.class);
    }


    public BufferedImage[] retrieveExamImageByPath(String examPath) {
        return new BufferedImage[0];
    }
}
