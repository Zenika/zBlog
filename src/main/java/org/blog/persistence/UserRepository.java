package org.blog.persistence;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.hash.Hashing;
import org.blog.domain.User;
import org.bson.types.ObjectId;
import restx.admin.AdminModule;
import restx.factory.Component;
import restx.jongo.JongoCollection;

import javax.inject.Named;

/**
 * This is a fake, in-memory, User repository intended to provide
 * basic authentication for Hello Resource
 * In the real world, you will rely on an in-house authentication layer
 * (which will maybe ask a db for the user)
 */
@Component
public class UserRepository {

    private final JongoCollection users;

    public UserRepository(@Named("restx.admin.password") String adminPassword,
                          @Named("users") JongoCollection users) {
        this.users = users;
        if (findUserByName("admin") == null) {
            createUser(new User("admin", hashPwd(adminPassword), AdminModule.RESTX_ADMIN_ROLE, User.ADMIN_ROLE));
        }
    }

    private static String hashPwd(String pwd) {
        return Hashing.md5().hashString(pwd, Charsets.UTF_8).toString();
    }

    public Iterable<User> findUsers(Optional<String> name) {
        if (name.isPresent()) {
            return users.get().find("{name: #}", name.get()).as(User.class);
        } else {
            return users.get().find().as(User.class);
        }
    }

    public Optional<User> findUserByName(final String name) {
        return Optional.of(users.get().findOne("{name: #}", name).as(User.class));
    }

    public Optional<User> findUserByNameAndPasswordHash(final String name, final String passwordHash) {
        return Optional.of(users.get().findOne("{name: #, passwordHash: #}", name, passwordHash).as(User.class));
    }

    public User createUser(User user) {
        users.get().save(user);
        return user;
    }

    public User updateUser(User user) {
        users.get().save(user);
        return user;
    }

    public void deleteUser(String oid) {
        users.get().remove(new ObjectId(oid));
    }

}
