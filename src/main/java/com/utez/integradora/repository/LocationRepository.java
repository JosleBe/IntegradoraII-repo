package com.utez.integradora.repository;

import com.utez.integradora.entity.LocationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends MongoRepository<LocationEntity,String > {


}
