package lansimetroModel;

import java.util.ArrayList;

/**
 *
 * @author Antti Nieminen, Antti Käyhkö, Anssi Chamorro, Heikki Tanttu
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
    
    public int indexOf(String asemaNimi) {
        return asemaLista.indexOf(haeAsema(asemaNimi));        
    }
}
