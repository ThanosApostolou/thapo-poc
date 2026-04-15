package thapo.pocspring.application.soap.public_api.greeting;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import thapo.pocspring.application.soap.dto.GetGreetingRequest;
import thapo.pocspring.application.soap.dto.GetGreetingResponse;

import java.time.Instant;

@Endpoint
public class GreetingController {

    public static final String NAMESPACE = "http://pocspring.thapo/soap/greeting";

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetGreetingRequest")
    @ResponsePayload
    public GetGreetingResponse getGreeting(@RequestPayload GetGreetingRequest request) {
        GetGreetingResponse response = new GetGreetingResponse();
        response.setMessage("Hello, " + request.getName() + "! Welcome to the SOAP service.");
        response.setTimestamp(Instant.now().toString());
        return response;
    }
}
