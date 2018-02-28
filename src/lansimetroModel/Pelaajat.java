package lansimetroModel;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Antti Nieminen, Antti Käyhkö, Anssi Chamorro, Heikki Tanttu
 */
public class Pelaajat {
    ArrayList<Pelaaja> pelaajatTulos = new ArrayList<>();
    
    
    public Pelaajat(ArrayList<Pelaaja> pelaajatTulos) {         
        this.pelaajatTulos = pelaajatTulos;     
    }
    public void tulostaPelaajat() {
        
        for (Pelaaja i : pelaajatTulos) {
           System.out.println(i);
       }
    }
    
    public ArrayList<Pelaaja> getSortedPelaajatByPiste() {         
        Collections.sort(pelaajatTulos, Pelaaja.pisteComparator);         
        return pelaajatTulos;     
    }
    
    public ArrayList<Pelaaja> getSortedPelaajatByRaha() {        
        Collections.sort(pelaajatTulos, Pelaaja.rahaComparator);         
        return pelaajatTulos;     
    }
    
}

