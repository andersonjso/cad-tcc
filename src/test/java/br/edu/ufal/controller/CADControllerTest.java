package br.edu.ufal.controller;

import br.edu.ufal.BaseTest;
import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.cad.NoduleDTO;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.repository.CADRepositoryTest;
import br.edu.ufal.util.ImageEncoded;
import br.edu.ufal.util.JsonMapperObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.mongodb.assertions.Assertions;
import junit.framework.Assert;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.test.Client;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created by andersonjso on 2/8/16.
 */
public class CADControllerTest extends BaseTest{

    JsonMapperObject mapper = new JsonMapperObject();
    //"/LIDC-IDRI/LIDC-IDRI-0001";
    /*
    final String token = userRepository.doAuth("admin@gmail.com", "123456").tokenize();

        org.jooby.test.Client.Response jsonResponse = server.get("/user/" + UtilConstants.USER_ID_ADMIN + "/client/" + UtilConstants.CLIENT_ID_1)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .expect(200);

        jsonResponse.expect(s -> {
            assertThat(new JsonMapperObject().toJson(s).get("email").asText()).isEqualToIgnoringCase("teste@gmail.com");
        });
     */

    @Test //@Path("/exams")
    public void shouldListExams() throws Exception {
        Client.Response jsonResponse = server.get("/exams")
                .header("Content-Type", "application/json")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);
            assertTrue(mapper.toJson(s).size() == 864);
        });
    }


    //TODO: check if there is multiple exams wit this kind of prefix
    @Test //@Path("/exam/:examPath/bignodules")
    public void shouldRetrieveNodulesFromExam() throws Exception {
        String path = "LIDC-IDRI-0329";

        Client.Response jsonResponse = server.get("/exam/" + path + "/bignodules")
                .header("Content-Type", "application/json")
                .expect(200);

        jsonResponse.expect(s -> {
            assertTrue(mapper.toJson(s).size() == 3);
        });
    }
    @Test //@Path("exam/:examPath")
    public void shouldRetrieveExamByPath() throws Exception {
        String path = "LIDC-IDRI-0398";
//@Path("exam/:examPath/bignodules/:noduleId")
        Client.Response jsonResponse = server.get("/exam/image/" + path + "0")
                .header("Content-Type", "application/json")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);
          //  assertTrue(mapper.toJson(s).size() == 3);
        });
    }

    @Test //@Path("exam/:examPath")
    public void shouldRetfsafasrieveExamByPath() throws Exception {
        String path = "LIDC-IDRI-0398";

        //http://localhost:8080/exam/LIDC-IDRI-0357/bignodules/Nodule%20004
//@Path("exam/:examPath/bignodules/:noduleId")
        Client.Response jsonResponse = server.get("/exam/LIDC-IDRI-0357/bignodules/Nodule%20004")
                .header("Content-Type", "application/json")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);
            //  assertTrue(mapper.toJson(s).size() == 3);
        });
    }


    @Test //@Path("exam/:examPath/nodule/:noduleId/similar")
    public void shouldRetrieveSimilarNodules() throws Exception {
        String examPath = "LIDC-IDRI-0323";
        String noduleId = "Nodule%20003";

        Client.Response jsonResponse = server.get("/exam/" + examPath + "/nodule/" + noduleId + "/similar")
                .header("Content-Type", "application/json")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);
            assertTrue(mapper.toJson(s).size() == 10);
        });
    }

    @Test //@Path("exams/:examPath/:page")
    public void shoudlRetrieveExamsByPath() throws Exception {
        String examPath = "0323";

        Client.Response jsonResponse = server.get("/exams/" + examPath + "/1")
                .header("Content-Type", "application/json")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);
          //  assertTrue(mapper.toJson(s).get("exams").size() == 1);
        });
    }

    @Test
    public void shouldRetrieveBigNodule() throws Exception {
        String examPath = "LIDC-IDRI-0323";
        String noduleId = "Nodule%20003";

        Client.Response jsonResponse = server.get("/exam/" + examPath + "/big-nodule/" + noduleId)
                .header("Content-Type", "application/json")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);
           // assertTrue(mapper.toJson(s).size() == 10);
        });
    }

    @Test
    public void shouldRetrieveSimilarNodules3D() throws Exception {
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
            byte[] bytes = CADRepositoryTest.loadFile(file);
            byte[] encoded = Base64.getEncoder().encode(bytes);

            String encodedString = new String(encoded);

            ImageEncoded imageEncoded = new ImageEncoded(encodedString);

            encodedImages.add(imageEncoded);
        }
        System.out.println(mapper.toJson(encodedImages).toString());

        Client.Response jsonResponse = server.post("/nodule/similar")
                .header("Content-Type", "application/json")
                .body(mapper.toJson(encodedImages).toString(), "String")
                .expect(200);

        jsonResponse.expect(s -> {
            assertTrue(mapper.toJson(s).size() == 10);
        });
    }

    @Test
    public void shouldCreateNodule() throws Exception {
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
            byte[] bytes = CADRepositoryTest.loadFile(file);
            byte[] encoded = Base64.getEncoder().encode(bytes);

            String encodedString = new String(encoded);

            ImageEncoded imageEncoded = new ImageEncoded(encodedString);

            encodedImages.add(imageEncoded);
        }
        double[] doubles = {};
        BigNodule bigNodule = new BigNodule("Meu nodulo teste", new ArrayList(), 1,
                2, 3, 4, 5, 6, 7, 8, 9, doubles);

//        final JsonNodeFactory factory = JsonNodeFactory.instance;
//
//        ObjectNode jsonNodes = factory.objectNode();
//
//        ObjectMapper mapper = new ObjectMapper();
//        String bigNoduleJson = mapper.writeValueAsString(bigNodule);
//        String encodedImagesJson = mapper.writeValueAsString(encodedImages);
//
//        jsonNodes.put("bigNodule", bigNoduleJson);
//        jsonNodes.put("encodedImages", encodedImagesJson);

        NoduleDTO noduleDTO = new NoduleDTO(bigNodule, encodedImages);

        Client.Response jsonResponse = server.post("/nodule")
                .header("Content-Type", "application/json")
                .body(mapper.toJson(noduleDTO).toString(), "String")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);
        });
    }

    @Test
    public void shouldRetrieveTexture() throws Exception {
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
            byte[] bytes = CADRepositoryTest.loadFile(file);
            byte[] encoded = Base64.getEncoder().encode(bytes);

            String encodedString = new String(encoded);

            ImageEncoded imageEncoded = new ImageEncoded(encodedString);

            encodedImages.add(imageEncoded);
        }

        Client.Response jsonResponse = server.post("/nodule/texture")
                .header("Content-Type", "application/json")
                .body(mapper.toJson(encodedImages).toString(), "String")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);
        });
    }

    @Test
    public void shouldListNodules() throws Exception {
        Client.Response jsonResponse = server.get("/nodules/1")
                .header("Content-Type", "application/json")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);

        });
    }

    @Test //    @Path("/nodule/:noduleId")
    public void shouldEditNodule() throws Exception {
        BigNodule bigNodule = new BigNodule(null, null, 10, 10, 10, 10, 10, 1, 1, 1, 1, null);

        String noduleId = "Meu%20nodulo%20teste";

        String enviar = mapper.toJson(bigNodule).toString();
        Client.Response jsonResponse = server.put("/nodule/" + noduleId)
                .header("Content-Type", "application/json")
                .body(mapper.toJson(bigNodule).toString(), "String")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);

        });
    }

}
