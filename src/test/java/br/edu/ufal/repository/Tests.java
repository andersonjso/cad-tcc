package br.edu.ufal.repository;

import br.edu.ufal.AttributesNodule;
import br.edu.ufal.cad.cbir.isa.NoduleRetrievalPrecisionEvaluation;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Roi;
import br.edu.ufal.util.MongoUtils;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import org.dcm4che2.tool.dcm2jpg.Dcm2Jpg;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static javax.imageio.ImageIO.read;

/**
 * Created by andersonjso on 2/18/16.
 */
public class Tests {


    @Test
    public void aff() throws IOException {
        File img = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule0.png");
        File img2 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule1.png");
        File img3 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule2.png");
        File img4 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule3.png");
        File img5 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule4.png");
        File img6 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule5.png");
        File img7 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule6.png");
        File img8 = new File("/Users/andersonjso/Downloads/meuteste/nodule/testeNodule7.png");
        BufferedImage[] images = {read(img), read(img2), read(img3), read(img4), read(img5), read(img6), read(img7),
                read(img8)};

        AttributesNodule attributesNodule = new AttributesNodule();

        double[] attribute = attributesNodule.getImageTextureAttributes(images);

        NoduleRetrievalPrecisionEvaluation noduleRetrievalPerformanceEvaluation =
                new NoduleRetrievalPrecisionEvaluation ();
//        List<SimilarNodule> nodules = noduleRetrievalPerformanceEvaluation.retrieveSimilarNodules(images);

        System.out.println();
    }

    @Test
    public void coverter() throws IOException {
        Dcm2Jpg dcm2Jpg = new Dcm2Jpg();

        dcm2Jpg.convert(new File("/Users/andersonjso/Downloads/teste.dcm"),
                new File("/Users/andersonjso/Downloads/anderson2015.png"));
    }

//    @Test
//    public void ae() throws IOException {
//        String filename = MongoUtils.imagesFiles().findOne("{filename: {$regex: #}}", "LIDC-IDRI-0330.*").map(
//                dbObject -> (String) dbObject.get("filename")
//        );
//
//
//        GridFS gfsPhoto = new GridFS(MongoUtils.databaseTest, "images");
//        GridFSDBFile imageForOutput = gfsPhoto.findOne(filename);
//        //imageForOutput.writeTo("/Users/andersonjso/Downloads/exam666.dcm");
//        InputStream imageIS = imageForOutput.getInputStream();
//        System.out.println(filename);
//
//        File newfile = null;
//        Dcm2Jpg dcm2Jpg = new Dcm2Jpg();
//        BufferedImage bufferedImage = dcm2Jpg.convertV2(imageIS);
//
//
//
//
//
//
//                /*
//                MongoCursor<Integer> ages = friends.find("{name: 'Joe'}").map(
//    new ResultHandler<Integer>() {
//        @Override
//        public Integer handle(DBObject result) {
//            //do manual stuff here
//            return result.get("age");
//        }
//    }
//);
//                 */
//    }

    @Test
    public void afsdfasfe() throws IOException {
        ///LIDC-IDRI/LIDC-IDRI-0102/1.3.6.1.4.1.14519.5.2.1.6279.6001.144098736774350495825430776051/000000
        List<BigNodule> bigNodules =  new CADRepository().retrieveBigNodulesFromExam("LIDC-IDRI-0398");
        BigNodule bigNodule = bigNodules.get(0);

        GridFS gfsPhoto = new GridFS(MongoUtils.databaseTest, "images");
        for (int i=0; i<bigNodule.getRois().size(); i++){
            Roi roi = bigNodule.getRois().get(i);
            GridFSDBFile imageForOutput = gfsPhoto.findOne(roi.getNoduleImage());
            InputStream imageIS = imageForOutput.getInputStream();
            Files.copy(imageIS, Paths.get("/Users/andersonjso/Downloads/another/nodlinho" + i + ".png"));
        }

        for (int i=0; i<bigNodule.getRois().size(); i++){
            Roi roi = bigNodule.getRois().get(i);
            GridFSDBFile imageForOutput = gfsPhoto.findOne(roi.getOriginalImage());
            InputStream imageIS = imageForOutput.getInputStream();
            Files.copy(imageIS, Paths.get("/Users/andersonjso/Downloads/another/examed" + i + ".dcm"));
        }



    }

    @Test
    public void cone() throws IOException {
        InputStream inputStream = new FileInputStream("/Users/andersonjso/Downloads/another/examed1.dcm");
        BufferedImage imBuff = ImageIO.read(inputStream);
        File file = new File("/Users/andersonjso/Downloads/another/vidaloka.png");
        ImageIO.write(imBuff, "png", file);
    }


}
