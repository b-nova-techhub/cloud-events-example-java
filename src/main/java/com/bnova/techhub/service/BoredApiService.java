package com.bnova.techhub.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import com.bnova.techhub.model.Activity;


@Path("/api")
@RegisterRestClient(configKey = "boredapi")
public interface BoredApiService
{

	@GET
	@Path("/activity")
	@Produces(MediaType.APPLICATION_JSON)
	Activity getActivity();
}