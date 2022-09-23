package com.prannoy.usermanagementservice.persistence.repository;

import com.prannoy.usermanagementservice.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    List<UserEntity> findUserEntityByUserName(String userName);
    UserEntity save(UserEntity userEntity);
}
