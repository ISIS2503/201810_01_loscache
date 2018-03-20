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

import co.edu.uniandes.isis2503.nosqljpa.interfaces.IInmuebleLogic;
import static co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.InmuebleConverter.CONVERTER;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.InmuebleDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.InmuebleEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.UnidadResidencialEntity;
import co.edu.uniandes.isis2503.nosqljpa.persistence.InmueblePersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.UnidadResidencialPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author ca.mendoza968
 */
public class InmuebleLogic implements IInmuebleLogic {

    private final InmueblePersistence persistence;
        private final UnidadResidencialPersistence perUn;


    public InmuebleLogic() {
        this.persistence = new InmueblePersistence();
        this.perUn = new UnidadResidencialPersistence();
    }

    @Override
    public InmuebleDTO add(InmuebleDTO dto, String idU) {
        if (dto.getId() == null) {
            dto.setId(UUID.randomUUID().toString());
        }
        InmuebleEntity x = CONVERTER.dtoToEntity(dto);
        x.setIdUnidad(idU);
        InmuebleDTO result = CONVERTER.entityToDto(persistence.add(x));
        UnidadResidencialEntity h=perUn.find(idU);
        List<String> z=h.getInmuebles();
        z.add(dto.getId());
        h.setInmuebles(z);
        perUn.update(h);
        return result;
    }

    @Override
    public InmuebleDTO update(InmuebleDTO dto) {
        InmuebleDTO result = CONVERTER.entityToDto(persistence.update(CONVERTER.dtoToEntity(dto)));
        return result;
    }

    @Override
    public InmuebleDTO find(String id) {
        return CONVERTER.entityToDto(persistence.find(id));
    }

    

    @Override
    public List<InmuebleDTO> all() {
        return CONVERTER.listEntitiesToListDTOs(persistence.all());
    }

    @Override
    public InmuebleDTO delete(String id) {
         InmuebleEntity a=persistence.find(id);
        a.setActivado(0);
        persistence.update(a);
        return CONVERTER.entityToDto(a);
    }

    @Override
    public List<InmuebleDTO> findDeUnidad(String idU) {
         List<InmuebleEntity> x = persistence.all();
        List<InmuebleEntity> z=new ArrayList();
        for(InmuebleEntity e:x)
        {
            if(e.getIdUnidad().equals(idU))
            z.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(z);

    }
}
