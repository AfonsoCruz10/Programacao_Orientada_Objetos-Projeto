import java.util.Scanner;
import java.io.Serializable;

public class SmartCamera extends SmartDevice implements Serializable
{
    
    private String resolucao; // resoluçao da imagem da SmartCamera
    
    private double tamanho; // tamanho dos ficheiros de imagem da SmartCamera
    
    private double valorB; 
    // Valor base que servirá para o cálculo do consumo diário

    /**
     * Construtores para objetos da classe SmartCamera
     */

    public SmartCamera()
    {
        super();
        this.resolucao = "";
        this.tamanho=0;
        this.valorB = 0.1;
    }
    
    public SmartCamera(String resolucao, double tamanho,double valorB)
    {
        super();
        this.resolucao=resolucao;
        this.tamanho=tamanho;
        this.valorB = valorB;
    }
    
    public SmartCamera(boolean on, String resolucao, double tamanho,double valorB)
    {
        super(on);
        this.resolucao=resolucao;
        this.tamanho=tamanho;
        this.valorB = valorB;
    }
    
    public SmartCamera(int id, boolean on, String resolucao, double tamanho,double valorB)
    {
        super(id,on);
        this.resolucao=resolucao;
        this.tamanho=tamanho;
        this.valorB = valorB;
    }
    
    /**
     * Métodos set da classe SmartDevice
     */
    
    /**
     * Método que muda o valor da variável de instância resolucao
     * @param valor a guardar em resolucao
     */
    public void setResolucao(String resolucao){
        this.resolucao = resolucao;
    }
    
    /**
     * Método que muda o valor da variável de instância tamanho
     * @param valor a guardar em tamanho
     */
    public void setTamanho(double tamanho){
        this.tamanho = tamanho;
    }
    
    /**
     * Metodo que muda valor da variável de instância valorB
     * @param d - valor a guardar em valorB 
     */
    public void setValorB(double d){
        this.valorB = d;
    }
    
    /**
     * Métodos get da classe SmartDevice
     */
    
    /**
     * Método que devolve o valor da variável de instância resolucao
     * @return valor em resolucao
     */
    public String getResolucao(){
        return this.resolucao;
    }
    
    
    
    /**
     * Método que devolve o valor da variável de instância tamanho
     * @return valor em tamanho
     */
        public double getTamanho(){
        return this.tamanho;
    }
    
    /**
     * Metodo que devolve o valor da variavel de instancia do valor base da SmartCamera
     * @return valor da variavel de instancia valorB
     */
    public double getValorB(){
        return this.valorB;
    }
    
    
    /**
     * Método que testa se dois objetos são a mesma SmartCamera
     * @param o parámetro que se vai comparar com o this
     * @return true caso os objetos sejam iguais e false caso sejam diferentes
     */
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) return false;
        SmartCamera e = (SmartCamera) o;
        return (e.resolucao == this.resolucao) && (e.tamanho == this.tamanho);
    }
    
    /**
     * Método que faz a clonagem de uma SmartCamera
     * @return um clone da SmartCamera this
     */
    public SmartCamera clone(){
        return new SmartCamera(this.getId(),this.getOn(),this.resolucao,this.tamanho,this.valorB);
    }
    
    /**
     * Método que calcula o consumo de energia diário da SmartCamera
     * @return 0 se a SmartCamera estiver desligada | consumo diário se a SmartCamera estiver ligada
     */
    public double ConsumoDiario(){
        if (this.getOn()){
            String resolucaoclean = resolucao.replaceAll("[^\\d]", " ").trim();
            String[] numeros = resolucaoclean.split(" "); 
            int resolucaox = Integer.parseInt(numeros[0]);
            int resolucaoy = Integer.parseInt(numeros[1]);
            return valorB + this.tamanho*resolucaox*resolucaoy;
        }
        return 0;
    }
    
    /**
     * Método que coloca numa string os valores que caracterizam um SmartSpeaker
     * @return String construida a partir das informações do SmartSpeaker
     */
    public String stringSD(){
        StringBuilder sb = new StringBuilder();
        sb.append("SmartCamera ||").append(this.getId()).append("||: \n\t")
                                                      .append("ON: ")
                                                      .append(this.getOn())
                                                      .append(", Resolução: ")
                                                      .append(this.resolucao)
                                                      .append(", Tamanho dos ficheiros: ")
                                                      .append(this.tamanho);
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
     * Método que cria uma menu para SmartCameras
     * @returns menu criado
     */
    public Menu newmenuSD(Faturador a){
        Menu menu = new Menu(new String[]{"On/Off","Mudar Resolução",
                                  "Mudar Tamanho"});
        menu.setHandler(1,()->onoff(a));
        menu.setHandler(2,()->mudarResolucao());
        menu.setHandler(3,()->mudarTamanho());
        return menu;
    }
    
    /**
     * Metodo que liga ou desliga o SmartCameras de acordo com o seu estado atual
     */
    
    public void onoff(Faturador a){
        if (this.getOn()) a.addOF(this,false);
        else a.addOF(this,true);
    }
    
    /**
     * Metodo que proporciona ao utilizador mudar a resolução da SmartCamera
     */
    public void mudarResolucao(){
        System.out.println("Nova resolução: ");
        String resolucao = gvInput().nextLine();
        setResolucao(resolucao);
    }
    
    /**
     * Metodo que proporciona ao utilizador mudar o tamanho dos ficheiros da SmartCamera
     */
    public void mudarTamanho(){
        System.out.println("Novo tamanho dos ficheiros (Megabytes): ");
        double tamanho = gvInput().nextDouble();
        setTamanho(tamanho);
    }
}
