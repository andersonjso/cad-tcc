package br.edu.ufal.services;

import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import br.edu.ufal.cad.mongodb.tags.BigNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import br.edu.ufal.repository.CADRepository;

import java.awt.image.BufferedImage;
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

    public List<Exam> listExams() {
        return cadRepository.listExams();
    }

    public List<BigNodule> retrieveBigNodulesFromExam(String examPath) {
        return cadRepository.retrieveBigNodulesFromExam(examPath);
    }

    public Exam retrieveExamByPath(String examPath) {
        return cadRepository.retrieveExamByPath(examPath);
    }

    public BufferedImage[] retrieveExamImageByPath(String examPath) {
        return cadRepository.retrieveExamImageByPath(examPath);
    }

//    public List<SimilarNodule> retrieveSimilarNodulesByPath(String dbPath) {
//        return cadRepository.retrieveSimilarNodulesByPath(dbPath);
//    }


    /*
        public Client createClient(String userId, Client newClient) throws UserNotFoundException, UserPermissionException, ExistingClientException {
        userS
        ervice.verifyPermission(userId, Permission.MANAGE_CLIENT);

        return clientRepository.createClient(newClient);
    }
     */

}
