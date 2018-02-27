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
public class KokoHairio {

    private final String nimi;
    private final String kuvaus;
    private ArrayList<Hairio> kokoHäiriölista;
    private boolean häiriöHoitumassa = false;

    public KokoHairio(String nimi, String kuvaus) {
        this.kokoHäiriölista = new ArrayList();
        this.nimi = nimi;
        this.kuvaus = kuvaus;
    }

    public KokoHairio(KokoHairio kokohäiriö) { // luodaan kopio kokohäiriöstä ja sen häiriöistä
        this.nimi = kokohäiriö.nimi;
        this.kuvaus = kokohäiriö.kuvaus;
        this.kokoHäiriölista = new ArrayList();
        for (Hairio h : kokohäiriö.getKokoHäiriölista()) {
            this.kokoHäiriölista.add(new Hairio(h));

        }
    }

    public boolean isHäiriöHoitumassa() {
        return häiriöHoitumassa;
    }

    public void setHäiriöHoitumassa(boolean häiriöHoitumassa) {
        this.häiriöHoitumassa = häiriöHoitumassa;
    }

    public void lisääVaihtoehdot(Hairio h) {
        this.kokoHäiriölista.add(h);
    }

    public String getNimi() {
        return nimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public ArrayList<Hairio> getKokoHäiriölista() {
        return kokoHäiriölista;
    }

}
