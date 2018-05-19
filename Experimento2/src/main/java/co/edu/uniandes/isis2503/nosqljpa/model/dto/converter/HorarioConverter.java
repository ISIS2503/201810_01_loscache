/*
 * The MIT License
 *
 * Copyright 2018 Universidad De Los Andes - Departamento de Ingeniería de Sistemas.
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

import co.edu.uniandes.isis2503.nosqljpa.interfaces.IHorarioConverter;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.HorarioDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.HorarioEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author loscache
 */
public class HorarioConverter implements IHorarioConverter {
    
        public static IHorarioConverter CONVERTER = new HorarioConverter();

    
    public HorarioConverter(){}
    
    public HorarioDTO entityToDTO(HorarioEntity a)
    {
        HorarioDTO r=new HorarioDTO();
        r.setId(a.getId());
        r.setActivado(a.getActivado());
        r.setHoraFinal(a.getHoraFinal());
        r.setHoraInicial(a.getHoraInicial());
        r.setDueno(a.getDueno());
        r.setIdSensor(a.getIdSensor());
        return r;
    }
    
    public HorarioEntity DtoToEntity(HorarioDTO a)
    {
        HorarioEntity r=new HorarioEntity();
        r.setId(a.getId());
        r.setActivado(a.getActivado());
        r.setHoraFinal(a.getHoraFinal());
        r.setHoraInicial(a.getHoraInicial());
        r.setDueno(a.getDueno());
        r.setIdSensor(a.getIdSensor());
        return r;
    }
    
    public List<HorarioDTO> listEntitiesToListDTOs(List<HorarioEntity> entities) {
        ArrayList<HorarioDTO> dtos = new ArrayList<>();
        for (HorarioEntity entity : entities) {
            dtos.add(entityToDTO(entity));
        }
        return dtos;
    }

    
    public List<HorarioEntity> listDTOsToListEntities(List<HorarioDTO> dtos) {
        ArrayList<HorarioEntity> entities = new ArrayList<>();
        for (HorarioDTO dto : dtos) {
            entities.add(DtoToEntity(dto));
        }
        return entities;
    }

    
    
}
