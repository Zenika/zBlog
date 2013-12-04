package org.blog.rest;

import com.google.common.base.Optional;
import org.blog.domain.User;
import org.blog.persistence.UserRepository;
import restx.Status;
import restx.annotations.*;
import restx.factory.Component;

import static restx.common.MorePreconditions.checkEquals;

@Component @RestxResource
public class UserResource {
    private UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET("/users")
    public Iterable<User> findUsers(Optional<String> name) {
        return userRepository.findUsers(name);
    }

    @POST("/users")
    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    @PUT("/users/{oid}")
    public User updateUser(String oid, User user) {
        checkEquals("oid", oid, "user.key", user.getKey());
        return userRepository.updateUser(user);
    }

    @DELETE("/users/{oid}")
    public Status deleteUser(String oid) {
        userRepository.deleteUser(oid);
        return Status.of("deleted");
    }

}
