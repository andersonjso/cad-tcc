package br.edu.ufal.controller;

import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import br.edu.ufal.services.CADService;
import br.edu.ufal.util.JsonMapperObject;

import com.google.inject.name.Named;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by andersonjso on 2/8/16.
 */
public class CADController {

    CADService cadService = new CADService();
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
    JsonMapperObject mapper = new JsonMapperObject();

    @GET
    @Path("/nodule/similar")
    public Result retrieveSimilarNodules(@Named("dbPath") Optional<String> dbPath,
                                         @Named("folder") Optional<String> folder){
        List<SimilarNodule> similarNodules = new ArrayList<>();
//        try {
            if (dbPath.isPresent()){
                similarNodules = cadService.retrieveSimilarNodulesByPath(dbPath.get());
            }
            else if (folder.isPresent()){
               // similarNodules = CADService.retrieveSimilarNodulesByFolder(folder.get());
            }
            return Results.ok(mapper.toJson(similarNodules));
//        } catch (UnknownHostException e) {
//            return Results.with(400);
//        }
    }

    @GET
    @Path("/exams/:page")
    public Result listExams(String page){
        ExamQueryResult examQueryResult = cadService.listExams(Integer.parseInt(page));

        return Results.ok(mapper.toJson(examQueryResult));
    }

    @GET
    @Path("/exam/:examPath/bignodules")
    public Result retrieveBigNodulesFromExam (String examPath){
        List<BigNodule> bigNodules = cadService.retrieveBigNodulesFromExam(examPath);

        return Results.ok(mapper.toJson(bigNodules));
    }

    @GET
    @Path("exam/:examPath/nodule/:noduleId/similar")
    public Result retrieveSimilarNodules(String examPath, String noduleId){
        try {
            List<SimilarNodule> similarNodules = cadService.retrieveSimilarNodules(examPath, noduleId);

            return Results.ok(mapper.toJson(similarNodules));
        } catch (UnknownHostException e) {
            //TODO: put a mapped exception here
            e.printStackTrace();
            return null;
        }
    }
}
