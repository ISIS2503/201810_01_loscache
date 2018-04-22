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

import static co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.HubConverter.CONVERTER;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.HubDTO;
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
public class HubLogic{
    
    private final HubPersistence persistence;
    private final UnidadResidencialPersistence perUn;

    public HubLogic() {
        this.persistence = new HubPersistence();
        this.perUn= new UnidadResidencialPersistence();
    }

    
    public HubDTO add(HubDTO dto) {
         if(dto.getId()==null){
            dto.setId(UUID.randomUUID().toString());
         }
        HubDTO result = CONVERTER.entityToDto(persistence.add(CONVERTER.dtoToEntity(dto)));
        return result;
    }

    
    public HubDTO update(HubDTO dto) {
        HubDTO result = CONVERTER.entityToDto(persistence.update(CONVERTER.dtoToEntity(dto)));
        return result;
    }

    
    public HubDTO find(String id) {
        HubDTO h=CONVERTER.entityToDto(persistence.find(id));
       
        return h;
    }
    
    
    public HubDTO findCode(String code) {
        return CONVERTER.entityToDto(persistence.findCode(code));
    }

    
    public List<HubDTO> all() {
        List<HubEntity> x= persistence.all();
        List<HubEntity> resp=new ArrayList();
        for(HubEntity e:x)
        {
            System.out.println(e.getId());
            if(e.getActivado()!=0)
                resp.add(e);
        }
        return CONVERTER.listEntitiesToListDTOs(resp);
    }

    
    public HubDTO delete(String id) {
        HubEntity a=persistence.find(id);
        System.err.println("ola");
        a.setActivado(0);
        persistence.update(a);
                System.err.println("ola2");

        return CONVERTER.entityToDto(a);
    }
}
