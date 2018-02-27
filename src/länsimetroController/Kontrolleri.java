/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package länsimetroController;

import länsimetroModel.Pelaaja;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

import java.util.Iterator;

import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import länsimetroModel.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Antti Käyhkö
 */
public class Kontrolleri implements Initializable {

    private final int häiriönMahdollisuus = 20;
    private final int häiriönEskalaatioMahdollisuus = 5;
    private final int päiväRaha = 50000;
    private final Asemat asemat;
    Pelaajat pelaajat;
    Pelaajat pelaajat2;
    Random r = new Random();
    Häiritsijä häiritsijä;
    private Kalenteri kalenteri = new Kalenteri();
    private Pankki pankki = new Pankki();
    private Asema viimeisinAsema;
    private KokoHäiriö viimeisinKokoHäiriö;
    private int valittuAsemaIndeksi = 8;
    private int valittuAsemaVaihtoehto;
    private Koeajo koeajo = new Koeajo();
    public String nimi;
    public int päiviäKulunut = 0;

    public int getPäiviäKulunut() {
        return päiviäKulunut;
    }

    public Kontrolleri() {

        this.asemat = new Asemat();
        this.häiritsijä = new Häiritsijä();

    }

    public boolean etenePäivä() {

        vaihto1Nappi.setText("");
        vaihto2Nappi.setText("");
        vaihto3Nappi.setText("");
        häiriöIkkuna.setText("");
        vaihto1Nappi.setDisable(true);
        vaihto2Nappi.setDisable(true);
        vaihto3Nappi.setDisable(true);
        kalenteri.etenePäivä();
        päiviäKulunut = päiviäKulunut + 1;
        pankki.lisääRaha(päiväRaha);
        kalenteriIkkuna.setText(tulostaPäivä());
        rahaIkkuna.setText(Integer.toString(tulostaRahat()) + " €");
        if (koeajo.isKäynnissä()) {
            koeajoNappi.setDisable(true);
            koeajo.etenePäivä();
            koeajoIkkuna.setText("Koeajoa jäljellä " + koeajo.getPäiviäJäljellä() + " pv.");

            if (koeajo.getPäiviäJäljellä() == 0) {
                koeajoOnnistunut();
                boolean peliläpi = true;
                return peliläpi;
            }
        }

        boolean tapahtuiHäiriö = false;
        katsoPoistetaankoHäiriöitäAsemilta();
        Asema asema;
        if (r.nextInt(101) <= häiriönMahdollisuus) {
            int montakoKertaaYritettiinLuodaHäiriö = 0;
            while (tapahtuiHäiriö == false && montakoKertaaYritettiinLuodaHäiriö < 50) { // yritetään luoda 50 kertaa häiriö
                asema = asemat.getAsemaLista().get(r.nextInt(asemat.getAsemaLista().size()));
                KokoHäiriö uusiHäiriö = häiritsijä.luoHäiriö();

                if (!asema.onkoAsemallaHäiriö(uusiHäiriö.getNimi())) {
                    if (asema.getHäiriöt().size() < 3) {
                        asema.lisääHäiriö(uusiHäiriö);
                        tulostaUusiHäiriö(asema, asema.getViimeisinHäiriö());
                        tapahtuiHäiriö = true;
                    }
                }
                montakoKertaaYritettiinLuodaHäiriö++;
            }

        }

        if (tarkistaEskalaatio()) {
            tapahtuiHäiriö = true;
        }
        listaaAsematJaHäiriöt();
        
        valittuAsemaIndeksi = 8;
        
        return tapahtuiHäiriö;
    }

    public void eteneviikko() {
        for (int i = 1; i <= 7; i++) {
            if (etenePäivä()) {
                break;
            }
        }
    }

    public void keskeytäKoeajo() {
        koeajo.keskeytä();
        koeajoNappi.setDisable(true);
        koeajoIkkuna.setStyle("-fx-text-inner-color: red;");
        koeajoIkkuna.setText("Koeajo ei käynnissä.");
        JOptionPane.showMessageDialog(null, "Koeajo on keskeytynyt vakavan häiriön vuoksi!");
    }

    public void koeajoOnnistunut() {
        vaihto1Nappi.setDisable(true);
        vaihto2Nappi.setDisable(true);
        vaihto3Nappi.setDisable(true);
        päiväNappi.setDisable(true);
        viikkoNappi.setDisable(true);
        // koeajoNappi.setDisable(true);
        matinkyläLabel.setDisable(true);
        niittykumpuLabel.setDisable(true);
        urheilupuistoLabel.setDisable(true);
        tapiolaLabel.setDisable(true);
        aaltoLabel.setDisable(true);
        keilaniemiLabel.setDisable(true);
        koivusaariLabel.setDisable(true);
        lauttasaariLabel.setDisable(true);

        häiriöIkkuna.setText("Onnittelut! Koeajo onnistui!");
        try {
            lisääJaTulostaHighScore();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Kontrolleri.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String tulostaPäivä() {
        return kalenteri.tulostaPäivä();
    }

    public int tulostaRahat() {
        return pankki.getKäytettyRaha();
    }

    public void lisääRaha(int summa) {
        pankki.lisääRaha(summa);
    }

    public int tarkastaOnkoAsemallaHäiriöitä(Asema asema) { // palauttaa 0 jos ei ole, 1 jos on häiriöitä jotka eivät estä koeajoa ja 2 jos on häiriöitä jotka keskeyttää koeajon

        if (!asema.getHäiriöt().isEmpty()) {
            ArrayList<KokoHäiriö> asemanKokoHäiriöt = asema.getHäiriöt();
            for (KokoHäiriö kokohäiriö : asemanKokoHäiriöt) {
                for (Häiriö häiriö : kokohäiriö.getKokoHäiriölista()) {
                    if (häiriö.isKeskeyttääKoeajon() && häiriö.isHoidettavana()) {
                        return 2;
                    }
                    if (häiriö.isKeskeyttääKoeajon() && häiriö.isValittu()) { // jos asemalla häiriö jota ei hoidettu mutta keskeyttää koeajon, laitetaan punainen valo päälle.
                        return 2;
                    }
                }
            }
            return 1;
        }
        return 0;
    }

    public void katsoPoistetaankoHäiriöitäAsemilta() {
        for (Asema asema : asemat.getAsemaLista()) {
            if (asema.getHäiriöt() != null) {
                Iterator<KokoHäiriö> i1 = asema.getHäiriöt().iterator();
                while (i1.hasNext()) {
                    KokoHäiriö kokohäiriö = i1.next();
                    for (Häiriö häiriö : kokohäiriö.getKokoHäiriölista()) {
                        if (häiriö.isHoidettavana()) {
                            häiriö.vähennäPäiviä();

                            if (häiriö.getPäivätHoitamiseen() == 0) {
                                // tulostaRatkaisu(asema, häiriö);
                                i1.remove();
                                poistaAsemaltaHäiriö(asema, kokohäiriö);
                            }
                        }
                    }
                }
            }
        }
    }

    public void asetaKokoHäiriöHoitumaanLopullisesti(KokoHäiriö kokohäiriö) {
        kokohäiriö.setHäiriöHoitumassa(true);
    }

    public int tarkistaOnkoKokoHäiriöHoidossa(KokoHäiriö kokohäiriö) { // palauttaa montako päivää kestää hoitaa kokohäiriö (jos se on hoidossa), muuten palauttaa 0
        for (Häiriö häiriö : kokohäiriö.getKokoHäiriölista()) {
            if (häiriö.isHoidettavana()) {
                return häiriö.getPäivätHoitamiseen();
            }
        }
        return 0;

    }

    public ArrayList<Asema> tarkistaOnkoHäiriö(KokoHäiriö haettavaHäiriö) {//palauttaa listan asemista, jotka eivät sisällä annettua kokohäiriötä
        ArrayList<Asema> asemalista = new ArrayList<>();
        boolean sisältää = false;
        for (Asema asema : asemat.getAsemaLista()) {
            sisältää = false;
            for (KokoHäiriö kokohäiriö : asema.getHäiriöt()) {
                if (haettavaHäiriö.getNimi().equals(kokohäiriö.getNimi())) {
                    sisältää = true;
                }

            }
            if (!sisältää) {
                asemalista.add(asema);
            }
        }
        return asemalista;
    }

    public boolean tarkistaEskalaatio() {
        boolean eskaloitui = false;
        ArrayList<Asema> mahdollinenAsema = new ArrayList();
        
        
        for (Asema asema : asemat.getAsemaLista()) {

            Iterator<KokoHäiriö> kokoHäiriöIteraattori = asema.getHäiriöt().iterator();
            
            while (kokoHäiriöIteraattori.hasNext()) {
                KokoHäiriö kokohäiriö = kokoHäiriöIteraattori.next();

                for (Häiriö häiriö : kokohäiriö.getKokoHäiriölista()) {
                    switch (häiriö.getPäivätHoitamiseen()) {
                        case -1: // voi eskaloitua milloin vain, häiriö kopioituu toiselle asemalle
                            if ((int) (r.nextDouble() * 100) >= 100 - häiriönEskalaatioMahdollisuus) {
                                mahdollinenAsema = tarkistaOnkoHäiriö(kokohäiriö);
                                if (!mahdollinenAsema.isEmpty()) {
                                    Asema rngAsema = mahdollinenAsema.get((int) (r.nextDouble() * mahdollinenAsema.size() - 1));
                                    rngAsema.lisääHäiriö(häiritsijä.luoSpesifiHäiriö(kokohäiriö));
                                    tulostaUusiHäiriö(rngAsema, rngAsema.getViimeisinHäiriö());
                                    eskaloitui = true;
                                }
                            }
                            break;
                        case -2: // voi eskaloitua vain kun koeajo on käynnissä, häiriö muuttuu muuksi
                            if (koeajo.isKäynnissä()) {
                                if ((int) (r.nextDouble() * 100) >= 100 - häiriönEskalaatioMahdollisuus) {                                    
                                    kokoHäiriöIteraattori.remove();
                                    poistaAsemaltaHäiriö(asema, kokohäiriö);                                    
                                    asema.lisääHäiriö(häiritsijä.luoSpesifiEskaloitunutHäiriö(kokohäiriö));
                                    kokoHäiriöIteraattori = asema.getHäiriöt().iterator();
                                    tulostaUusiHäiriö(asema, asema.getViimeisinHäiriö());
                                    eskaloitui = true;
                                }

                            }
                            break;
                        case -3: // voi eskaloitua milloin vain, häiriö muuttuu toiseksi
                            if ((int) (r.nextDouble() * 100) >= 100 - häiriönEskalaatioMahdollisuus) {
                                kokoHäiriöIteraattori.remove();
                                poistaAsemaltaHäiriö(asema, kokohäiriö);
                                asema.lisääHäiriö(häiritsijä.luoSpesifiEskaloitunutHäiriö(kokohäiriö));
                                tulostaUusiHäiriö(asema, asema.getViimeisinHäiriö());
                                eskaloitui = true;
                            }
                            break;
                    }

                }

            }

        }
        return eskaloitui;
    }

    public void poistaAsemaltaHäiriö(Asema asema, KokoHäiriö kokohäiriö) {
        asema.poistaHäiriö(kokohäiriö);
    }

    public void teeHoidettavaksi(Häiriö häiriö) {
        häiriö.setHoidettavana(true);
    }

    public ArrayList<Asema> getAsemat() {
        return asemat.getAsemaLista();
    }

    public void listaaAsematJaHäiriöt() {

        if (koeajo.isKäynnissä()) { // muuttaa koeajo ikkunan tekstiä
            koeajoIkkuna.setStyle("-fx-text-inner-color: green;");
            koeajoIkkuna.setText("Koeajoa jäljellä " + koeajo.getPäiviäJäljellä() + " pv.");
        } else {
            koeajoIkkuna.setStyle("-fx-text-inner-color: red;");
            koeajoIkkuna.setText("Koeajo ei käynnissä.");
        }

        for (Asema asema : getAsemat()) { // jos asemalla koeajon keskeyttävä häiriö, ottaa koeajo napin pois käytöstä.
            if (tarkastaOnkoAsemallaHäiriöitä(asema) == 2) {
                koeajoNappi.setDisable(true);
                if (koeajo.isKäynnissä()) {
                    keskeytäKoeajo();
                }
                break;
            }

            if (!koeajo.isKäynnissä()) {
                koeajoNappi.setDisable(false);
            }
        }

        for (Asema asema : getAsemat()) {
            int onkoAsemallaHäiriöitä = tarkastaOnkoAsemallaHäiriöitä(asema);

            switch (onkoAsemallaHäiriöitä) {
                case 0:
                    if (asema.getNimi().equals("Matinkylä")) {
                        matinkyläCircle.setFill(häiriötönVäri);
                    }
                    if (asema.getNimi().equals("Niittykumpu")) {
                        niittykumpuCircle.setFill(häiriötönVäri);
                    }
                    if (asema.getNimi().equals("Urheilupuisto")) {
                        urheilupuistoCircle.setFill(häiriötönVäri);
                    }
                    if (asema.getNimi().equals("Tapiola")) {
                        tapiolaCircle.setFill(häiriötönVäri);
                    }
                    if (asema.getNimi().equals("Aalto-yliopisto")) {
                        aaltoCircle.setFill(häiriötönVäri);
                    }
                    if (asema.getNimi().equals("Keilaniemi")) {
                        keilaniemiCircle.setFill(häiriötönVäri);
                    }
                    if (asema.getNimi().equals("Koivusaari")) {
                        koivusaariCircle.setFill(häiriötönVäri);
                    }
                    if (asema.getNimi().equals("Lauttasaari")) {
                        lauttasaariCircle.setFill(häiriötönVäri);
                    }

                    break;
                case 1:
                    if (asema.getNimi().equals("Matinkylä")) {
                        matinkyläCircle.setFill(häiriöVäri);
                    }
                    if (asema.getNimi().equals("Niittykumpu")) {
                        niittykumpuCircle.setFill(häiriöVäri);
                    }
                    if (asema.getNimi().equals("Urheilupuisto")) {
                        urheilupuistoCircle.setFill(häiriöVäri);
                    }
                    if (asema.getNimi().equals("Tapiola")) {
                        tapiolaCircle.setFill(häiriöVäri);
                    }
                    if (asema.getNimi().equals("Aalto-yliopisto")) {
                        aaltoCircle.setFill(häiriöVäri);
                    }
                    if (asema.getNimi().equals("Keilaniemi")) {
                        keilaniemiCircle.setFill(häiriöVäri);
                    }
                    if (asema.getNimi().equals("Koivusaari")) {
                        koivusaariCircle.setFill(häiriöVäri);
                    }
                    if (asema.getNimi().equals("Lauttasaari")) {
                        lauttasaariCircle.setFill(häiriöVäri);
                    }

                    break;
                case 2:
                    if (asema.getNimi().equals("Matinkylä")) {
                        matinkyläCircle.setFill(eikoeajoaVäri);
                    }
                    if (asema.getNimi().equals("Niittykumpu")) {
                        niittykumpuCircle.setFill(eikoeajoaVäri);
                    }
                    if (asema.getNimi().equals("Urheilupuisto")) {
                        urheilupuistoCircle.setFill(eikoeajoaVäri);
                    }
                    if (asema.getNimi().equals("Tapiola")) {
                        tapiolaCircle.setFill(eikoeajoaVäri);
                    }
                    if (asema.getNimi().equals("Aalto-yliopisto")) {
                        aaltoCircle.setFill(eikoeajoaVäri);
                    }
                    if (asema.getNimi().equals("Keilaniemi")) {
                        keilaniemiCircle.setFill(eikoeajoaVäri);
                    }
                    if (asema.getNimi().equals("Koivusaari")) {
                        koivusaariCircle.setFill(eikoeajoaVäri);
                    }
                    if (asema.getNimi().equals("Lauttasaari")) {
                        lauttasaariCircle.setFill(eikoeajoaVäri);
                    }

                    break;
            }

            if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Matinkylä")) {
                matinkyläIkkuna.setText("EI HÄIRIÖITÄ");
            }
            if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Niittykumpu")) {
                niittykumpuIkkuna.setText("EI HÄIRIÖITÄ");
            }
            if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Urheilupuisto")) {
                urheilupuistoIkkuna.setText("EI HÄIRIÖITÄ");
            }
            if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Tapiola")) {
                tapiolaIkkuna.setText("EI HÄIRIÖITÄ");
            }
            if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Aalto-yliopisto")) {
                aaltoIkkuna.setText("EI HÄIRIÖITÄ");
            }
            if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Keilaniemi")) {
                keilaniemiIkkuna.setText("EI HÄIRIÖITÄ");
            }
            if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Koivusaari")) {
                koivusaariIkkuna.setText("EI HÄIRIÖITÄ");
            }
            if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Lauttasaari")) {
                lauttasaariIkkuna.setText("EI HÄIRIÖITÄ");

            } else {
                for (KokoHäiriö kokohäiriö : asema.getHäiriöt()) {
                    if (asema.getNimi().equals("Matinkylä")) {
                        matinkyläIkkuna.setText("Häiriö: " + kokohäiriö.getNimi());
                    }
                    if (asema.getNimi().equals("Niittykumpu")) {
                        niittykumpuIkkuna.setText("Häiriö: " + kokohäiriö.getNimi());
                    }
                    if (asema.getNimi().equals("Urheilupuisto")) {
                        urheilupuistoIkkuna.setText("Häiriö: " + kokohäiriö.getNimi());
                    }
                    if (asema.getNimi().equals("Tapiola")) {
                        tapiolaIkkuna.setText("Häiriö: " + kokohäiriö.getNimi());
                    }
                    if (asema.getNimi().equals("Aalto-yliopisto")) {
                        aaltoIkkuna.setText("Häiriö: " + kokohäiriö.getNimi());
                    }
                    if (asema.getNimi().equals("Keilaniemi")) {
                        keilaniemiIkkuna.setText("Häiriö: " + kokohäiriö.getNimi());
                    }
                    if (asema.getNimi().equals("Koivusaari")) {
                        koivusaariIkkuna.setText("Häiriö: " + kokohäiriö.getNimi());
                    }
                    if (asema.getNimi().equals("Lauttasaari")) {
                        lauttasaariIkkuna.setText("Häiriö: " + kokohäiriö.getNimi());
                    }

                    int montakoPäivääHoidettavana = tarkistaOnkoKokoHäiriöHoidossa(kokohäiriö);
                    if (montakoPäivääHoidettavana > 0 && asema.getNimi().equals("Matinkylä")) {
                        matinkyläIkkuna.setText("Hoidossa: " + montakoPäivääHoidettavana + " pv jäljellä.");
                    }
                    if (montakoPäivääHoidettavana > 0 && asema.getNimi().equals("Niittykumpu")) {
                        niittykumpuIkkuna.setText("Hoidossa: " + montakoPäivääHoidettavana + " pv jäljellä.");
                    }
                    if (montakoPäivääHoidettavana > 0 && asema.getNimi().equals("Urheilupuisto")) {
                        urheilupuistoIkkuna.setText("Hoidossa: " + montakoPäivääHoidettavana + " pv jäljellä.");
                    }
                    if (montakoPäivääHoidettavana > 0 && asema.getNimi().equals("Tapiola")) {
                        tapiolaIkkuna.setText("Hoidossa: " + montakoPäivääHoidettavana + " pv jäljellä.");
                    }
                    if (montakoPäivääHoidettavana > 0 && asema.getNimi().equals("Aalto-yliopisto")) {
                        aaltoIkkuna.setText("Hoidossa: " + montakoPäivääHoidettavana + " pv jäljellä.");
                    }
                    if (montakoPäivääHoidettavana > 0 && asema.getNimi().equals("Keilaniemi")) {
                        keilaniemiIkkuna.setText("Hoidossa: " + montakoPäivääHoidettavana + " pv jäljellä.");
                    }
                    if (montakoPäivääHoidettavana > 0 && asema.getNimi().equals("Koivusaari")) {
                        koivusaariIkkuna.setText("Hoidossa: " + montakoPäivääHoidettavana + " pv jäljellä.");
                    }
                    if (montakoPäivääHoidettavana > 0 && asema.getNimi().equals("Lauttasaari")) {
                        lauttasaariIkkuna.setText("Hoidossa: " + montakoPäivääHoidettavana + " pv jäljellä.");
                    }

                }
            }

        }
    }

    public void tulostaRatkaisu(Asema asema, Häiriö häiriö) {
        häiriöIkkuna.setText(häiriö.getRatkaisuKuvaus());
    }

    public void tulostaUusiHäiriö(Asema asema, KokoHäiriö kokohäiriö) {
        ArrayList<Häiriö> häiriöt = kokohäiriö.getKokoHäiriölista();
        viimeisinAsema = asema;
        viimeisinKokoHäiriö = kokohäiriö;

        häiriöIkkuna.setText("Uusi häiriö asemalla " + asema + "\n" + kokohäiriö.getKuvaus() + "\n" + listaaHäiriönRatkaisuVaihtoehdot(kokohäiriö));
        vaihto1Nappi.setText("");
        vaihto2Nappi.setText("");
        vaihto3Nappi.setText("");
        if (!häiriöt.isEmpty()) {

            if (häiriöt.size() >= 1) {
                vaihto1Nappi.setDisable(false);
                vaihto1Nappi.setText(häiriöt.get(0).getHäiriöNimi());
            }
            if (häiriöt.size() >= 2) {
                vaihto2Nappi.setDisable(false);

                vaihto2Nappi.setText(häiriöt.get(1).getHäiriöNimi());
                vaihto3Nappi.setDisable(true);
                vaihto3Nappi.setText("");
            }
            if (häiriöt.size() == 3) {
                vaihto3Nappi.setDisable(false);
                vaihto3Nappi.setText(häiriöt.get(2).getHäiriöNimi());
            }

            päiväNappi.setDisable(true);
            viikkoNappi.setDisable(true);
            //koeajoNappi.setDisable(true);
        }
    }

    public String listaaHäiriönRatkaisuVaihtoehdot(KokoHäiriö kokohäiriö) {
        StringBuilder sb = new StringBuilder();
        sb.append("Vaihtoehdot:").append("\n\n");
        for (Häiriö häiriö : kokohäiriö.getKokoHäiriölista()) {
            sb.append(häiriö.getHäiriöNimi()).append("\n");
            sb.append("Maksaa: ").append(häiriö.getHinta()).append(" €\n");
            //sb.append("Keskeyttää koeajon: ").append(häiriö.isKeskeyttääKoeajon()).append("\n");
            /*if (häiriö.getPäivätHoitamiseen() <= -1) {
                sb.append("Häiriö jatkuu toistaiseksi.").append("\n");
            } else if (häiriö.getPäivätHoitamiseen() == 0) {
                sb.append("Häiriö poistuu samantien.").append("\n");
            } else {
                sb.append("Hoitaminen kestää: ").append(häiriö.getPäivätHoitamiseen()).append(" päivää").append("\n");
            }*/
            sb.append("\n");
        }

        return sb.toString();
    }

    public void lisääJaTulostaHighScore() throws FileNotFoundException {

        JFrame frame = new JFrame("InputDialog Example #1");
        nimi = JOptionPane.showInputDialog(frame, "LOISTAVAA! KOEAJO ONNISTUI! \nAnna nimesi!");

        if (nimi == null) {
            nimi = "noname";
        }
        if (nimi.isEmpty()) {
            nimi = "noname";
        }
        nimi = nimi.replaceAll("\\s", "");
        File file = new File("HighScore.txt");
        System.out.println("nimi");

        int points = getPäiviäKulunut();
        int menetettyraha = pankki.getKäytettyRaha();

        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            output.newLine();
            output.append(nimi + " " + points + " " + pankki.getKäytettyRaha());
            output.close();

        } catch (IOException ex1) {
            System.out.printf("ERROR pisteiden kirjoitus epäonnistui: %s\n", ex1);
        }

        Scanner input = new Scanner(new File("HighScore.txt"));

        ArrayList<Pelaaja> pelaajatLista = new ArrayList<>();
        while (input.hasNext()) {

            String pelaajaNimi = input.next();
            int pelaajaPoints = input.nextInt();
            int pelaajaRahat = input.nextInt();

            Pelaaja uusiPelaaja = new Pelaaja(pelaajaNimi, pelaajaPoints, pelaajaRahat);
            pelaajatLista.add(uusiPelaaja);
        }
        pelaajat = new Pelaajat(pelaajatLista);
        ArrayList<Pelaaja> pelaajatRahojenMukaan = pelaajat.getSortedPelaajatByRaha();
        pelaajat2 = new Pelaajat(pelaajatRahojenMukaan);
        ArrayList<Pelaaja> pelaajatPisteidenMukaan = pelaajat2.getSortedPelaajatByPiste();
        häiriöIkkuna.getText();
        String s = "";
        String highscore = "Onnittelut " + nimi + "! Koeajo onnistui!\nSinulla meni " + Integer.toString(points) + " päivää ja rahaa kului VAIN " + Integer.toString(menetettyraha) + " euroa \n\nParhaiten simuloineet TOP-"+pelaajatPisteidenMukaan.size()+"\n\n";
        int lopeta = 0;
        for (int i = 0; i < pelaajatPisteidenMukaan.size(); i++) {
            s= (""+Integer.toString(i+1)+". "+pelaajatPisteidenMukaan.get(i).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(i).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(i).getRahat()+"\n");
            highscore += s+"\n";
            lopeta++;
            if (lopeta == 50) {
                break;
            }
        }
        häiriöIkkuna.setText(highscore);
        /*häiriöIkkuna.setText("Onnittelut " + nimi + "! Koeajo onnistui!\nSinulla meni " + Integer.toString(points) + " päivää ja rahaa kului VAIN " + Integer.toString(menetettyraha) + " euroa \n\nParhaiten simuloineet TOP 10:"
                + "\n\n1. " + pelaajatPisteidenMukaan.get(0).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(0).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(0).getRahat()
                + "\n2. " + pelaajatPisteidenMukaan.get(1).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(1).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(1).getRahat()
                + "\n3. " + pelaajatPisteidenMukaan.get(2).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(2).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(2).getRahat()
                + "\n4. " + pelaajatPisteidenMukaan.get(3).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(3).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(3).getRahat()
                + "\n5. " + pelaajatPisteidenMukaan.get(4).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(4).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(4).getRahat()
                + "\n6. " + pelaajatPisteidenMukaan.get(5).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(5).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(5).getRahat()
                + "\n7. " + pelaajatPisteidenMukaan.get(6).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(6).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(6).getRahat()
                + "\n8. " + pelaajatPisteidenMukaan.get(7).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(7).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(7).getRahat()
                + "\n9. " + pelaajatPisteidenMukaan.get(8).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(8).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(8).getRahat()
                + "\n10. " + pelaajatPisteidenMukaan.get(9).getNimi() + "\npäivät: " + pelaajatPisteidenMukaan.get(9).getPisteet() + " rahat: " + pelaajatPisteidenMukaan.get(9).getRahat());*/
    }

    @FXML
    private void paivaKayntiin(ActionEvent event) {
        System.out.println("päivä");
        etenePäivä();

    }

    @FXML
    private void viikkoKayntiin(ActionEvent event) {
        System.out.println("viikko");
        eteneviikko();
    }

    @FXML
    private void suljeNappi(ActionEvent event) throws FileNotFoundException {
        System.out.println("sulje");
        System.exit(0);
    }

    @FXML
    private void käynnistäKoeAjo(ActionEvent event) {
        koeajo.käynnistä();
        häiriöIkkuna.setText("Koeajo käynnistetty onnistuneesti!");
        koeajoNappi.setDisable(true);
        listaaAsematJaHäiriöt();
    }

    @FXML
    private void vaihtoehto1Nappi(ActionEvent event) {
        if (valittuAsemaIndeksi == 8) {
            hoidaAsemallaOlevaOngelma(viimeisinKokoHäiriö.getKokoHäiriölista().get(0));
            listaaAsematJaHäiriöt();
            
        }
        if (valittuAsemaIndeksi < 8) {
            valittuAsemaVaihtoehto = 0;

            Asema asema = asemat.getAsemaLista().get(valittuAsemaIndeksi);
            KokoHäiriö kokohäiriö = asema.getHäiriöt().get(valittuAsemaVaihtoehto);
            viimeisinKokoHäiriö = kokohäiriö;
            viimeisinAsema = asema;
            ArrayList<Häiriö> häiriöt = kokohäiriö.getKokoHäiriölista();
            häiriöIkkuna.setText("Aseman " + asema + " häiriö\n" + kokohäiriö.getKuvaus() + "\n" + listaaHäiriönRatkaisuVaihtoehdot(kokohäiriö));
            vaihto1Nappi.setText("");
            vaihto2Nappi.setText("");
            vaihto3Nappi.setText("");
            if (häiriöt.size() >= 1) {
                vaihto1Nappi.setDisable(false);
                vaihto1Nappi.setText(häiriöt.get(0).getHäiriöNimi());
            }
            if (häiriöt.size() >= 2) {
                vaihto2Nappi.setDisable(false);

                vaihto2Nappi.setText(häiriöt.get(1).getHäiriöNimi());
                vaihto3Nappi.setDisable(true);
                vaihto3Nappi.setText("");
            }
            if (häiriöt.size() == 3) {
                vaihto3Nappi.setDisable(false);
                vaihto3Nappi.setText(häiriöt.get(2).getHäiriöNimi());
            }

            päiväNappi.setDisable(true);
            viikkoNappi.setDisable(true);
            koeajoNappi.setDisable(true);

            listaaAsematJaHäiriöt();
            valittuAsemaIndeksi = 8;
        }

    }

    @FXML
    private void vaihtoehto2Nappi(ActionEvent event) {
        if (valittuAsemaIndeksi == 8) {
            
            hoidaAsemallaOlevaOngelma(viimeisinKokoHäiriö.getKokoHäiriölista().get(1));
            listaaAsematJaHäiriöt();
            
            
        }
        if (valittuAsemaIndeksi < 8) {
            valittuAsemaVaihtoehto = 1;

            Asema asema = asemat.getAsemaLista().get(valittuAsemaIndeksi);
            KokoHäiriö kokohäiriö = asema.getHäiriöt().get(valittuAsemaVaihtoehto);
            viimeisinKokoHäiriö = kokohäiriö;
            viimeisinAsema = asema;
            ArrayList<Häiriö> häiriöt = kokohäiriö.getKokoHäiriölista();
            häiriöIkkuna.setText("Aseman " + asema + " häiriö\n" + kokohäiriö.getKuvaus() + "\n" + listaaHäiriönRatkaisuVaihtoehdot(kokohäiriö));
            vaihto1Nappi.setText("");
            vaihto2Nappi.setText("");
            vaihto3Nappi.setText("");
            if (häiriöt.size() >= 1) {
                vaihto1Nappi.setDisable(false);
                vaihto1Nappi.setText(häiriöt.get(0).getHäiriöNimi());
            }
            if (häiriöt.size() >= 2) {
                vaihto2Nappi.setDisable(false);
                vaihto2Nappi.setText(häiriöt.get(1).getHäiriöNimi());
                vaihto3Nappi.setDisable(true);
                vaihto3Nappi.setText("");
            }
            if (häiriöt.size() == 3) {
                vaihto3Nappi.setDisable(false);
                vaihto3Nappi.setText(häiriöt.get(2).getHäiriöNimi());
            }

            päiväNappi.setDisable(true);
            viikkoNappi.setDisable(true);
            koeajoNappi.setDisable(true);

            listaaAsematJaHäiriöt();
            valittuAsemaIndeksi = 8;

        }

    }

    @FXML
    private void vaihtoehto3Nappi(ActionEvent event) {
        if (valittuAsemaIndeksi == 8) {
            
            hoidaAsemallaOlevaOngelma(viimeisinKokoHäiriö.getKokoHäiriölista().get(2));
            listaaAsematJaHäiriöt();
            
            
        }
        if (valittuAsemaIndeksi < 8) {
            valittuAsemaVaihtoehto = 2;

            Asema asema = asemat.getAsemaLista().get(valittuAsemaIndeksi);
            KokoHäiriö kokohäiriö = asema.getHäiriöt().get(valittuAsemaVaihtoehto);
            viimeisinKokoHäiriö = kokohäiriö;
            viimeisinAsema = asema;
            ArrayList<Häiriö> häiriöt = kokohäiriö.getKokoHäiriölista();
            häiriöIkkuna.setText("Aseman " + asema + " häiriön" + kokohäiriö.getKuvaus() + "\n" + listaaHäiriönRatkaisuVaihtoehdot(kokohäiriö));
            vaihto1Nappi.setText("");
            vaihto2Nappi.setText("");
            vaihto3Nappi.setText("");
            if (häiriöt.size() >= 1) {
                vaihto1Nappi.setDisable(false);
                vaihto1Nappi.setText(häiriöt.get(0).getHäiriöNimi());
            }
            if (häiriöt.size() >= 2) {
                vaihto2Nappi.setDisable(false);
                vaihto2Nappi.setText(häiriöt.get(1).getHäiriöNimi());
                vaihto3Nappi.setDisable(true);
                vaihto3Nappi.setText("");
            }
            if (häiriöt.size() == 3) {
                vaihto3Nappi.setDisable(false);
                vaihto3Nappi.setText(häiriöt.get(2).getHäiriöNimi());
            }

            päiväNappi.setDisable(true);
            viikkoNappi.setDisable(true);
            koeajoNappi.setDisable(true);

            listaaAsematJaHäiriöt();
            valittuAsemaIndeksi = 8;

        }

    }

    private void hoidaAsemallaOlevaOngelma(Häiriö häiriö) {
        tulostaRatkaisu(viimeisinAsema, häiriö);
        if (häiriö.getPäivätHoitamiseen() <= 0) {

            if (häiriö.isPoistaaKokoHäiriön()) {
                poistaAsemaltaHäiriö(viimeisinAsema, viimeisinKokoHäiriö);
            }

            if (häiriö.isKeskeyttääKoeajon()) {
                /* jos valittu häiriö keskeyttää koeajon mutta vaatii toisen
                            valinnan ratkaisuksi, valitaan häiriö jotta asema tunnistaa sen 
                            koeajon keskeyttäväksi häiriöksi
                 */

                for (Häiriö h : viimeisinKokoHäiriö.getKokoHäiriölista()) {
                    h.setValittu(false); // ettei kaksi häiriötä ole samaan aikaan valittuna
                }
                häiriö.setValittu(true);
            }

        } else {
            teeHoidettavaksi(häiriö);
        }
        lisääRaha(häiriö.getHinta());

        päiväNappi.setDisable(false);
        viikkoNappi.setDisable(false);
        vaihto1Nappi.setDisable(true);
        vaihto2Nappi.setDisable(true);
        vaihto3Nappi.setDisable(true);
    }

    @FXML
    private void handleLabelClick(MouseEvent event) {
        StringBuilder sb = new StringBuilder();

        if (!päiväNappi.isDisable()) {
            vaihto1Nappi.setText("");
            vaihto2Nappi.setText("");
            vaihto3Nappi.setText("");
        }

        for (Asema asema : getAsemat()) {
            int onkoAsemallaHäiriöitä = tarkastaOnkoAsemallaHäiriöitä(asema);
            
            if (event.getSource() == matinkyläLabel && !päiväNappi.isDisable()) {
                valittuAsemaIndeksi = 7;
                if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Matinkylä")) {
                    vaihto1Nappi.setDisable(true);
                    vaihto2Nappi.setDisable(true);
                    vaihto3Nappi.setDisable(true);
                    häiriöIkkuna.setText("EI HÄIRIÖITÄ");
                } else if (asema.getNimi().equals("Matinkylä")) {
                    for (KokoHäiriö kokohäiriö : asemat.haeAsema("Matinkylä").getHäiriöt()) {
                        sb.append(kokohäiriö.getNimi());
                        sb.append("\n");
                    }
                    häiriöIkkuna.setText("Aseman häiriöt:" + "\n\n" + sb.toString());
                    if (asema.getHäiriöt().size() >= 1) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(true);
                        vaihto3Nappi.setDisable(true);
                        vaihto1Nappi.setText(asema.getHäiriöt().get(0).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(0)) != 0) {
                            vaihto1Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 2) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(true);
                        vaihto2Nappi.setText(asema.getHäiriöt().get(1).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(1)) != 0) {
                            vaihto2Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 3) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(false);
                        vaihto3Nappi.setText(asema.getHäiriöt().get(2).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(2)) != 0) {
                            vaihto3Nappi.setDisable(true);
                        }

                    }
                }

            }
            if (event.getSource() == niittykumpuLabel && !päiväNappi.isDisable()) {
                valittuAsemaIndeksi = 6;
                if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Niittykumpu")) {
                    vaihto1Nappi.setDisable(true);
                    vaihto2Nappi.setDisable(true);
                    vaihto3Nappi.setDisable(true);
                    häiriöIkkuna.setText("EI HÄIRIÖITÄ");
                } else if (asema.getNimi().equals("Niittykumpu")) {

                    for (KokoHäiriö kokohäiriö : asemat.haeAsema("Niittykumpu").getHäiriöt()) {
                        sb.append(kokohäiriö.getNimi());
                        sb.append("\n");
                    }
                    häiriöIkkuna.setText("Aseman häiriöt:" + "\n\n" + sb.toString());
                    if (asema.getHäiriöt().size() >= 1) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(true);
                        vaihto3Nappi.setDisable(true);
                        vaihto1Nappi.setText(asema.getHäiriöt().get(0).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(0)) != 0) {
                            vaihto1Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 2) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(true);
                        vaihto2Nappi.setText(asema.getHäiriöt().get(1).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(1)) != 0) {
                            vaihto2Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 3) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(false);
                        vaihto3Nappi.setText(asema.getHäiriöt().get(2).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(2)) != 0) {
                            vaihto3Nappi.setDisable(true);
                        }

                    }
                }
            }
            if (event.getSource() == urheilupuistoLabel && !päiväNappi.isDisable()) {
                valittuAsemaIndeksi = 5;
                if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Urheilupuisto")) {
                    vaihto1Nappi.setDisable(true);
                    vaihto2Nappi.setDisable(true);
                    vaihto3Nappi.setDisable(true);
                    häiriöIkkuna.setText("EI HÄIRIÖITÄ");
                } else if (asema.getNimi().equals("Urheilupuisto")) {
                    for (KokoHäiriö kokohäiriö : asemat.haeAsema("Urheilupuisto").getHäiriöt()) {
                        sb.append(kokohäiriö.getNimi());
                        sb.append("\n");
                    }
                    häiriöIkkuna.setText("Aseman häiriöt:" + "\n\n" + sb.toString());
                    if (asema.getHäiriöt().size() >= 1) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(true);
                        vaihto3Nappi.setDisable(true);
                        vaihto1Nappi.setText(asema.getHäiriöt().get(0).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(0)) != 0) {
                            vaihto1Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 2) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(true);
                        vaihto2Nappi.setText(asema.getHäiriöt().get(1).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(1)) != 0) {
                            vaihto2Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 3) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(false);
                        vaihto3Nappi.setText(asema.getHäiriöt().get(2).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(2)) != 0) {
                            vaihto3Nappi.setDisable(true);
                        }

                    }
                }
            }
            if (event.getSource() == tapiolaLabel && !päiväNappi.isDisable()) {
                valittuAsemaIndeksi = 4;
                if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Tapiola")) {
                    vaihto1Nappi.setDisable(true);
                    vaihto2Nappi.setDisable(true);
                    vaihto3Nappi.setDisable(true);
                    häiriöIkkuna.setText("EI HÄIRIÖITÄ");
                } else if (asema.getNimi().equals("Tapiola")) {
                    for (KokoHäiriö kokohäiriö : asemat.haeAsema("Tapiola").getHäiriöt()) {
                        sb.append(kokohäiriö.getNimi());
                        sb.append("\n");
                    }
                    häiriöIkkuna.setText("Aseman häiriöt:" + "\n\n" + sb.toString());
                    if (asema.getHäiriöt().size() >= 1) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(true);
                        vaihto3Nappi.setDisable(true);
                        vaihto1Nappi.setText(asema.getHäiriöt().get(0).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(0)) != 0) {
                            vaihto1Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 2) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(true);
                        vaihto2Nappi.setText(asema.getHäiriöt().get(1).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(1)) != 0) {
                            vaihto2Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 3) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(false);
                        vaihto3Nappi.setText(asema.getHäiriöt().get(2).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(2)) != 0) {
                            vaihto3Nappi.setDisable(true);
                        }

                    }
                }
            }
            if (event.getSource() == aaltoLabel && !päiväNappi.isDisable()) {
                valittuAsemaIndeksi = 3;
                if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Aalto-yliopisto")) {
                    vaihto1Nappi.setDisable(true);
                    vaihto2Nappi.setDisable(true);
                    vaihto3Nappi.setDisable(true);
                    häiriöIkkuna.setText("EI HÄIRIÖITÄ");
                } else if (asema.getNimi().equals("Aalto-yliopisto")) {
                    for (KokoHäiriö kokohäiriö : asemat.haeAsema("Aalto-yliopisto").getHäiriöt()) {
                        sb.append(kokohäiriö.getNimi());
                        sb.append("\n");
                    }
                    häiriöIkkuna.setText("Aseman häiriöt:" + "\n\n" + sb.toString());
                    if (asema.getHäiriöt().size() >= 1) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(true);
                        vaihto3Nappi.setDisable(true);
                        vaihto1Nappi.setText(asema.getHäiriöt().get(0).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(0)) != 0) {
                            vaihto1Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 2) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(true);
                        vaihto2Nappi.setText(asema.getHäiriöt().get(1).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(1)) != 0) {
                            vaihto2Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 3) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(false);
                        vaihto3Nappi.setText(asema.getHäiriöt().get(2).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(2)) != 0) {
                            vaihto3Nappi.setDisable(true);
                        }

                    }
                }
            }
            if (event.getSource() == keilaniemiLabel && !päiväNappi.isDisable()) {
                valittuAsemaIndeksi = 2;
                if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Keilaniemi")) {
                    vaihto1Nappi.setDisable(true);
                    vaihto2Nappi.setDisable(true);
                    vaihto3Nappi.setDisable(true);
                    häiriöIkkuna.setText("EI HÄIRIÖITÄ");
                } else if (asema.getNimi().equals("Keilaniemi")) {
                    for (KokoHäiriö kokohäiriö : asemat.haeAsema("Keilaniemi").getHäiriöt()) {
                        sb.append(kokohäiriö.getNimi());
                        sb.append("\n");
                    }
                    häiriöIkkuna.setText("Aseman häiriöt:" + "\n\n" + sb.toString());
                    if (asema.getHäiriöt().size() >= 1) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(true);
                        vaihto3Nappi.setDisable(true);
                        vaihto1Nappi.setText(asema.getHäiriöt().get(0).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(0)) != 0) {
                            vaihto1Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 2) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(true);
                        vaihto2Nappi.setText(asema.getHäiriöt().get(1).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(1)) != 0) {
                            vaihto2Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 3) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(false);
                        vaihto3Nappi.setText(asema.getHäiriöt().get(2).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(2)) != 0) {
                            vaihto3Nappi.setDisable(true);
                        }

                    }
                }
            }
            if (event.getSource() == koivusaariLabel && !päiväNappi.isDisable()) {
                valittuAsemaIndeksi = 1;
                if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Koivusaari")) {
                    vaihto1Nappi.setDisable(true);
                    vaihto2Nappi.setDisable(true);
                    vaihto3Nappi.setDisable(true);
                    häiriöIkkuna.setText("EI HÄIRIÖITÄ");
                } else if (asema.getNimi().equals("Koivusaari")) {
                    for (KokoHäiriö kokohäiriö : asemat.haeAsema("Koivusaari").getHäiriöt()) {
                        sb.append(kokohäiriö.getNimi());
                        sb.append("\n");
                    }
                    häiriöIkkuna.setText("Aseman häiriöt:" + "\n\n" + sb.toString());
                    if (asema.getHäiriöt().size() >= 1) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(true);
                        vaihto3Nappi.setDisable(true);
                        vaihto1Nappi.setText(asema.getHäiriöt().get(0).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(0)) != 0) {
                            vaihto1Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 2) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(true);
                        vaihto2Nappi.setText(asema.getHäiriöt().get(1).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(1)) != 0) {
                            vaihto2Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 3) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(false);
                        vaihto3Nappi.setText(asema.getHäiriöt().get(2).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(2)) != 0) {
                            vaihto3Nappi.setDisable(true);
                        }

                    }
                }
            }
            if (event.getSource() == lauttasaariLabel && !päiväNappi.isDisable()) {
                valittuAsemaIndeksi = 0;
                if (onkoAsemallaHäiriöitä == 0 && asema.getNimi().equals("Lauttasaari")) {
                    vaihto1Nappi.setDisable(true);
                    vaihto2Nappi.setDisable(true);
                    vaihto3Nappi.setDisable(true);
                    häiriöIkkuna.setText("EI HÄIRIÖITÄ");
                } else if (asema.getNimi().equals("Lauttasaari")) {
                    for (KokoHäiriö kokohäiriö : asemat.haeAsema("Lauttasaari").getHäiriöt()) {
                        sb.append(kokohäiriö.getNimi());
                        sb.append("\n");
                    }
                    häiriöIkkuna.setText("Aseman häiriöt:" + "\n\n" + sb.toString());
                    if (asema.getHäiriöt().size() >= 1) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(true);
                        vaihto3Nappi.setDisable(true);
                        vaihto1Nappi.setText(asema.getHäiriöt().get(0).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(0)) != 0) {
                            vaihto1Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 2) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(true);
                        vaihto2Nappi.setText(asema.getHäiriöt().get(1).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(1)) != 0) {
                            vaihto2Nappi.setDisable(true);
                        }

                    }
                    if (asema.getHäiriöt().size() >= 3) {
                        vaihto1Nappi.setDisable(false);
                        vaihto2Nappi.setDisable(false);
                        vaihto3Nappi.setDisable(false);
                        vaihto3Nappi.setText(asema.getHäiriöt().get(2).getNimi());
                        if (tarkistaOnkoKokoHäiriöHoidossa(asema.getHäiriöt().get(2)) != 0) {
                            vaihto3Nappi.setDisable(true);
                        }

                    }
                }
            }
        }
    }
    @FXML
    private Button päiväNappi;
    @FXML
    private Button viikkoNappi;
    @FXML
    private Button koeajoNappi;
    @FXML
    private Button lopetaNappi;
    @FXML
    private Button vaihto1Nappi;
    @FXML
    private Button vaihto2Nappi;
    @FXML
    private Button vaihto3Nappi;

    @FXML
    private TextArea häiriöIkkuna;
    @FXML
    private TextField kalenteriIkkuna;
    @FXML
    private TextField matinkyläIkkuna;
    @FXML
    private TextField niittykumpuIkkuna;
    @FXML
    private TextField urheilupuistoIkkuna;
    @FXML
    private TextField tapiolaIkkuna;
    @FXML
    private TextField aaltoIkkuna;
    @FXML
    private TextField keilaniemiIkkuna;
    @FXML
    private TextField koivusaariIkkuna;
    @FXML
    private TextField lauttasaariIkkuna;
    @FXML
    private TextField rahaIkkuna;
    @FXML
    private TextField koeajoIkkuna;
    @FXML
    private Circle matinkyläCircle;
    @FXML
    private Circle niittykumpuCircle;
    @FXML
    private Circle urheilupuistoCircle;
    @FXML
    private Circle tapiolaCircle;
    @FXML
    private Circle aaltoCircle;
    @FXML
    private Circle keilaniemiCircle;
    @FXML
    private Circle koivusaariCircle;
    @FXML
    private Circle lauttasaariCircle;
    @FXML
    private Label matinkyläLabel;
    @FXML
    private Label niittykumpuLabel;
    @FXML
    private Label urheilupuistoLabel;
    @FXML
    private Label tapiolaLabel;
    @FXML
    private Label aaltoLabel;
    @FXML
    private Label keilaniemiLabel;
    @FXML
    private Label koivusaariLabel;
    @FXML
    private Label lauttasaariLabel;

    @FXML
    Color häiriöVäri = Color.YELLOW;
    @FXML
    Color häiriötönVäri = Color.GREEN;
    @FXML
    Color eikoeajoaVäri = Color.RED;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vaihto1Nappi.setDisable(true);
        vaihto2Nappi.setDisable(true);
        vaihto3Nappi.setDisable(true);
        koeajoNappi.setStyle("-fx-background-color: lightgreen;");

    }

}
