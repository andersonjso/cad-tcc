package br.edu.ufal.repository;

import br.edu.ufal.AttributesNodule;
import br.edu.ufal.cad.cbir.isa.NoduleRetrievalPrecisionEvaluation;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Roi;
import br.edu.ufal.util.MongoUtils;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

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
    public void olar () throws UnsupportedEncodingException {
        String ae = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAIAAADTED8xAAAGJ0lEQVR42u3cWU8UaRQG4PlJ" +
                "JkbFoIisgmwqO7KDgKyKC+7ruBP/8rypk+FqZm4npc9zUSmqq2++nO0ruvuPPwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/tHly5c7OjquXLnS29s" +
                "72Oju7rYs/BYS+l1dXVcb/f39Nxs3btzo6+vr6emxPvziLl68mHqf0B8YGBgeHh4dHb1169adRs5z/d/emEbxH69CO4afFP6E" +
                "8vj4eOJ+YmLi9u3b8/PzS0tLd+/enZqayvVcHBsbGxkZyXF2dnZtbW15eTkv1f1JEstIW126dCkJkIGnqn6if3JycnFxcXV1d" +
                "WVlJZkwMzMzNze3sLCQiwn9vb29hw8f7u/vb25u5p7kSVIi77WStNK5c+cuXLhQOTDeSA6k9m82kgMp9uvr61tbWwn9R48evX" +
                "jx4vXr1ycnJ0dHRzs7O8mBJElljsWkfc6fP59NcPYA2fim9ieUM9sk7hPx29vbOSbKU+8fPHjw9OnTN2/e/Nl49epVEiCJUdG" +
                "f6WhoaMhi0j6dnZ29vb2J4Iw6KfYJ/RT1e/fu7e7uHh4eZtp58uRJVf3E/bdv3378+PHly5dnz54lPdIoEv3JnOvXr3tsSisl" +
                "dlO8p6enNzY2dv9WJT+h//z581T9Dx8+fP78OaH/8+fP09PT/Pn48eMMSCn/mf4zO1lG2mpwcDDjewr//fv3U/IPDg5yzLSTI" +
                "eft27fv37//+PFjan8S4Kz8pxukM6RLZGecDmANabFMPplkMuun5GePm9Keql8lvyT63717l2TIMcmQYyai3JlGkWHJAtJumWR" +
                "q+KnQT3VP9H/69Cmhn2L/9evXJEAuPm+kLeQ8CZDpKJtgq0frZeCpJzwV35l5zqI/M08lQLYB2fWenJxUAuQ8HSBTkNWj9eU/t" +
                "T+hnOCuRz01/CT0v3//nok/x2RCxp7qADUFVQ6kCSRzrCFtNTIyMjc3l71s4jgBnepe8X223z09Pc0xTSBZkaCvfwIkH3LMli" +
                "D5cHx8bBlpq76+vvHx8ZWVlUzzKf+J/gR6PfBJ0Kf2nzaSDBmKPjRy8q2Re2oukgO0VW9v79jY2OLiYqag7AEy/Sfua+6v7e" +
                "+PRq5U1U8CpD/U3iDHnOctSYPsBywmrTQ8PDw1NbW2tpYgTnzX3F8JUIGeK2db4XeNmoLqnvSB5ED2D1aSVrp27dro6GgSI" +
                "MNMKnomnKr0CesaeBLlOdbj/xT7jP654Wsj0V/XkwDb29sWk/bp7u6emJhI+GZH+/Lly6eN7AfqcWelQYV+bYJzJXH/vZHE" +
                "qJfyFglAW7cBd+7cqQdBh4eHOzs7BwcHCeg0hHoqWqFfH4uoJ6Q1JtXwk+sp/3nj+vq6xaR9+vv70wHq0//3Gvv7+8fHx2ef" +
                "iajoT3OoBPjzb7X9TZLk5q2treykLSYtc/Xq1XSA7IMnJyeXlpZSxevzcOkGB436QGhqfGVCIj6ZkOZQ81Lkpdy2srIyOzt" +
                "rPWnlJriawNzc3Orqan33paI/yZCGkCv1f+KEe+I+KVFflEmepEvUn2kgCwsLFpO2bgPGxsYSxBsbG7UHeNhIAiQl0hZqKEo" +
                "ryFC0t7eXel9fDq7vi6V1zMzMSABaLCNQRX/iuz4UnQRIfC8vL1egJwdS8o+OjnZ3d3Nlfn4+M0+aRo7pHiMjI74QTIsloOu" +
                "7v9UBEuiJ+KREfUMyJzX2VMlPW5ienk7QDw4O9vT0dHV1dXZ2+v0s2urmzZtJgM3NzQR3HTP8JNYT6EmA+nWg+UZOMvyk6o+" +
                "Pj/f19XV0dFg9Wq9+BiuRXXvZHOsXgRYamXNyQ/1mVk6mGhl7Uv4tHb+CbGGzB6gffqsfRZxu3Gok1i0Rv7L6AdCzP5MGFfc" +
                "5GR4etj78jtIQEv0DAwOWgt/U0NBQdrrWAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/kd/AaNlNMMUmXl1AAAAAElFTkSuQmCC";





        byte[] decodedByteImage = java.util.Base64.getDecoder().decode(ae);

        ByteArrayInputStream bais = new ByteArrayInputStream(decodedByteImage);
        //ImageIO.write(bais, "png",  new File("/Users/andersonjso/Downloads/meuteste/nodule/anderson.png"));
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
        File file = new File("/Users/andersonjso/Downloads/another/forallthekings.png");
        ImageIO.write(imBuff, "png", file);
    }


}
