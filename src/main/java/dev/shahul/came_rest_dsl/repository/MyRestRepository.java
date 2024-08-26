package dev.shahul.came_rest_dsl.repository;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;
import dev.shahul.came_rest_dsl.model.RequestDTO;

@Repository
public interface MyRestRepository extends CouchbaseRepository<RequestDTO, String> {

}
