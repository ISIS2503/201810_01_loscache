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
package co.edu.uniandes.isis2503.nosqljpa.model.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 *
 * @author loscache
 */
@Entity
@Table(name = "Hub")
public class HubEntity {
    
     @Id
    private String id;
     
    private int maxChecksPermitidos;


    @ElementCollection
    private List<String> unidadesResidenciales;
    
    private int activado;
    
    public HubEntity()
    {
        unidadesResidenciales=new ArrayList<>();
        activado=1;
    }
    
    public HubEntity(String i, List<String> ur, int m)
    {
        this.id=i;
        this.unidadesResidenciales=ur;
        this.maxChecksPermitidos=m;
        activado=1;
    }

    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getUnidadesResidenciales() {
        return unidadesResidenciales;
    }

    public void setUnidadesResidenciales(List<String> unidadesResidenciales) {
        this.unidadesResidenciales = unidadesResidenciales;
    }

    public int getActivado() {
        return activado;
    }

    public void setActivado(int activado) {
        this.activado = activado;
    }

    public int getMaxChecksPermitidos() {
        return maxChecksPermitidos;
    }

    public void setMaxChecksPermitidos(int maxChecksPermitidos) {
        this.maxChecksPermitidos = maxChecksPermitidos;
    }
    
    
    
    
}
