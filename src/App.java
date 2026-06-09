import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

class ZaDużoDoZwróceniaException extends Exception {
    public ZaDużoDoZwróceniaException(String message) {super(message);}
}

class ZłaLiczbaDrużynException extends Exception {
    public ZłaLiczbaDrużynException(String message) {super(message);}
}

class ZłaParaDrużynException extends Exception {
    public ZłaParaDrużynException(String message) {super(message);}
}

class ZaDużoMeczówBezpośrednichException extends Exception {
    public ZaDużoMeczówBezpośrednichException(String message) {super(message);}
}

class ZaDużoMeczyWRundzieException extends Exception {
    public ZaDużoMeczyWRundzieException(String message) {super(message);}
}

class ZaDużoRundException extends Exception {
    public ZaDużoRundException(String message) {super(message);}
}

class ZaMałoZwyciezcowException extends Exception {
    public ZaMałoZwyciezcowException(String message) {super(message);}
}

class ZłyWynikKarnychException extends Exception {
    public ZłyWynikKarnychException(String message) {super(message);}
}

class Rozgrywka {
    protected ArrayList<Drużyna> listaDrużyn;
    private ArrayList<Wynik> listaWyników;
    private HashMap<String, Integer> listaPunktów;
    Rozgrywka() {
        this.listaDrużyn = new ArrayList<Drużyna>();
        this.listaWyników = new ArrayList<Wynik>();
        this.listaPunktów = new HashMap<String, Integer>();
    }

    protected void zajerestrujDrużyne(Drużyna drużyna) {
        listaDrużyn.add(drużyna);
        listaPunktów.put(drużyna.getNazwa(), 0);
    }
    public void pokażTabele(){
        for (Wynik wynikIMeczu : getListaWyników()) {
            System.out.println(wynikIMeczu.getDrużyna1().getNazwa() + " vs " + wynikIMeczu.getDrużyna2().getNazwa());
            System.out.println(wynikIMeczu.getWynik1() + " : " + wynikIMeczu.getWynik2());
        }
        System.out.println("\n\n" + getListaPunktów());
    }
    public void zapiszWyniki(Wynik wynikMeczu) throws Exception {
        getListaWyników().add(wynikMeczu);
        Integer punktyDrużyny1 = getListaPunktów().get(wynikMeczu.getDrużyna1().getNazwa());
        Integer punktyDrużyny2 = getListaPunktów().get(wynikMeczu.getDrużyna2().getNazwa());
        getListaPunktów().put(wynikMeczu.getDrużyna1().getNazwa(), punktyDrużyny1 + wynikMeczu.getWynik1());
        getListaPunktów().put(wynikMeczu.getDrużyna2().getNazwa(), punktyDrużyny2 + wynikMeczu.getWynik2());
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
}

class TurniejeZbiorowo extends Rozgrywka {
    private ArrayList<ArrayList<Drużyna>> listaParDrużyn;
    private HashMap<Integer,Drużyna> listaZwycięzcówRundy;
    protected int liczbaRund = 0;

    public TurniejeZbiorowo() {
        super();
        this.listaParDrużyn = new ArrayList<ArrayList<Drużyna>>();
        this.listaZwycięzcówRundy = new HashMap<Integer,Drużyna>();
    }
    public void losujParyDrużyn(ArrayList<Drużyna> listaDrużynDoPar) throws ZłaLiczbaDrużynException {
        if ((listaDrużynDoPar.size() & listaDrużynDoPar.size() - 1)  != 0) {
            throw new ZłaLiczbaDrużynException("Podano złą liczbę drużyn, musi ona być potęgą dwójki");
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
        int liczbaDzielona = listaParDrużyn.size();
        while (liczbaDzielona != 0 && liczbaDzielona % 2 == 0) {
            liczbaDzielona = liczbaDzielona / 2;
            liczbaRund += 1;
        } 
        if (liczbaRund != 0) {
            liczbaRund += 1;
        }
    }
    @Override
    public void zapiszWyniki(Wynik wynikMeczu) throws ZłaParaDrużynException, ZaDużoMeczyWRundzieException {
        if (getListaZwycięzcówRundy().size() >= getListaParDrużyn().size()) {
            throw new ZaDużoMeczyWRundzieException("Za dużo zwycięzców w rundzie");
        }
        String nazwaDrużyny1 = wynikMeczu.getDrużyna1().getNazwa();
        String nazwaDrużyny2 = wynikMeczu.getDrużyna2().getNazwa();

        ArrayList<Integer> punkty = wynikMeczu.punktyZaMecz();
        
        Integer punktyDrużyny1 = getListaPunktów().get(nazwaDrużyny1);
        Integer punktyDrużyny2 = getListaPunktów().get(nazwaDrużyny2);
        getListaPunktów().put(nazwaDrużyny1, punktyDrużyny1 + punkty.get(0));
        getListaPunktów().put(nazwaDrużyny2, punktyDrużyny2 + punkty.get(1));    

        Drużyna zwycięzca = wynikMeczu.getZwycięzcaMeczu();
        if (zwycięzca == null) {
            // RemisException
            Scanner scanner = new Scanner(System.in);
            System.out.print("Podaj która drużyna wygrywa karne(1 lub 2): \n");
            int n = scanner.nextInt();
            //scanner.close();
            if (n == 1) {
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
            // Zamiast pytać wynikMeczu, używamy naszej zmiennej lokalnej 'zwycięzca'
            if (getListaParDrużyn().get(i).contains(zwycięzca)) {
                getListaZwycięzcówRundy().put(i, zwycięzca);
            }
        }
    }

    public ArrayList<ArrayList<Drużyna>> getListaParDrużyn() {
        return listaParDrużyn;
    }
    public int getLiczbaRund() {
        return liczbaRund;
    }
    public HashMap<Integer, Drużyna> getListaZwycięzcówRundy() {
        return listaZwycięzcówRundy;
    }
}

class Turniej extends TurniejeZbiorowo {
    private int obecnaRunda = 0;

    public Turniej() {
        super();
    }
    @Override
    public void pokażTabele(){
        for (Wynik wynikIMeczu : getListaWyników()) {
            System.out.println(wynikIMeczu.getDrużyna1().getNazwa() + " vs " + wynikIMeczu.getDrużyna2().getNazwa());
            System.out.println(wynikIMeczu.getWynik1() + " : " + wynikIMeczu.getWynik2());
        }
        System.out.println("\n\n" + getListaPunktów());
    };
    public void przejdźDoNastępnejRundy() throws ZaDużoRundException, ZaMałoZwyciezcowException {
        if (obecnaRunda == getLiczbaRund()) {
            throw new ZaDużoRundException("Za dużo rund (powinien być już zwycięzca)");
        }
        if (getListaZwycięzcówRundy().size() == getListaParDrużyn().size()/2) {
            throw new ZaMałoZwyciezcowException("Zbyt mało zwycięzców rund żeby przejść do następnej rundy");
        }
        getListaParDrużyn().clear();
        obecnaRunda += 1;
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
                System.out.println("Zwycięzca to: " + drużynaZwycięska.getNazwa());
            }
        }
        getListaZwycięzcówRundy().clear();
    }

    public void losujParyZKoszykow(ArrayList<Drużyna> listaPierwszychMiejsc, ArrayList<Drużyna> listaDrugichMiejsc) {
        getListaParDrużyn().clear(); // Czyścimy listę par z klasy bazowej
        int iloscGrup = listaPierwszychMiejsc.size();

        for (int i = 0; i < iloscGrup; i++) {
            ArrayList<Drużyna> para = new ArrayList<>();
            para.add(listaPierwszychMiejsc.get(i));

            // Trik przesunięcia indeksu o 1. Dzięki temu nigdy nie zagra z drużyną ze swojej grupy!
            int indeksPrzeciwnika = (i + 1) % iloscGrup;
            para.add(listaDrugichMiejsc.get(indeksPrzeciwnika));

            getListaParDrużyn().add(para);
        }

        int liczbaDzielona = getListaParDrużyn().size();
        liczbaRund = 0;
        while (liczbaDzielona != 0 && liczbaDzielona % 2 == 0) {
            liczbaDzielona = liczbaDzielona / 2;
            liczbaRund += 1;
        }
        if (liczbaRund != 0) {
            liczbaRund += 1;
        }
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
    protected void zajerestrujDrużyne(Drużyna drużyna) {
        getListaDrużyn().add(drużyna);
        getListaPunktów().put(drużyna.getNazwa(), 0);
        HashMap<String, Integer> iHashMap = new HashMap<String, Integer>();
        for (Drużyna kDrużyna : listaDrużyn) {
            if (kDrużyna.getNazwa() != drużyna.getNazwa()) {
                iHashMap.put(kDrużyna.getNazwa(), 0);
            }
        }
        getListaLiczbyMeczyBezpośrednich().put(drużyna.getNazwa(), iHashMap);
        for (Drużyna lDrużyna : listaDrużyn) {
            for (Drużyna nDrużyna : listaDrużyn) {
                if (nDrużyna.getNazwa() != lDrużyna.getNazwa()) {
                    if (!getListaLiczbyMeczyBezpośrednich().get(lDrużyna.getNazwa()).containsKey(nDrużyna.getNazwa())) {
                        getListaLiczbyMeczyBezpośrednich().get(lDrużyna.getNazwa()).put(nDrużyna.getNazwa(), 0);
                    }
                }
            }
        }
    }
    @Override
    public void pokażTabele() {
        for (Wynik wynikIMeczu : getListaWyników()) {
            System.out.println(wynikIMeczu.getDrużyna1().getNazwa() + " vs " + wynikIMeczu.getDrużyna2().getNazwa());
            System.out.println(wynikIMeczu.getWynik1() + " : " + wynikIMeczu.getWynik2());
        }
        System.out.println("\n\nLista punktów: " + getListaPunktów());
        //System.out.println("\n\nLista liczby zagranych meczów bezpośrednich:" + getListaLiczbyMeczyBezpośrednich());
    }
    @Override
    public void zapiszWyniki(Wynik wynikMeczu) throws ZaDużoMeczówBezpośrednichException {
        String nazwaDrużyny1 = wynikMeczu.getDrużyna1().getNazwa();
        String nazwaDrużyny2 = wynikMeczu.getDrużyna2().getNazwa();

        Integer rozegraneMeczePomiędzyDrużynami = getListaLiczbyMeczyBezpośrednich().get(nazwaDrużyny1).get(nazwaDrużyny2);

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
    //--------------------------------------------------------------------------------------
    public void rozegrajMeczeRecznie() throws Exception {
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
    }
    //--------------------------------------------------------------------------------------

}

class GrupyPlusTurniej extends TurniejeZbiorowo {
    private int liczbaDrużyn;
    private int liczbaGrup;

    private ArrayList<ArrayList<Drużyna>> listaKoszyków = new ArrayList<>();
    private ArrayList<ArrayList<Drużyna>> listaGrup = new ArrayList<>();
    private ArrayList<ArrayList<Drużyna>> listaPar = new ArrayList<>();

    private ArrayList<Drużyna> listaPierwszychMiejsc = new ArrayList<>();
    private ArrayList<Drużyna> listaDrugichMiejsc = new ArrayList<>();

    public ArrayList<Drużyna> getListaPierwszychMiejsc() {return listaPierwszychMiejsc;}
    public ArrayList<Drużyna> getListaDrugichMiejsc() {return listaDrugichMiejsc;}

    public GrupyPlusTurniej(int liczbaDrużyn) throws ZłaLiczbaDrużynException {
        super();
        if (liczbaDrużyn % 2 == 0) {
            this.liczbaDrużyn = liczbaDrużyn;
            this.liczbaGrup = liczbaDrużyn / 4; // DOPIERO TUTAJ obliczamy, gdy mamy już liczbę drużyn!
        }
        else {throw new ZłaLiczbaDrużynException("Podano złą liczbę drużyn");}
    }

    /*protected void losujGrupy() {
        ArrayList<Drużyna> posortowaneDrużyny = new ArrayList<>(listaDrużyn);
        posortowaneDrużyny.sort((d1, d2) -> Integer.compare(d1.getPoziomDrużyny(), d2.getPoziomDrużyny()));
        if (listaDrużyn == null) {listaDrużyn = new ArrayList<>();}
        else {listaDrużyn.clear();}
        if (listaKoszyków == null) {listaKoszyków = new ArrayList<>();}
        else {listaKoszyków.clear();}
        for (int i = 0; i < posortowaneDrużyny.size() - 3; i+=4) {
            ArrayList<Drużyna> koszyk = new ArrayList<>();
            koszyk.add(posortowaneDrużyny.get(i));
            koszyk.add(posortowaneDrużyny.get(i+1));
            koszyk.add(posortowaneDrużyny.get(i+2));
            koszyk.add(posortowaneDrużyny.get(i+3));
            listaKoszyków.add(koszyk);
        }
        Random rnd = new Random();
        for (int i = 0; i < liczbaGrup; i++) {
            ArrayList<Drużyna> grupa = new ArrayList<>();
            int los = rnd.nextInt(4-i);
            grupa.add(listaKoszyków.get(0).get(los));
            listaKoszyków.get(0).remove(los);
            los = rnd.nextInt(4-i);
            grupa.add(listaKoszyków.get(1).get(los));
            listaKoszyków.get(1).remove(los);
            los = rnd.nextInt(4-i);
            grupa.add(listaKoszyków.get(2).get(los));
            listaKoszyków.get(2).remove(los);
            los = rnd.nextInt(4-i);
            grupa.add(listaKoszyków.get(3).get(los));
            listaKoszyków.get(3).remove(los);
            los = rnd.nextInt(4-i);
            listaGrup.add(grupa);
        }
    }*/
    protected void losujGrupy() {
        ArrayList<Drużyna> posortowaneDrużyny = new ArrayList<>(listaDrużyn);
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

    /*protected void losujParyZKoszykow() {
        Random rnd = new Random();
        for (int i = 0; i < listaPierwszychMiejsc.size(); i++) {
            ArrayList<Drużyna> para = new ArrayList<>();
            para.add(listaPierwszychMiejsc.get(i));
            int los = rnd.nextInt(listaPierwszychMiejsc.size() - i);
            do {
                los = rnd.nextInt(listaDrugichMiejsc.size() - i);
            } while (los == i);
            para.add(listaDrugichMiejsc.get(los));
            listaPar.add(para);
            listaDrugichMiejsc.remove(los);
        }
    }*/

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

    protected void fazapucharowa() throws ZaDużoMeczyWRundzieException, ZłaParaDrużynException, ZaMałoZwyciezcowException, ZaDużoRundException {
        Turniej fazaPucharowa = new Turniej();

        for (Drużyna d : listaPierwszychMiejsc) { fazaPucharowa.zajerestrujDrużyne(d); }
        for (Drużyna d : listaDrugichMiejsc) { fazaPucharowa.zajerestrujDrużyne(d); }

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
                fazaPucharowa.zapiszWyniki(wynikMeczu);
            }
            fazaPucharowa.przejdźDoNastępnejRundy();

            if (liczbaMeczow == 1) {
                break;
            }
        }

    }
}

class Drużyna {
    private String nazwa;
    private int poziomDrużyny;
    Drużyna(String nazwa, int poziomDrużyny) {
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

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Drużyna drużynaA = new Drużyna("Korsarze", 1);
        Drużyna drużynaB = new Drużyna("Marynarze", 2);
        Drużyna drużynaC = new Drużyna("Kosiarze", 3);
        Drużyna drużynaD = new Drużyna("Lekarze", 4);
        Liga liga1 = new Liga(2);
        liga1.zajerestrujDrużyne(drużynaA);
        liga1.zajerestrujDrużyne(drużynaB);
        liga1.zajerestrujDrużyne(drużynaC);

        ArrayList<Integer> wynikmeczu1 = new ArrayList<Integer>();
        wynikmeczu1.add(0);
        wynikmeczu1.add(1);
        Wynik wynik1 = new Wynik(drużynaA, drużynaB, wynikmeczu1);
        liga1.zapiszWyniki(wynik1);

        ArrayList<Integer> wynikmeczu2 = new ArrayList<Integer>();
        wynikmeczu2.add(3);
        wynikmeczu2.add(2);
        Wynik wynik2 = new Wynik(drużynaA, drużynaB, wynikmeczu2);
        liga1.zapiszWyniki(wynik2);

        ArrayList<Integer> wynikmeczu3 = new ArrayList<Integer>();
        wynikmeczu3.add(5);
        wynikmeczu3.add(3);
        Wynik wynik3 = new Wynik(drużynaA, drużynaB, wynikmeczu3);
        // liga1.zapiszWyniki(wynik3);

        // liga1.pokażTabele();
        System.out.println(liga1.zwróćXDrużynPoPunktacji(3));


        System.out.println("\n\n");
        // Turnieje
        // Turnieje turnieje1 = new Turnieje();
        // turnieje1.zajerestrujDrużyne(drużynaA);
        // turnieje1.zajerestrujDrużyne(drużynaB);
        // turnieje1.zajerestrujDrużyne(drużynaC);
        // turnieje1.zajerestrujDrużyne(drużynaD);
        // turnieje1.losujParyDrużyn(turnieje1.getListaDrużyn());
        // for (ArrayList<Drużyna> para_drużyn : turnieje1.getListaParDrużyn()) {
        //     System.out.println(para_drużyn.get(0).getNazwa() + " " + para_drużyn.get(1).getNazwa());
        // }

        // if (turnieje1.getListaParDrużyn().get(0).get(1) != null) {
        //     for (ArrayList<Drużyna> paraDrużyn : turnieje1.getListaParDrużyn()) {
        //         System.out.println("Para: " + paraDrużyn.get(0).getNazwa() + " " + paraDrużyn.get(1).getNazwa());
        //     }
        //     System.out.println("\n");
        // }

        // ArrayList<Integer> wynikmeczuT1 = new ArrayList<Integer>();
        // wynikmeczuT1.add(0);
        // wynikmeczuT1.add(1);
        // Wynik wynikT1 = new Wynik(drużynaA, drużynaD, wynikmeczuT1);
        // turnieje1.zapiszWyniki(wynikT1);

        // ArrayList<Integer> wynikmeczuT2 = new ArrayList<Integer>();
        // wynikmeczuT2.add(3);
        // wynikmeczuT2.add(1);
        // Wynik wynikT2 = new Wynik(drużynaB, drużynaC, wynikmeczuT2);
        // turnieje1.zapiszWyniki(wynikT2);

        // // System.out.println(test1.getListaParDrużyn());
        // turnieje1.przejdźDoNastępnejRundy();

        // if (turnieje1.getListaParDrużyn().get(0).get(1) != null) {
        //     for (ArrayList<Drużyna> paraDrużyn : turnieje1.getListaParDrużyn()) {
        //         System.out.println("Para: " + paraDrużyn.get(0).getNazwa() + " " + paraDrużyn.get(1).getNazwa());
        //     }
        // }

        // ArrayList<Integer> wynikmeczuT3 = new ArrayList<Integer>();
        // wynikmeczuT3.add(3);
        // wynikmeczuT3.add(1);
        // Wynik wynikT3 = new Wynik(drużynaB, drużynaD, wynikmeczuT3);
        // turnieje1.zapiszWyniki(wynikT3);

        // turnieje1.przejdźDoNastępnejRundy();

        // if (turnieje1.getListaParDrużyn().get(0).get(1) != null) {
        //     for (ArrayList<Drużyna> paraDrużyn : turnieje1.getListaParDrużyn()) {
        //         System.out.println("Para: " + paraDrużyn.get(0).getNazwa() + " " + paraDrużyn.get(1).getNazwa());
        //     }
        // }
        // // test1.pokażTabele();


        // System.out.println("\n\n");
        // // Liga + Turniej
        // LigaPlusTurniej ligaPTurniej1 = new LigaPlusTurniej(2, 2);
        // ligaPTurniej1.getEtapLigi().zajerestrujDrużyne(drużynaA);
        // ligaPTurniej1.getEtapLigi().zajerestrujDrużyne(drużynaB);
        // ligaPTurniej1.getEtapLigi().zajerestrujDrużyne(drużynaC);
        // ligaPTurniej1.getEtapLigi().zajerestrujDrużyne(drużynaD);
        // ligaPTurniej1.getEtapLigi().zapiszWyniki(wynik1);
        // ligaPTurniej1.getEtapLigi().pokażTabele();
        // ligaPTurniej1.getEtapLigi().zwróćXDrużynPoPunktacji(1);




        System.out.println("=== WITAMY W TURNIEJU ===");

        // 1. Tworzymy obiekt mistrzostw dla 4 drużyn (1 grupa)
        // Jeśli podasz np. 3, program od razu rzuci wyjątek ZłaLiczbaDrużynException!
        GrupyPlusTurniej mistrzostwa = new GrupyPlusTurniej(8);

        // 2. Tworzymy drużyny (Nazwa, Poziom)
        Drużyna d1 = new Drużyna("Korsarze", 1);
        Drużyna d2 = new Drużyna("Marynarze", 2);
        Drużyna d3 = new Drużyna("Kosiarze", 3);
        Drużyna d4 = new Drużyna("Lekarze", 4);
        Drużyna d5 = new Drużyna("Knicks", 5);
        Drużyna d6 = new Drużyna("Lakers", 6);
        Drużyna d7 = new Drużyna("Celtics", 7);
        Drużyna d8 = new Drużyna("Rockets", 8);

        // 3. Rejestrujemy drużyny
        mistrzostwa.zajerestrujDrużyne(d1);
        mistrzostwa.zajerestrujDrużyne(d2);
        mistrzostwa.zajerestrujDrużyne(d3);
        mistrzostwa.zajerestrujDrużyne(d4);
        mistrzostwa.zajerestrujDrużyne(d5);
        mistrzostwa.zajerestrujDrużyne(d6);
        mistrzostwa.zajerestrujDrużyne(d7);
        mistrzostwa.zajerestrujDrużyne(d8);

        System.out.println("Drużyny pomyślnie zarejestrowane.");

        // 4. Losowanie grup (podział na koszyki i przydział)
        System.out.println("Trwa losowanie grup...\n");
        mistrzostwa.losujGrupy();

        // 5. URUCHOMIENIE ROZGRYWEK (To tutaj program zatrzyma się i poprosi o wyniki)
        mistrzostwa.stwórzLigi();

        // 6. Podsumowanie awansów (Drukowanie list wygenerowanych w stwórzLigi)
        System.out.println("\n=====================================");
        System.out.println(" PODSUMOWANIE AWANSÓW DO FAZY PUCHAROWEJ");
        System.out.println("=====================================");

        System.out.println("Drużyny z pierwszych miejsc:");
        for (Drużyna d : mistrzostwa.getListaPierwszychMiejsc()) {
            System.out.println(" 🥇 " + d.getNazwa() + " (Poziom: " + d.getPoziomDrużyny() + ")");
        }

        System.out.println("\nDrużyny z drugich miejsc:");
        for (Drużyna d : mistrzostwa.getListaDrugichMiejsc()) {
            System.out.println(" 🥈 " + d.getNazwa() + " (Poziom: " + d.getPoziomDrużyny() + ")");
        }

        // 7. (W przyszłości) Wywołanie fazy pucharowej
        mistrzostwa.fazapucharowa();
    }
}