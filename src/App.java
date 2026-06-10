import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

class ProjektowyException extends Exception {
    public ProjektowyException(String message) {super(message);}
}

class EtapLigiNieSkończonyException extends ProjektowyException {
    public EtapLigiNieSkończonyException(String message) {super(message);}
}

class DrużynaJużZagrałaMeczException extends ProjektowyException {
    public DrużynaJużZagrałaMeczException(String message) {super(message);}
}

class ZaMałoDrużynException extends ProjektowyException {
    public ZaMałoDrużynException(String message) {super(message);}
}

class BrakTakiejDrużynyException extends ProjektowyException {
    public BrakTakiejDrużynyException(String message) {super(message);}
}

class PustePoleException extends ProjektowyException {
    public PustePoleException(String message) {super(message);}
}

class RozgrywkaNieZakończonaException extends ProjektowyException {
    public RozgrywkaNieZakończonaException(String message) {super(message);}
}

class RozgrywkaRozpoczętaException extends ProjektowyException {
    public RozgrywkaRozpoczętaException(String message) {super(message);}
}

class RozgrywkaNieRozpoczętaException extends ProjektowyException {
    public RozgrywkaNieRozpoczętaException(String message) {super(message);}
}

class ZaDużoDoZwróceniaException extends ProjektowyException {
    public ZaDużoDoZwróceniaException(String message) {super(message);}
}

class ZłaLiczbaDrużynException extends ProjektowyException {
    public ZłaLiczbaDrużynException(String message) {super(message);}
}

class ZłaParaDrużynException extends ProjektowyException {
    public ZłaParaDrużynException(String message) {super(message);}
}

class ZaDużoMeczówBezpośrednichException extends ProjektowyException {
    public ZaDużoMeczówBezpośrednichException(String message) {super(message);}
}

class ZaDużoMeczyWRundzieException extends ProjektowyException {
    public ZaDużoMeczyWRundzieException(String message) {super(message);}
}

class ZaDużoRundException extends ProjektowyException {
    public ZaDużoRundException(String message) {super(message);}
}

class ZaMałoZwyciezcowException extends ProjektowyException {
    public ZaMałoZwyciezcowException(String message) {super(message);}
}

class ZłyWynikKarnychException extends ProjektowyException {
    public ZłyWynikKarnychException(String message) {super(message);}
}

class Rozgrywka {
    private ArrayList<Drużyna> listaDrużyn;
    private ArrayList<Wynik> listaWyników;
    private HashMap<String, Integer> listaPunktów;
    private boolean stanRozpoczęcia = false; 
    private boolean stanKońca = false; 
    Rozgrywka() {
        this.listaDrużyn = new ArrayList<Drużyna>();
        this.listaWyników = new ArrayList<Wynik>();
        this.listaPunktów = new HashMap<String, Integer>();
    }

    protected void zajerestrujDrużyne(Drużyna drużyna) throws RozgrywkaRozpoczętaException {
        if (stanRozpoczęcia == true) {
            throw new RozgrywkaRozpoczętaException("Nie można dodać drużyn po rozpoczęciu rozgrywki.");
        }
        listaDrużyn.add(drużyna);
        listaPunktów.put(drużyna.getNazwa(), 0);
    }
    public void pokażTabele() throws RozgrywkaNieZakończonaException {
        if (stanKońca == false) {
            throw new RozgrywkaNieZakończonaException("Nie można pokazać wyników przed zakończeniem rozgrywki.");
        }
        for (Wynik wynikIMeczu : getListaWyników()) {
            System.out.println(wynikIMeczu.getDrużyna1().getNazwa() + " vs " + wynikIMeczu.getDrużyna2().getNazwa());
            System.out.println(wynikIMeczu.getWynik1() + " : " + wynikIMeczu.getWynik2());
        }
        System.out.println("\n\n" + getListaPunktów());
    }
    public void stworzIZapiszWyniki(Drużyna drużyna1, Drużyna drużyna2, int gole1, int gole2) throws ProjektowyException {
        if (!listaDrużyn.contains(drużyna1) || !listaDrużyn.contains(drużyna2)) {
            throw new BrakTakiejDrużynyException("Przynajmniej jedna z drużyn nie istnieje.");
        }
        ArrayList<Integer> wynikGoli = new ArrayList<Integer>();
        wynikGoli.add(gole1);
        wynikGoli.add(gole2);
        Wynik wynikMeczu = new Wynik(drużyna1, drużyna2, wynikGoli);
        zapiszWyniki(wynikMeczu);
    }
    public void zapiszWyniki(Wynik wynikMeczu) throws ProjektowyException {
        stanRozpoczęcia = true;
        getListaWyników().add(wynikMeczu);
        Integer punktyDrużyny1 = getListaPunktów().get(wynikMeczu.getDrużyna1().getNazwa());
        Integer punktyDrużyny2 = getListaPunktów().get(wynikMeczu.getDrużyna2().getNazwa());
        getListaPunktów().put(wynikMeczu.getDrużyna1().getNazwa(), punktyDrużyny1 + wynikMeczu.getWynik1());
        getListaPunktów().put(wynikMeczu.getDrużyna2().getNazwa(), punktyDrużyny2 + wynikMeczu.getWynik2());
    }
    public String getCaleInfo() throws RozgrywkaNieZakończonaException {
        if (stanKońca == false) {
            throw new RozgrywkaNieZakończonaException("Jeszcze nie zakończono rozgrywki.");
        }
        String caleInfo = "\nRozgrywka\nPunkty\n";

        for (int i = 0; i < getListaDrużyn().size(); i++) {
            Drużyna aktualnaDrużyna = getListaDrużyn().get(i);
            String nazwa = aktualnaDrużyna.getNazwa();
            Integer punkty = getListaPunktów().get(nazwa);

            caleInfo += nazwa + " : " + punkty + " pkt\n";
        }
        caleInfo += "\n";
        caleInfo += "Rozegrane Mecze\n";
        for (int i = 0; i < getListaWyników().size(); i++) {
            Wynik w = getListaWyników().get(i);

            caleInfo += w.getDrużyna1().getNazwa() + " " + w.getWynik1() + ":" + w.getWynik2() + " " + w.getDrużyna2().getNazwa() + "\n";
        }
        return caleInfo;
    }
    public Drużyna getDrużynaPoNazwie(String nazwaDrużyny) {
        for (Drużyna drużyna : listaDrużyn) {
            if (drużyna.getNazwa().equals(nazwaDrużyny)) {
                return drużyna;
            }
        }
        return null;
    }
    public ArrayList<Drużyna> getListaDrużyn() {
        return listaDrużyn;
    }
    public HashMap<String, Integer> getListaPunktów() {
        return listaPunktów;
    }
    public ArrayList<Wynik> getListaWyników() {
        return listaWyników;
    }
    public boolean getStanRozpoczęcia() {
        return stanRozpoczęcia;
    }
    public void zmieńStanRozpoczęcia() {
        stanRozpoczęcia = true;
    }
    public boolean getStanKońca() {
        return stanKońca;
    }
    public void zmieńStanKońca() {
        stanKońca = true;
    }
}

class TurniejeZbiorowo extends Rozgrywka {
    private ArrayList<ArrayList<Drużyna>> listaParDrużyn;
    private HashMap<Integer,Drużyna> listaZwycięzcówRundy;
    private int liczbaRund = 0;
    private int obecnaRunda = 0;
    private Scanner turniejeScanner;

    public TurniejeZbiorowo() {
        super();
        this.listaParDrużyn = new ArrayList<ArrayList<Drużyna>>();
        this.listaZwycięzcówRundy = new HashMap<Integer,Drużyna>();
        this.turniejeScanner = new Scanner(System.in);
    }
    public TurniejeZbiorowo(Scanner scanner) {
        super();
        this.listaParDrużyn = new ArrayList<ArrayList<Drużyna>>();
        this.listaZwycięzcówRundy = new HashMap<Integer,Drużyna>();
        this.turniejeScanner = scanner;
    }
    
    public void losujParyDrużyn(ArrayList<Drużyna> listaDrużynDoPar) throws ZłaLiczbaDrużynException, RozgrywkaRozpoczętaException, ZaMałoDrużynException {
        if ((listaDrużynDoPar.size() & listaDrużynDoPar.size() - 1)  != 0) {
            throw new ZłaLiczbaDrużynException("Podano złą liczbę drużyn, musi ona być potęgą dwójki.");
        }
        if (listaDrużynDoPar.size() < 2) {
            throw new ZaMałoDrużynException("Podano za małą liczbę drużyn, musi ona być większa niż 1.");
        }
        if (getStanRozpoczęcia() == true) {
            throw new RozgrywkaRozpoczętaException("Już rozpoczęto rozgrywkę.");
        }
        ArrayList<Drużyna> posortowaneDrużyny = new ArrayList<>(listaDrużynDoPar);
        posortowaneDrużyny.sort((d1, d2) -> Integer.compare(d1.getPoziomDrużyny(), d2.getPoziomDrużyny()));
        int L = 0;
        int P = posortowaneDrużyny.size() - 1;
        for (int i = 0; i < posortowaneDrużyny.size() / 2; i++) {
            ArrayList<Drużyna> para = new ArrayList<>();
            para.add(posortowaneDrużyny.get(L));
            para.add(posortowaneDrużyny.get(P));
            listaParDrużyn.add(para);
            L++;
            P--;
        }
        obliczIUstawLiczbęRund();
        zmieńStanRozpoczęcia();
    }
    @Override
    public void zapiszWyniki(Wynik wynikMeczu) throws ZłaParaDrużynException, ZaDużoMeczyWRundzieException, RozgrywkaNieRozpoczętaException, DrużynaJużZagrałaMeczException {
        if (getListaZwycięzcówRundy().size() >= getListaParDrużyn().size()) {
            throw new ZaDużoMeczyWRundzieException("Za dużo zwycięzców w rundzie");
        }
        if (getStanRozpoczęcia() == false) {
            throw new RozgrywkaNieRozpoczętaException("Jeszcze nie wylosowano drużyn.");
        }
        String nazwaDrużyny1 = wynikMeczu.getDrużyna1().getNazwa();
        String nazwaDrużyny2 = wynikMeczu.getDrużyna2().getNazwa();
        if (listaZwycięzcówRundy.containsValue(getDrużynaPoNazwie(nazwaDrużyny1))||listaZwycięzcówRundy.containsValue(getDrużynaPoNazwie(nazwaDrużyny2))) {
            throw new DrużynaJużZagrałaMeczException("Jedna z drużyn już zagrała mecz w tej rundzie.");
        }
        ArrayList<Integer> punkty = wynikMeczu.punktyZaMecz();
        
        Integer punktyDrużyny1 = getListaPunktów().get(nazwaDrużyny1);
        Integer punktyDrużyny2 = getListaPunktów().get(nazwaDrużyny2);
        getListaPunktów().put(nazwaDrużyny1, punktyDrużyny1 + punkty.get(0));
        getListaPunktów().put(nazwaDrużyny2, punktyDrużyny2 + punkty.get(1));    

        Drużyna zwycięzca = wynikMeczu.getZwycięzcaMeczu();
        if (zwycięzca == null) {
            System.out.print("Podaj która drużyna wygrywa karne (1 jeśli pierwsza, inne jeśli 2):");
            String n = turniejeScanner.nextLine();
            // turniejeScanner.close();
            if (n.equals("1")) {
                zwycięzca = wynikMeczu.getDrużyna1();
            }
            else {
                zwycięzca = wynikMeczu.getDrużyna2();
            }
        }
        ArrayList<Drużyna> paraDrużyn = new ArrayList<Drużyna>();
        paraDrużyn.add(wynikMeczu.getDrużyna1());
        paraDrużyn.add(wynikMeczu.getDrużyna2());
        if (!getListaParDrużyn().contains(paraDrużyn)) {
            if (!getListaParDrużyn().contains(paraDrużyn.reversed())) {
                throw new ZłaParaDrużynException("Taka para nie rozgrywa razem meczy");
            }
        }
        for (int i = 0; i < getListaParDrużyn().size(); i++){
            if (getListaParDrużyn().get(i).contains(zwycięzca)) {
                listaZwycięzcówRundy.put(i, zwycięzca);
            }
        }
        getListaWyników().add(wynikMeczu);
    }

    public void obliczIUstawLiczbęRund() {
        int liczbaDzielona = getListaParDrużyn().size();
        int licznikRund = 0;
        while (liczbaDzielona != 0 && liczbaDzielona % 2 == 0) {
            liczbaDzielona = liczbaDzielona / 2;
            licznikRund += 1;
        }
        if (licznikRund != 0) {
            licznikRund += 1;
        }
        liczbaRund = licznikRund;
    }
    public ArrayList<ArrayList<Drużyna>> getListaParDrużyn() {
        return listaParDrużyn;
    }
    public int getLiczbaRund() {
        return liczbaRund;
    }
    public int getObecnaRunda() {
        return obecnaRunda;
    }
    protected void dodajDoObecnejRundy(){
        obecnaRunda += 1;
    }
    public HashMap<Integer, Drużyna> getListaZwycięzcówRundy() {
        return listaZwycięzcówRundy;
    }
}

class Turniej extends TurniejeZbiorowo {
    private String zwycięzca;

    public Turniej() {
        super();
    }
    public Turniej(Scanner turniejScanner) {
        super(turniejScanner);
    }
    @Override
    public void pokażTabele() throws RozgrywkaNieZakończonaException {
        if (getStanKońca() == false) {
            throw new RozgrywkaNieZakończonaException("Nie można pokazać wyników przed zakończeniem rozgrywki.");
        }
        for (Wynik wynikIMeczu : getListaWyników()) {
            System.out.println(wynikIMeczu.getDrużyna1().getNazwa() + " vs " + wynikIMeczu.getDrużyna2().getNazwa());
            System.out.println(wynikIMeczu.getWynik1() + " : " + wynikIMeczu.getWynik2());
        }
    };
    public void przejdźDoNastępnejRundy() throws ZaDużoRundException, ZaMałoZwyciezcowException {
        if (getObecnaRunda() > getLiczbaRund()) {
            throw new ZaDużoRundException("Za dużo rund (powinien być już zwycięzca)");
        }
        if (getListaZwycięzcówRundy().size() <= getListaParDrużyn().size()/2) {
            throw new ZaMałoZwyciezcowException("Zbyt mało zwycięzców rund żeby przejść do następnej rundy");
        }
        getListaParDrużyn().clear();
        dodajDoObecnejRundy();
        boolean czy_pominiete = false;
        for (Integer i : getListaZwycięzcówRundy().keySet()) {
            if (!czy_pominiete) {
                ArrayList<Drużyna> paraDrużyn = new ArrayList<Drużyna>();
                paraDrużyn.add(getListaZwycięzcówRundy().get(i));
                paraDrużyn.add(getListaZwycięzcówRundy().get(i+1));
                getListaParDrużyn().add(paraDrużyn);
                czy_pominiete = true;
            }
            else {
                czy_pominiete = false;
            }
        }
        if (getListaZwycięzcówRundy().size() == 1) {
            for (Drużyna drużynaZwycięska : getListaZwycięzcówRundy().values()) {
                zwycięzca = drużynaZwycięska.getNazwa();
                System.out.println("Zwycięzca to: " + drużynaZwycięska.getNazwa());
                zmieńStanKońca();
            }
        }
        getListaZwycięzcówRundy().clear();
    }
    @Override
    public String getCaleInfo() throws RozgrywkaNieZakończonaException {
        if (getStanKońca() == false) {
            throw new RozgrywkaNieZakończonaException("Jeszcze nie zakończono rozgrywki.");
        }
        String caleInfo = "\nTurniej\n";
        
        caleInfo += "Rozegrane Mecze\n";
        for (int i = 0; i < getListaWyników().size(); i++) {
            Wynik w = getListaWyników().get(i);

            caleInfo += w.getDrużyna1().getNazwa() + " " + w.getWynik1() + ":" + w.getWynik2() + " " + w.getDrużyna2().getNazwa() + "\n";
        }
        caleInfo += "\nZwycięzca:\n";
        caleInfo += zwycięzca + "\n";
        return caleInfo;
    }
}

class TurniejGrupowy extends Turniej {
    TurniejGrupowy() {
        super();
    }
    public void losujParyZKoszykow(ArrayList<Drużyna> listaPierwszychMiejsc, ArrayList<Drużyna> listaDrugichMiejsc) {
        getListaParDrużyn().clear(); // Czyścimy listę par z klasy bazowej
        int iloscGrup = listaPierwszychMiejsc.size();

        for (int i = 0; i < iloscGrup; i++) {
            ArrayList<Drużyna> para = new ArrayList<>();
            para.add(listaPierwszychMiejsc.get(i));

            // Przesunięcie indeksu o 1 dzięki czemu nikt nigdy nie zagra z drużyną ze swojej grupy
            int indeksPrzeciwnika = (i + 1) % iloscGrup;
            para.add(listaDrugichMiejsc.get(indeksPrzeciwnika));

            getListaParDrużyn().add(para);
        }

        obliczIUstawLiczbęRund();
    }
}

class Liga extends Rozgrywka {
    private int liczbaMeczyBezpośrednich;
    private HashMap<String, HashMap<String, Integer>> listaLiczbyMeczyBezpośrednich;
    Liga(int liczbaMeczBezpośrednich) {
        super();
        this.liczbaMeczyBezpośrednich = liczbaMeczBezpośrednich;
        this.listaLiczbyMeczyBezpośrednich = new HashMap<String, HashMap<String, Integer>>();
    }
    @Override
    protected void zajerestrujDrużyne(Drużyna drużyna) throws RozgrywkaRozpoczętaException {
        if (getStanRozpoczęcia() == true) {
            throw new RozgrywkaRozpoczętaException("Nie można dodać drużyn po rozpoczęciu rozgrywki.");
        }
        getListaDrużyn().add(drużyna);
        getListaPunktów().put(drużyna.getNazwa(), 0);
        HashMap<String, Integer> iHashMap = new HashMap<String, Integer>();
        for (Drużyna kDrużyna : getListaDrużyn()) {
            if (kDrużyna.getNazwa() != drużyna.getNazwa()) {
                iHashMap.put(kDrużyna.getNazwa(), 0);
            }
        }
        getListaLiczbyMeczyBezpośrednich().put(drużyna.getNazwa(), iHashMap);
        for (Drużyna lDrużyna : getListaDrużyn()) {
            for (Drużyna nDrużyna : getListaDrużyn()) {
                if (nDrużyna.getNazwa() != lDrużyna.getNazwa()) {
                    if (!getListaLiczbyMeczyBezpośrednich().get(lDrużyna.getNazwa()).containsKey(nDrużyna.getNazwa())) {
                        getListaLiczbyMeczyBezpośrednich().get(lDrużyna.getNazwa()).put(nDrużyna.getNazwa(), 0);
                    }
                }
            }
        }
    }
    @Override
    public void pokażTabele() throws RozgrywkaNieZakończonaException {
        if (getStanKońca() != true) {
            throw new RozgrywkaNieZakończonaException("Wyników nie można wyświetlić podczas trwania rozgrywki.");
        }
        for (Wynik wynikIMeczu : getListaWyników()) {
            System.out.println(wynikIMeczu.getDrużyna1().getNazwa() + " vs " + wynikIMeczu.getDrużyna2().getNazwa());
            System.out.println(wynikIMeczu.getWynik1() + " : " + wynikIMeczu.getWynik2());
        }
        System.out.println("\n\nLista punktów: " + getListaPunktów());
    }
    @Override
    public void zapiszWyniki(Wynik wynikMeczu) throws ZaDużoMeczówBezpośrednichException {
        String nazwaDrużyny1 = wynikMeczu.getDrużyna1().getNazwa();
        String nazwaDrużyny2 = wynikMeczu.getDrużyna2().getNazwa();

        Integer rozegraneMeczePomiędzyDrużynami = listaLiczbyMeczyBezpośrednich.get(nazwaDrużyny1).get(nazwaDrużyny2);

        if (rozegraneMeczePomiędzyDrużynami == getLiczbaMeczyBezpośrednich()) {
            throw new ZaDużoMeczówBezpośrednichException("Drużyny już zagrały między sobą przewidzianą liczbę meczy bezpośrednich");
        }

        getListaWyników().add(wynikMeczu);
        ArrayList<Integer> punkty = wynikMeczu.punktyZaMecz();

        getListaLiczbyMeczyBezpośrednich().get(nazwaDrużyny1).put(nazwaDrużyny2, rozegraneMeczePomiędzyDrużynami + 1);
        getListaLiczbyMeczyBezpośrednich().get(nazwaDrużyny2).put(nazwaDrużyny1, rozegraneMeczePomiędzyDrużynami + 1);
        
        Integer punktyDrużyny1 = getListaPunktów().get(nazwaDrużyny1);
        Integer punktyDrużyny2 = getListaPunktów().get(nazwaDrużyny2);
        getListaPunktów().put(nazwaDrużyny1, punktyDrużyny1 + punkty.get(0));
        getListaPunktów().put(nazwaDrużyny2, punktyDrużyny2 + punkty.get(1));
        
        boolean czyBędzieKoniec = true;
        for (HashMap<String,Integer> hashMapBezpośrednichMeczyDrużyny : listaLiczbyMeczyBezpośrednich.values()) {
            for (Integer liczbaMeczyBezpośrednichDlaDrużyny : hashMapBezpośrednichMeczyDrużyny.values()) {
                if (liczbaMeczyBezpośrednichDlaDrużyny < liczbaMeczyBezpośrednich) {
                    czyBędzieKoniec = false;
                    break;
                }
            }
        }
        if (czyBędzieKoniec) {
            zmieńStanKońca();
        }
    }
    public int getLiczbaMeczyBezpośrednich() {
        return liczbaMeczyBezpośrednich;
    }
    public HashMap<String, HashMap<String, Integer>> getListaLiczbyMeczyBezpośrednich() {
        return listaLiczbyMeczyBezpośrednich;
    }
    public ArrayList<Drużyna> zwróćXDrużynPoPunktacji(int ileDrużynDoZwrócenia) throws ZaDużoDoZwróceniaException {
        if (ileDrużynDoZwrócenia > getListaDrużyn().size()) {
            throw new ZaDużoDoZwróceniaException("Nie ma tyle drużyn do zwrócenia.");
        }
        ArrayList<Drużyna> posortowaneDrużyny = new ArrayList<Drużyna>();
        for (int i = 0; i < getListaDrużyn().size(); i++) {
            posortowaneDrużyny.add(getListaDrużyn().get(i));
        }
        for (int i = 0; i < posortowaneDrużyny.size(); i++) {
            for (int j = 1; j < posortowaneDrużyny.size() - i; j++) {
                Drużyna d1 = posortowaneDrużyny.get(j - 1);
                Drużyna d2 = posortowaneDrużyny.get(j);
                int punkty1 = getListaPunktów().get(d1.getNazwa());
                int punkty2 = getListaPunktów().get(d2.getNazwa());
                if (punkty1 < punkty2) {
                    posortowaneDrużyny.set(j - 1, d2);
                    posortowaneDrużyny.set(j, d1);
                } 
                else if (punkty1 == punkty2) {
                    int bramki1 = 0;
                    for (int k = 0; k < getListaWyników().size(); k++) {
                        Wynik w = getListaWyników().get(k);
                        if (w.getDrużyna1().getNazwa().equals(d1.getNazwa())) {
                            bramki1 += w.getWynik1();
                        }
                        else if (w.getDrużyna2().getNazwa().equals(d1.getNazwa())) {
                            bramki1 += w.getWynik2();
                        }
                    }
                    int bramki2 = 0;
                    for (int k = 0; k < getListaWyników().size(); k++) {
                        Wynik w = getListaWyników().get(k);
                        if (w.getDrużyna1().getNazwa().equals(d2.getNazwa())) {
                            bramki2 += w.getWynik1();
                        } else if (w.getDrużyna2().getNazwa().equals(d2.getNazwa())) {
                            bramki2 += w.getWynik2();
                        }
                    }
                    if (bramki1 < bramki2) {
                        posortowaneDrużyny.set(j - 1, d2);
                        posortowaneDrużyny.set(j, d1);
                    } 
                    else if (bramki1 == bramki2) {
                        if (d1.getPoziomDrużyny() < d2.getPoziomDrużyny()) {
                            posortowaneDrużyny.set(j - 1, d2);
                            posortowaneDrużyny.set(j, d1);
                        }
                    }
                }
            }
        }
        ArrayList<Drużyna> topDrużyny = new ArrayList<Drużyna>();
        int limit = ileDrużynDoZwrócenia;
        if (limit > posortowaneDrużyny.size()) {
            limit = posortowaneDrużyny.size();
        }
        System.out.println("Top " + limit + " Najlepszych drużyn");
        for (int i = 0; i < limit; i++) {
            Drużyna d = posortowaneDrużyny.get(i);
            int p = getListaPunktów().get(d.getNazwa());
            int bramki = 0;
            for (int k = 0; k < getListaWyników().size(); k++) {
                Wynik w = getListaWyników().get(k);
                if (w.getDrużyna1().getNazwa().equals(d.getNazwa())) {
                    bramki += w.getWynik1();
                } else if (w.getDrużyna2().getNazwa().equals(d.getNazwa())) {
                    bramki += w.getWynik2();
                }
            }
            System.out.println((i + 1) + ". " + d.getNazwa() + " (Punkty: " + p + " Bramki: " + bramki + " Poziom drużyny: " + d.getPoziomDrużyny() + ")");
            topDrużyny.add(d);
        }
        return topDrużyny;
    }
    public void rozegrajMeczeRecznie() throws ProjektowyException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n===LIGA===");

        for (int i = 0; i < getListaDrużyn().size(); i++) {
            for (int j = i + 1; j < getListaDrużyn().size(); j++) {
                Drużyna d1 = getListaDrużyn().get(i);
                Drużyna d2 = getListaDrużyn().get(j);

                System.out.println("\nMECZ: " + d1.getNazwa() + " vs " + d2.getNazwa());

                System.out.print("Podaj gole dla " + d1.getNazwa() + ": ");
                int goleD1 = scanner.nextInt();

                System.out.print("Podaj gole dla " + d2.getNazwa() + ": ");
                int goleD2 = scanner.nextInt();

                ArrayList<Integer> wynik = new ArrayList<>();
                wynik.add(goleD1);
                wynik.add(goleD2);

                Wynik wpisanyWynik = new Wynik(d1, d2, wynik);

                try {
                    zapiszWyniki(wpisanyWynik);
                    System.out.println("Zapisano wynik: " + goleD1 + ":" + goleD2);
                } catch (ZaDużoMeczówBezpośrednichException e) {
                    System.out.println("BŁĄD ZAPISU: " + e.getMessage());
                }
            }
        }
        scanner.close();
    }

}

class LigaPlusTurniej extends Turniej {
    private int liczbaDrużynDoEtapuTurnieju;
    private Liga etapLigi;

    LigaPlusTurniej(Liga etapLigi, int liczbaDrużynDoEtapuTurnieju) throws EtapLigiNieSkończonyException {
        super();
        if (etapLigi.getStanKońca() == false) {
            throw new EtapLigiNieSkończonyException("Nie skończono etapu ligi.");
        }
        this.etapLigi = etapLigi;
        this.liczbaDrużynDoEtapuTurnieju = liczbaDrużynDoEtapuTurnieju;
    }
    public Turniej przygotujTurniej() throws ZłaLiczbaDrużynException, ZaDużoDoZwróceniaException, RozgrywkaRozpoczętaException  {
        int n = liczbaDrużynDoEtapuTurnieju;
        if ((n & n-1) != 0) {
            throw new ZłaLiczbaDrużynException("Podano złą liczbę drużyn, musi ona być potęgą dwójki");
        }
        ArrayList<Drużyna> topDrużyny = etapLigi.zwróćXDrużynPoPunktacji(n);

        if (getStanRozpoczęcia() == true) {
            throw new RozgrywkaRozpoczętaException("Rozpoczęto rozgrywkę, nie można dodać nowej drużyny.");
        }
        Turniej etapTurnieju = new Turniej();
        for (int i = 0; i < topDrużyny.size(); i++) {
            etapTurnieju.zajerestrujDrużyne(topDrużyny.get(i));
        }
        // 
        for (int i = 0; i < n / 2; i++) {
            Drużyna najlepsza = topDrużyny.get(i);
            Drużyna najgorsza = topDrużyny.get(n - 1 - i);

            ArrayList<Drużyna> para = new ArrayList<>();
            para.add(najlepsza);
            para.add(najgorsza);

            getListaParDrużyn().add(para);
        }
        int ilePar = getListaParDrużyn().size();
        int liczbaRund=0;
        while (ilePar > 0) {
            liczbaRund=liczbaRund+1;            
            ilePar = ilePar / 2; 
        }
        return etapTurnieju;
    }
}

class GrupyPlusTurniej extends TurniejeZbiorowo {
    private int liczbaGrup;

    private ArrayList<ArrayList<Drużyna>> listaKoszyków = new ArrayList<>();
    private ArrayList<ArrayList<Drużyna>> listaGrup = new ArrayList<>();

    private ArrayList<Drużyna> listaPierwszychMiejsc = new ArrayList<>();
    private ArrayList<Drużyna> listaDrugichMiejsc = new ArrayList<>();

    GrupyPlusTurniej(int liczbaDrużyn) throws ZłaLiczbaDrużynException {
        super();
        if (liczbaDrużyn % 2 == 0) {
            this.liczbaGrup = liczbaDrużyn / 4;
        }
        else {
            throw new ZłaLiczbaDrużynException("Podano złą liczbę drużyn");
        }
    }
    protected void losujGrupy() {
        ArrayList<Drużyna> posortowaneDrużyny = new ArrayList<>(getListaDrużyn());
        posortowaneDrużyny.sort((d1, d2) -> Integer.compare(d1.getPoziomDrużyny(), d2.getPoziomDrużyny()));
        listaKoszyków.clear();
        listaGrup.clear();

        for (int i = 0; i < 4; i++) {
            listaKoszyków.add(new ArrayList<>());
        }
        int teamIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < liczbaGrup; j++) {
                listaKoszyków.get(i).add(posortowaneDrużyny.get(teamIndex));
                teamIndex++;
            }
        }

        Random rnd = new Random();
        for (int i = 0; i < liczbaGrup; i++) {
            ArrayList<Drużyna> grupa = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                int los = rnd.nextInt(listaKoszyków.get(j).size());
                grupa.add(listaKoszyków.get(j).get(los));
                listaKoszyków.get(j).remove(los);
            }
            listaGrup.add(grupa);
        }
        System.out.println("Zakończono losowanie do grup");
    }

    protected void stwórzLigi() throws Exception {
        for (int i = 0; i < liczbaGrup; i++) {
            System.out.println("\n==================================");
            System.out.println("GRUPA " + (i+1));
            System.out.println("==================================");

            ArrayList<Drużyna> aktualnaGrupa = listaGrup.get(i);
            Liga fazaGrupowa = new Liga(1);

            for (Drużyna d : aktualnaGrupa) {
                fazaGrupowa.zajerestrujDrużyne(d);
            }

            fazaGrupowa.rozegrajMeczeRecznie();

            System.out.println("\n---TABELA GRUPY " + (i+1) + "---");
            fazaGrupowa.pokażTabele();

            ArrayList<Drużyna> awans = fazaGrupowa.zwróćXDrużynPoPunktacji(2);
            listaPierwszychMiejsc.add(awans.get(0));
            listaDrugichMiejsc.add(awans.get(1));
        }
    }

    protected void awanse(Liga fazaGrupowa) throws ZaDużoDoZwróceniaException {
        for (int i = 0; i < liczbaGrup; i++) {
            ArrayList<Drużyna> awans = fazaGrupowa.zwróćXDrużynPoPunktacji(2);
            listaPierwszychMiejsc.add(awans.get(0));
            listaDrugichMiejsc.add(awans.get(1));
        }
    }

    protected void fazapucharowa() throws ZaDużoMeczyWRundzieException, ZłaParaDrużynException, ZaMałoZwyciezcowException, ZaDużoRundException, RozgrywkaRozpoczętaException, RozgrywkaNieRozpoczętaException, DrużynaJużZagrałaMeczException {
        TurniejGrupowy fazaPucharowa = new TurniejGrupowy();

        for (Drużyna d : listaPierwszychMiejsc) {
            fazaPucharowa.zajerestrujDrużyne(d);
        }
        for (Drużyna d : listaDrugichMiejsc) { 
            fazaPucharowa.zajerestrujDrużyne(d); 
        }

        fazaPucharowa.losujParyZKoszykow(listaPierwszychMiejsc, listaDrugichMiejsc);
        Scanner scanner = new Scanner(System.in);

        while (fazaPucharowa.getListaParDrużyn().size() > 0) {
            int liczbaMeczow = fazaPucharowa.getListaParDrużyn().size();
            System.out.println("\n--- ETAP: " + (liczbaMeczow == 1 ? "WIELKI FINAŁ" : liczbaMeczow + " MECZE") + " ---");

            for (ArrayList<Drużyna> para : fazaPucharowa.getListaParDrużyn()) {
                Drużyna d1 = para.get(0);
                Drużyna d2 = para.get(1);

                System.out.println("\nMECZ: " + d1.getNazwa() + " vs " + d2.getNazwa());
                System.out.print("Podaj gole dla " + d1.getNazwa() + ": ");
                int goleD1 = scanner.nextInt();
                System.out.print("Podaj gole dla " + d2.getNazwa() + ": ");
                int goleD2 = scanner.nextInt();

                ArrayList<Integer> bramki = new ArrayList<>();
                bramki.add(goleD1);
                bramki.add(goleD2);

                Wynik wynikMeczu = new Wynik(d1, d2, bramki);
                if (getListaZwycięzcówRundy().containsValue(d1)||getListaZwycięzcówRundy().containsValue(d2)) {
                    throw new DrużynaJużZagrałaMeczException("Jedna z drużyn zagrała już mecz w tej rundzie.");
                }
                fazaPucharowa.zapiszWyniki(wynikMeczu);
            }
            fazaPucharowa.przejdźDoNastępnejRundy();

            if (liczbaMeczow == 1) {
                break;
            }
        }
        scanner.close();
    }
    public ArrayList<Drużyna> getListaPierwszychMiejsc() {
        return listaPierwszychMiejsc;
    }
    public ArrayList<Drużyna> getListaDrugichMiejsc() {
        return listaDrugichMiejsc;
    }
}

class Drużyna {
    private String nazwa;
    private int poziomDrużyny;
    Drużyna(String nazwa, int poziomDrużyny) throws PustePoleException {
        if (nazwa.equals("")) {
            throw new PustePoleException("Drużyna musi mieć nazwę.");
        }
        this.nazwa = nazwa;
        this.poziomDrużyny = poziomDrużyny;
    }
    public String getNazwa() {
        return nazwa;
    }
    public int getPoziomDrużyny() {
        return poziomDrużyny;
    }
}

class Wynik {
    private Drużyna zwycięzcaMeczu;
    private ArrayList<Integer> wynik;
    private Drużyna drużyna1;
    private Drużyna drużyna2;
    Wynik(Drużyna drużyna1, Drużyna drużyna2, ArrayList<Integer> wynik) {
        this.drużyna1 = drużyna1;
        this.drużyna2 = drużyna2;
        this.wynik = wynik;
    }
    public Drużyna getDrużyna1() {
        return drużyna1;
    }
    public Drużyna getDrużyna2() {
        return drużyna2;
    }
    public ArrayList<Integer> getWynik() {
        return wynik;
    }
    public int getWynik1() {
        return wynik.get(0);
    }
    public int getWynik2() {
        return wynik.get(1);
    }
    public Drużyna getZwycięzcaMeczu() {
        return zwycięzcaMeczu;
    }

    public ArrayList<Integer> punktyZaMecz() {
        ArrayList<Integer> punkty = new ArrayList<Integer>(2);
        if (getWynik1() > getWynik2()) {
            punkty.add(3);
            punkty.add(0);
            zwycięzcaMeczu = drużyna1;
        }
        else if (getWynik1() < getWynik2()) {
            punkty.add(0);
            punkty.add(3);
            zwycięzcaMeczu = drużyna2;
        }
        else {
            punkty.add(1);
            punkty.add(1);
        }
        return punkty;
    }
}

class Menu {
    private ArrayList<String> archiwum;
    Menu() {
        this.archiwum = new ArrayList<String>();
    }
    public void run() {
        Scanner menuScanner = new Scanner(System.in);
        String command = "";
        while (!command.equals("koniec")) {
            System.out.print("\n1.Utwórz rozgrywkę.\n2.Przejrzyj archiwum.\n(koniec - aby zakończyć):");
            command = menuScanner.nextLine();
            switch (command) {
                case "1":
                    System.out.print("\n1.Liga.\n2.Turniej.\n2.Liga+Turniej.\n2.Grupy+Turniej.\n:");
                    command = menuScanner.nextLine();
                    switch (command) {
                        case "1":
                            int liczbaMeczBezpośrednichOdpowiedź;
                            System.out.println("\nPodaj liczbę meczy bezpośrednich:");
                            try {
                                liczbaMeczBezpośrednichOdpowiedź = Integer.parseInt(menuScanner.nextLine());
                            }catch (NumberFormatException e){
                                System.out.println("Niepoprawna liczba meczy bezpośrednich.");
                                break;
                            }
                            try {
                                przeprowadźLigę(menuScanner, liczbaMeczBezpośrednichOdpowiedź);
                            } catch (ProjektowyException e) {
                                System.out.println(e);
                            }
                            break;
                        case "2":
                            try {
                                przeprowadźTurniej(menuScanner);
                            } catch (ProjektowyException e) {
                                System.out.println(e);
                            }
                            break;
                        case "3":
                            int liczbaMeczBezpośrednich2Odpowiedź;
                            System.out.println("\nPodaj liczbę meczy bezpośrednich:");
                            try {
                                liczbaMeczBezpośrednich2Odpowiedź = Integer.parseInt(menuScanner.nextLine());
                            } catch (NumberFormatException e){
                                System.out.println("Niepoprawna liczba meczy bezpośrednich.");
                                break;
                            }
                            int liczbaDrużynDoTurniejuOdpowiedź;
                            System.out.println("\nPodaj liczbę drużyn do etapu turnieju:");
                            try {
                                liczbaDrużynDoTurniejuOdpowiedź = Integer.parseInt(menuScanner.nextLine());
                            } catch (NumberFormatException e){
                                System.out.println("Niepoprawna liczba drużyn.");
                                break;
                            }
                            try {
                                przeprowadźLigaPlusTurniej(menuScanner, liczbaMeczBezpośrednich2Odpowiedź, liczbaDrużynDoTurniejuOdpowiedź);
                            } catch (ProjektowyException e) {
                                System.out.println(e);
                            }
                            break;
                        case "4":
                            int liczbaDrużynOdpowiedź;
                            System.out.println("\nPodaj liczbę drużyn:\n:");
                            try {
                                liczbaDrużynOdpowiedź = Integer.parseInt(menuScanner.nextLine());
                            }catch (NumberFormatException e){
                                System.out.println("Niepoprawna liczba Drużyn.");
                                break;
                            }
                            try {
                                przeprowadźGrupyPlusTurniej(liczbaDrużynOdpowiedź);
                            } catch (ProjektowyException e) {
                                System.out.println(e);
                            }
                            break;
                        default:
                            System.out.println("Brak komendy, powrót do wyboru działania.");
                            break;
                    }
                    break;
                case "2":
                    System.out.println("Archiwum:");
                    for (String infoRozgrywki : archiwum) {
                        System.out.println(infoRozgrywki + "\n");
                    }
                    break;
                case "koniec":
                    break;
                default:
                    System.out.println("Brak komendy.");
                    break;
            }
        }

        menuScanner.close();
    }
    public void przeprowadźLigę(Scanner scanner,int liczbaMeczBezpośrednich) throws ProjektowyException {
        Liga liga = new Liga(liczbaMeczBezpośrednich);
        String command = "";
        while (!command.equals("stop")) {
            System.out.println("\n1.Zarejestruj drużynę.\n2.Zacznij rozgrywkę.\n(stop - aby zakończyć):");
            command = scanner.nextLine();
            switch (command) {
                case "1":
                    String rejestracjaOdpowiedź1 = "";
                    int rejestracjaOdpowiedź2;
                    System.out.println("\nPodaj nazwę drużyny:");
                    rejestracjaOdpowiedź1 = scanner.nextLine();
                    System.out.println("\nPodaj poziom drużyny:");
                    try {
                        rejestracjaOdpowiedź2 = Integer.parseInt(scanner.nextLine());
                        Drużyna d = new Drużyna(rejestracjaOdpowiedź1, rejestracjaOdpowiedź2);
                        liga.zajerestrujDrużyne(d);
                        System.out.println(liga.getListaDrużyn());
                    }catch (NumberFormatException e){
                        System.out.println("Niepoprawny poziom drużyny.");
                        break;
                    }catch (ProjektowyException e) {
                        System.out.println(e);
                        break;
                    }
                    break;
                case "2":
                    while (!command.equals("stop")) {
                        System.out.println("\n1.Zapisz wynik.\n2.Pokaż tabele.\n3.Zapisz do archiwum.\n(stop - aby zakończyć):");
                        command = scanner.nextLine();
                        switch (command) {
                            case "1":
                                System.out.println("\nWprowadzanie wyniku:\n");
                                String drużynaNazwaOdpowiedź1 = "";
                                String drużynaNazwaOdpowiedź2 = "";
                                int drużynaGoleOdpowiedź1;
                                int drużynaGoleOdpowiedź2;
                                System.out.println("\nPodaj nazwę drużyny 1:");
                                drużynaNazwaOdpowiedź1 = scanner.nextLine();
                                System.out.println("\nPodaj gole drużyny 1:");
                                try {
                                    drużynaGoleOdpowiedź1 = Integer.parseInt(scanner.nextLine());
                                }catch (NumberFormatException e){
                                    System.out.println("Niepoprawna liczba goli.");
                                    break;
                                }
                                System.out.println("\nPodaj nazwę drużyny 2:");
                                drużynaNazwaOdpowiedź2 = scanner.nextLine();
                                System.out.println("\nPodaj gole drużyny 2:");
                                try {
                                    drużynaGoleOdpowiedź2 = Integer.parseInt(scanner.nextLine());
                                }catch (NumberFormatException e){
                                    System.out.println("Niepoprawna liczba goli.");
                                    break;
                                }
                                try {
                                    liga.stworzIZapiszWyniki(liga.getDrużynaPoNazwie(drużynaNazwaOdpowiedź1), liga.getDrużynaPoNazwie(drużynaNazwaOdpowiedź2), drużynaGoleOdpowiedź1, drużynaGoleOdpowiedź2);
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "2":
                                try {
                                    liga.pokażTabele();
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "3":
                                try {
                                    archiwum.add(liga.getCaleInfo());
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "stop":
                                break;
                            default:
                                System.out.println("Brak komendy.");
                                break;
                        }
                    }
                    String odpowiedź1 = "";
                    System.out.println("\nPodaj nazwę drużyny:\n:");
                    switch (odpowiedź1) {
                        
                    }
                    break;
                case "stop":
                    break;
                default:
                    System.out.println("Brak komendy.");
                    break;
            }
        }
    }
    public void przeprowadźTurniej(Scanner scanner) throws ProjektowyException {
        Turniej turniej = new Turniej(scanner);
        String command = "";
        while (!command.equals("stop")) {
            System.out.println("\n1.Zarejestruj drużynę.\n2.Zacznij rozgrywkę.\n(stop - aby zakończyć):");
            command = scanner.nextLine();
            switch (command) {
                case "1":
                    String rejestracjaOdpowiedź1 = "";
                    int rejestracjaOdpowiedź2;
                    System.out.println("\nPodaj nazwę drużyny:");
                    rejestracjaOdpowiedź1 = scanner.nextLine();
                    System.out.println("\nPodaj poziom drużyny:");
                    try {
                        rejestracjaOdpowiedź2 = Integer.parseInt(scanner.nextLine());
                        Drużyna d = new Drużyna(rejestracjaOdpowiedź1, rejestracjaOdpowiedź2);
                        turniej.zajerestrujDrużyne(d);
                        System.out.println(turniej.getListaDrużyn());
                    }catch (NumberFormatException e){
                        System.out.println("Niepoprawny poziom drużyny.");
                        break;
                    }catch (ProjektowyException e) {
                        System.out.println(e);
                        break;
                    }
                    break;
                case "2":
                    while (!command.equals("stop")) {
                        System.out.print("\n1.Losuj pary drużyny.\n2.Zapisz wynik.\n3.Przejdź do następnej rundy.\n4.Pokaż tabele.\n5.Zapisz do archiwum.\n(stop - aby zakończyć):");
                        command = scanner.nextLine();
                        switch (command) {
                            case "1":
                                try {
                                    turniej.losujParyDrużyn(turniej.getListaDrużyn());
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "2":
                                System.out.println("\nWprowadzanie wyniku:\n");
                                String drużynaNazwaOdpowiedź1 = "";
                                String drużynaNazwaOdpowiedź2 = "";
                                int drużynaGoleOdpowiedź1;
                                int drużynaGoleOdpowiedź2;
                                System.out.println("\nPodaj nazwę drużyny 1:");
                                drużynaNazwaOdpowiedź1 = scanner.nextLine();
                                System.out.println("\nPodaj gole drużyny 1:");
                                try {
                                    drużynaGoleOdpowiedź1 = Integer.parseInt(scanner.nextLine());
                                }catch (NumberFormatException e){
                                    System.out.println("Niepoprawna liczba goli.");
                                    break;
                                }
                                System.out.println("\nPodaj nazwę drużyny 2:");
                                drużynaNazwaOdpowiedź2 = scanner.nextLine();
                                System.out.println("\nPodaj gole drużyny 2:");
                                try {
                                    drużynaGoleOdpowiedź2 = Integer.parseInt(scanner.nextLine());
                                }catch (NumberFormatException e){
                                    System.out.println("Niepoprawna liczba goli.");
                                    break;
                                }
                                try {
                                    turniej.stworzIZapiszWyniki(turniej.getDrużynaPoNazwie(drużynaNazwaOdpowiedź1), turniej.getDrużynaPoNazwie(drużynaNazwaOdpowiedź2), drużynaGoleOdpowiedź1, drużynaGoleOdpowiedź2);
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "3":
                                try {
                                    turniej.przejdźDoNastępnejRundy();
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "4":
                                try {
                                    turniej.pokażTabele();
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "5":
                                try {
                                    archiwum.add(turniej.getCaleInfo());
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "stop":
                                break;
                            default:
                                System.out.println("Brak komendy.");
                                break;
                        }
                    }
                case "stop":
                    break;
                default:
                    System.out.println("Brak komendy.");
                    break;
            }
        }
    }
    public void przeprowadźTurniej(Scanner scanner, Turniej etapTurniejowy) throws ProjektowyException {
        Turniej turniej = etapTurniejowy;
        String command = "";
        while (!command.equals("stop")) {
            System.out.println("\n1.Zacznij rozgrywkę.\n(stop - aby zakończyć):");
            command = scanner.nextLine();
            switch (command) {
                case "1":
                    while (!command.equals("stop")) {
                        System.out.print("\n1.Losuj pary drużyny.\n2.Zapisz wynik.\n3.Przejdź do następnej rundy.\n4.Pokaż tabele.\n5.Zapisz do archiwum.\n(stop - aby zakończyć):");
                        command = scanner.nextLine();
                        switch (command) {
                            case "1":
                                try {
                                    turniej.losujParyDrużyn(turniej.getListaDrużyn());
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "2":
                                System.out.println("\nWprowadzanie wyniku:\n");
                                String drużynaNazwaOdpowiedź1 = "";
                                String drużynaNazwaOdpowiedź2 = "";
                                int drużynaGoleOdpowiedź1;
                                int drużynaGoleOdpowiedź2;
                                System.out.println("\nPodaj nazwę drużyny 1:");
                                drużynaNazwaOdpowiedź1 = scanner.nextLine();
                                System.out.println("\nPodaj gole drużyny 1:");
                                try {
                                    drużynaGoleOdpowiedź1 = Integer.parseInt(scanner.nextLine());
                                }catch (NumberFormatException e){
                                    System.out.println("Niepoprawna liczba goli.");
                                    break;
                                }
                                System.out.println("\nPodaj nazwę drużyny 2:");
                                drużynaNazwaOdpowiedź2 = scanner.nextLine();
                                System.out.println("\nPodaj gole drużyny 2:");
                                try {
                                    drużynaGoleOdpowiedź2 = Integer.parseInt(scanner.nextLine());
                                }catch (NumberFormatException e){
                                    System.out.println("Niepoprawna liczba goli.");
                                    break;
                                }
                                try {
                                    turniej.stworzIZapiszWyniki(turniej.getDrużynaPoNazwie(drużynaNazwaOdpowiedź1), turniej.getDrużynaPoNazwie(drużynaNazwaOdpowiedź2), drużynaGoleOdpowiedź1, drużynaGoleOdpowiedź2);
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "3":
                                try {
                                    turniej.przejdźDoNastępnejRundy();
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "4":
                                try {
                                    turniej.pokażTabele();
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "5":
                                try {
                                    archiwum.add(turniej.getCaleInfo());
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "stop":
                                break;
                            default:
                                System.out.println("Brak komendy.");
                                break;
                        }
                    }
                case "stop":
                    break;
                default:
                    System.out.println("Brak komendy.");
                    break;
            }
        }
    }
    public void przeprowadźLigaPlusTurniej(Scanner scanner,int liczbaMeczBezpośrednich,int liczbaDrużynDoEtapuTurnieju) throws ProjektowyException {
        if ((liczbaDrużynDoEtapuTurnieju & liczbaDrużynDoEtapuTurnieju-1) != 0) {
            throw new ZłaLiczbaDrużynException("Zła liczba drużyn");
        }
        Liga etapLigi = new Liga(liczbaMeczBezpośrednich);
        String command = "";
        while (!command.equals("stop")) {
            System.out.println("\n1.Zarejestruj drużynę.\n2.Zacznij rozgrywkę.\n(stop - aby zakończyć):");
            command = scanner.nextLine();
            switch (command) {
                case "1":
                    String rejestracjaOdpowiedź1 = "";
                    int rejestracjaOdpowiedź2;
                    System.out.println("\nPodaj nazwę drużyny:");
                    rejestracjaOdpowiedź1 = scanner.nextLine();
                    System.out.println("\nPodaj poziom drużyny:");
                    try {
                        rejestracjaOdpowiedź2 = Integer.parseInt(scanner.nextLine());
                        Drużyna d = new Drużyna(rejestracjaOdpowiedź1, rejestracjaOdpowiedź2);
                        etapLigi.zajerestrujDrużyne(d);
                        System.out.println(etapLigi.getListaDrużyn());
                    }catch (NumberFormatException e){
                        System.out.println("Niepoprawny poziom drużyny.");
                        break;
                    }catch (ProjektowyException e) {
                        System.out.println(e);
                        break;
                    }
                    break;
                case "2":
                    while (!command.equals("stop")) {
                        System.out.println("\n1.Zapisz wynik etapu ligi.\n2.Przejdź do etapu turnieju.\n(stop - aby zakończyć):");
                        command = scanner.nextLine();
                        switch (command) {
                            case "1":
                                System.out.println("\nWprowadzanie wyniku:\n");
                                String drużynaNazwaOdpowiedź1 = "";
                                String drużynaNazwaOdpowiedź2 = "";
                                int drużynaGoleOdpowiedź1;
                                int drużynaGoleOdpowiedź2;
                                System.out.println("\nPodaj nazwę drużyny 1:");
                                drużynaNazwaOdpowiedź1 = scanner.nextLine();
                                System.out.println("\nPodaj gole drużyny 1:");
                                try {
                                    drużynaGoleOdpowiedź1 = Integer.parseInt(scanner.nextLine());
                                }catch (NumberFormatException e){
                                    System.out.println("Niepoprawna liczba goli.");
                                    break;
                                }
                                System.out.println("\nPodaj nazwę drużyny 2:");
                                drużynaNazwaOdpowiedź2 = scanner.nextLine();
                                System.out.println("\nPodaj gole drużyny 2:");
                                try {
                                    drużynaGoleOdpowiedź2 = Integer.parseInt(scanner.nextLine());
                                }catch (NumberFormatException e){
                                    System.out.println("Niepoprawna liczba goli.");
                                    break;
                                }
                                try {
                                    etapLigi.stworzIZapiszWyniki(etapLigi.getDrużynaPoNazwie(drużynaNazwaOdpowiedź1), etapLigi.getDrużynaPoNazwie(drużynaNazwaOdpowiedź2), drużynaGoleOdpowiedź1, drużynaGoleOdpowiedź2);
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "2":
                                try {
                                    LigaPlusTurniej ligaPlusTurniej = new LigaPlusTurniej(etapLigi, liczbaDrużynDoEtapuTurnieju);
                                    etapLigi.pokażTabele();
                                    Turniej turniejEtap = ligaPlusTurniej.przygotujTurniej();
                                    przeprowadźTurniej(scanner, turniejEtap);
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "3":
                                try {
                                    archiwum.add(etapLigi.getCaleInfo());
                                } catch (ProjektowyException e) {
                                    System.out.println(e);
                                }
                                break;
                            case "stop":
                                break;
                            default:
                                System.out.println("Brak komendy.");
                                break;
                        }
                    }
                    String odpowiedź1 = "";
                    System.out.println("\nPodaj nazwę drużyny:\n:");
                    switch (odpowiedź1) {
                        
                    }
                    break;
                case "stop":
                    break;
                default:
                    System.out.println("Brak komendy.");
                    break;
            }
        }
    }
    public void przeprowadźGrupyPlusTurniej(int liczbaDrużyn) throws ProjektowyException {
        GrupyPlusTurniej turniej = new GrupyPlusTurniej(liczbaDrużyn);
        Scanner rozrywkaScanner = new Scanner(System.in);
        String command = "";
        while (!command.equals("stop")) {
            System.out.println("\n1.Zarejestruj drużynę.\n2.Zacznij rozgrywkę.\n(stop - aby zakończyć):");
            command = rozrywkaScanner.nextLine();
            switch (command) {
                case "1":
                    String rejestracjaOdpowiedź1 = "";
                    int rejestracjaOdpowiedź2;
                    System.out.println("\nPodaj nazwę drużyny:\n:");
                    rejestracjaOdpowiedź1 = rozrywkaScanner.nextLine();
                    System.out.println("\nPodaj poziom drużyny:\n:");
                    try {
                        rejestracjaOdpowiedź2 = Integer.parseInt(rozrywkaScanner.nextLine());
                        Drużyna d = new Drużyna(rejestracjaOdpowiedź1, rejestracjaOdpowiedź2);
                        turniej.zajerestrujDrużyne(d);
                        System.out.println(turniej.getListaDrużyn());
                    }catch (NumberFormatException e){
                        System.out.println("Niepoprawny poziom drużyny.");
                        break;
                    }catch (ProjektowyException e) {
                        System.out.println(e);
                        break;
                    }
                    break;
                case "2":
                    String odpowiedź1 = "";
                    System.out.println("\nPodaj nazwę drużyny:\n:");
                    switch (odpowiedź1) {
                        
                    }
                    break;
                case "stop":
                    break;
                default:
                    System.out.println("Brak komendy.");
                    break;
            }
        }
        rozrywkaScanner.close();
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        Menu menu = new Menu();
        menu.run();

        // System.out.println("Hello, World!");
        // Drużyna drużynaA = new Drużyna("Korsarze", 1);
        // Drużyna drużynaB = new Drużyna("Marynarze", 2);
        // Drużyna drużynaC = new Drużyna("Kosiarze", 3);
        // Drużyna drużynaD = new Drużyna("Lekarze", 4);
        // Liga liga1 = new Liga(2);
        // liga1.zajerestrujDrużyne(drużynaA);
        // liga1.zajerestrujDrużyne(drużynaB);
        // liga1.zajerestrujDrużyne(drużynaC);

        // ArrayList<Integer> wynikmeczu1 = new ArrayList<Integer>();
        // wynikmeczu1.add(0);
        // wynikmeczu1.add(1);
        // Wynik wynik1 = new Wynik(drużynaA, drużynaB, wynikmeczu1);
        // liga1.zapiszWyniki(wynik1);

        // ArrayList<Integer> wynikmeczu2 = new ArrayList<Integer>();
        // wynikmeczu2.add(3);
        // wynikmeczu2.add(2);
        // Wynik wynik2 = new Wynik(drużynaA, drużynaB, wynikmeczu2);
        // liga1.zapiszWyniki(wynik2);

        // ArrayList<Integer> wynikmeczu3 = new ArrayList<Integer>();
        // wynikmeczu3.add(5);
        // wynikmeczu3.add(3);
        // Wynik wynik3 = new Wynik(drużynaA, drużynaB, wynikmeczu3);
        // // liga1.zapiszWyniki(wynik3);

        // // liga1.pokażTabele();
        // System.out.println(liga1.zwróćXDrużynPoPunktacji(3));


        // System.out.println("\n\n");

        // System.out.println("=== WITAMY W TURNIEJU ===");

        // // 1. Tworzymy obiekt mistrzostw dla 4 drużyn (1 grupa)
        // // Jeśli podasz np. 3, program od razu rzuci wyjątek ZłaLiczbaDrużynException!
        // GrupyPlusTurniej mistrzostwa = new GrupyPlusTurniej(8);

        // // 2. Tworzymy drużyny (Nazwa, Poziom)
        // Drużyna d1 = new Drużyna("Korsarze", 1);
        // Drużyna d2 = new Drużyna("Marynarze", 2);
        // Drużyna d3 = new Drużyna("Kosiarze", 3);
        // Drużyna d4 = new Drużyna("Lekarze", 4);
        // Drużyna d5 = new Drużyna("Knicks", 5);
        // Drużyna d6 = new Drużyna("Lakers", 6);
        // Drużyna d7 = new Drużyna("Celtics", 7);
        // Drużyna d8 = new Drużyna("Rockets", 8);

        // // 3. Rejestrujemy drużyny
        // mistrzostwa.zajerestrujDrużyne(d1);
        // mistrzostwa.zajerestrujDrużyne(d2);
        // mistrzostwa.zajerestrujDrużyne(d3);
        // mistrzostwa.zajerestrujDrużyne(d4);
        // mistrzostwa.zajerestrujDrużyne(d5);
        // mistrzostwa.zajerestrujDrużyne(d6);
        // mistrzostwa.zajerestrujDrużyne(d7);
        // mistrzostwa.zajerestrujDrużyne(d8);

        // System.out.println("Drużyny pomyślnie zarejestrowane.");

        // // 4. Losowanie grup (podział na koszyki i przydział)
        // System.out.println("Trwa losowanie grup...\n");
        // mistrzostwa.losujGrupy();

        // // 5. URUCHOMIENIE ROZGRYWEK (To tutaj program zatrzyma się i poprosi o wyniki)
        // mistrzostwa.stwórzLigi();

        // // 6. Podsumowanie awansów (Drukowanie list wygenerowanych w stwórzLigi)
        // System.out.println("\n=====================================");
        // System.out.println(" PODSUMOWANIE AWANSÓW DO FAZY PUCHAROWEJ");
        // System.out.println("=====================================");

        // System.out.println("Drużyny z pierwszych miejsc:");
        // for (Drużyna d : mistrzostwa.getListaPierwszychMiejsc()) {
        //     System.out.println(" 🥇 " + d.getNazwa() + " (Poziom: " + d.getPoziomDrużyny() + ")");
        // }

        // System.out.println("\nDrużyny z drugich miejsc:");
        // for (Drużyna d : mistrzostwa.getListaDrugichMiejsc()) {
        //     System.out.println(" 🥈 " + d.getNazwa() + " (Poziom: " + d.getPoziomDrużyny() + ")");
        // }

        // // 7. (W przyszłości) Wywołanie fazy pucharowej
        // mistrzostwa.fazapucharowa();
    }
}