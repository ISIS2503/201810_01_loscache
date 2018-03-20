package co.edu.uniandes.isis2503.nosqljpa.service;

///*
// * The MIT License
// *
// * Copyright 2017 Universidad De Los Andes - Departamento de Ingeniería de Sistemas.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package co.edu.uniandes.isis2503.experimento1.service;
//
//import co.edu.uniandes.isis2503.experimento1.interfaces.IRoomLogic;
//import co.edu.uniandes.isis2503.experimento1.interfaces.IConsolidatedDataLogic;
//import co.edu.uniandes.isis2503.experimento1.interfaces.ISensorLogic;
//import co.edu.uniandes.isis2503.experimento1.logic.RoomLogic;
//import co.edu.uniandes.isis2503.experimento1.logic.ConsolidatedDataLogic;
//import co.edu.uniandes.isis2503.experimento1.logic.SensorLogic;
//import co.edu.uniandes.isis2503.experimento1.model.dto.model.RoomDTO;
//import co.edu.uniandes.isis2503.experimento1.model.dto.model.ConsolidatedDataDTO;
//import co.edu.uniandes.isis2503.experimento1.model.dto.model.SensorDTO;
//import com.sun.istack.logging.Logger;
//import java.util.List;
//import java.util.logging.Level;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
///**
// *
// * @author ca.mendoza968
// */
//@Path("/rooms")
//@Produces(MediaType.APPLICATION_JSON)
//public class RoomService {
//    private final IRoomLogic roomLogic;
//    private final IConsolidatedDataLogic consolidateddataLogic;
//    private final ISensorLogic sensorLogic;
//
//    public RoomService() {
//        this.roomLogic = new RoomLogic();
//        this.consolidateddataLogic = new ConsolidatedDataLogic();
//        this.sensorLogic = new SensorLogic();
//    }
//
//    @POST
//    public RoomDTO add(RoomDTO dto) {
//        return roomLogic.add(dto);
//    }
//
//    @POST
//    @Path("{code}/consolidateddata")
//    public ConsolidatedDataDTO addConsolidatedData(@PathParam("code") String code, ConsolidatedDataDTO dto) {
//        RoomDTO room = roomLogic.findCode(code);
//        //Find the id of the measurement associated with the first sensor on the room
//        dto.setMeasurementID(sensorLogic.find(room.getSensors().get(0)).getMeasurements().get(0));
//        dto.setRoomID(room.getId());
//        ConsolidatedDataDTO result = consolidateddataLogic.add(dto);
//        room.addConsolidatedData(dto.getId());
//        roomLogic.update(room);
//        return result;
//    }
//    
//    @GET
//    @Path("{code}/consolidateddata")
//    public List<ConsolidatedDataDTO> getConsolidatedData(@PathParam("code") String code) {
//        RoomDTO room = roomLogic.findCode(code);
//        return consolidateddataLogic.findByRoomId(room.getId());
//    }
//    
//    @POST
//    @Path("{code}/sensors")
//    public SensorDTO addSensor(@PathParam("code") String code, SensorDTO dto) {
//        RoomDTO room = roomLogic.findCode(code);
//        SensorDTO result = sensorLogic.add(dto);
//        room.addSensor(dto.getId());
//        roomLogic.update(room);
//        return result;
//    }
//
//    @PUT
//    public RoomDTO update(RoomDTO dto) {
//        return roomLogic.update(dto);
//    }
//
//    @GET
//    @Path("/{id}")
//    public RoomDTO find(@PathParam("id") String id) {
//        return roomLogic.find(id);
//    }
//
//    @GET
//    public List<RoomDTO> all() {
//        return roomLogic.all();
//    }
//
//    @DELETE
//    @Path("/{id}")
//    public Response delete(@PathParam("id") String id) {
//        try {
//            roomLogic.delete(id);
//            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Room was deleted").build();
//        } catch (Exception e) {
//            Logger.getLogger(RoomService.class).log(Level.WARNING, e.getMessage());
//            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
//        }
//    }    
//}
