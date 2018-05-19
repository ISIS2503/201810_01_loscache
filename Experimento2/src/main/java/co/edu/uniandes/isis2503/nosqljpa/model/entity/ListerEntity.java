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
@Table(name = "LISTER")
public class ListerEntity implements Serializable {

    @Id
    private String id;

    @ElementCollection
    private List<String> alarmas;
    
    @ElementCollection
    private List<String> sensores;
        
    @ElementCollection
    private List<String> inmuebles;
    
    @ElementCollection
    private List<String> horarios;
    
    @ElementCollection
    private List<String> hubs;
    
    @ElementCollection
    private List<String> claves;
    
    @ElementCollection
    private List<String> unidades;
    

    public ListerEntity() {
        id="Lister1";
        alarmas = new ArrayList();
        horarios=new ArrayList();
        sensores=new ArrayList();
        inmuebles=new ArrayList();
        hubs=new ArrayList();
        claves= new ArrayList();
        unidades=new ArrayList();
    }

    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAlarmas() {
        return alarmas;
    }

    public void setAlarmas(List<String> alarmas) {
        this.alarmas = alarmas;
    }

    public List<String> getSensores() {
        return sensores;
    }

    public void setSensores(List<String> sensores) {
        this.sensores = sensores;
    }

    public List<String> getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(List<String> inmuebles) {
        this.inmuebles = inmuebles;
    }

    public List<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<String> horarios) {
        this.horarios = horarios;
    }

    public List<String> getHubs() {
        return hubs;
    }

    public void setHubs(List<String> hubs) {
        this.hubs = hubs;
    }

    public List<String> getClaves() {
        return claves;
    }

    public void setClaves(List<String> claves) {
        this.claves = claves;
    }

    public List<String> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<String> unidades) {
        this.unidades = unidades;
    }
    
    

    
}
