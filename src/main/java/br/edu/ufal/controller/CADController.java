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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
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

    /**
     * list only the exams that have big nodules
     * @param
     * @return
     */
    @GET
    @Path("/exams")
    public Result listExams(){
        List<Exam> exams = cadService.listExams();

        return Results.ok(mapper.toJson(exams));
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

    //TODO create test for this
    @GET
    @Path("exam/:examPath")
    public Result retrieveExamByPath(String examPath, @Named("image") Optional<Boolean> image) throws IOException {

            Exam exam = cadService.retrieveExamByPath(examPath);

            return Results.ok(mapper.toJson(exam));



    }

    @GET
    @Path("exam/image/:examPath")
    public Result retrieveImageExamByPath(String examPath) throws IOException {

            BufferedImage imageExam = cadService.retrieveExamImageByPath(examPath);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( imageExam, "png", baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();

             byte[] encoded = Base64.getEncoder().encode(imageInByte);

            return Results.ok(encoded);
    }

    @GET
    @Path("exam/:examPath/bignodules")
    public Result retrieveBigNodulesImagesFromExam(String examPath) throws IOException {
        BufferedImage[] bigNodules = cadService.retrieveBigNodulesImagesFromExam(examPath);

        return Results.accepted(bigNodules);
    }

    @GET
    @Path("exam/:examPath/bignodules/:noduleId")
    public Result retrieveBigNoduleImage(String examPath, String noduleId) throws IOException {
        BufferedImage bigNodule = cadService.retrieveBigNoduleImage(examPath, noduleId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bigNodule, "png", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        byte[] encoded = Base64.getEncoder().encode(imageInByte);

        return Results.ok(encoded);
    }





}
