package org.blog.domain;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;
import restx.security.RestxPrincipal;

public class User implements RestxPrincipal   {

    public static final String ADMIN_ROLE = "admin-role";

    @Id
    @ObjectId
    private String key;
    private ImmutableSet<String> roles;
    private String name;
    private String passwordHash;

    public User() {

    }

    public User(String name, String passwordHash, String... roles) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.roles = ImmutableSet.copyOf(roles);
    }

    @Override
    public ImmutableSet<String> getPrincipalRoles() {
        return roles;
    }

    public String getKey() {
        return key;
    }

    public User setKey(final String key) {
        this.key = key;
        return this;
    }


    @Override
    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public User setRoles(final ImmutableSet<String> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", name)
                .add("roles", roles)
                .toString();
    }
}
