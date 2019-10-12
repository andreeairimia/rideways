package com.booking.rides;

import java.util.*;
import com.google.gson.Gson;
import com.booking.rides.App;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;

@Path("/taxis")
public class RidesService {

    @GET
    @Path("/dave")
    @Produces("application/json")
    public String getDaveTaxis() {
        return "Hello";
    }

}