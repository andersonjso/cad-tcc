package br.edu.ufal.services;

import br.edu.ufal.ExamQueryResult;
import br.edu.ufal.cad.cbir.isa.SimilarNodule;
import br.edu.ufal.cad.mongodb.tags.Exam;
import br.edu.ufal.repository.CADRepository;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by andersonjso on 2/8/16.
 */
public class CADService {

    CADRepository cadRepository = new CADRepository();

    public List<SimilarNodule> retrieveSimilarNodules(String path) throws UnknownHostException {
        return cadRepository.retrieveSimilarNodules(path);
    }

    public List<SimilarNodule> retrieveSimilarNodulesByPath(String s) {
        return null;
    }

    public ExamQueryResult listExams(int page) {
        return cadRepository.listExams(page);
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
