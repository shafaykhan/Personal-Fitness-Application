package com.example.fitness_application.identity.lookup;

import com.example.fitness_application.common.enums.Status;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LookupRepository extends ListCrudRepository<Lookup, Long> {

  List<Lookup> findAllByRecordStatusTrueOrderByGroupKeyAscIdDesc();

  Optional<Lookup> findByIdAndRecordStatusTrue(Long id);

  List<Lookup> findAllByGroupKeyInAndStatusAndRecordStatusTrue(String[] groupKeys, Status status);

  List<Lookup> findAllByIdIn(Set<Long> ids);

  int countByIdNotAndGroupKeyAndValue(Long id, String groupKey, String value);
}
