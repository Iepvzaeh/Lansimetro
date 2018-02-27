/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package länsimetroModel;
import java.util.Comparator;


/**
 *
 * @author Anzu
 */
public class Pelaaja {
    private String nimi;
    private int pisteet;
    private int rahat;

    public Pelaaja(String nimi, int pisteet, int rahat) {
        this.nimi = nimi;
        this.pisteet = pisteet;
        this.rahat = rahat;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getPisteet() {
        return pisteet;
    }

    public void setPisteet(int pisteet) {
        this.pisteet = pisteet;
    }

    public int getRahat() {
        return rahat;
    }

    public void setRahat(int rahat) {
        this.rahat = rahat;
    }

    
    public static Comparator<Pelaaja> pisteComparator = new Comparator<Pelaaja>() {         
        @Override         
        public int compare(Pelaaja o1, Pelaaja o2) {             
            return (o2.getPisteet() > o1.getPisteet() ? -1 :                     
                (o2.getPisteet() == o1.getPisteet() ? 0 : 1));           
        }     
    };       
    public static Comparator<Pelaaja> rahaComparator = new Comparator<Pelaaja>() {         
        @Override         
        public int compare(Pelaaja o1, Pelaaja o2) {             
            return (o2.getRahat() > o1.getRahat() ? -1 :                     
                (o2.getRahat() == o1.getRahat() ? 0 : 1));         
        }     
    };
    @Override
    public String toString() {
        return "Pelaaja: " + nimi + ", päivät: " + pisteet + ", rahat: " + rahat;
    }  

    
}
