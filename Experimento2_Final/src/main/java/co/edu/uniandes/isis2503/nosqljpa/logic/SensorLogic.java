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

import co.edu.uniandes.isis2503.nosqljpa.interfaces.ISensorLogic;
import static co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.SensorConverter.CONVERTER;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.SensorDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.InmuebleEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.ListerEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.SensorEntity;
import co.edu.uniandes.isis2503.nosqljpa.persistence.InmueblePersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.ListerPersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.SensorPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author loscache
 */
public class SensorLogic implements ISensorLogic {

    private final SensorPersistence persistence;
        private final InmueblePersistence perIn;
            private final ListerPersistence perLis;



    public SensorLogic() {
        this.persistence = new SensorPersistence();
    this.perIn=new InmueblePersistence();
    this.perLis= new ListerPersistence();
    }
    
    @Override
    public SensorDTO add(SensorDTO dto, String idIn) {
       if (dto.getId() == null) {
            dto.setId(UUID.randomUUID().toString());
        }
        SensorEntity x = CONVERTER.dtoToEntity(dto);
        x.setIdInmueble(idIn);
        addToLister(x);
        SensorDTO result = CONVERTER.entityToDto(persistence.add(x));
        InmuebleEntity h=perIn.find(idIn);
        List<String> z=h.getSensores();
        z.add(dto.getId());
        h.setSensores(z);
        perIn.update(h);
        return result;
    }

        @Override
    public SensorDTO update(SensorDTO dto) {
        SensorDTO result = CONVERTER.entityToDto(persistence.update(CONVERTER.dtoToEntity(dto)));
        return result;
    }

    @Override
    public SensorDTO find(String id) {
       SensorDTO x=CONVERTER.entityToDto(persistence.find(id));
       if(x!=null)
       {
           if(x.getActivado()==0)
               return null;
           else
               return x;
       }
       return x;
    }
    
  

    @Override
    public List<SensorDTO> all() {
        return CONVERTER.listEntitiesToListDTOs(persistence.all());
    }

    @Override
    public SensorDTO delete(String id) {
         SensorEntity a=persistence.find(id);
        a.setActivado(0);
        persistence.update(a);
        return CONVERTER.entityToDto(a);
    }    

    @Override
    public List<SensorDTO> findDeInmueble(String idIn) {
        List<SensorEntity> x = persistence.all();
        List<SensorEntity> z=new ArrayList();
        for(SensorEntity e:x)
        {
            if(e.getIdInmueble().equals(idIn))
            z.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(z);
    }
    
    private void addToLister(SensorEntity x) {
        if (perLis.all().isEmpty()) {
            perLis.add(new ListerEntity());
        }

        List<ListerEntity> le = perLis.all();
        if(le.isEmpty())
            le.add(new ListerEntity());
        ListerEntity fir = le.get(0);
        List<String> li = fir.getSensores();
        li.add(x.getId());
        fir.setSensores(li);
        perLis.update(fir);
    }
}
