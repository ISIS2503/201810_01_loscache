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

import co.edu.uniandes.isis2503.nosqljpa.interfaces.IUnidadResidencialLogic;
import static co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.UnidadResidencialConverter.CONVERTER;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.UnidadResidencialDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.HubEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.UnidadResidencialEntity;
import co.edu.uniandes.isis2503.nosqljpa.persistence.HubPersistence;
import co.edu.uniandes.isis2503.nosqljpa.persistence.UnidadResidencialPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author loscache
 */
public class UnidadResidencialLogic implements IUnidadResidencialLogic {

    private final UnidadResidencialPersistence persistence;
    private final HubPersistence persistenceHub;

    public UnidadResidencialLogic() {
        this.persistence = new UnidadResidencialPersistence();
        this.persistenceHub = new HubPersistence();
    }

    @Override
    public UnidadResidencialDTO add(UnidadResidencialDTO dto, String hubId) {
        if (dto.getId() == null) {
            dto.setId(UUID.randomUUID().toString());
        }
        UnidadResidencialEntity x = CONVERTER.dtoToEntity(dto);
        x.setIdHub(hubId);
        UnidadResidencialDTO result = CONVERTER.entityToDto(persistence.add(x));
        HubEntity h=persistenceHub.find(hubId);
        List<String> z=h.getUnidadesResidenciales();
        z.add(dto.getName());
        h.setUnidadesResidenciales(z);
        persistenceHub.update(h);
        
        return result;
    }

    @Override
    public UnidadResidencialDTO update(UnidadResidencialDTO dto) {
        UnidadResidencialDTO result = CONVERTER.entityToDto(persistence.update(CONVERTER.dtoToEntity(dto)));
        return result;
    }

    @Override
    public UnidadResidencialDTO find(String id) {
        return CONVERTER.entityToDto(persistence.find(id));
    }

    @Override
    public List<UnidadResidencialDTO> all() {
        return CONVERTER.listEntitiesToListDTOs(persistence.all());
    }

    @Override
    public UnidadResidencialDTO delete(String id) {
         UnidadResidencialEntity a=persistence.find(id);
        a.setActivado(0);
        persistence.update(a);
        return CONVERTER.entityToDto(a);
    }

    @Override
    public List<UnidadResidencialDTO> allDeUnHub(String idHub) {
        List<UnidadResidencialEntity> x = persistence.all();
        List<UnidadResidencialEntity> z=new ArrayList();
        for(UnidadResidencialEntity e:x)
        {
            if(e.getIdHub().equals(idHub))
            z.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(z);
    }

    
}
