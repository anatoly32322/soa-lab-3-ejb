package org.example.resource;

import org.example.ejb.DemographyServiceImpl;
import org.example.interfaces.DemographyService;
import org.example.models.ErrorWrapper;
import org.example.wrapper.CountWrapper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.GET;
import org.example.wrapper.PercentWrapper;

import javax.naming.NamingException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;


@Path("")
public class MyResource {

    public MyResource() throws NamingException {
    }

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPing() {
        return Response.ok().entity("ping").build();
    }

    @GET
    @Path("/nationality/{nationality}/hair-color/{hair-color}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonsCount(
            @PathParam("nationality") String nationality,
            @PathParam("hair-color") String hairColor
    ) throws NamingException {
        DemographyService service = createTextProcessorBeanFromJNDI("ejb:");
        PercentWrapper result;
        try {
            result = service.getPersonCountByNationalityAndHairColor(nationality, hairColor);
        } catch (Exception e) {
            return Response.status(500).entity(new ErrorWrapper(e.getMessage())).build();
        }
        return Response.ok().entity(result).build();
    }

    @GET
    @Path("/hair-color/{hairColor}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonsCountByHairColor(
            @PathParam("hairColor") String hairColor
    ) throws NamingException {
        DemographyService service = createTextProcessorBeanFromJNDI("ejb:");
        CountWrapper result;
        try {
            result = service.getPersonCountByHairColor(hairColor);
        } catch (Exception e) {
            return Response.status(500).entity(new ErrorWrapper(e.getMessage())).build();
        }
        return Response.ok().entity(result).build();
    }

    private static DemographyService createTextProcessorBeanFromJNDI(String namespace) throws NamingException {
        return lookupTextProcessorBean(namespace);
    }

    private static DemographyService lookupTextProcessorBean
            (String namespace) throws NamingException {
        Context ctx = createInitialContext();
        String appName = "";
        String moduleName = "ejb";
        String distinctName = "";
        String beanName = DemographyServiceImpl.class.getSimpleName();
        String viewClassName = DemographyService.class.getName();
        return (DemographyService) ctx.lookup(namespace
                + appName + "/" + moduleName
                + "/" + distinctName + "/" + beanName + "!" + viewClassName);
    }

    private static Context createInitialContext() throws NamingException {
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.jboss.naming.remote.client.InitialContextFactory");
        jndiProperties.put(Context.URL_PKG_PREFIXES,
                "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.PROVIDER_URL,
                "http-remoting://localhost:8080");
        jndiProperties.put("jboss.naming.client.ejb.context", true);
        return new InitialContext(jndiProperties);
    }
}
