package co.edu.uniandes.isis2503.nosqljpa.auth;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext crc, ContainerResponseContext crc1) {
        //TODO Edit to localhost:3000
        crc1.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        crc1.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        crc1.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
        crc1.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization");
    }
}