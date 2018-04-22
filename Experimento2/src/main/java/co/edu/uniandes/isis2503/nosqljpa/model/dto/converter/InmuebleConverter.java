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
package co.edu.uniandes.isis2503.nosqljpa.model.dto.converter;

import co.edu.uniandes.isis2503.nosqljpa.interfaces.IInmuebleConverter;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.InmuebleDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.InmuebleEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author loscache
 */
public class InmuebleConverter implements IInmuebleConverter{
    
        public static IInmuebleConverter CONVERTER = new InmuebleConverter();



    public InmuebleConverter() {
    }

    public InmuebleDTO entityToDto(InmuebleEntity entity) {
        InmuebleDTO dto = new InmuebleDTO();
        dto.setId(entity.getId());
        dto.setPropietario(entity.getPropietario());
        dto.setSensores(entity.getSensores());
        dto.setIdUnidad(entity.getIdUnidad());
        return dto;
    }

    public InmuebleEntity dtoToEntity(InmuebleDTO dto) {
        InmuebleEntity entity = new InmuebleEntity();
        entity.setId(dto.getId());
        entity.setPropietario(dto.getPropietario());
        entity.setSensores(dto.getSensores());
        entity.setIdUnidad(dto.getIdUnidad());
        return entity;
    }

    
    public List<InmuebleDTO> listEntitiesToListDTOs(List<InmuebleEntity> entities) {
        ArrayList<InmuebleDTO> dtos = new ArrayList<>();
        for (InmuebleEntity entity : entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }

    public List<InmuebleEntity> listDTOsToListEntities(List<InmuebleDTO> dtos) {
        ArrayList<InmuebleEntity> entities = new ArrayList<>();
        for (InmuebleDTO dto : dtos) {
            entities.add(dtoToEntity(dto));
        }
        return entities;
    }
}
