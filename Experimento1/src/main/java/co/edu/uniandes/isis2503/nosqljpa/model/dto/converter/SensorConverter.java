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

import co.edu.uniandes.isis2503.nosqljpa.interfaces.ISensorConverter;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.SensorDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.SensorEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author loscache
 */
public class SensorConverter implements ISensorConverter {

    public static ISensorConverter CONVERTER = new SensorConverter();

    public SensorConverter() {
    }

    @Override
    public SensorDTO entityToDto(SensorEntity entity) {
        SensorDTO dto = new SensorDTO();
        dto.setActivado(1);
        dto.setAlarmas(entity.getAlarmas());
        dto.setId(entity.getId());
        dto.setMaxIntentosFallidos(entity.getMaxIntentosFallidos());
        dto.setIdInmueble(entity.getIdInmueble());
        return dto;
    }

    @Override
    public SensorEntity dtoToEntity(SensorDTO dto) {
        SensorEntity entity = new SensorEntity();
        entity.setId(dto.getId());
        entity.setAlarmas(dto.getAlarmas());
        entity.setMaxIntentosFallidos(dto.getMaxIntentosFallidos());
        entity.setIdInmueble(dto.getIdInmueble());
        return entity;
    }

    @Override
    public List<SensorDTO> listEntitiesToListDTOs(List<SensorEntity> entities) {
        ArrayList<SensorDTO> dtos = new ArrayList<>();
        for (SensorEntity entity : entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }

    @Override
    public List<SensorEntity> listDTOsToListEntities(List<SensorDTO> dtos) {
        ArrayList<SensorEntity> entities = new ArrayList<>();
        for (SensorDTO dto : dtos) {
            entities.add(dtoToEntity(dto));
        }
        return entities;
    }
}
