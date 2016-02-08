package br.edu.ufal.controller;

import br.edu.ufal.services.SimilarNoduleService;
import br.edu.ufal.util.JsonMapperObject;
import cbir.isa.SimilarNodule;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by andersonjso on 2/8/16.
 */
public class SimilarNoduleController {

    SimilarNoduleService similarNoduleService = new SimilarNoduleService();
    /*
    @POST
    @Path("/user/:userId/client")
    public Result createClient(@Body String bodyResponseNewClient, String userId) {
        try {
            Client newClient = mapper.parse(bodyResponseNewClient, Client.class);

            clientService.createClient(userId, newClient);
            return Results.ok(mapper.toJson(newClient));
        } catch (ExistingClientException | UserPermissionException | UserNotFoundException e) {
            return Results.with(mapper.toJson(e.mapToJsonResponse()), 500);
        }
    }
     */

    @GET
    @Path("/nodule/similar/:path")
    public Result retrieveSimilarNodules(String path){
        List<SimilarNodule> similarNodules;
        try {
            similarNodules = similarNoduleService.retrieveSimilarNodules(path);

            return Results.ok(new JsonMapperObject().toJson(similarNodules));
        } catch (UnknownHostException e) {
            return Results.with(400);
        }
    }
}
