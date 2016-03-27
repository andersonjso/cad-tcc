package br.edu.ufal.repository;



import br.edu.ufal.AttributesNodule;
import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.NoduleQueryResult;
import br.edu.ufal.cad.cbir.isa.NoduleRetrievalPrecisionEvaluation;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import br.edu.ufal.cad.mongodb.tags.Roi;
import br.edu.ufal.util.ImageEncoded;
import br.edu.ufal.util.MongoUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.jongo.MongoCursor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by andersonjso on 2/8/16.
 */
public class CADRepository {

    private final int QUANTITY = 10;
    private GridFS gfsPhoto = new GridFS(MongoUtils.databaseTest, "images");
    private GridFS gfsMyPhoto = new GridFS(MongoUtils.databaseMyNodules, "images");

    public List<SimilarNodule> retrieveSimilarNodules(String examPath, String noduleId) throws UnknownHostException {
        Exam exam = MongoUtils.exams().findOne("{path: {$regex: #}}", examPath + ".*").as(Exam.class);

        Optional<BigNodule> nodule = exam.getReadingSession().getBignodule().stream()
                .filter(bigNodule -> bigNodule.getNoduleID().equals(noduleId)).findFirst();

        double[] textureAttributes = nodule.get().getTextureAttributes();

        NoduleRetrievalPrecisionEvaluation NRPEval =
                new NoduleRetrievalPrecisionEvaluation();

        return NRPEval.retrieveSimilarNodules(textureAttributes);
    }

    public ExamQueryResult listExams(int page) {
        List<Exam> exams = StreamSupport.stream(MongoUtils.exams()
                .find("{readingSession.bignodule.0: {$exists: true}}")
                .skip(QUANTITY * (page -1))
                .limit(QUANTITY).as(Exam.class).spliterator(), false).collect(Collectors.toList());

        long totalExams = MongoUtils.exams()
                .count("{readingSession.bignodule.0: {$exists: true}}");

        long totalPages = (long) (Math.ceil(totalExams / (double) QUANTITY));

        return new ExamQueryResult(exams, totalPages);
    }

    public List<BigNodule> retrieveBigNodulesFromExam(String examPath) {
        Exam exam = retrieveExamByPath(examPath);

        return exam.getReadingSession().getBignodule();
    }

    public Exam retrieveExamByPath(String examPath) {
        return MongoUtils.exams().findOne("{path: {$regex: #}}", examPath + ".*").as(Exam.class);
    }

    public BufferedImage retrieveExamImageByPath(String examPath, String noduleReference) throws IOException {
        List<BigNodule> bigNodules = retrieveExamByPath(examPath)
                .getReadingSession()
                .getBignodule();

        ObjectId originalImageID = null;

        for(BigNodule bigNodule : bigNodules){
            if(bigNodule.getNoduleID().equals(noduleReference)){
                originalImageID = bigNodule.getRois().get(0).getOriginalImage();
                break;
            }
        }

        GridFSDBFile imageForOutput = gfsPhoto.findOne(originalImageID);
        InputStream imageIS = imageForOutput.getInputStream();

        BufferedImage bufferedImage = ImageIO.read(imageIS);

        return bufferedImage;
    }

    public void retrieveExamImageSlicesByPath(String examPath, String noduleReference) throws IOException {
        List<BigNodule> bigNodules = retrieveExamByPath(examPath)
                .getReadingSession()
                .getBignodule();

        ObjectId originalImageID = null;

        for(BigNodule bigNodule : bigNodules){
            if(bigNodule.getNoduleID().equals(noduleReference)){
                int i = 0;
                for (Roi roi : bigNodule.getRois()){
                    GridFSDBFile imageForOutput = gfsPhoto.findOne(roi.getOriginalImage());
                    imageForOutput.writeTo("/Users/andersonjso/Downloads/allimagesExam/exam" +
                            bigNodule.getNoduleID().toString() + " " + i + ".dcm");
                    i++;
                }
                break;
            }
        }




    }
    /*
    imageForOutput.writeTo("/Users/andersonjso/Downloads/allimagesExam/exam" +
                        bigNodule.getNoduleID().toString() + " " + i + ".dcm");
     */

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

    public BufferedImage retrieveBigNoduleImage(String examPath, String noduleId) throws IOException {
        List<BigNodule> bigNodules = retrieveExamByPath(examPath).getReadingSession().getBignodule();
        ObjectId noduleImageId = null;
        for(BigNodule bigNodule : bigNodules){
            if(bigNodule.getNoduleID().equals(noduleId)){
                noduleImageId = bigNodule.getRois().get(0).getNoduleImage();
                break;
            }
        }

        GridFSDBFile imageForOutput = gfsPhoto.findOne(noduleImageId);
        InputStream imageIS = imageForOutput.getInputStream();
        BufferedImage noduleBI = ImageIO.read(imageIS);

        return noduleBI;
    }

    public ExamQueryResult retrieveExamsByPath(String examPath, int page) {
        List<Exam> exams = StreamSupport.stream(MongoUtils.exams()
                .find("{readingSession.bignodule.0: {$exists: true}, path: {$regex: #}}", examPath+".*")
                .skip(QUANTITY * (page -1))
                .limit(QUANTITY).as(Exam.class).spliterator(), false).collect(Collectors.toList());

        long totalExams = MongoUtils.exams()
                .count("{readingSession.bignodule.0: {$exists: true}, path: {$regex: #}}", examPath+".*");

        long totalPages = (long) (Math.ceil(totalExams / (double) QUANTITY));

        return new ExamQueryResult(exams, totalPages);
    }

    public BufferedImage retrieveExamSlices(String examPath, String noduleId, String roiNumber) throws IOException {
        Exam exam = retrieveExamByPath(examPath);
        ObjectId originalImageID = null;

        for (BigNodule bigNodule : exam.getReadingSession().getBignodule()){
            if (bigNodule.getNoduleID().equals(noduleId)){
                originalImageID = bigNodule.getRois().get(Integer.parseInt(roiNumber)).getOriginalImage();
                break;
            }
        }

        GridFSDBFile imageForOutput = gfsPhoto.findOne(originalImageID);
        InputStream imageIS = imageForOutput.getInputStream();

        BufferedImage bufferedImage = ImageIO.read(imageIS);

        return bufferedImage;
    }

    public void retrieveExamSlicesLocal(String examPath, String noduleId, String roiNumber) throws IOException {
        Exam exam = retrieveExamByPath(examPath);
        ObjectId originalImageID = null;

        for (BigNodule bigNodule : exam.getReadingSession().getBignodule()){
            if (bigNodule.getNoduleID().equals(noduleId)){
                originalImageID = bigNodule.getRois().get(Integer.parseInt(roiNumber)).getOriginalImage();
                GridFSDBFile imageForOutput = gfsPhoto.findOne(originalImageID);
                imageForOutput.writeTo("/Users/andersonjso/Downloads/allimagesExam/examSlice" +
                        bigNodule.getNoduleID().toString() + " " + roiNumber + ".dcm");
                break;
            }
        }


    }

    public BufferedImage retrieveNodulesSlices(String examPath, String noduleId, String roiNumber) throws IOException {
        Exam exam = retrieveExamByPath(examPath);
        ObjectId originalImageID = null;

        for (BigNodule bigNodule : exam.getReadingSession().getBignodule()){
            if (bigNodule.getNoduleID().equals(noduleId)){
                originalImageID = bigNodule.getRois().get(Integer.parseInt(roiNumber)).getNoduleImage();
                break;
            }
        }

        GridFSDBFile imageForOutput = gfsPhoto.findOne(originalImageID);
        InputStream imageIS = imageForOutput.getInputStream();

        BufferedImage bufferedImage = ImageIO.read(imageIS);

        return bufferedImage;
    }

    public BigNodule retrieveBigNodule(String examPath, String noduleId) {
        Exam exam = retrieveExamByPath(examPath);

        for (BigNodule bigNodule : exam.getReadingSession().getBignodule()) {
            if (bigNodule.getNoduleID().equals(noduleId)){
                return bigNodule;
            }
        }

        return null;
    }

    public List<SimilarNodule> retrieveSimilarNodulesFrom3DNodule(List<ImageEncoded> encodedImages) throws IOException {
        BufferedImage[] image3D = new BufferedImage[encodedImages.size()];

        for (int i=0; i<encodedImages.size(); i++) {
            byte[] decodedByteImage = java.util.Base64.getDecoder().decode(encodedImages.get(i).imageEncoded);

            ByteArrayInputStream bais = new ByteArrayInputStream(decodedByteImage);
            image3D[i] = ImageIO.read(bais);
        }

        AttributesNodule attributesNodule = new AttributesNodule();
        double[] attributes = attributesNodule.getImageTextureAttributes(image3D);

        NoduleRetrievalPrecisionEvaluation noduleRetrievalPerformanceEvaluation =
                new NoduleRetrievalPrecisionEvaluation ();

        List<SimilarNodule> nodules = noduleRetrievalPerformanceEvaluation.retrieveSimilarNodules(attributes);

        return nodules;
    }

    public class ExamQuantityNodule implements Comparable<ExamQuantityNodule>{
        public final Exam exam;
        public final int quantity;

        public ExamQuantityNodule(Exam exam, int quantity) {
            this.exam = exam;
            this.quantity = quantity;
        }

        @Override
        public int compareTo(ExamQuantityNodule o) {
            return quantity < o.quantity ? 1
                    : quantity > o.quantity ? -1
                    : 0;
        }
    }

    public ExamQueryResult listExamsByDegree(int noduleDegree, int page) {
        MongoCursor<Exam> examCursor = MongoUtils.exams()
                .find("{readingSession.bignodule.malignancy: #}", Integer.toString(noduleDegree)).as(Exam.class);

        List<Exam> allExams = StreamSupport.stream(examCursor.spliterator(), false).collect(Collectors.toList());

        List<Exam> examsToSave = new ArrayList<>();
        List<ExamQuantityNodule> examQuantityNodules = new ArrayList<>();

        for (Exam exam : allExams) {
            int count = 0;
            for (BigNodule bigNodule : exam.getReadingSession().getBignodule()) {
                if (bigNodule.getMalignancy() == noduleDegree){
                    count++;
                }
            }
            if (count > 10){
                System.out.println();
            }
            ExamQuantityNodule examQuantityNodule = new ExamQuantityNodule(exam, count);
            examQuantityNodules.add(examQuantityNodule);
        }

        Collections.sort(examQuantityNodules);


        int endIndex = (QUANTITY * (page -1)) + QUANTITY;

        if (endIndex > allExams.size()-1){
            endIndex = allExams.size()-1;
        }
        for (int i=QUANTITY * (page -1); i<endIndex; i++){
            examsToSave.add(examQuantityNodules.get(i).exam);
        }

        /*
          .skip(QUANTITY * (page -1))
                .limit(QUANTITY).as(Exam.class).spliterator(), false).collect(Collectors.toList());
         */

        long totalPages = (long) (Math.ceil(allExams.size() / (double) QUANTITY));

        return new ExamQueryResult(examsToSave, totalPages);
    }

    public BigNodule createBigNodule(BigNodule bigNodule, List<ImageEncoded> encodedImages) throws IOException {

        GridFS gfsPhoto = new GridFS(MongoUtils.databaseMyNodules, "images");
        int i = 0;
        for (ImageEncoded encodedImage : encodedImages) {
            byte[] decodedByteImage = java.util.Base64.getDecoder().decode(encodedImage.imageEncoded);

            GridFSInputFile gfsFile = gfsPhoto.createFile(decodedByteImage);
            gfsFile.setFilename(bigNodule.getNoduleID() + i);
            gfsFile.save();
            i++;
        }

        i = 0;
        List<Roi> rois = new ArrayList<>();
        for (ImageEncoded encoded : encodedImages){
            GridFSDBFile imageForOutput = gfsPhoto.findOne(bigNodule.getNoduleID() + i);

            rois.add(new Roi(imageForOutput.getId().toString()));
            i++;
        }

        bigNodule.setRois(rois);

        MongoUtils.nodules().save(bigNodule);

        return byId(bigNodule.getNoduleID());
    }

    public BigNodule byId(String id){
        BigNodule big = MongoUtils.nodules().findOne("{noduleId: #}", id).as(BigNodule.class);

        return big;
    }

    public double[] retrieveTextureAttributes(List<ImageEncoded> encodedImages) throws IOException {
        BufferedImage[] image3D = new BufferedImage[encodedImages.size()];

        for (int i=0; i<encodedImages.size(); i++) {
            byte[] decodedByteImage = java.util.Base64.getDecoder().decode(encodedImages.get(i).imageEncoded);

            ByteArrayInputStream bais = new ByteArrayInputStream(decodedByteImage);
            image3D[i] = ImageIO.read(bais);
        }

        AttributesNodule attributesNodule = new AttributesNodule();
        return attributesNodule.getImageTextureAttributes(image3D);
    }

    public NoduleQueryResult listNodules(int page) {
        List<BigNodule> bigNodules = StreamSupport.stream(MongoUtils.nodules().find()
                .skip(QUANTITY * (page -1))
                .limit(QUANTITY).as(BigNodule.class).spliterator(), false).collect(Collectors.toList());

        long totalNodules = MongoUtils.nodules().count();

        long totalPages = (long) (Math.ceil(totalNodules / (double) QUANTITY));

        return new NoduleQueryResult(bigNodules, totalPages);
    }

    public BigNodule retrieveNoduleById(String noduleId) {
        BigNodule bigNodule = MongoUtils.nodules().findOne("{noduleId: #}", noduleId).as(BigNodule.class);

        return bigNodule;
    }

    public BigNodule editNodule(String noduleId, BigNodule mergedNodule) {
        MongoUtils.nodules().update("{noduleId: #}", noduleId)
                .with("{$set: {subtlety: #, " +
                        "internalStructure: #," +
                        "calficication: #," +
                        "sphericity: #," +
                        "margin: #," +
                        "lobulation: #," +
                        "spiculation: #," +
                        "texture: #," +
                        "malignancy: #}}",
                        mergedNodule.getSubtlety(),
                        mergedNodule.getInternalStructure(),
                        mergedNodule.getCalcification(),
                        mergedNodule.getSphericity(),
                        mergedNodule.getMargin(),
                        mergedNodule.getLobulation(),
                        mergedNodule.getSpiculation(),
                        mergedNodule.getTexture(),
                        mergedNodule.getMalignancy());

        return byId(noduleId);
    }

    public void removeNodule(String noduleId) {
        MongoUtils.nodules().remove("{noduleId: #}", noduleId);
    }

    public List<SimilarNodule> retrieveMySimilarNodules(String noduleId) throws UnknownHostException {
        BigNodule bigNodule = MongoUtils.nodules().findOne("{noduleId: #}", noduleId).as(BigNodule.class);

        double[] textureAtts = bigNodule.getTextureAttributes();

        NoduleRetrievalPrecisionEvaluation NRPEval = new NoduleRetrievalPrecisionEvaluation();

        return NRPEval.retrieveSimilarNodules(textureAtts);
    }

    public BufferedImage retrieveMyNodulesSlices(String noduleId, String roiNumber) throws IOException {
        BigNodule bigNodule = byId(noduleId);

        ObjectId noduleImageID = bigNodule.getRois().get(Integer.parseInt(roiNumber)).getNoduleImage();

        GridFSDBFile imageForOutput = gfsMyPhoto.findOne(noduleImageID);
        InputStream imageIS = imageForOutput.getInputStream();

        BufferedImage bufferedImage = ImageIO.read(imageIS);

        return bufferedImage;

    }

    public NoduleQueryResult retrieveNoduleByName(String noduleId, int page) {
        MongoCursor<BigNodule> bigNoduleCursor = MongoUtils.nodules()
                .find("{noduleId: {$regex: #}}", noduleId + ".*").as(BigNodule.class);

        List<BigNodule> bigNodules = StreamSupport.stream(bigNoduleCursor.spliterator(), false)
                .skip(QUANTITY * (page - 1))
                .limit(QUANTITY)
                .collect(Collectors.toList());

        long totalNodules = MongoUtils.nodules().count("{noduleId: {$regex: #}}", noduleId + ".*");

        long totalPages = (long) (Math.ceil(totalNodules / (double) QUANTITY));

        return new NoduleQueryResult(bigNodules, totalPages);
    }
}
