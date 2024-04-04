package com.bnova.techhub;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.data.PojoCloudEventData;
import io.cloudevents.jackson.JsonFormat;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cloudevents.jackson.PojoCloudEventDataMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import com.bnova.techhub.model.Activity;
import com.bnova.techhub.model.ButtonEvent;
import com.bnova.techhub.service.BoredApiService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/")
@Consumes({JsonFormat.CONTENT_TYPE})
@Produces({JsonFormat.CONTENT_TYPE})
public class ActivityResource
{
    @Inject
    ObjectMapper mapper;

    @Inject
    @RestClient
    BoredApiService boredApiService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityResource.class);
    @SneakyThrows @POST
    public Response create(CloudEvent event) {
        LOGGER.info("Received event: {}", event);
        if (event == null || event.getData() == null) {
            throw new BadRequestException("Invalid data received. Null or empty event");
        }

	    switch (event.getType())
	    {
		    case "com.bnova.techhub.button.clicked" ->
		    {
			    var buttenEvent = PojoCloudEventDataMapper
					    .from(new ObjectMapper(), ButtonEvent.class)
					    .map(event.getData())
					    .getValue();
			    LOGGER.info("Received ButtonEvent: {}", buttenEvent);
			    Thread.sleep(Duration.ofSeconds(2).toMillis());

			    return Response
					    .ok()
					    .build();
		    }
		    case "com.bnova.techhub.get.activity" ->
		    {
			    var buttenEvent = PojoCloudEventDataMapper
					    .from(mapper, ButtonEvent.class)
					    .map(event.getData())
					    .getValue();
			    LOGGER.info("Received ButtonEvent: {}", buttenEvent);

			    Activity activity = boredApiService.getActivity();

			    CloudEvent cloudEvent = CloudEventBuilder.v1(event)
					    .withSource(URI.create("cloud-events-example-java"))
					    .withData(PojoCloudEventData.wrap(activity, mapper::writeValueAsBytes))
					    .build();

			    LOGGER.info("Prepare Sending activity: {}", cloudEvent);
			    Thread.sleep(Duration.ofSeconds(2).toMillis());
			    LOGGER.info("Now Sending activity: {}", cloudEvent);

			    return Response.ok(cloudEvent).build();
		    }
		    default ->
		    {
			    LOGGER.info("Received event: {}", event);
			    return Response
					    .status(Response.Status.BAD_REQUEST)
					    .entity("Invalid event type")
					    .build();
		    }
	    }
    }
}
