package org.blog;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.blog.persistence.UserRepository;
import org.joda.time.Duration;
import restx.factory.Module;
import restx.factory.Provides;
import restx.jongo.JongoFactory;
import restx.security.BasicPrincipalAuthenticator;
import restx.security.RestxPrincipal;
import restx.security.RestxSession;
import restx.security.SignatureKey;

import javax.inject.Named;

@Module
public class AppModule {
    @Provides
    public SignatureKey signatureKey() {
         return new SignatureKey("blog-backend 5194371215776320189 backend bcb58267-9f16-4b9e-aa03-73f1f8896851".getBytes(Charsets.UTF_8));
    }

    @Provides
    @Named("restx.admin.password")
    public String restxAdminPassword() {
        return "admin";
    }

    @Provides
    @Named("app.name")
    public String appName(){
        return "blog-backend";
    }

    @Provides
    public BasicPrincipalAuthenticator basicPrincipalAuthenticator(final UserRepository userRepository) {
        return new BasicPrincipalAuthenticator() {
            @Override
            public Optional<? extends RestxPrincipal> findByName(String name) {
                return userRepository.findUserByName(name);
            }

            @Override
            public Optional<? extends RestxPrincipal> authenticate(String name, String passwordHash, ImmutableMap<String, ?> principalData) {
                boolean rememberMe = Boolean.valueOf((String) principalData.get("rememberMe"));
                Optional<? extends RestxPrincipal> user = userRepository.findAndCheckCredentials(name, passwordHash);
                if (user.isPresent()) {
                    RestxSession.current().expires(rememberMe ? Duration.standardDays(30) : Duration.ZERO);
                }

                return user;
            }
        };
    }

    @Provides @Named(JongoFactory.JONGO_DB_NAME)
    public String dbName() {
        return "blog-mongo";
    }
}
