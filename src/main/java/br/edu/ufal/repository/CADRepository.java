package br.edu.ufal.repository;



import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.cad.cbir.isa.NoduleRetrievalPrecisionEvaluation;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import br.edu.ufal.util.MongoUtils;
import com.mongodb.Mongo;
import org.jongo.MongoCursor;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by andersonjso on 2/8/16.
 */
public class CADRepository {

    private final int QUANTITY = 10;

    public List<SimilarNodule> retrieveSimilarNodules(String path) throws UnknownHostException {
        NoduleRetrievalPrecisionEvaluation noduleRetrievalPrecisionEvaluation =
                new NoduleRetrievalPrecisionEvaluation();

        return null;
//        return noduleRetrievalPrecisionEvaluation.retrieveSimilarNodules(path);
    }

    public ExamQueryResult listExams(int page) {
        MongoCursor<Exam> examsCursor = MongoUtils.exams().find()
                .skip(QUANTITY * (page -1))
                .limit(QUANTITY).as(Exam.class);

        List<Exam> exams = StreamSupport.stream(examsCursor.spliterator(), false)
                .collect(Collectors.toList());

        long totalExams = MongoUtils.exams().count();
        long totalPages = (long) (Math.ceil(totalExams / (double) QUANTITY));

        return new ExamQueryResult(exams, totalPages);
    }

    /*
    List<Device> devices = StreamSupport.stream(MeyerCollections.devices().find()
                .skip(QUANTITY * (page - 1))
                .limit(QUANTITY)
                .sort("{lastModification: -1}").as(Device.class).spliterator(), false)
                .collect(toList());

        long totalDevices = MeyerCollections.devices().count();
        long totalPages = (long) (Math.ceil(totalDevices / (double) QUANTITY));

        return new DeviceQueryResult(devices, totalPages);
     */
}
