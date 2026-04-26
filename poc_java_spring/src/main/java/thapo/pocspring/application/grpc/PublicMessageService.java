package thapo.pocspring.application.grpc;

import org.springframework.stereotype.Service;

@Service
public class PublicMessageService {

    public String hello(String name) {
        return "Hello, " + (name != null ? name : "World") + "!";
    }

    public String getTest() {
        return "some test message";
    }
}

