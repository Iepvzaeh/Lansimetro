/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansimetroModel;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Anzu
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

