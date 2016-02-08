package br.edu.ufal.services;

import br.edu.ufal.repository.SimilarNoduleRepository;
import cbir.isa.SimilarNodule;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by andersonjso on 2/8/16.
 */
public class SimilarNoduleService {

    SimilarNoduleRepository similarNoduleRepository = new SimilarNoduleRepository();

    public List<SimilarNodule> retrieveSimilarNodules(String path) throws UnknownHostException {
        return similarNoduleRepository.retrieveSimilarNodules(path);
    }
    /*
        public Client createClient(String userId, Client newClient) throws UserNotFoundException, UserPermissionException, ExistingClientException {
        userService.verifyPermission(userId, Permission.MANAGE_CLIENT);

        return clientRepository.createClient(newClient);
    }
     */

}
