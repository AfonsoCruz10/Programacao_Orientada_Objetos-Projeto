import java.io.Serializable;
import java.util.Random;

public abstract class SmartDevice implements Serializable
{
    private int id; // identificador do SmartDevice
    private boolean on; // estado do SmartDevice 
    // false - desligado | true - ligado
    
     /**
     * Constructor para objetos de classe SmartDevice
     */
    public SmartDevice() {
        this.id = gerador();
        this.on = false;
    }

    public SmartDevice(boolean b) {
        this.id = gerador();
        this.on = b;
    }
    
    public SmartDevice(int id,boolean b) {
        this.id = id;
        this.on = b;
    }
    
    /**
     * Métodos set da classe SmartDevice
     */
    
    public void setX(boolean b){
        this.on = b;
    }
    
    public void setOn() {
        this.on = true;
    }
    
    public void setOff() {
        this.on = false;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    /**
     * Métodos get da classe SmartDevice
     */
    
    public boolean getOn() {
        return this.on;
    }
    
    public int getId() {
        return this.id;
    }
    
    /**
     * Método equals da classe SmartDevice
     */
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        SmartDevice e = (SmartDevice) o;
        return (e.id == this.id) && (e.on == this.on);
    }
    /**
     * Metodo que gera numero aleatorios
     * @return id de SmartDevice
     */
    public int gerador(){
        Random ran = new Random();
        return ran.nextInt(9999);
    }
    
    public abstract SmartDevice clone();
    public abstract double ConsumoDiario();
    public abstract String stringSD();
    public abstract Menu newmenuSD(Faturador a);
}
