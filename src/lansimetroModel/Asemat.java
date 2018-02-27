/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansimetroModel;

import java.util.ArrayList;

/**
 *
 * @author Antti Käyhkö
 */
public class Asemat {
    ArrayList<Asema> asemaLista;
       
    public Asemat() {
        this.asemaLista = new ArrayList<>();
        asemaLista.add(new Asema("Lauttasaari"));
        asemaLista.add(new Asema("Koivusaari"));
        asemaLista.add(new Asema("Keilaniemi"));
        asemaLista.add(new Asema("Aalto-yliopisto"));
        asemaLista.add(new Asema("Tapiola"));
        asemaLista.add(new Asema("Urheilupuisto"));
        asemaLista.add(new Asema("Niittykumpu"));
        asemaLista.add(new Asema("Matinkylä"));
        
    }

    public ArrayList<Asema> getAsemaLista() {
        return asemaLista;
    }
    
    public Asema haeAsema(String asemaNimi){
        for (Asema a : asemaLista){
            if (a.getNimi().equals(asemaNimi)){
                return a;
            }
        }
        return null;
    }
}
