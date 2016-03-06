package br.edu.ufal.services;

import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import br.edu.ufal.repository.CADRepository;

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

    public BufferedImage retrieveExamImageByPath(String examPath) throws IOException {
        return cadRepository.retrieveExamImageByPath(examPath);
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
}
