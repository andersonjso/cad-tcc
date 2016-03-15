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
    @Path("/exams/:page")
    public Result listExams(int page){
        ExamQueryResult examQueryResult = cadService.listExams(page);

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

    //TODO create test for this
    @GET
    @Path("exam/:examPath")
    public Result retrieveExamByPath(String examPath, @Named("image") Optional<Boolean> image) throws IOException {

            Exam exam = cadService.retrieveExamByPath(examPath);

            return Results.ok(mapper.toJson(exam));

    }

    @GET
    @Path("exam/image/:examPath/nodule/:noduleReference")
    public Result retrieveImageExamByPath(String examPath, String noduleReference) throws IOException {

        BufferedImage imageExam = cadService.retrieveExamImageByPath(examPath, noduleReference);

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

    @GET
    @Path("exams/:examPath/:page")
    public Result retrieveExamsByPath(String examPath, int page){
        ExamQueryResult examQueryResult = cadService.retrieveExamsByPath(examPath, page);

        return Results.ok(mapper.toJson(examQueryResult));
    }

    @GET
    @Path("exam/:examPath/nodule/:noduleId/slices/:roiNumber")
    public Result retrieveExamSlices (String examPath, String noduleId, String roiNumber) throws IOException {
        BufferedImage sliceExam = cadService.retrieveExamSlices(examPath, noduleId, roiNumber);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(sliceExam, "png", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        byte[] encoded = Base64.getEncoder().encode(imageInByte);

        return Results.ok(encoded);
    }

    @GET
    @Path("exam/:examPath/nodule-images/:noduleId/slices/:roiNumber")
    public Result retrieveNodulesSlices (String examPath, String noduleId, String roiNumber) throws IOException {
        BufferedImage sliceExam = cadService.retrieveNodulesSlices(examPath, noduleId, roiNumber);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(sliceExam, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        byte[] encoded = Base64.getEncoder().encode(imageInByte);

        return Results.ok(encoded);
    }

    @GET
    @Path("exam/:examPath/big-nodule/:noduleId")
    public Result retrieveNodulesSlices (String examPath, String noduleId) throws IOException {
        BigNodule bigNodule = cadService.retrieveBigNodule(examPath, noduleId);

        return Results.ok(mapper.toJson(bigNodule));
    }

}
