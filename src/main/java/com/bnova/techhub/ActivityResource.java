package com.bnova.techhub;

import io.cloudevents.CloudEvent;
import io.cloudevents.jackson.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cloudevents.jackson.PojoCloudEventDataMapper;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.bnova.techhub.model.Activity;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/")
@Consumes({MediaType.APPLICATION_JSON, JsonFormat.CONTENT_TYPE})
@Produces(MediaType.APPLICATION_JSON)
public class ActivityResource
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityResource.class);
    @POST
    public Response create(CloudEvent event) {
        LOGGER.info("Received event: {}", event);
        if (event == null || event.getData() == null) {
            throw new BadRequestException("Invalid data received. Null or empty event");
        }
        if(event.getType().equals("com.bnova.techhub.button.clicked")) {
            Activity activity = PojoCloudEventDataMapper
                    .from(new ObjectMapper(), Activity.class)
                    .map(event.getData())
                    .getValue();
            LOGGER.info("Received Activity: {}", activity);
            return Response
                    .ok()
                    .build();
        } else {
            LOGGER.info("Received event: {}", event);
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Invalid event type")
                    .build();
        }
    }
}
