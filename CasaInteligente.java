
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import java.io.Serializable;

public class CasaInteligente implements Serializable
{
    private long nifP; // nif do proprietário da casa
    
    private String nomeP; // nome do proprietário
    
    private Map<Integer, SmartDevice> devices; 
    // identificador do SmartDevice -> SmartDevice
    
    private Map<String, List<Integer>> locations; 
    // nome da divisão da casa -> Lista de SmartDevices que estão na divisão
    
    private Comercializadores energia; // Comercializador da casa
    
    /**
     * Construtores para objetos da classe CasaInteligente
     */
    public CasaInteligente(){
        this.nifP = 0;
        this.nomeP = "";
        this.devices = new HashMap();
        this.locations = new HashMap();
    }
    
    public CasaInteligente(long nifP, String nomeP, Comercializadores energia){
        this.nifP = nifP;
        this.nomeP = nomeP;
        this.devices = new HashMap();
        this.locations = new HashMap();
        this.energia = energia.clone();
    }
    
    public CasaInteligente(CasaInteligente c){
        this.nifP = c.getNifP();
        this.nomeP = c.getNomeP();
        this.devices = c.getDevices();
        this.locations = c.getLocations();
        this.energia = c.getEnergia();
    }
    
    /**
     * ------------------------------------------------------------------------------------
     * Métodos get da classe CasaInteligente
     * ------------------------------------------------------------------------------------
     */
    
    public long getNifP(){
        return this.nifP;
    }
    
    public String getNomeP(){
        return this.nomeP;
    }
    
    public Comercializadores getEnergia(){
        return this.energia.clone();
    }
    
    public SmartDevice getDevice(int s) {
        return this.devices.get(s);
    }
    
    public List<Integer> getLocationDevices(String s){
        return locations.get(s);
    }
    
    public Map getDevices(){
        return this.devices;
    }
    
    public Map getLocations(){
        return this.locations;
    }
    
    
    /**
     * ------------------------------------------------------------------------------------
     * Métodos set da classe CasaInteligente
     * ------------------------------------------------------------------------------------
     */
    
    public void setNifP(long nifP){
        this.nifP = nifP;
    }
    
    public void setNomeP(String nomeP){
        this.nomeP = nomeP;
    }
    
    public void setEnergia(Comercializadores energia){
        this.energia = energia.clone();
    }
    
    
    /**
     * ------------------------------------------------------------------------------------ 
     * Métodos que adicionam valores aos respetivos maps
     * ------------------------------------------------------------------------------------
     */
    
    public void addDevice(SmartDevice d){
        this.devices.put(d.getId(),d.clone()); 
    }
    
    public void addLocation(String l) {
        this.locations.put(l, new ArrayList());
    }
    
    public void addToLocation(String l, int id) {
        this.locations.get(l).add(id);
    }
    //--------------------------------------------------------------------------------------
    
    
    /**
     * Metodo que verifica se uma casa tem s localização
     * @param s - nome da localização a verificar
     * @return true caso exista essa localização na casa this | false caso nao exista
     */
    public boolean hasLocation(String s) {
        if (this.locations.get(s) == null) return false;
        return true;
    }
    
    public boolean hasDevice(int sd) {
        if (this.devices.get(sd) == null) return false;
        return true;
    }
    
    /**
     * Metodo que verifica se uma localização da casa tem um certo SmartDevice
     * @param s1 - Id do SmartDevice que se procura
     * @param s2 - Divisão em que se está a procurar o SmartDevice
     * @return true caso exista | false caso nao exista
     */
    public boolean LocationHasDevice (int s1, String s2) {
        boolean encontrado = false;
        List<Integer> al = this.locations.get(s1);
        Iterator<Integer> it = al.iterator();
        while(it.hasNext() && encontrado == false){
            if (s2.equals(it.next())) encontrado = true;
        }
        return encontrado;
    }
    
    public void addAllX(boolean b,Faturador f) {
        Set keySet = this.devices.keySet();
        Iterator<Integer> it = keySet.iterator();
        while(it.hasNext()){
            f.addOF(devices.get(it.next()),b);
        }
    }
    
    public void addAllXLocation(String sala, boolean b, Faturador f){
        Set keySet = this.locations.keySet();
        Iterator<Integer> it = locations.get(sala).iterator();
        while(it.hasNext()){
            f.addOF(devices.get(it.next()),b);
        }
    }
    
    /**
     * @return valor monetario que a casa consome diariamente
     */
    public double ConsumoTotalDiario(){
        return this.devices.entrySet()
                           .stream()
                           .map(Map.Entry::getValue)
                           .mapToDouble(SmartDevice::ConsumoDiario)
                           .sum();
    }
    
    /**
     * @return uma copia da CasaInteligente this
     */
    public CasaInteligente clone(){
        return new CasaInteligente(this);
    }
    
    /**
     * 
     * Método que coloca numa string os valores que caracterizam uma CasaInteligente
     * @return String construida a partir das informações da CasaInteligente
     *
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Proprietário: ").append(this.nomeP).append("\nNif do proprietário: ").append(this.nifP + "\n")
        .append("Fornecedor de energia: ").append(this.energia.getCom1() + "\n");
        sb.append("------------------------------\n");
        for(Map.Entry<String, List<Integer>> entry : locations.entrySet()){
            sb.append(entry.getKey()).append(": \n");
            Iterator it = entry.getValue().iterator();
            while(it.hasNext()){
                SmartDevice sd = this.devices.get(it.next());
                sb.append("\t").append(sd.stringSD()).append("\n");
            }
        }
        return sb.toString();
    }
    
    /**
     * Método que cria o menu de edição dos SmartDevices
     */
    public Menu newmenuSD(int id, Faturador a){
        return this.devices.get(id).newmenuSD(a);
    }
    
}
