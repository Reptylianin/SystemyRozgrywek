import java.util.ArrayList;
import java.util.HashMap;

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
    };
    public void zapiszWyniki(Wynik wynikMeczu) throws Exception {
        getListaWyników().add(wynikMeczu);
        Integer punktyDrużyny1 = getListaPunktów().get(wynikMeczu.getDrużyna1().getNazwa());
        Integer punktyDrużyny2 = getListaPunktów().get(wynikMeczu.getDrużyna2().getNazwa());
        getListaPunktów().put(wynikMeczu.getDrużyna1().getNazwa(), punktyDrużyny1 + wynikMeczu.getWynik1());
        getListaPunktów().put(wynikMeczu.getDrużyna2().getNazwa(), punktyDrużyny2 + wynikMeczu.getWynik2());
    };
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

class Turnieje extends Rozgrywka {
    private HashMap<Integer,Drużyna> listaZwycięzcówRundy;
    private ArrayList<ArrayList<Drużyna>> listaParDrużyn;
    private int liczbaRund;
    private int obecnaRunda = 0;

    public Turnieje() {
        super();
        this.listaZwycięzcówRundy = new HashMap<Integer,Drużyna>();
        this.listaParDrużyn = new ArrayList<ArrayList<Drużyna>>();
    }

    @Override
    public void pokażTabele(){
        for (Wynik wynikIMeczu : getListaWyników()) {
            System.out.println(wynikIMeczu.getDrużyna1().getNazwa() + " vs " + wynikIMeczu.getDrużyna2().getNazwa());
            System.out.println(wynikIMeczu.getWynik1() + " : " + wynikIMeczu.getWynik2());
        }
        System.out.println("\n\n" + getListaPunktów());
    };
    @Override
    public void zapiszWyniki(Wynik wynikMeczu) throws ZłaParaDrużynException, ZaDużoMeczyWRundzieException {
        if (getListaZwycięzcówRundy().size() > getListaParDrużyn().size()/2) {
            throw new ZaDużoMeczyWRundzieException("Za dużo zwycięzców w rundzie");
        }

        // 
        String nazwaDrużyny1 = wynikMeczu.getDrużyna1().getNazwa();
        String nazwaDrużyny2 = wynikMeczu.getDrużyna2().getNazwa();

        ArrayList<Integer> punkty = wynikMeczu.punktyZaMecz();
        
        Integer punktyDrużyny1 = getListaPunktów().get(nazwaDrużyny1);
        Integer punktyDrużyny2 = getListaPunktów().get(nazwaDrużyny2);
        getListaPunktów().put(nazwaDrużyny1, punktyDrużyny1 + punkty.get(0));
        getListaPunktów().put(nazwaDrużyny2, punktyDrużyny2 + punkty.get(1));
        // 

        ArrayList<Drużyna> paraDrużyn = new ArrayList<Drużyna>();
        paraDrużyn.add(wynikMeczu.getDrużyna1());
        paraDrużyn.add(wynikMeczu.getDrużyna2());
        if (!getListaParDrużyn().contains(paraDrużyn)) {
            if (!getListaParDrużyn().contains(paraDrużyn.reversed())) {
                throw new ZłaParaDrużynException("Taka para nie rozgrywa razem meczy");
            }
            // Collections.reverse(paraDrużyn);
        }
        for (int i=0; i<getListaParDrużyn().size(); i++){
            if (getListaParDrużyn().get(i).contains(wynikMeczu.getZwyciężcaMeczu())) {
                getListaZwycięzcówRundy().put(i, wynikMeczu.getZwyciężcaMeczu());
            }
        }
    }
    public void przejdźDoNastępnejRundy() throws ZaDużoRundException, ZaMałoZwyciezcowException {
        if (obecnaRunda == liczbaRund) {
            throw new ZaDużoRundException("Za dużo rund (powinien być już zwycięzca)");
        }
        if (getListaZwycięzcówRundy().size() == 1) {
            System.out.println("Zwycięzca to :" + getListaZwycięzcówRundy().values());
        }
        System.out.println(getListaZwycięzcówRundy().size());
        if (getListaZwycięzcówRundy().size() == getListaParDrużyn().size()/2) {
            throw new ZaMałoZwyciezcowException("Zbyt mało zwycięzców rund żeby przejść do następnej rundy");
        }
        listaParDrużyn.clear();
        System.out.println(getListaZwycięzcówRundy().keySet());
        obecnaRunda += 1;
        boolean czy_pominiete = false;
        for (Integer i : getListaZwycięzcówRundy().keySet()) {
            if (!czy_pominiete) {
                ArrayList<Drużyna> paraDrużyn = new ArrayList<Drużyna>();
                paraDrużyn.add(getListaZwycięzcówRundy().get(i));
                paraDrużyn.add(getListaZwycięzcówRundy().get(i+1));
                listaParDrużyn.add(paraDrużyn);
                czy_pominiete = true;
                // System.out.println(paraDrużyn);
                // if (paraDrużyn.get(1) == null){
                //     System.out.println("zwyciezca: " + paraDrużyn.get(0));
                // }
            }
            else {
                czy_pominiete = false;
            }
        }
        getListaZwycięzcówRundy().clear();
    }
    protected void losujParyDrużyn(ArrayList<Drużyna> listaDrużynDoPar) throws ZłaLiczbaDrużynException {
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
        System.out.println("\n\nLista liczby zagranych meczów bezpośrednich:" + getListaLiczbyMeczyBezpośrednich());
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
}

// class Turniej extends Turnieje {
//     public Turniej() {
//         super();
//     }
//     @Override
//     public void zapiszWyniki(Wynik wynikMeczu) throws ZłaParaDrużynException {
//         ArrayList<Drużyna> paraDrużyn = new ArrayList<Drużyna>();
//         paraDrużyn.add(wynikMeczu.getDrużyna1());
//         paraDrużyn.add(wynikMeczu.getDrużyna2());
//         String nazwaDrużyny1 = wynikMeczu.getDrużyna1().getNazwa();
//         String nazwaDrużyny2 = wynikMeczu.getDrużyna2().getNazwa();
//         if (!getListaParDrużyn().contains(paraDrużyn)) {
//             if (!getListaParDrużyn().contains(paraDrużyn.reversed())) {
//                 throw new ZłaParaDrużynException("Taka para nie rozgrywa razem meczy");
//             }
//             Collections.reverse(paraDrużyn);
//         }
//     }
// }

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
    private Drużyna zwyciężcaMeczu;
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
    public Drużyna getZwyciężcaMeczu() {
        return zwyciężcaMeczu;
    }

    public ArrayList<Integer> punktyZaMecz() {
        ArrayList<Integer> punkty = new ArrayList<Integer>(2);
        if (getWynik1() > getWynik2()) {
            punkty.add(3);
            punkty.add(0);
            zwyciężcaMeczu = drużyna1;
        }
        else if (getWynik1() < getWynik2()) {
            punkty.add(0);
            punkty.add(3);
            zwyciężcaMeczu = drużyna2;
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


        Turnieje test1 = new Turnieje();
        test1.zajerestrujDrużyne(drużynaA);
        test1.zajerestrujDrużyne(drużynaB);
        test1.zajerestrujDrużyne(drużynaC);
        test1.zajerestrujDrużyne(drużynaD);
        test1.losujParyDrużyn(test1.getListaDrużyn());
        for (ArrayList<Drużyna> para_drużyn : test1.getListaParDrużyn()) {
            System.out.println(para_drużyn.get(0).getNazwa() + " " + para_drużyn.get(1).getNazwa());
        }

        ArrayList<Integer> wynikmeczuT1 = new ArrayList<Integer>();
        wynikmeczuT1.add(0);
        wynikmeczuT1.add(1);
        Wynik wynikT1 = new Wynik(drużynaA, drużynaD, wynikmeczuT1);
        test1.zapiszWyniki(wynikT1);

        ArrayList<Integer> wynikmeczuT2 = new ArrayList<Integer>();
        wynikmeczuT2.add(3);
        wynikmeczuT2.add(1);
        Wynik wynikT2 = new Wynik(drużynaB, drużynaC, wynikmeczuT2);
        test1.zapiszWyniki(wynikT2);
        System.out.println(test1.getListaParDrużyn());
        test1.przejdźDoNastępnejRundy();
        System.out.println(test1.getListaParDrużyn());
        test1.przejdźDoNastępnejRundy();
        test1.przejdźDoNastępnejRundy();
        // System.out.println(test1.getLiczbaRund());
        // for (ArrayList<Drużyna> dr : test1.getListaParDrużyn()) {
        //     System.out.println(dr.get(0).getNazwa() + " " + dr.get(1).getNazwa());
        // }

        // test1.pokażTabele();
    }
}