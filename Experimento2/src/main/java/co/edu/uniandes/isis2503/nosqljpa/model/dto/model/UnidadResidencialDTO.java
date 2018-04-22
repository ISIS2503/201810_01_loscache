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
package co.edu.uniandes.isis2503.nosqljpa.model.dto.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author loscache
 */
@XmlRootElement
public class UnidadResidencialDTO {
    
    private String id;
    
    private String name;
    
    private String direccion;
    
    private int activado;

    private List<String> inmuebles;
    
    private String idHub;
    
    public UnidadResidencialDTO() {
        this.inmuebles = new ArrayList();
        this.activado = 1;
    }

    public UnidadResidencialDTO(String id, String name, String code, List<String> rooms, String idH) {
        this.id = id;
        this.name = name;
        this.direccion = code;
        this.inmuebles = rooms;
        this.activado = 1;
        this.idHub=idH;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getActivado() {
        return activado;
    }

    public void setActivado(int activado) {
        this.activado = activado;
    }

    public List<String> getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(List<String> inmuebles) {
        this.inmuebles = inmuebles;
    }
    
    public void addInmueble(String in)
    {
        this.inmuebles.add(in);
    }
    
    public String getIdHub() {
        return idHub;
    }

    public void setIdHub(String idHub) {
        this.idHub = idHub;
    }
}
