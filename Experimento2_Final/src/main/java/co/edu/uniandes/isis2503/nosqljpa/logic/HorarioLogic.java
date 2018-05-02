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
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IHorarioLogic;
import static co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.HorarioConverter.CONVERTER;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.HorarioDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.HorarioEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.InmuebleEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.ListerEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.SensorEntity;
import co.edu.uniandes.isis2503.nosqljpa.persistence.HorarioPersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.ListerPersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.SensorPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author loscache
 */
public class HorarioLogic implements IHorarioLogic {

    private final HorarioPersistence persistence;
    private final SensorPersistence perSe;
        private final ListerPersistence perLis;


    public HorarioLogic() {
        this.persistence = new HorarioPersistence();
        this.perSe= new SensorPersistence();
        this.perLis= new ListerPersistence();
    }

      
    @Override
    public HorarioDTO add(HorarioDTO dto, String idS) {
        if (dto.getId() == null) {
            dto.setId(UUID.randomUUID().toString());
        }        
        HorarioEntity x = CONVERTER.DtoToEntity(dto);        
        x.setIdSensor(idS);
        addToLister(x);
        SensorEntity h=perSe.find(idS);
        List<String> z1=h.getHorarios();
        z1.add(dto.getDueno()+";"+dto.getHoraInicial()+";"+dto.getHoraFinal());
        h.setHorarios(z1);
        perSe.update(h);
        
        HorarioDTO result = CONVERTER.entityToDTO(persistence.add(x));        
        return result;
    }

    
    public HorarioDTO update(HorarioDTO dto) {
        HorarioDTO result = CONVERTER.entityToDTO(persistence.update(CONVERTER.DtoToEntity(dto)));
        return result;
    }
    
    
    public List<HorarioDTO> findBySensorId(String id) {
       List<HorarioEntity> x = persistence.all();
        List<HorarioEntity> z=new ArrayList();
        for(HorarioEntity e:x)
        {
            if(e.getIdSensor().equals(id))
            z.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(z);
    }

    
    public HorarioDTO find(String id) {
        HorarioDTO x= CONVERTER.entityToDTO(persistence.find(id));
        return x;
    }

    
    public List<HorarioDTO> all() {
        return CONVERTER.listEntitiesToListDTOs(persistence.all());
    }
    
    @Override
    public List<HorarioDTO> allDeUnSensor(String idS) {
        List<SensorEntity> x = perSe.all();
        System.out.println("HAY SENSORES ASDASD "+x.size());
        List<HorarioDTO> z=new ArrayList();
        for(SensorEntity e:x)
        {
            List<String> alAct = e.getHorarios(); 
            if(e.getId().equals(idS))
                {
                    for(String alarmita:alAct){
                        String[] data=alarmita.split(";");
                    HorarioDTO ag=new HorarioDTO();
                    ag.setDueno(data[0]);
                    ag.setHoraInicial(data[1]);
                    ag.setHoraFinal(data[2]);
                    ag.setIdSensor(idS);
                    z.add(ag);
                    }
                }             
       }         
        
        return z;
    }
    
    public List<HorarioDTO> allDeUnInmueble(String idIn) {
        List<SensorEntity> x = perSe.all();
        System.out.println("HAY SENSORES ASDASD "+x.size());
        List<HorarioDTO> z=new ArrayList();
        for(SensorEntity e:x)
        {
            List<String> alAct = e.getHorarios(); 
            if(e.getIdInmueble().equals(idIn))
                {
                    for(String alarmita:alAct){
                        String[] data=alarmita.split(";");
                    HorarioDTO ag=new HorarioDTO();
                    ag.setDueno(data[0]);
                    ag.setHoraInicial(data[1]);
                    ag.setHoraFinal(data[2]);
                    ag.setIdSensor(e.getId());
                    z.add(ag);
                    }
                }             
       }         
        
        return z;
    }

    
    public HorarioDTO delete(String id) {
        HorarioEntity a=persistence.find(id);
        a.setActivado(0);
        persistence.update(a);
        return CONVERTER.entityToDTO(a);
    }

    public List<HorarioDTO> allDeUnDueno(String due) {
        List<HorarioEntity> x = persistence.all();
        List<HorarioEntity> z=new ArrayList();
        for(HorarioEntity e:x)
        {
            if(e.getDueno().equals(due))
            z.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(z);
    }

    public List<HorarioDTO> allDeUnDueno(String du, String idS) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     private void addToLister(HorarioEntity x) {
        if (perLis.all().isEmpty()) {
            perLis.add(new ListerEntity());
        }

        List<ListerEntity> le = perLis.all();
        if(le.isEmpty())
            le.add(new ListerEntity());
        ListerEntity fir = le.get(0);
        List<String> li = fir.getHorarios();
        li.add(x.getId());
        fir.setHorarios(li);
        perLis.update(fir);
    }

    
}