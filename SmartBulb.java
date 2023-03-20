import java.util.Scanner;
import java.io.Serializable;


public class SmartBulb extends SmartDevice implements Serializable
{
    public static final int Warm = 2;
    public static final int Neutral = 1; // diferentes tons da SmartBulb
    public static final int Cold = 0;
    
    private int tone; // tom da SmartBulb
    private double valorB; // Valor base que servirá para o cálculo do consumo diário.
    private double dimensao; // tamanho da SmartBulb
    
    /**
     * Constructores para objetos de classe SmartBulb
     */
    
    public SmartBulb() {
        super();
        this.dimensao = 5;
        this.tone = Neutral;
        this.valorB = 0.1;
    }

    public SmartBulb(int tone, double dimensao, double valorB) {
        super();
        this.dimensao = dimensao;
        this.tone = tone;
        this.valorB = valorB;
    }
    
    public SmartBulb(double dimensao, double valorB) {
        super();
        this.dimensao = dimensao;
        this.tone = Neutral;
        this.valorB = valorB;
    }
    
    public SmartBulb(boolean on, double dimensao, int tone, double valorB){
        super(on);
        this.dimensao = dimensao;
        this.tone = tone;
        this.valorB = valorB;
    }
    
    public SmartBulb(int id, boolean on, double dimensao, int tone, double valorB){
        super(id,on);
        this.dimensao = dimensao;
        this.tone = tone;
        this.valorB = valorB;
    }
    
    //--------------------------GETS     E      SETS--------------------------------------
    
    
    /**
     * Método que devolve o valor na variável de instância tone
     * @return o valor da variavel de instancia tone
     */
    
    public int getTone(){
        return this.tone;
    }
    
    /**
     * Método que devolve o valor na variável de instância da dimensao
     * @return valor da variavel de instancia dimensao
     */
    
    public double getDimensao(){
        return this.dimensao;
    }
    
    /**
     * Metodo que devolve o valor da variavel de instancia do valor base da SmartBulb
     * @return valor da variavel de instancia valorB
     */
    public double getValorB(){
        return this.valorB;
    }
    
    /**
     * Método que modifica o valor da variável de instância tone
     * @param tom da SmartBulb
     */
    public void setTone(int tone){
        if (tone>Warm) this.tone = Warm;
        else if (tone<Cold) this.tone = Cold;
        else this.tone = tone;
    }
    
    /**
     * Método que modifica o valor da variável de instância dimensao
     * @param dimensao da SmartBulb
     */
    public void setDimensao(double dimensao){
        this.dimensao = dimensao;
    }
    
    
    /**
     * Método que modifica o valor da variável de instância d valorB
     * @param Valor Base da SmartBulb
     */
    public void setValorB(double d){
        this.valorB = d;
    }
    
    /**
     * Método que testa se dois objetos são a mesma SmartBulb
     * @param o parámetro que se vai comparar com o this
     * @return true caso os objetos sejam iguais e false caso sejam diferentes
     */
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        SmartBulb e = (SmartBulb) o;
        return (e.dimensao == this.dimensao) && (e.tone == this.tone);
    }
    
    /**
     * Método que faz a clonagem de uma SmartBulb
     * @return um clone da SmartBulb this
     */
    public SmartBulb clone(){
        return new SmartBulb(this.getId(),this.getOn(),this.dimensao,this.tone,this.valorB);
    }
    
    /**
     * Método que calcula o consumo de energia diário da SmartBulb
     * @return 0 se a SmartBulb estiver desligada | consumo diário se a SmartBulb estiver ligada
     */
    public double ConsumoDiario(){
        if (this.getOn()){
            return valorB + 0.5*this.tone;
        }
        return 0;
    }
    
    /**
     * Método que coloca numa string os valores que caracterizam uma SmartBulb
     * @return String construida a partir das informações da SmartBulb
     */
    public String stringSD(){
        StringBuilder sb = new StringBuilder();
        sb.append("SmartBulb ||").append(this.getId()).append("||: \n\t")
                                                      .append("ON: ")
                                                      .append(this.getOn())
                                                      .append(", Dimensão: ")
                                                      .append(this.dimensao)
                                                      .append(", Tone: ")
                                                      .append(this.tone);
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
     * Método que cria uma menu para SmartBulbs
     * @returns menu criado
     */
    public Menu newmenuSD(Faturador a){
        Menu menu = new Menu(new String[]{"On/Off","Mudar Tone"});
        menu.setHandler(1,()->onoff(a));
        menu.setHandler(2,()->mudarTone());
        return menu;
    }
    
    /**
     * Metodo que proporciona ao utilizador mudar o tom da SmartBulb
     */
    public void mudarTone(){
        System.out.println("Novo Tone: ");
        int tone = gvInput().nextInt();
        setTone(tone);
    }
    /**
     * Metodo que liga ou desliga a SmartBulb de acordo com o seu estado atual
     */
    public void onoff(Faturador a){
        if (this.getOn()) a.addOF(this,false);
        else a.addOF(this,true);
    }
}