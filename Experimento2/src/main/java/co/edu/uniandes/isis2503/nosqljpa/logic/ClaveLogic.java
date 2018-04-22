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
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IClaveLogic;
import static co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.ClaveConverter.CONVERTER;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ClaveDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.ClaveEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.SensorEntity;
import co.edu.uniandes.isis2503.nosqljpa.persistence.ClavePersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.SensorPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author loscache
 */
public class ClaveLogic implements IClaveLogic {

    private final ClavePersistence persistence;
    private final SensorPersistence perSe;

    public ClaveLogic() {
        this.persistence = new ClavePersistence();
        this.perSe= new SensorPersistence();
    }

      
    @Override
    public ClaveDTO add(ClaveDTO dto, String idS) {
        if (dto.getClave()== null) {
            throw new WebApplicationException("La clave no puede ser nula");
        }        
        ClaveEntity x = CONVERTER.DtoToEntity(dto);
        x.setIdSensor(idS);
        ClaveDTO result = CONVERTER.entityToDTO(persistence.add(x));        
        return result;
    }

    
    public ClaveDTO update(ClaveDTO dto) {
        ClaveDTO result = CONVERTER.entityToDTO(persistence.update(CONVERTER.DtoToEntity(dto)));
        return result;
    }
    
    
    public List<ClaveDTO> findBySensorId(String id) {
       List<ClaveEntity> x = persistence.all();
        List<ClaveEntity> z=new ArrayList();
        for(ClaveEntity e:x)
        {
            if(e.getIdSensor().equals(id))
            z.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(z);
    }

    
    public ClaveDTO find(String id) {
        ClaveDTO x= CONVERTER.entityToDTO(persistence.find(id));
        return x;
    }

    
    public List<ClaveDTO> all() {
        return CONVERTER.listEntitiesToListDTOs(persistence.all());
    }
    
    @Override
    public List<ClaveDTO> allDeUnSensor(String idS) {
        List<ClaveEntity> x = persistence.all();
        List<ClaveEntity> z=new ArrayList();
        for(ClaveEntity e:x)
        {
            if(e.getIdSensor().equals(idS))
            z.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(z);
    }
    
    public List<ClaveDTO> allDeUnDueno(String due, String idS) {
        List<ClaveEntity> x = persistence.all();
        List<ClaveEntity> z=new ArrayList();
        for(ClaveEntity e:x)
        {
            if(e.getDueno().equals(due)&&e.getIdSensor().equals(idS))
            z.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(z);
    }

    
    public ClaveDTO delete(String id) {
        ClaveEntity a=persistence.find(id);
        a.setActivado(0);
        persistence.update(a);
        return CONVERTER.entityToDTO(a);
    }

    
}