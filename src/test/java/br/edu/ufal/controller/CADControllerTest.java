package br.edu.ufal.controller;

import br.edu.ufal.BaseTest;
import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.util.JsonMapperObject;
import junit.framework.Assert;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.test.Client;
import org.junit.Test;

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

}
