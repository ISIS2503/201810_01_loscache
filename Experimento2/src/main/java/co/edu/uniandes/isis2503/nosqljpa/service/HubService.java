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

import co.edu.uniandes.isis2503.nosqljpa.auth.AuthorizationFilter.Role;
import co.edu.uniandes.isis2503.nosqljpa.auth.Secured;
import co.edu.uniandes.isis2503.nosqljpa.logic.AlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.ClaveLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.HorarioLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.HubLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.InmuebleLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.SensorLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.UnidadResidencialLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ClaveDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.HubDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.InmuebleDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.SensorDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.HorarioDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.UnidadResidencialDTO;
import java.util.logging.Logger;
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
 * @author loscache
 */
@Path("/{idAcceso}/hubs")
@Secured
@Produces(MediaType.APPLICATION_JSON)
public class HubService {

    private final HubLogic hubLogic;
    private final AlarmaLogic alarmaLogic;
    private final SensorLogic sensorLogic;
    private final UnidadResidencialLogic uniResLogic;
    private final InmuebleLogic inmuebleLogic;
    private final ClaveLogic ClaveLogic;
    private final HorarioLogic HorarioLogic;

    public HubService() {
        this.hubLogic = new HubLogic();
        this.alarmaLogic = new AlarmaLogic();
        this.sensorLogic = new SensorLogic();
        this.uniResLogic = new UnidadResidencialLogic();
        this.inmuebleLogic = new InmuebleLogic();
        this.ClaveLogic=new ClaveLogic();
        this.HorarioLogic= new HorarioLogic();

    }

    @GET
    @Secured({Role.yale})
    public List<HubDTO> allHubs(@PathParam("idAcceso") String idAc) throws Exception {
if (!idAc.equalsIgnoreCase("Yale")) {
            throw new Exception("Solo un administrador de Yale puede manipular los Hubs");
        } else 
        return hubLogic.all();

    }

    @GET
    @Path("/{id}")
    @Secured({Role.yale})
    public HubDTO findHub(@PathParam("idAcceso") String idAc, @PathParam("id") String id) throws Exception {
        if (!idAc.equalsIgnoreCase("Yale")) {
            throw new Exception("Solo un administrador de Yale puede manipular los Hubs");
        } else 
        return hubLogic.find(id);
    }

    @POST
    @Secured({Role.yale})
    public HubDTO addHub(@PathParam("idAcceso") String idAc, HubDTO dto) throws Exception {
        if (!idAc.equalsIgnoreCase("Yale")) {
            throw new Exception("Solo un administrador de Yale puede manipular los Hubs");
        } else {
            return hubLogic.add(dto);
        }
    }

    @PUT
    @Secured({Role.yale})
    public HubDTO updateHub(@PathParam("idAcceso") String idAc, HubDTO dto) throws Exception {
        if (!idAc.equalsIgnoreCase("Yale")) {
            throw new Exception("Solo un administrador de Yale puede manipular los Hubs");
        } else {
            return hubLogic.update(dto);
        }
    }

    @DELETE
    @Secured({Role.yale})
    @Path("/{id}")
    public HubDTO deleteHub(@PathParam("idAcceso") String idAc, @PathParam("id") String id) throws Exception {
        if (!idAc.equalsIgnoreCase("Yale")) {   
            throw new Exception("Solo un administrador de Yale puede manipular los Hubs");
        } else {
            try {
                return hubLogic.delete(id);
            } catch (Exception e) {
                Logger.getLogger(HubService.class.getName()).log(Level.WARNING, e.getMessage());
                return null;
            }
        }
    }

    @POST
    @Secured({Role.yale})
    @Path("{idHub}/unidadesResidenciales")
    public UnidadResidencialDTO addUniRes(@PathParam("idAcceso") String idAc, @PathParam("idHub") String idH, UnidadResidencialDTO dto) throws Exception {
        if (!idAc.equalsIgnoreCase("Yale")) {
            throw new Exception("Solo un administrador de Yale puede agregar nuevas unidades residenciales");
        } else {
            return uniResLogic.add(dto, idH);
        }
    }

    @GET
    @Secured({Role.admin, Role.yale})
    @Path("{idHub}/unidadesResidenciales")
    public List<UnidadResidencialDTO> getUniRes(@PathParam("idAcceso") String idAc, @PathParam("idHub") String idH) throws Exception {
        if (!idAc.equalsIgnoreCase("Yale")) {
            throw new Exception("Solo un administrador de Yale puede agregar nuevas unidades residenciales");
        } else {
            List<UnidadResidencialDTO> result = uniResLogic.allDeUnHub(idH);
            return result;
        }
    }

    @GET
    @Secured({Role.admin, Role.yale})
    @Path("{idHub}/unidadesResidenciales/{idUn}")
    public UnidadResidencialDTO getUniResPorId(@PathParam("idAcceso") String idAc, @PathParam("idUn") String idU) throws Exception {

        UnidadResidencialDTO result = uniResLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getName().equalsIgnoreCase(idAc)) {
                throw new Exception("Sólo la seguridad de la unidad residencial o Yale puede acceder a la información de la unidad residencial " + result.getName());
            } else {
                return result;
            }
        } else {
            return result;
        }
    }

    @PUT
    @Secured({Role.admin, Role.yale})
    @Path("{idHub}/unidadesResidenciales/{idUn}")
    public UnidadResidencialDTO updateUniRes(@PathParam("idAcceso") String idAc, UnidadResidencialDTO dto) throws Exception {
        UnidadResidencialDTO result = uniResLogic.find(dto.getId());
        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getName().equalsIgnoreCase(idAc)) {
                throw new Exception("Sólo la seguridad de la unidad residencial o Yale puede acceder a la información de la unidad residencial " + result.getName());
            } else {
                return uniResLogic.update(dto);
            }
        } else {
            return uniResLogic.update(dto);
        }
    }

    @DELETE
    @Secured({Role.yale})
    @Path("{idHub}/unidadesResidenciales/{idUn}")
    public UnidadResidencialDTO deleteUniRes(@PathParam("idAcceso") String idAc, @PathParam("idUn") String idU) throws Exception {
        if (!idAc.equalsIgnoreCase("Yale")) {
            throw new Exception("Solo un administrador de Yale puede eliminar una unidad residencial");
        } else {
            try {
                return uniResLogic.delete(idU);
            } catch (Exception e) {
                Logger.getLogger(HubService.class.getName()).log(Level.WARNING, e.getMessage());
                return null;
            }
        }
    }

    @POST
    @Secured({Role.admin, Role.yale})
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles")
    public InmuebleDTO addInmueble(@PathParam("idAcceso") String idAc, @PathParam("idUn") String idU, InmuebleDTO dto) throws Exception {
        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!idU.equalsIgnoreCase(idAc)) {
                throw new Exception("Sólo la seguridad de la unidad residencial o Yale puede acceder a la información de la unidad residencial " + idU);
            } else {
                return inmuebleLogic.add(dto, idU);
            }
        } else {
            return inmuebleLogic.add(dto, idU);
        }
    }

    @GET
    @Secured({Role.admin, Role.yale})
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles")
    public List<InmuebleDTO> getInmuebles(@PathParam("idAcceso") String idAc, @PathParam("idUn") String idH) throws Exception {
        List<InmuebleDTO> result = inmuebleLogic.findDeUnidad(idH);
        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!idAc.equalsIgnoreCase(idH)) {
                throw new Exception("Sólo la seguridad de la unidad residencial o Yale puede acceder a la información de la unidad residencial " + idH);
            }
        }
        return result;

    }

    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idI}")
    public InmuebleDTO getInmueble(@PathParam("idAcceso") String idAc, @PathParam("idI") String idI) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idI);
        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información del inmueble " + result.getId());
                } else {
                    return result;
                }
            }
        }
        return result;
    }

    @PUT
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idI}")
    public InmuebleDTO updateInmueble(@PathParam("idAcceso") String idAc, InmuebleDTO dto, @PathParam("idI") String idI) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idI);
        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información del inmueble " + result.getId());
                } else {
                    return inmuebleLogic.update(dto);
                }
            }
        }
        return inmuebleLogic.update(dto);
    }

    @DELETE
    @Secured({Role.admin, Role.yale})
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idI}")
    public InmuebleDTO deleteInmueble(@PathParam("idAcceso") String idAc, @PathParam("idI") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);
        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información del inmueble " + result.getId());
                }
            }
        }

        try {
            return inmuebleLogic.delete(idU);
        } catch (Exception e) {
            Logger.getLogger(HubService.class.getName()).log(Level.WARNING, e.getMessage());
            return null;
        }
    }

    @POST
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores")
    public SensorDTO addSensor(@PathParam("idAcceso") String idAc, @PathParam("idU") String idU, SensorDTO dto) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);
        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información del inmueble " + result.getId());
                }
            }
        }

        return sensorLogic.add(dto, idU);
    }

    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores")
    public List<SensorDTO> getSensors(@PathParam("idAcceso") String idAc, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);
        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información del inmueble " + result.getId());
                }
            }
        }

        return sensorLogic.findDeInmueble(idU);
    }

    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}")
    public SensorDTO getSensor(@PathParam("idAcceso") String idAc, @PathParam("idS") String idS, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información del inmueble " + result.getId());
                }
            }
        }

        return sensorLogic.find(idS);
    }

    @PUT
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores")
    public SensorDTO updateSensor(@PathParam("idAcceso") String idAc, SensorDTO dto, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información del inmueble " + result.getId());
                }
            }
        }

        return sensorLogic.update(dto);
    }

    @DELETE
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}")
    public Response deleteSensor(@PathParam("idAcceso") String idAc, @PathParam("idS") String idS, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);
        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información del inmueble " + result.getId());
                }
            }
        }
        try {
            sensorLogic.delete(idS);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Unidad Residencial was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(HubService.class.getName()).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }

    @POST
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/alarmas")
    public AlarmaDTO addAlarma(@PathParam("idAcceso") String idAc, @PathParam("idS") String idS, AlarmaDTO dto, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!dto.getIdSensor().equals(idS)) {
                    if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                        throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                    }
                }
            }
        }

        return alarmaLogic.add(dto, idS);
    }

    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/alarmas")
    public List<AlarmaDTO> getAlarmas(@PathParam("idAcceso") String idAc, @PathParam("idS") String idS, @PathParam("idU") String idU) throws Exception {

        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }

        return alarmaLogic.findBySensorId(idS);
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/reporte")
    public List<AlarmaDTO> getAlarmasUnidad(@PathParam("idAcceso") String idAc, @PathParam("idUn") String idU) throws Exception {

        return alarmaLogic.allDeUnUnidad(idU);
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idIn}/reporte")
    public List<AlarmaDTO> getAlarmasInmueble(@PathParam("idAcceso") String idAc, @PathParam("idIn") String idU) throws Exception {

        return alarmaLogic.allDeUnInmueble(idU);
    }

    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/alarmas/{idA}")
    public AlarmaDTO getAlarma(@PathParam("idAcceso") String idAc, @PathParam("idA") String idA, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }

        return alarmaLogic.find(idA);
    }

    @PUT
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/alarmas")
    public AlarmaDTO updateAlarma(@PathParam("idAcceso") String idAc, AlarmaDTO dto, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }

        return alarmaLogic.update(dto);
    }

    @DELETE
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/alarmas/{idA}")
    public AlarmaDTO deleteAlarma(@PathParam("idAcceso") String idAc, @PathParam("idA") String idA, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }
        try {
            return alarmaLogic.delete(idA);
        } catch (Exception e) {
            Logger.getLogger(HubService.class.getName()).log(Level.WARNING, e.getMessage());
            return null;
        }
    }
    
    @POST
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Claves")
    public ClaveDTO addClave(@PathParam("idAcceso") String idAc, @PathParam("idS") String idS, ClaveDTO dto, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!dto.getIdSensor().equals(idS)) {
                    if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                        throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                    }
                }
            }
        }

        return ClaveLogic.add(dto, idS);
    }

    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Claves")
    public List<ClaveDTO> getClaves(@PathParam("idAcceso") String idAc, @PathParam("idS") String idS, @PathParam("idU") String idU) throws Exception {

        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }

        return ClaveLogic.findBySensorId(idS);
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Claves/{dueno}/dueno")
    public List<ClaveDTO> getClavesDueno(@PathParam("idAcceso") String idAc, @PathParam("idS") String idS, @PathParam("idU") String idU, @PathParam("dueno") String du) throws Exception {

        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }

        return ClaveLogic.allDeUnDueno(du,idS);
    }
    
    

    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Claves/{idA}")
    public ClaveDTO getClave(@PathParam("idAcceso") String idAc, @PathParam("idA") String idA, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }

        return ClaveLogic.find(idA);
    }

    @PUT
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Claves")
    public ClaveDTO updateClave(@PathParam("idAcceso") String idAc, ClaveDTO dto, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }

        return ClaveLogic.update(dto);
    }

    @DELETE
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Claves/{idA}")
    public ClaveDTO deleteClave(@PathParam("idAcceso") String idAc, @PathParam("idA") String idA, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }
        try {
            return ClaveLogic.delete(idA);
        } catch (Exception e) {
            Logger.getLogger(HubService.class.getName()).log(Level.WARNING, e.getMessage());
            return null;
        }
    }
    
    @POST
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Horarios")
    public HorarioDTO addHorario(@PathParam("idAcceso") String idAc, @PathParam("idS") String idS, HorarioDTO dto, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!dto.getIdSensor().equals(idS)) {
                    if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                        throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                    }
                }
            }
        }
        
        System.out.println(idS);
        dto.setIdSensor(idS);

        return HorarioLogic.add(dto, idS);
    }

    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Horarios")
    public List<HorarioDTO> getHorarios(@PathParam("idAcceso") String idAc, @PathParam("idS") String idS, @PathParam("idU") String idU) throws Exception {

        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }

        return HorarioLogic.allDeUnSensor(idS);
    }
    
    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/allHorarios")
    public List<HorarioDTO> getHorariosInmueble(@PathParam("idAcceso") String idAc, @PathParam("idS") String idS, @PathParam("idU") String idU, @PathParam("dueno") String du) throws Exception {

        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }
        return HorarioLogic.allDeUnInmueble(idU);
    }
    
    

    @GET
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Horarios/{idA}")
    public HorarioDTO getHorario(@PathParam("idAcceso") String idAc, @PathParam("idA") String idA, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }

        return HorarioLogic.find(idA);
    }

    @PUT
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Horarios")
    public HorarioDTO updateHorario(@PathParam("idAcceso") String idAc, HorarioDTO dto, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }

        return HorarioLogic.update(dto);
    }

    @DELETE
    @Path("{idHub}/unidadesResidenciales/{idUn}/inmuebles/{idU}/sensores/{idS}/Horarios/{idA}")
    public HorarioDTO deleteHorario(@PathParam("idAcceso") String idAc, @PathParam("idA") String idA, @PathParam("idU") String idU) throws Exception {
        InmuebleDTO result = inmuebleLogic.find(idU);

        if (!idAc.equalsIgnoreCase("Yale")) {
            if (!result.getIdUnidad().equalsIgnoreCase(idAc)) {
                if (!result.getPropietario().equalsIgnoreCase(idAc)) {
                    throw new Exception("Sólo la seguridad de la unidad residencial, Yale o el propietario " + result.getPropietario() + " puede acceder a la información de sensores del inmueble " + result.getId());
                }
            }
        }
        try {
            return HorarioLogic.delete(idA);
        } catch (Exception e) {
            Logger.getLogger(HubService.class.getName()).log(Level.WARNING, e.getMessage());
            return null;
        }
    }

}
