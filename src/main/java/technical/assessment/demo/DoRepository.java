package technical.assessment.demo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DoRepository extends MongoRepository<DoList,String> {
    Optional<DoList> findByTitle(String title);

    Optional<DoList> findById(String userId);

    void deleteById(Integer userId);
}
