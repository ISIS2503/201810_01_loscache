/*
 * The MIT License
 *
 * Copyright 2017 Universidad De Los Andes - Departamento de Ingeniería de Sistemas.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.edu.uniandes.isis2503.nosqljpa.service;


import co.edu.uniandes.isis2503.nosqljpa.logic.AlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.HubLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.InmuebleLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.SensorLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.UnidadResidencialLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.HubDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.InmuebleDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.SensorDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.UnidadResidencialDTO;
import com.sun.istack.logging.Logger;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ca.mendoza968
 */
@Path("/hubs")
@Produces(MediaType.APPLICATION_JSON)
public class HubService {

    private final HubLogic hubLogic;
    private final AlarmaLogic alarmaLogic;
    private final SensorLogic sensorLogic;
    private final UnidadResidencialLogic uniResLogic;
    private  final InmuebleLogic inmuebleLogic;

    public HubService() {
        this.hubLogic= new HubLogic();
        this.alarmaLogic= new AlarmaLogic();
        this.sensorLogic = new SensorLogic();
        this.uniResLogic= new UnidadResidencialLogic();
        this.inmuebleLogic= new InmuebleLogic();
               
    }
    
    @GET
    public List<HubDTO> allHubs() {
        return hubLogic.all();
    }
    
     @GET
    @Path("/{id}")
    public HubDTO findHub(@PathParam("id") String id) {
        return hubLogic.find(id);
    }

    @POST
    public HubDTO addHub(HubDTO dto) {
        return hubLogic.add(dto);
    }
    
    @PUT
    public HubDTO updateHub(HubDTO dto) {
        return hubLogic.update(dto);
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteHub(@PathParam("id") String id) {
        try {
            hubLogic.delete(id);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Hub was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(HubService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }

    @POST
    @Path("{idHub}/unidadesResidenciales")
    public UnidadResidencialDTO addUniRes(@PathParam("idHub") String idH, UnidadResidencialDTO dto) {
        UnidadResidencialDTO result = uniResLogic.add(dto,idH);
        return result;
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales")
    public List<UnidadResidencialDTO> getUniRes(@PathParam("idHub") String idH) {        
        List<UnidadResidencialDTO> result = uniResLogic.allDeUnHub(idH);
        return result;
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}")
    public UnidadResidencialDTO getUniResPorId(@PathParam("idUn") String idU) {        
        UnidadResidencialDTO result = uniResLogic.find(idU);
        return result;
    }
    
    @PUT
    @Path("{idHub}/unidadesResidenciales/{idUn}")
    public UnidadResidencialDTO updateUniRes(UnidadResidencialDTO dto) {
        UnidadResidencialDTO result = uniResLogic.update(dto);
        return result;
    }
    
    @DELETE
    @Path("{idHub}/unidadesResidenciales/{idUn}")
    public Response deleteUniRes(@PathParam("idUn") String idU) {
        try {
            uniResLogic.delete(idU);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Unidad Residencial was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(HubService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }
    
    @POST
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles")
    public InmuebleDTO addInmueble(@PathParam("idUn") String idU, InmuebleDTO dto) {
        InmuebleDTO result = inmuebleLogic.add(dto,idU);
        return result;
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles")
    public List<InmuebleDTO> getInmuebles(@PathParam("idUn") String idH) {        
        List<InmuebleDTO> result = inmuebleLogic.findDeUnidad(idH);
        return result;
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idI}")
    public InmuebleDTO getInmueble(@PathParam("idI") String idI) {        
        InmuebleDTO result = inmuebleLogic.find(idI);
        return result;
    }
    
    @PUT
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idI}")
    public InmuebleDTO updateInmueble(InmuebleDTO dto) {
        InmuebleDTO result = inmuebleLogic.update(dto);
        return result;
    }
    
    @DELETE
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idI}")
    public Response deleteInmueble(@PathParam("idI") String idU) {
        try {
            inmuebleLogic.delete(idU);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Unidad Residencial was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(HubService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }
    
    @POST
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores")
    public SensorDTO addSensor(@PathParam("idU") String idU, SensorDTO dto) {
        SensorDTO result = sensorLogic.add(dto,idU);
        return result;
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores")
    public List<SensorDTO> getSensors(@PathParam("idU") String idU) {        
        List<SensorDTO> result = sensorLogic.findDeInmueble(idU);
        return result;
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}")
    public SensorDTO getSensor(@PathParam("idS") String idS) {        
        SensorDTO result = sensorLogic.find(idS);
        return result;
    }
    
    @PUT
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores")
    public SensorDTO updateSensor(SensorDTO dto) {
        SensorDTO result = sensorLogic.update(dto);
        return result;
    }
    
    @DELETE
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}")
    public Response deleteSensor(@PathParam("idS") String idS) {
        try {
            sensorLogic.delete(idS);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Unidad Residencial was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(HubService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }

    @POST
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/alarmas")
    public AlarmaDTO addAlarma(@PathParam("idS") String idS, AlarmaDTO dto) {
        AlarmaDTO result = alarmaLogic.add(dto,idS);
        return result;
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/alarmas")
    public List<AlarmaDTO> getAlarmas(@PathParam("idS") String idS) {        
        List<AlarmaDTO> result = alarmaLogic.findBySensorId(idS);
        return result;
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/alarmas/{idA}")
    public AlarmaDTO getAlarma(@PathParam("idA") String idA) {        
        AlarmaDTO result = alarmaLogic.find(idA);
        return result;
    }
    
    @PUT
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/alarmas")
    public AlarmaDTO updateAlarma(AlarmaDTO dto) {
        AlarmaDTO result = alarmaLogic.update(dto);
        return result;
    }
    
    @DELETE
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/alarmas/{idA}")
    public Response deleteAlarma(@PathParam("idA") String idA) {
        try {
            alarmaLogic.delete(idA);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Unidad Residencial was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(HubService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }

   

    

    
}
