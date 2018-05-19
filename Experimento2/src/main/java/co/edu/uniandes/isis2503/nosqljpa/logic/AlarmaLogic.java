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
package co.edu.uniandes.isis2503.nosqljpa.logic;
import ch.qos.logback.classic.util.ContextInitializer;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAlarmaLogic;
import static co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.AlarmaConverter.CONVERTER;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaMensualDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaReporteDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.AlarmaEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.InmuebleEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.ListerEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.SensorEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.UnidadResidencialEntity;
import co.edu.uniandes.isis2503.nosqljpa.persistence.AlarmaPersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.InmueblePersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.ListerPersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.SensorPersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.UnidadResidencialPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author loscache
 */
public class AlarmaLogic implements IAlarmaLogic {

    private final AlarmaPersistence persistence;
    private final SensorPersistence perSe;
    private final InmueblePersistence perIn;
    private final UnidadResidencialPersistence perUn;
    private final ListerPersistence perLis;

    public AlarmaLogic() {
        this.persistence = new AlarmaPersistence();
        this.perSe= new SensorPersistence();
        this.perUn= new UnidadResidencialPersistence();
        this.perIn= new InmueblePersistence();
        this.perLis= new ListerPersistence();
    }

      
    @Override
    public AlarmaDTO add(AlarmaDTO dto, String idS) {
        if (dto.getId() == null) {
            dto.setId(UUID.randomUUID().toString());
        }
        AlarmaEntity x = CONVERTER.DtoToEntity(dto);
        x.setIdSensor(idS);
        AlarmaEntity z=persistence.add(x);
        AlarmaDTO result = CONVERTER.entityToDTO(z);

        
        
        SensorEntity h=perSe.find(idS);
        List<String> z1=h.getAlarmas();
        z1.add(dto.getId()+";"+dto.getMensaje());
        h.setAlarmas(z1);
        perSe.update(h);
        return result;
    }

    
    public AlarmaDTO update(AlarmaDTO dto) {
        AlarmaDTO result = CONVERTER.entityToDTO(persistence.update(CONVERTER.DtoToEntity(dto)));
        return result;
    }
    
    
    public List<AlarmaDTO> findBySensorId(String id) {
       List<AlarmaEntity> x = persistence.all();
        List<AlarmaEntity> z=new ArrayList();
        for(AlarmaEntity e:x)
        {
            if(e.getIdSensor().equals(id))
            z.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(z);
    }

    
    public AlarmaDTO find(String id) {
        AlarmaDTO x= CONVERTER.entityToDTO(persistence.find(id));
        return x;
    }

    
    public List<AlarmaDTO> all() {
        return CONVERTER.listEntitiesToListDTOs(persistence.all());
    }
    
    @Override
    public List<AlarmaDTO> allDeUnSensor(String idS) {
        List<AlarmaEntity> x = persistence.all();
        List<AlarmaEntity> z=new ArrayList();
        for(AlarmaEntity e:x)
        {
            if(e.getIdSensor().equals(idS))
            z.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(z);
    }
    
    public List<AlarmaDTO> allDeUnUnidad2(String idS) {
        ListerEntity lix=perLis.all().get(0);        
        List<SensorEntity> x = new ArrayList();
        for(String idU:lix.getUnidades())
        {
            x.add(perSe.find(idU));
        }
        System.out.println("HAY SENSORES ASDASD "+x.size());
        List<AlarmaDTO> z=new ArrayList();
        for(SensorEntity e:x)
        {
            List<String> alAct = e.getAlarmas();            
            InmuebleEntity x1=null;
            x1=perIn.find(e.getIdInmueble());
            if(x1!=null)
            {
                if(x1.getIdUnidad().equals(idS))
                {
                    for(String alarmita:alAct){
                        String[] data=alarmita.split(";");
                    AlarmaDTO ag=new AlarmaDTO(data[0],data[1],e.getId());
                    z.add(ag);
                    }
                }             
            }          
        }
        return z;
    }
    
    public List<AlarmaDTO> allDeUnInmueble(String idS) {
        
        List<AlarmaEntity> z=persistence.all();
                List<AlarmaDTO> z2=new ArrayList();

        System.out.println("HAY ESTAS ALARMAS "+z.size());
        for(AlarmaEntity e:z)
        {
            SensorEntity s=perSe.find(e.getIdSensor());
            String idInmueble=s.getIdInmueble();
            if(idInmueble.equals(idS))
             z2.add(CONVERTER.entityToDTO(e));            
        }         
        return z2;
        
    }
    
    
    
    public List<AlarmaMensualDTO> mensualesBarrio(String pbarrio) {
        
        List<AlarmaEntity> z=persistence.all();
        List<AlarmaDTO> z2=new ArrayList();
        AlarmaMensualDTO[] mens=new AlarmaMensualDTO[12];
        for(int i=0;i<12;i++)
        {
            mens[i]=new AlarmaMensualDTO();
            mens[i].setMes(i+1);
        }

        System.out.println("HAY ESTAS ALARMAS "+z.size());
        for(AlarmaEntity e:z)
        {
            SensorEntity s=perSe.find(e.getIdSensor());
            String idInmueble=s.getIdInmueble();
            InmuebleEntity ie=perIn.find(idInmueble);
            String idUni=ie.getIdUnidad();
            UnidadResidencialEntity ur=perUn.find(idUni);
            if(ur.getBarrio().equals(pbarrio))
            {
                String mesSt=e.getFecha().substring(3, 5);
                int mesI=Integer.parseInt(mesSt);
                mens[mesI-1].addAlarma(e.getIdSensor()+"-"+e.getMensaje());
             z2.add(CONVERTER.entityToDTO(e));  
            }
        }         
        ArrayList<AlarmaMensualDTO> resp = new ArrayList<AlarmaMensualDTO>();
        for(int i=0;i<12;i++)
        {
            resp.add(mens[i]);
        }
        return resp;
        
    }
    
    public List<AlarmaMensualDTO> mensualesUnidad(String idU) {
        
        List<AlarmaEntity> z=persistence.all();
        List<AlarmaDTO> z2=new ArrayList();
        AlarmaMensualDTO[] mens=new AlarmaMensualDTO[12];
        for(int i=0;i<12;i++)
        {
            mens[i]=new AlarmaMensualDTO();
            mens[i].setMes(i+1);
        }

        System.out.println("HAY ESTAS ALARMAS "+z.size());
        for(AlarmaEntity e:z)
        {
            SensorEntity s=perSe.find(e.getIdSensor());
            String idInmueble=s.getIdInmueble();
            InmuebleEntity ie=perIn.find(idInmueble);
            String idUni=ie.getIdUnidad();
            if(idUni.equals(idU))
            {
                String mesSt=e.getFecha().substring(3, 5);
                System.out.println("A PARSEAR: "+mesSt);
                int mesI=Integer.parseInt(mesSt);
                System.out.println(mesI);

                mens[mesI-1].addAlarma(e.getIdSensor()+"-"+e.getMensaje());
             z2.add(CONVERTER.entityToDTO(e));  
            }
        }         
        ArrayList<AlarmaMensualDTO> resp = new ArrayList();
        for(int i=0;i<12;i++)
        {
            resp.add(mens[i]);
        }
        return resp;
        
    }
    
    public List<AlarmaMensualDTO> mensualesInmueble(String idIn) {
        
        List<AlarmaEntity> z=persistence.all();
        List<AlarmaDTO> z2=new ArrayList();
        AlarmaMensualDTO[] mens=new AlarmaMensualDTO[12];
        for(int i=0;i<12;i++)
        {
            mens[i]=new AlarmaMensualDTO();
            mens[i].setMes(i+1);
        }

        System.out.println("HAY ESTAS ALARMAS "+z.size());
        for(AlarmaEntity e:z)
        {
            SensorEntity s=perSe.find(e.getIdSensor());
            String idInmueble=s.getIdInmueble();
            if(idInmueble.equals(idIn))
            {
                String mesSt=e.getFecha().substring(3, 5);
                int mesI=Integer.parseInt(mesSt);
                mens[mesI-1].addAlarma(e.getIdSensor()+"-"+e.getMensaje());
             z2.add(CONVERTER.entityToDTO(e));  
            }
        }         
        ArrayList<AlarmaMensualDTO> resp = new ArrayList();
        for(int i=0;i<12;i++)
        {
            resp.add(mens[i]);
        }
        return resp;
        
    }
    
    public List<AlarmaReporteDTO> allDeUnUnidad(String idS) {
        
        List<AlarmaEntity> z=persistence.all();
                List<AlarmaReporteDTO> z2=new ArrayList();

        System.out.println("HAY ESTAS ALARMAS "+z.size());
        for(AlarmaEntity e:z)
        {
            
            SensorEntity s=perSe.find(e.getIdSensor());
            String idInmueble=s.getIdInmueble();
            InmuebleEntity ie=perIn.find(idInmueble);
            String idUni=ie.getIdUnidad();
            if(idUni.equals(idS))
            {
                AlarmaReporteDTO x=new AlarmaReporteDTO();
                x.setId(e.getId());
                x.setFecha(e.getFecha());
                x.setActivado(1);
                x.setMensaje(e.getMensaje());
                x.setIdSensor(e.getIdSensor());
                x.setLat(s.getLat());
                x.setLon(s.getLon());
                z2.add(x); 
            
            }           
        }         
        return z2;
        
    }

    
    public AlarmaDTO delete(String id) {
        AlarmaEntity a=persistence.find(id);
        a.setActivado(0);
        persistence.update(a);
        return CONVERTER.entityToDTO(a);
    }

    
    private void addToLister(AlarmaEntity x) {
        if (perLis.all().isEmpty()) {
            perLis.add(new ListerEntity());
        }

        List<ListerEntity> le = perLis.all();
        if(le.isEmpty())
            le.add(new ListerEntity());
        ListerEntity fir = le.get(0);
        List<String> li = fir.getAlarmas();
        li.add(x.getId());
        fir.setAlarmas(li);
        perLis.update(fir);
    }
    

    
}