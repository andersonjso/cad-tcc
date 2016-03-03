package br.edu.ufal.repository;



import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.cad.cbir.isa.NoduleRetrievalPrecisionEvaluation;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import br.edu.ufal.util.MongoUtils;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;
import org.dcm4che2.tool.dcm2jpg.Dcm2Jpg;
import org.jongo.MongoCursor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    private GridFS gfsPhoto = new GridFS(MongoUtils.databaseTest, "images");

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

    public BufferedImage retrieveExamImageByPath(String examPath) throws IOException {
//        String filename = MongoUtils.imagesFiles().findOne("{filename: {$regex: #}}", examPath + ".*")
//                .map(dbObject -> (String) dbObject.get("filename"));
//
//
//        GridFSDBFile imageForOutput = gfsPhoto.findOne(filename);
//        InputStream imageIS = imageForOutput.getInputStream();
//
//        BufferedImage bufferedImage = ImageIO.read(imageIS);

        ObjectId originalImageID = retrieveExamByPath(examPath)
                .getReadingSession()
                .getBignodule().get(0)
                .getRois().get(0)
                .getOriginalImage();

        GridFSDBFile imageForOutput = gfsPhoto.findOne(originalImageID);
        InputStream imageIS = imageForOutput.getInputStream();

        BufferedImage bufferedImage = ImageIO.read(imageIS);

        return bufferedImage;
    }

    public BufferedImage[] retrieveBigNodulesImagesFromExam(String examPath) throws IOException {
        List<BigNodule> bigNodules = retrieveBigNodulesFromExam(examPath);
        BufferedImage[] bigNodulesImages = new BufferedImage[bigNodules.size()];
        for(int i=0; i< bigNodules.size(); i++){
            GridFSDBFile imageForOutput = gfsPhoto.findOne(bigNodules.get(i)
                    .getRois().get(0).getNoduleImage());
            InputStream imageIS = imageForOutput.getInputStream();

            bigNodulesImages[i] = ImageIO.read(imageIS);
        }

        return bigNodulesImages;
    }
}
