package lansimetroModel;

import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Antti Nieminen, Antti Käyhkö, Anssi Chamorro, Heikki Tanttu
 */
public class Hairitsija {

    HashMap<Integer, KokoHairio> häiriöListaNormaali;
    HashMap<Integer, KokoHairio> häiriöListaHarvinainen;
    HashMap<Integer, KokoHairio> häiriöListaEskaloituminen;

    public Hairitsija() {

        this.häiriöListaNormaali = new HashMap<>();
        this.häiriöListaHarvinainen = new HashMap<>();
        this.häiriöListaEskaloituminen = new HashMap<>();
        
        //<0 monta päivää keskeytys kestää, 0 jatkuu ilman viivästystä, -1 saattaa eskaloitua muille asemille, -2 saattaa eskaloitua muuksi ongelmaksi KOEAJON OLLESSA KÄYNNISSÄ, -3 saattaa eskaloitua muuksi ongelmaksi MILLOIN VAIN, -4 keskeyttää pelin, -9 koeajo keskeytynyt kunnes asia hoidetaan, 
        
        //NORMAALIT
        häiriöListaNormaali.put(0, new KokoHairio("Lepakot", "Harvinainen bulgarialainen lepakkolaji on havaittu pesimässä asemalla."));
        häiriöListaNormaali.get(0).lisääVaihtoehdot(new Hairio(50000, "Kutsu lepakonkesyttäjät", "", 3, "Bulgarialaiset lepakonkesyttäjät tulevat poistamaan lepakot turvallisesti. Koeajo jatkuu kolmen päivän päästä.", true, true));
        häiriöListaNormaali.get(0).lisääVaihtoehdot(new Hairio(15000, "Ammu lepakot", "", 0, "Lepakot ovat ammuttu", false, true));
        häiriöListaNormaali.get(0).lisääVaihtoehdot(new Hairio(0, "Jätä lepakot rauhaan", "", -1, "Lepakot jatkavat infestaatiotaan", false, false));
        
        häiriöListaEskaloituminen.put(0, new KokoHairio("Hirvi", "Hirvi on jäänyt metron alle."));
        häiriöListaEskaloituminen.get(0).lisääVaihtoehdot(new Hairio(10000,"Siivoa hirvi. Ja metro.", "", 1, "Hirveä siivotaan. Koeajo jatkuu yhden päiviän päästä.", true, true));
        häiriöListaEskaloituminen.get(0).lisääVaihtoehdot(new Hairio(0, "Anna hirven olla", "", -9, "Koeajoa ei voida jatkaa, kun hirven raato on raiteilla", true, false));
        
        häiriöListaNormaali.put(1, new KokoHairio("Hirvi", "Kaupunkiin eksynyt hirvi harhailee asemalla"));
        häiriöListaNormaali.get(1).lisääVaihtoehdot(new Hairio(5000, "Soita eläinpartio", "", 1, "Eläinpartion tulee hakemaan hirven turvallisesti pois. Koeajo jatkuu yhden päivän päästä.", true, true));
        häiriöListaNormaali.get(1).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -2, "Kyllä hirvi osaa varoa junaa", false, false));
        
        häiriöListaNormaali.put(2, new KokoHairio("Varkaus", "Varkaat ryöstivät yön aikana aseman työmaan varastosta raiteita"));
        häiriöListaNormaali.get(2).lisääVaihtoehdot(new Hairio(25000, "Osta uudet raiteet", "", 0, "Uudet raiteet ostettu", false, true));
        häiriöListaNormaali.get(2).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", 0, "Raiteethan on jo paikallaan", false, true));
        
        häiriöListaNormaali.put(3, new KokoHairio("Ryöstö", "Varkaat murtautuivat yön aikana asemalle ja veivät aseman ohjausjärjestelmän tietokoneen"));
        häiriöListaNormaali.get(3).lisääVaihtoehdot(new Hairio(100000, "Osta uusi tietokone", "", 4, "Uusi tietokone tilataan Saksasta pikatilauksella ja asennetaan heti sen saapuessa. Koeajo jatkuu neljän päivän päästä.", true, true));
        häiriöListaNormaali.get(3).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -9, "Koeajoa ei voida jatkaa ilman aseman ohjausjärjestelmää", true, false));        
        
        häiriöListaNormaali.put(4, new KokoHairio("Haunted station", "Shamaani kertoi havainneensa haamuja asemalla. Hän kertoo että vain ammattilainen voi poistaa haamut asemalta suolaamalla aseman kalliilla kuolleenmeren suolalla"));
        häiriöListaNormaali.get(4).lisääVaihtoehdot(new Hairio(150000, "Tilaa Ghost Busters", "", 3, "Ghost Busters saapuu suolaamaan aseman. He matkustavat paikalle välittömästi töihin Yhdysvalloista. Koeajo jatkuu kolmen päivän kuluttua", true, true));
        häiriöListaNormaali.get(4).lisääVaihtoehdot(new Hairio(3, "Suolaa itse", "", 0, "Menet itse suolaamaan asema testipäivän jälkeen", false, true));
        häiriöListaNormaali.get(4).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", 0, "Ei kummituksia ole olemassa", false, true));
        
        häiriöListaNormaali.put(5, new KokoHairio("Mielenosoitus", "Länsimetron vastainen mielenosoitus on järjestetty aseman läheisyydessä"));
        häiriöListaNormaali.get(5).lisääVaihtoehdot(new Hairio(60000, "Lahjo mielenosoituksen johtaja", "", 0, "Mielenosoituksen johtaja lopettaa mielenosoituksen", false, true));
        häiriöListaNormaali.get(5).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", 0, "Siellähän osoittavat mieltänsä", false, true));
        
        häiriöListaEskaloituminen.put(1, new KokoHairio("Märkää betonia", "Huonosti kuivattu betoni on valunut raiteille"));
        häiriöListaEskaloituminen.get(1).lisääVaihtoehdot(new Hairio(100000,"Korjaa vahinko", "", 10, "Vahkojen korjaus käynnistyy. Korjaus kestää kymmenen päivää", true, true));
        häiriöListaEskaloituminen.get(1).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -9, "Koeajoa ei voida jatkaa ennen kuin vahinko on korjattu", true, false));
        
        häiriöListaNormaali.put(6, new KokoHairio("Märkää betonia", "Rakennustarkastaja on huomannut märkää betonia asemalaiturissa"));
        häiriöListaNormaali.get(6).lisääVaihtoehdot(new Hairio(40000, "Kuivata betoni", "", 7, "Betonia täytyy kuivata tehostetusti. Junat eivät voi kulkea sinä aikana ettei betoni valu raiteille. Koeajo jatkuu seitsemän päivän kuluttua.", true, true));
        häiriöListaNormaali.get(6).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -2, "Betoni kyllä kestää. On se tähänkin asti kestänyt.", false, false));
        
        häiriöListaNormaali.put(7, new KokoHairio("Tulipalo", "Raiteista lentäneet kipinät aiheuttivat tulipalon asemalla."));
        häiriöListaNormaali.get(7).lisääVaihtoehdot(new Hairio(0, "Soita palokunta", "", 1, "Palokunta sammutti palon. Onneksi se huomattiin ajoissa eikä vahinkoja tullut juurikaan. Koeajo jatkuu yhden päivän kuluttua.", true, true));
        häiriöListaNormaali.get(7).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", 1, "Ohikulkija näki savun nousevan asemalta ja soitti palokunnan paikalle. Onneksi tulipalo huomattiin ajoissa eikä vahinkoja tullut juurikaan. Koeajo jatkuu yhden päivän kuluttua.", true, true));

        häiriöListaNormaali.put(8, new KokoHairio("Sadevesi", "Viime yönä satanut sadevesi on aiheuttanut tulvan asemalla."));
        häiriöListaNormaali.get(8).lisääVaihtoehdot(new Hairio(17000, "Pumppaa vesi pois", "", 1, "Vesi pumpataan pois. Koeajo jatkuu yhden päivän pumppauksen jälkeen.", true, true));
        häiriöListaNormaali.get(8).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -9, "Junat eivät voi kulkea jos raiteilla on tulva", true, false));
        
        häiriöListaEskaloituminen.put(2, new KokoHairio("Pummit asemalla", "Pummi jäi junan alle. Pummin kaverit pakenivat asemalta."));
        häiriöListaEskaloituminen.get(2).lisääVaihtoehdot(new Hairio(8000,"Siivoa pummi. Ja metro...", "", 1, "Pummin ruumista siivotaan pois. Koeajo jatkuu yhden päivän kuluttua.", true, true));
        häiriöListaEskaloituminen.get(2).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -9, "Koeajoa ei voida jatkaa ennen kuin pummi on siivottu", true, false));
        
        häiriöListaNormaali.put(9, new KokoHairio("Pummit asemalla", "Ryhmä pummeja on majoittunut asemalle. He käyttävät asemaa majapaikkanaan ja rellestävät siellä humalassa."));
        häiriöListaNormaali.get(9).lisääVaihtoehdot(new Hairio(0, "Soita poliisit", "", 1, "Poliisit tulevat poistamaan pummit asemalta. Junat eivät voi kulkea kun siviilejä on asematunneleissa. Koeajo jatkuu yhden päivän kuluttua", true, true));
        häiriöListaNormaali.get(9).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -2, "Kyllä he osaavat varoa junia.", false, false));
        
        häiriöListaNormaali.put(10, new KokoHairio("Länsimetro-appro", "Opiskelijat eivät enää jaksa odottaa metron valmistusta. He ovat järjestäneet appron asemalla."));
        häiriöListaNormaali.get(10).lisääVaihtoehdot(new Hairio(100000, "Tarjoa baari-ilta", "", 0, "Tarjoat opiskelijoille baaari-illan viereisessä baarissa.", false, true));
        häiriöListaNormaali.get(10).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", 1, "Koeajo ei voi jatkua jos 1000 opiskelijaa kulkee raiteilla. Koeajo jatkuu yhden päivän kuluttua.", true, true));        
        
        häiriöListaEskaloituminen.put(3, new KokoHairio("Ohjausvalot", "Junat törmäsivät toisiinsa ohjausvalojen puuttumisen takia"));
        häiriöListaEskaloituminen.get(3).lisääVaihtoehdot(new Hairio(200000,"Korjaa vahingot", "", 30, "Vahinkoja korjataan. Koeajo jatkuu kolmenkymmenen päivän kuluttua.", true, true));
        häiriöListaEskaloituminen.get(3).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -9, "Koeajoa ei voida jatkaa ennen kuin vahingot on korjattu.", true, false));
        
        häiriöListaNormaali.put(11, new KokoHairio("Ohjausvalot", "Aseman ohjausvalot ovat epäkunnossa"));
        häiriöListaNormaali.get(11).lisääVaihtoehdot(new Hairio(130000, "Korjaa valot", "", 3, "Valoja korjataan. Koeajo jatkuu kolmen päivän kuluttua", true, true));
        häiriöListaNormaali.get(11).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -2, "Ei kait niitä tarvita.", false, false));
        
        häiriöListaNormaali.put(12, new KokoHairio("Sähkövika", "Sähkövika asemalla aiheuttaa sulakkeiden palamisen."));
        häiriöListaNormaali.get(12).lisääVaihtoehdot(new Hairio(150000, "Korjaa sähkövika", "", 3, "Sähkövikaa korjataan. Koeajo jatkuu kolmen päivän kuluttua.", true, true));
        häiriöListaNormaali.get(12).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -9, "Junat eivät voi kulkea kun asemalla ei ole sähköä.", true, false));
        
        häiriöListaNormaali.put(13, new KokoHairio("Liito-oravat", "Liito-oravat ovat ottaneet pesäpaikakseen aseman."));
        häiriöListaNormaali.get(13).lisääVaihtoehdot(new Hairio(200000, "Soita liito-orava ekspertille", "", 1, "Ekspertti poistaa liito-oravat. Koeajo jatkuu yhden päivän kuluttua.", true, true));
        häiriöListaNormaali.get(13).lisääVaihtoehdot(new Hairio(18000, "Ammu oravat", "", 0, "Liito-oravat on ammuttu.", false, true));
        häiriöListaNormaali.get(13).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -1, "Liito-oravat jatkavat pesimistään.", false, false));
        
        häiriöListaNormaali.put(14, new KokoHairio("Vaihdevika", "Aseman vaihdejärjestelmä on vioittunut."));
        häiriöListaNormaali.get(14).lisääVaihtoehdot(new Hairio(10700, "Korjaa Vaihdevika", "", 3, "Vaihdevikaa korjataan. Koeajo jatkuu kolmen päivän kuluttua.", true, true));
        häiriöListaNormaali.get(14).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", -9, "Junat eivät voi kulkea kun asemalla ei ole sähköä.", true, false));
        
        
        //HARVINAISET
        häiriöListaHarvinainen.put(0, new KokoHairio("POMMIUHKA", "Metron johtaja on saanut puhelun pommista asemalla."));
        häiriöListaHarvinainen.get(0).lisääVaihtoehdot(new Hairio(0, "Kutsu Karhuryhmä", "", 1, "Karhuryhmä saapuu paikalle hoitamaan tilanteen. Koeajo jatkuu yhden päivän kuluttua.", true, true));
        häiriöListaHarvinainen.get(0).lisääVaihtoehdot(new Hairio(0, "Anna olla", "", 30, "RÄJÄHTI", true, true));

        häiriöListaHarvinainen.put(1, new KokoHairio("Huumeongelma", "Metron johtaja on jäänyt kiinni kovista huumeista."));
        häiriöListaHarvinainen.get(1).lisääVaihtoehdot(new Hairio(100000, "Lähetä vieroitukseen", "", 14, "Johtaja menee vieroitukseen. Koeajo jatkuu neljäntoista päivän kuluttua.", true, true));
        häiriöListaHarvinainen.get(1).lisääVaihtoehdot(new Hairio(10000000, "Lahjo poliisi", "", 0, "Poliisi hiljenee", false, true));
        
    }

    public KokoHairio luoHäiriö() {
        Random r = new Random();
        int mahdollisuus = (int) (r.nextDouble() * 100);
        if (mahdollisuus > 80) {
            return new KokoHairio(häiriöListaHarvinainen.get(r.nextInt(häiriöListaHarvinainen.size())));
        } else {
            return new KokoHairio(häiriöListaNormaali.get(r.nextInt(häiriöListaNormaali.size())));
        }
    }

    public KokoHairio luoSpesifiHäiriö(KokoHairio luotavaKokohäiriö) {
        for (KokoHairio kokohäiriö : häiriöListaHarvinainen.values()) {
            if (kokohäiriö.getNimi().equals(luotavaKokohäiriö.getNimi())) {
                return new KokoHairio(kokohäiriö);
            }
        }
        for (KokoHairio kokohäiriö : häiriöListaNormaali.values()) {
            if (kokohäiriö.getNimi().equals(luotavaKokohäiriö.getNimi())) {
                return new KokoHairio(kokohäiriö);
            }
        }      
        
        return null;

    }
    
    public KokoHairio luoSpesifiEskaloitunutHäiriö(KokoHairio eskaloituvaHäiriö) {
        
        for (KokoHairio kokohäiriö : häiriöListaEskaloituminen.values()) {
            if (kokohäiriö.getNimi().equals(eskaloituvaHäiriö.getNimi())) {
                return new KokoHairio(kokohäiriö);
            }
        }  
        
        return null;
    }
}
