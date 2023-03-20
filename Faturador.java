import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Iterator;

public class Faturador implements Serializable
{
    private Map<Long,CasaInteligente> casas; // Nif do dono da casa -> casa
    private Map<CasaInteligente,Double> fatura; // Casa -> valor da fatura da casa
    private Map<String,CasaInteligente> mudancaPendente; // Nome do Comercializador a mudar -> Casa
    private Map<SmartDevice, Boolean> ligardesligar; // SmartDevice a alterar -> true - ligado | false - desligado
    private Map<String,Comercializadores> comercializadores; // nome do comercializador -> dados do comercializador
    
    /**
     * Construtor Vazio de Faturador
     */
    public Faturador(){
        this.casas = new HashMap<>();
        this.fatura = new HashMap<>();
        this.mudancaPendente = new HashMap<>();
        this.comercializadores = new HashMap<>();
        this.ligardesligar = new HashMap<>();
    }
    
    /**
     * Gets
     */
    public Comercializadores getCom(String s){
        return comercializadores.get(s);
    }
    
    public CasaInteligente getCasa(long nif){
        return casas.get(nif);
    }
    
    public double getFatura(CasaInteligente ci){
        return fatura.get(ci);
    }
    
    public Map<Long,CasaInteligente> getCasas(){
        return this.casas;
    }
    
    public Map<String,CasaInteligente> getMudanca(){
        return this.mudancaPendente;
    }
    
    //--------------------------------------------------
    
    /**
     * Metodo que adiciona um SmartDevice(ligado ou desligado) a um mapa de SmartDevices
     * @param sd - SmartDevice
     * @param b - ligado ou desligado
     */
    public void addOF(SmartDevice sd, boolean b){
        if (sd != null) ligardesligar.put(sd,b);
        else throw new NullPointerException();
    }
    
    /**
     * Metodo que adiciona uma casa como value, com o respetivo nif como key
     */
    public void addCasa(CasaInteligente c){
        casas.put(c.getNifP(),c);
    }
    
    /**
     * Metodo que adiciona o nome do comercializador como value, com a respetiva casa como key
     */
    public void addMudanca(String s, CasaInteligente ci){
        mudancaPendente.put(s,ci);
    }
    
    /**
     *  Metodo que adiciona o nome do comercializador como value, com os respetivos dados como key
     */
    public void addCom(String s, Comercializadores com){
        comercializadores.put(s,com);
    }
    
    /**
     * Metodo que calcula fatura entre duas datas, avançando no tempo. 
     * Após o avanço do tempo, possivelmente fazer uma mudança de um comercializador de uma casa 
     * e/ou alterar o estado de SmartDevices
     * @param dataI - data inicial
     * @param dataD - data final
     */
    
    public void calcularfatura(String dataI,String dataD){
        long diasB = ChronoUnit.DAYS.between(LocalDate.parse(dataI),LocalDate.parse(dataD));
        for (Map.Entry<Long,CasaInteligente> entry : casas.entrySet()){
            double preco = 0;
            for (Map.Entry<String,SmartDevice> sd : ((Map<String, SmartDevice>) entry.getValue().getDevices()).entrySet()){
                preco = preco + entry.getValue().getEnergia().getFuncao().apply(sd.getValue(),entry.getValue());
            }
            fatura.put(entry.getValue(),preco * diasB);
            Comercializadores com = comercializadores.get(entry.getValue().getEnergia().getCom1());
            double bfr = com.getTotalFaturado();
            com.setTotalFaturado(bfr + (preco*diasB));
            if ((preco * diasB) > 0) com.addFat(dataI + "->" + dataD + ": " + entry.getValue().getNifP(),preco*diasB);
        }
        if (mudancaPendente.size() > 0){
            for (Map.Entry<String,CasaInteligente> listaespera : mudancaPendente.entrySet()){
                listaespera.getValue().setEnergia(comercializadores.get(listaespera.getKey()));
            }
        }
        if (ligardesligar.size() > 0){
            for (Map.Entry<SmartDevice,Boolean> listaespera : ligardesligar.entrySet()){
                listaespera.getKey().setX(listaespera.getValue());
            }
        }
    
    }
    
    /**
     * Metodo que devolve uma StringBuilder com as 5 casas mais consumidoras de energia
     * @return StringBuild do top 5
     */
    public String simulacaotop5(){
        Comparator<CasaInteligente> c = (c1,c2) -> (int) (c2.ConsumoTotalDiario() - c1.ConsumoTotalDiario());
        List<CasaInteligente> cil = this.casas.values().stream().sorted(c).limit(5).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        Iterator i = cil.iterator();
        int count = 0;
        while(i.hasNext()){
            sb.append(count+1).append(" - ").append(cil.get(count).getNifP()).append(" com consumo de: ").append(cil.get(count).ConsumoTotalDiario()).append("\n");
            i.next();
            count++;
        }
        return sb.toString();
    }
    
    /**
     * Metodo que devolve uma StringBuilder com a casa mais faturadora
     * @return StringBuilder das informaçoes da casa mais faturadora e o valor faturado
     */
    public String maisFcasa(){
        Comparator<CasaInteligente> c = (c1,c2) -> (int) (getFatura(c2) - getFatura(c1));
        List<CasaInteligente> cil = this.casas.values().stream().sorted(c).collect(Collectors.toList());
        CasaInteligente m = cil.get(0);
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.##");
        sb.append("------------------------------").append("\nProprietário: ").append(m.getNomeP()).append("\nNif do proprietário: ").append(m.getNifP() + "\n")
        .append("Fornecedor de energia: ").append(m.getEnergia().getCom1() + "\n");
        sb.append("------------------------------\n").append("Valor: ").append(df.format(getFatura(m))).append("€");
        return sb.toString();
    }
    
    /**
     * Metodo que devolve uma StringBuilder com o comercializador mais faturador
     * @return StringBuilder do comercializador mais faturador e o valor faturado
     */
    public String maisFfornecedor(){
        Comparator<Comercializadores> c = (c1,c2) -> (int) (c2.getTotalFaturado() - c1.getTotalFaturado());
        List<Comercializadores> cl = this.comercializadores.values().stream().sorted(c).collect(Collectors.toList());
        Comercializadores m = cl.get(0);
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------").append("\nFornecedor: ").append(m.getCom1())
          .append("\nValor: ").append(df.format(m.getTotalFaturado())).append("€");
        return sb.toString();
    }
    
    /**
     * Metodo que grava em objeto tudo o que é calculado pela consola
     * @param ficheiro onde é gravado
     */
    public void grava(String fn) throws FileNotFoundException, IOException{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fn));
        oos.writeObject(this);
        oos.close();
    }
    
    /**
     * Metodo que le aquilo que está escrito no ficheiro fn
     */
    public static Faturador le(String fn) throws FileNotFoundException, IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fn));
        Faturador a = (Faturador) ois.readObject();
        ois.close();
        return a;
    }
    
    /**
     * Metodo que verifica se existe fatura ou não
     */
    public boolean checkfatura(){
        return fatura.isEmpty();
    }
    
    /**
     * Metodo que transforma a fatura para cada casa num StringBuilder para ser escrita na consola
     */
    public String faturaString(){
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder sb = new StringBuilder();
        sb.append("--FATURAS--\n");
        for (Map.Entry<CasaInteligente,Double> casa : fatura.entrySet()){
            sb.append("Proprietário: ").append(casa.getKey().getNomeP()).append("\nNif do proprietário: ").append(casa.getKey().getNifP() + "\n");
            sb.append("Valor: ").append(df.format(casa.getValue())).append("€");
            sb.append("\n-----------\n");
        }
        return sb.toString();
    }
    
    /**
     * Metodo que transforma uma fatura de uma casa ci, num StringBuilder para ser escrita na consola
     */
    public String singlefaturaString(CasaInteligente ci){
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder sb = new StringBuilder();
        sb.append("--FATURAS--\n");
        sb.append("Proprietário: ").append(ci.getNomeP()).append("\nNif do proprietário: ").append(ci.getNifP() + "\n");
        sb.append("Valor: ").append(df.format(fatura.get(ci))).append("€");
        sb.append("\n-----------\n");
        return sb.toString();
    }
    
    /**
     * Metodo que transforma nifs das casas num StringBuilder para ser escrita na consola
     */
    public String nifstoString(){
       StringBuilder sb = new StringBuilder();
       for (Map.Entry<Long,CasaInteligente> casa : casas.entrySet()){
           sb.append("->").append(casa.getKey()).append("\n");;
        }
       return sb.toString();
    }
    
    /**
     * Metodo que devolve a fatura de um determinado comercializador em StringBuilder
     */
    public String ffornecedorestoString(String com){
        Comercializadores forn = comercializadores.get(com);
        return forn.faturasforn();
    }
    /**
     * Metodo que devolve os comercializadores em StringBuilder
     */
    public String forntoString(){
       StringBuilder sb = new StringBuilder();
       for (Map.Entry<String,Comercializadores> com : comercializadores.entrySet()){
           sb.append("->").append(com.getKey()).append("\n");;
        }
        return sb.toString();
    }
    
}
