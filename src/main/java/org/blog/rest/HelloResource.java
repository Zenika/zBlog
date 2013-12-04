package org.blog.rest;

import org.blog.domain.Message;
import org.blog.persistence.UserRepository;
import org.joda.time.DateTime;
import restx.annotations.GET;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.RolesAllowed;

@Component @RestxResource
public class HelloResource {
    @GET("/message")
    @RolesAllowed(UserRepository.User.HELLO_ROLE)
    public Message sayHello(String who) {
        return new Message().setMessage(String.format(
                "hello %s, it's %s",
                who, DateTime.now().toString("HH:mm:ss")));
    }
}
