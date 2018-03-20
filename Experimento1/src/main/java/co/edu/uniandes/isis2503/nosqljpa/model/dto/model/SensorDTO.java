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
package co.edu.uniandes.isis2503.nosqljpa.model.dto.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ca.mendoza968
 */
@XmlRootElement
public class SensorDTO {
    private String id;

    private int maxIntentosFallidos;
    
    private int activado;
    
        private List<String> alarmas;
        
        private String idInmueble;


    public SensorDTO() {
        alarmas = new ArrayList();
        activado=1;
    }

    public SensorDTO(String id, int m, List<String> a, String idI) {
        this.id = id;
        maxIntentosFallidos=m;
        alarmas = a;
        activado=1;
        idInmueble=idI;
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

    public int getActivado() {
        return activado;
    }

    public void setActivado(int activado) {
        this.activado = activado;
    }

    public List<String> getAlarmas() {
        return alarmas;
    }

    public void setAlarmas(List<String> alarmas) {
        this.alarmas = alarmas;
    }
    
    public void addAlarma(String a)
    {
        alarmas.add(a);
    }

    public String getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(String idInmueble) {
        this.idInmueble = idInmueble;
    }
    
    

   
}
