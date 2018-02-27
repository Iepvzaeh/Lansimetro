/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansimetroModel;

/**
 *
 * @author Antti Käyhkö
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
