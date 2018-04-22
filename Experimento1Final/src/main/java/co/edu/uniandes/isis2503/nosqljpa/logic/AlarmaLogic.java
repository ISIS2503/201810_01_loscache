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
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAlarmaLogic;
import static co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.AlarmaConverter.CONVERTER;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.AlarmaEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.SensorEntity;
import co.edu.uniandes.isis2503.nosqljpa.persistence.AlarmaPersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.SensorPersistence;
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

    public AlarmaLogic() {
        this.persistence = new AlarmaPersistence();
        this.perSe= new SensorPersistence();
    }

      
    @Override
    public AlarmaDTO add(AlarmaDTO dto, String idS) {
        if (dto.getId() == null) {
            dto.setId(UUID.randomUUID().toString());
        }
        AlarmaEntity x = CONVERTER.DtoToEntity(dto);
        x.setIdSensor(idS);
        AlarmaDTO result = CONVERTER.entityToDTO(persistence.add(x));
        SensorEntity h=perSe.find(idS);
        List<String> z=h.getAlarmas();
        z.add(dto.getMensaje());
        h.setAlarmas(z);
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

    
    public AlarmaDTO delete(String id) {
        AlarmaEntity a=persistence.find(id);
        a.setActivado(0);
        persistence.update(a);
        return CONVERTER.entityToDTO(a);
    }

    

    
}