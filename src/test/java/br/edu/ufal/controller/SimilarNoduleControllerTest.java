package br.edu.ufal.controller;

import br.edu.ufal.BaseTest;
import org.jooby.test.Client;
import org.junit.Test;

/**
 * Created by andersonjso on 2/8/16.
 */
public class SimilarNoduleControllerTest extends BaseTest{

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
    @Test
    public void shouldRetrieveSimilarNodules() throws Exception {
        //    @Path("/nodule/similar/:path")
        Client.Response jsonResponse = server.get("/nodule/similar/134")
                .header("Content-Tyoe", "application/json")
                .expect(200);

        jsonResponse.expect(s -> {
            System.out.println(s);
        });
    }
}
