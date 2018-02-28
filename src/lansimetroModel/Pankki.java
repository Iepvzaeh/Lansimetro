package lansimetroModel;

/**
 *
 * @author Antti Nieminen, Antti Käyhkö, Anssi Chamorro, Heikki Tanttu
 */
public class Pankki {
    private int käytettyRaha;

    public Pankki() {
        this.käytettyRaha = 0;
    }

    
    
    public void setKäytettyRaha(int käytettyRaha) {
        this.käytettyRaha = käytettyRaha;
    }

    public int getKäytettyRaha() {
        return käytettyRaha;
    }
    
    public void lisääRaha(int summa){
        this.käytettyRaha += summa;
    }
    
    
    
    
}
