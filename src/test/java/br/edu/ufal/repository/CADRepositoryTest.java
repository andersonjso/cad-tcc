package br.edu.ufal.repository;

import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import br.edu.ufal.services.CADService;
import br.edu.ufal.util.ImageEncoded;
import br.edu.ufal.util.MongoUtils;
import com.mongodb.*;
import com.mongodb.assertions.Assertions;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static javax.imageio.ImageIO.read;
import static junit.framework.Assert.*;

/**
 * Created by andersonjso on 2/18/16.
 */
public class CADRepositoryTest {

    CADService cadService = new CADService();

    @Test
    public void shouldListExams(){
        ExamQueryResult exams = cadService.listExams(1);

        assertTrue(exams.exams.size() == 10);
    }

    @Test
    public void shouldRetrieveBigNodulesFromExam(){
        String path = "/LIDC-IDRI/LIDC-IDRI-0329/";

        List<BigNodule> bigNodules = cadService.retrieveBigNodulesFromExam(path);

        assertTrue(bigNodules.size() == 3);
    }

    @Test
    public void shouldRetrieveExamByPath(){
        String path = "0329";

        ExamQueryResult exam = cadService.retrieveExamsByPath(path, 1);

        System.out.println();

    }

    @Test
    public void shouldRetrieveExamsByPath() throws IOException {
        File img = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule0.png");
        File img2 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule1.png");
        File img3 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule2.png");
        File img4 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule3.png");
        File img5 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule4.png");
        File img6 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule5.png");
        File img7 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule6.png");
        File img8 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule7.png");

        List<File> files = new ArrayList<>();

        files.add(img); files.add(img2); files.add(img3); files.add(img4); files.add(img5); files.add(img6);
        files.add(img7); files.add(img8);

        List<ImageEncoded> encodedImages = new ArrayList<>();

        for (File file : files) {
            byte[] bytes = loadFile(file);
            byte[] encoded = Base64.getEncoder().encode(bytes);

            String encodedString = new String(encoded);

            ImageEncoded imageEncoded = new ImageEncoded(encodedString);

            encodedImages.add(imageEncoded);
        }

        List<SimilarNodule> similarNodules = cadService.retrieveSimilarNodulesFrom3DNodule(encodedImages);

        assertTrue(similarNodules.size() == 10);
    }

    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }

    @Test
    public void shouldRetrieveSimilarNodues() throws UnknownHostException {
        String examPath = "LIDC-IDRI-0323";
        String noduleId = "Nodule 003";
        List<SimilarNodule> similarNodules = cadService.retrieveSimilarNodules(examPath, noduleId);

        assertTrue(similarNodules.size() == 10);
    }

    @Test
    public void shouldRetrieveImagesBigNodule() throws IOException {
        ///LIDC-IDRI/LIDC-IDRI-0102/1.3.6.1.4.1.14519.5.2.1.6279.6001.144098736774350495825430776051/000000
        BufferedImage[] bufferedImages = cadService.retrieveBigNodulesImagesFromExam("LIDC-IDRI-0398");

        for (int i=0; i<bufferedImages.length; i++){
            ImageIO.write(bufferedImages[i], "png", new File("/Users/andersonjso/Downloads/another/jose" + i + ".png"));
        }

    }
    @Test
    public void examImageByPath() throws IOException {
        BufferedImage bufferedImage = cadService.retrieveExamImageByPath("LIDC-IDRI-0398", "0");

        ImageIO.write(bufferedImage, "png", new File("/Users/andersonjso/Downloads/another/ionapot.png"));
    }

    @Test
    public void retrieveetcetc() throws IOException {
        BufferedImage bufferedImage = cadService.retrieveBigNoduleImage("LIDC-IDRI-0398", "0");

        ImageIO.write(bufferedImage, "png", new File("/Users/andersonjso/Downloads/another/meninodaporteira.png"));
    }

    @Test
    public void retrieveExamSlicesByTest() throws IOException {
        Exam exam = cadService.retrieveExamByPath("LIDC-IDRI-0398");

        for (BigNodule bigNodule : exam.getReadingSession().getBignodule()){
            new CADRepository().retrieveExamImageSlicesByPath("LIDC-IDRI-0398", bigNodule.getNoduleID());
        }

    }

    @Test
    public void shouldRetrieveExamSlice () throws IOException {
        Exam exam = cadService.retrieveExamByPath("LIDC-IDRI-0398");
        for (BigNodule bigNodule : exam.getReadingSession().getBignodule()){
            for (int i=0; i<bigNodule.getRois().size();  i++){
                new CADRepository().retrieveExamSlicesLocal("LIDC-IDRI-0398", bigNodule.getNoduleID(), Integer.toString(i));
            }
        }


    }

    @Test
    public void shouldCreateBigNodule() throws IOException {
        File img = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule0.png");
        File img2 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule1.png");
        File img3 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule2.png");
        File img4 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule3.png");
        File img5 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule4.png");
        File img6 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule5.png");
        File img7 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule6.png");
        File img8 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule7.png");

        List<File> files = new ArrayList<>();

        files.add(img); files.add(img2); files.add(img3); files.add(img4); files.add(img5); files.add(img6);
        files.add(img7); files.add(img8);

        List<ImageEncoded> encodedImages = new ArrayList<>();

        for (File file : files) {
            byte[] bytes = loadFile(file);
            byte[] encoded = Base64.getEncoder().encode(bytes);

            String encodedString = new String(encoded);

            ImageEncoded imageEncoded = new ImageEncoded(encodedString);

            encodedImages.add(imageEncoded);
        }

        double[] doubles = {};
        BigNodule bigNodule = new BigNodule("Meu nodulo teste", new ArrayList(), 1,
                2, 3, 4, 5, 6, 7, 8, 9, doubles);

        cadService.createBigNodule(bigNodule, encodedImages);
    }

    @Test
    public void shouldRetrieveTexture() throws IOException {
        File img = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule0.png");
        File img2 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule1.png");
        File img3 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule2.png");
        File img4 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule3.png");
        File img5 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule4.png");
        File img6 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule5.png");
        File img7 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule6.png");
        File img8 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule7.png");

        List<File> files = new ArrayList<>();

        files.add(img); files.add(img2); files.add(img3); files.add(img4); files.add(img5); files.add(img6);
        files.add(img7); files.add(img8);

        List<ImageEncoded> encodedImages = new ArrayList<>();

        for (File file : files) {
            byte[] bytes = loadFile(file);
            byte[] encoded = Base64.getEncoder().encode(bytes);

            String encodedString = new String(encoded);

            ImageEncoded imageEncoded = new ImageEncoded(encodedString);

            encodedImages.add(imageEncoded);
        }


        double[] doubles = cadService.retrieveTextureAttributes(encodedImages);

        System.out.println();
    }
    @Test
    public void shouldRetrieveSimilarBy3DNodule(){

    }

    @Test
    public void shouldRetrieveSimilarNodules() throws Exception {
//        List<SimilarNodule> similarNodules = cadService.retrieveSimilarNodules("/LIDC-IDRI/LIDC-IDRI-0001");
//
//        System.out.println();

        DB db = new MongoClient().getDB("test");

        //    Jongo jongo = new Jongo(db);
        //    MongoCollection exams = jongo.getCollection("Images");

        //   File imageFile = new File("/Users/andersonjso/Downloads/11059745_950523074967955_7819764773030040282_o.jpg");
        //   GridFS gfsPhoto = new GridFS(db, "photo");
        //   GridFSInputFile gsfFile = gfsPhoto.createFile(imageFile);
        //   gsfFile.setFilename("Testando");
        //   gsfFile.save();
        //query.put("_id", new ObjectId(id));

        String id = "53453eaae4b05911cd6db135";
        BasicDBObject queryId = new BasicDBObject();
        queryId.put("_id", new ObjectId(id));

        MongoClient mongoClient = new MongoClient();
        DBCollection col = db.getCollection("exams");
        BasicDBObject exam = (BasicDBObject) col.findOne(queryId);
        BasicDBObject reading = (BasicDBObject) exam.get("readingSession");
        BasicDBList big = (BasicDBList) reading.get("bignodule");
        BasicDBObject nodule = (BasicDBObject) big.get(0);
        BasicDBList roi = (BasicDBList) nodule.get("roi");

        DBCollection colImages = db.getCollection("images.files");

        GridFS gfsPhoto = new GridFS(db, "images");
        for (int i = 0; i < roi.size(); i++){

            BasicDBObject actualRoi = (BasicDBObject) roi.get(i);
//            System.out.println("Original Image Id: " + actualRoi.get("originalImage"));
//            System.out.println("Nodule Image Id: " + actualRoi.get("noduleImage"));

            BasicDBObject queryOriginalImage = new BasicDBObject();
            queryOriginalImage.put("_id", new ObjectId(actualRoi.get("originalImage").toString()));

            BasicDBObject queryNoduleImage = new BasicDBObject();
            queryNoduleImage.put("_id", new ObjectId(actualRoi.get("noduleImage").toString()));

            GridFSDBFile originalImage = gfsPhoto.findOne(queryOriginalImage);
            GridFSDBFile noduleImage = gfsPhoto.findOne(queryNoduleImage);

            originalImage.writeTo("/Users/andersonjso/Downloads/meuteste/original/" + "testeOriginal" + i + ".dcm");
            noduleImage.writeTo("/Users/andersonjso/Downloads/meuteste/nodule/" + "testeNodule" + i + ".png");

            originalImage.getInputStream();
            noduleImage.getInputStream();
            //TODO return the objects images by the given id
        }

//
//        GridFS gfsPhoto = new GridFS(db, "images");
//        String fileName = "/LIDC-IDRI/LIDC-IDRI-0001/1.3.6.1.4.1.14519.5.2.1.6279.6001.298806137288633453246975630178/000000/Nodule 001.40.png";
//        GridFSDBFile imageForOutput = gfsPhoto.findOne(fileName);
//        imageForOutput.writeTo("/Users/andersonjso/Downloads/teste.png");
//        imageForOutput.getInputStream();
//


    }

    @Test
    public void randomTest() throws IOException {
        GridFS gfsPhoto = new GridFS(MongoUtils.databaseMyNodules, "images");

        GridFSDBFile imageForOutput = gfsPhoto.findOne("nodulo da galera0");

        //noduleImage.writeTo("/Users/andersonjso/Downloads/meuteste/nodule/" + "testeNodule" + i + ".png");
        imageForOutput.writeTo("/Users/andersonjso/Downloads/meuteste/nodule/vamosver.png");
    }

    @Test
    public void shoudlRetrieveNoduleById(){
        BigNodule bigNodule = new CADRepository().retrieveNoduleById("Meu nodulo teste");

        System.out.println();
        //Assertions.isTrue(bigNodule.getNoduleID().equals("Meu nodulo teste"));
    }

    @Test
    public void shouldListExamsByDegree(){
        ExamQueryResult examQueryResult = cadService.listExamsByDegree(3, 1);

        System.out.println();
    }

}
