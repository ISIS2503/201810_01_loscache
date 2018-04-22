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
package co.edu.uniandes.isis2503.nosqljpa.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author loscache
 */
@Entity
@Table(name = "SENSOR")
public class SensorEntity implements Serializable {

    @Id
    private String id;

    private int maxIntentosFallidos;
    
    private int activado;

    @ElementCollection
    private List<String> alarmas;
    
    @ElementCollection
    private List<String> horarios;
    
    private String idInmueble;
    

    public SensorEntity() {
        alarmas = new ArrayList();
        horarios=new ArrayList();
        activado=1;
    }

    public SensorEntity(String id, int max, List<String> al, String idI) {
        this.id = id;
        this.maxIntentosFallidos = max;
        this.alarmas = al;
        this.idInmueble=idI;
        activado=1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxIntentosFallidos() {
        return maxIntentosFallidos;
    }

    public void setMaxIntentosFallidos(int maxIntentosFallidos) {
        this.maxIntentosFallidos = maxIntentosFallidos;
    }

    public List<String> getAlarmas() {
        return alarmas;
    }

    public void setAlarmas(List<String> alarmas) {
        this.alarmas = alarmas;
    }
    
    public int getActivado() {
        return activado;
    }

    public void setActivado(int activado) {
        this.activado = activado;
    }

    public String getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(String idInmueble) {
        this.idInmueble = idInmueble;
    }

    public List<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<String> horarios) {
        this.horarios = horarios;
    }

    
       
}
