import java.util.Scanner;
import java.io.Serializable;

public class SmartSpeaker extends SmartDevice implements Serializable
{
    private int volume; // Volume do Audio da SmartSpeaker
    
    private String marca; // Marca fabricante da SmartSpeaker
    
    private String radio; // radio transmitida pela SmartSpeker
    
    private double valorB; 
    // Valor base que servirá para o cálculo do consumo diário.
    
    
    /**
     * Construtores de objetos de classe SmartSpeaker
     */
    
    public SmartSpeaker(){
        super();
        this.volume = 0;
        this.marca = "";
        this.radio = "";
    }
    
    public SmartSpeaker(int volume, String radio ,String marca, double valorB){
        super();
        this.volume = volume;
        this.marca = marca;
        this.radio =  radio;
        this.valorB = valorB;
    }
    
    public SmartSpeaker(String marca, String radio, double valorB){
        super();
        this.volume = 50;
        this.marca = marca;
        this.radio =  radio;
        this.valorB = valorB;
    }
    
    public SmartSpeaker(int id, boolean on, int volume, String marca, String radio, double valorB){
        super(id,on);
        this.volume = volume;
        this.marca = marca;
        this.radio =  radio;
        this.valorB = valorB;
    }
    
    /*
     * Métodos get da classe SmartSpeaker
     */
    
    /**
     * Método que devolve o valor da variável de instância volume
     * @return valor em volume
     */
    public int getVolume(){
        return this.volume;
    }
    
    /**
     * Método que devolve o valor da variável de instância marca
     * @return valor em marca
     */
    public String getMarca(){
        return this.marca;
    }
    
    /**
     * Método que devolve o valor da variável de instância radio
     * @return valor em radio
     */
    public String getRadio(){
        return this.radio;
    }
    
    
    /**
     * Metodo que devolve o valor da variavel de instancia do valor base da SmartSpeaker
     * @return valor da variavel de instancia valorB
     */
    public double getValorB(){
        return this.valorB;
    }
    
    /*
     * Métodos set da classe SmartSpeaker
     */
    
    /**
     * Método que muda o valor na variável de instância volume
     * @param Volume do SmartSpeaker (limite : 0<=volume<=100)
     */
    public void setVolume(int volume){
        if (volume > 100) this.volume = 100;
        else if (volume < 0) this.volume = 0;
        else this.volume = volume;
    }
    
    /**
     * Método que muda o valor na variável de instância marca
     * @param Marca fabricante do SmartSpeaker
     */
    public void setMarca(String marca){
        this.marca = marca;
    }
    
    /**
     * Método que muda o valor na variável de instância radio
     * @param radio que o SmartSpeaker transmite
     */    
    public void setRadio(String radio){
        this.radio = radio;
    }
    
    /**
     * Método que muda o valor na variável de instância valorB
     * @param Valor base a mudar
     */
    public void setValorB(double d){
        this.valorB = d;
    }
    
    /**
     * Método que testa se dois objetos são o mesmo SmartSpeaker
     * @param o parámetro que se vai comparar com o this
     * @return true caso os objetos sejam iguais e false caso sejam diferentes
     */
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        SmartSpeaker e = (SmartSpeaker) o;
        return (e.volume == this.volume) && (e.marca == this.marca) && (e.radio == this.radio);
    }
    
    /**
     * Método que faz clonagem a um SmartSpeaker
     * @return uma cópia da SmartSpeaker this
     */
    public SmartSpeaker clone(){
        return new SmartSpeaker(this.getId(),this.getOn(),this.volume,this.marca,this.radio,this.valorB);
    }
    
    
    /**
     * Método que calcula o consumo de energia diário da SmartSpeaker
     * @return 0 se o SmartSpeaker estiver desligado | consumo diário se o SmartSpeaker estiver ligado
     */
    public double ConsumoDiario(){
        if (this.getOn()){
            return valorB + 0.05*this.volume;
        } 
        return 0;
    }
    
    /**
     * Método que coloca numa string os valores que caracterizam um SmartSpeaker
     * @return String construida a partir das informações do SmartSpeaker
     */
    
    public String stringSD(){
        StringBuilder sb = new StringBuilder();
        sb.append("SmartSpeaker ||").append(this.getId()).append("||: \n\t")
                                                      .append("ON: ")
                                                      .append(this.getOn())
                                                      .append(", Volume: ")
                                                      .append(this.volume)
                                                      .append(", Marca: ")
                                                      .append(this.marca)
                                                      .append(", Radio: ")
                                                      .append(this.radio);
        return sb.toString();
    }
    
    /**
     * @return Scanner que nos possibilita utilizar o terminal para obter os resultados
     */
    
    public Scanner gvInput(){
        Scanner is = new Scanner(System.in);
        return is;
    }
    
    /**
     * Método que cria uma menu para SmartSpeakers
     * @returns menu criado
     */
    
    public Menu newmenuSD(Faturador a){
        Menu menu = new Menu(new String[]{"On/Off","Mudar Volume",
                                  "Mudar Rádio"});
        menu.setHandler(1,()->onoff(a));
        menu.setHandler(2,()->mudarVolume());
        menu.setHandler(3,()->mudarRadio());
        return menu;
    }
    
    /**
     * Metodo que proporciona ao utilizador mudar a estaçao de rádio
     */
    
    public void mudarRadio(){
        System.out.println("Novo rádio: ");
        String radio = gvInput().nextLine();
        this.setRadio(radio);
    }
    
    /**
     * Metodo que proporciona ao utilizador mudar o volume do SmartSpeaker
     */
    
    public void mudarVolume(){
        System.out.println("Novo volume: ");
        int volume = gvInput().nextInt();
        this.setVolume(volume);
    }
    
    /**
     * Metodo que liga ou desliga o SmartSpeaker de acordo com o seu estado atual
     */
    public void onoff(Faturador a){
        if (this.getOn()) a.addOF(this,false);
        else a.addOF(this,true);
    }
}
