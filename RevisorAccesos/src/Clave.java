
public class Clave {
	
	 private String clave;
	    
	    private String dueno;
	    
	    private int activado;
	    
	    private String idSensor;
	    
	    public Clave()
	    {
	        activado=1;
	    }

	    public String getClave() {
	        return clave;
	    }

	    public void setClave(String clave) {
	        this.clave = clave;
	    }

	    public String getDueno() {
	        return dueno;
	    }

	    public void setDueno(String dueno) {
	        this.dueno = dueno;
	    }

	    public int getActivado() {
	        return activado;
	    }

	    public void setActivado(int activado) {
	        this.activado = activado;
	    }
	    
	    public String getIdSensor() {
	        return idSensor;
	    }

	    public void setIdSensor(String idSensor) {
	        this.idSensor = idSensor;
	    }

}
