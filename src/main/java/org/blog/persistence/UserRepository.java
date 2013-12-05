package org.blog.persistence;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hashing;
import org.blog.domain.User;
import org.blog.domain.UserCredentials;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
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
    private final JongoCollection userCredentials;

    private final User defaultAdminUser = new User()
            .setKey(new ObjectId().toString())
            .setName("admin")
            .setRoles(ImmutableSet.<String>of(User.ADMIN_ROLE, "restx-admin"));
    private final String adminPassword;
    private final String adminPasswordHash;

    public UserRepository(@Named("restx.admin.password") String adminPassword,
                          @Named("restx.admin.passwordHash") final String adminPasswordHash,
                          @Named("usersCredentials") JongoCollection usersCredentials,
                          @Named("users") JongoCollection users) {
        this.users = users;
        this.userCredentials = usersCredentials;
        this.adminPassword = adminPassword;
        this.adminPasswordHash = adminPasswordHash;
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
        Optional<User> user = Optional.fromNullable(users.get().findOne("{name: #}", name).as(User.class));
        if (!user.isPresent() && "admin".equals(name) && !isAdminDefined()) {
            // use in memory admin user as long as no user with admin role is defined in DB
            return Optional.of(defaultAdminUser);
        } else {
            return user;
        }
    }

    public Optional<User> findAndCheckCredentials(String name, String passwordHash) {
        Optional<User> user = findUserByName(name);
        if (!user.isPresent()) {
            return Optional.absent();
        }

        UserCredentials credentials = findCredentialsForUserKey(user.get().getKey());

        if (credentials == null) {
            if ("admin".equals(name)) {
                // allow admin log in with config password as long as it is not defined in DB
                if (adminPasswordHash.equals(passwordHash)) {
                    return user;
                }
            }

            return Optional.absent();
        }

        if (BCrypt.checkpw(passwordHash, credentials.getPasswordHash())) {
            return user;
        } else {
            return Optional.absent();
        }
    }

    private UserCredentials findCredentialsForUserKey(String userKey) {
        return userCredentials.get().findOne("{ userRef: # }", new ObjectId(userKey)).as(UserCredentials.class);
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

    private boolean isAdminDefined() {
        return users.get().count("{roles: {$all: [ # ]}}", User.ADMIN_ROLE) > 0;
    }

}
