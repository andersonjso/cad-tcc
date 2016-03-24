package br.edu.ufal.services;

import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import br.edu.ufal.repository.CADRepository;
import br.edu.ufal.util.ImageEncoded;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by andersonjso on 2/8/16.
 */
public class CADService {

    CADRepository cadRepository = new CADRepository();

    public List<SimilarNodule> retrieveSimilarNodules(String examPath, String idNodule) throws UnknownHostException {
        return cadRepository.retrieveSimilarNodules(examPath, idNodule);
    }

    public List<SimilarNodule> retrieveSimilarNodulesByPath(String s) {
        return null;
    }

    public ExamQueryResult listExams(int page) {
        return cadRepository.listExams(page);
    }

    public List<BigNodule> retrieveBigNodulesFromExam(String examPath) {
        return cadRepository.retrieveBigNodulesFromExam(examPath);
    }

    public Exam retrieveExamByPath(String examPath) {
        return cadRepository.retrieveExamByPath(examPath);
    }

    public BufferedImage retrieveExamImageByPath(String examPath, String noduleReference) throws IOException {
        return cadRepository.retrieveExamImageByPath(examPath, noduleReference);
    }

    public BufferedImage[] retrieveBigNodulesImagesFromExam(String examPath) throws IOException {
        return cadRepository.retrieveBigNodulesImagesFromExam(examPath);
    }

    public BufferedImage retrieveBigNoduleImage(String examPath, String noduleId) throws IOException {
        return cadRepository.retrieveBigNoduleImage(examPath, noduleId);
    }

    public ExamQueryResult retrieveExamsByPath(String examPath, int page) {
        return cadRepository.retrieveExamsByPath(examPath, page);
    }

    public BufferedImage retrieveExamSlices(String examPath, String noduleId, String roiNumber) throws IOException {
        return cadRepository.retrieveExamSlices(examPath, noduleId, roiNumber);
    }

    public BufferedImage retrieveNodulesSlices(String examPath, String noduleId, String roiNumber) throws IOException {
        return cadRepository.retrieveNodulesSlices(examPath, noduleId, roiNumber);
    }

    public BigNodule retrieveBigNodule(String examPath, String noduleId) {
        return cadRepository.retrieveBigNodule(examPath, noduleId);
    }

    public List<SimilarNodule> retrieveSimilarNodulesFrom3DNodule(List<ImageEncoded> encodedImages) throws IOException {
        return cadRepository.retrieveSimilarNodulesFrom3DNodule(encodedImages);
    }

    public ExamQueryResult listExamsByDegree(int noduleDegree, int page) {
        return cadRepository.listExamsByDegree(noduleDegree, page);
    }

    public BigNodule createBigNodule(BigNodule bigNodule, List<ImageEncoded> encodedImages) throws IOException {
        return cadRepository.createBigNodule(bigNodule, encodedImages);
    }
}
