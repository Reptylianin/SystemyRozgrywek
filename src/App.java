import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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

class TurniejeZbiorowo extends Rozgrywka {
    private ArrayList<ArrayList<Drużyna>> listaParDrużyn;
    private HashMap<Integer,Drużyna> listaZwycięzcówRundy;
    private int liczbaRund = 0;
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
        if (getListaZwycięzcówRundy().size() > getListaParDrużyn().size()/2) {
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
            scanner.close();
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
        for (int i=0; i<getListaParDrużyn().size(); i++){
            if (getListaParDrużyn().get(i).contains(wynikMeczu.getZwycięzcaMeczu())) {
                getListaZwycięzcówRundy().put(i, wynikMeczu.getZwycięzcaMeczu());
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

// class Turnieje extends Rozgrywka {
//     private HashMap<Integer,Drużyna> listaZwycięzcówRundy;
//     private ArrayList<ArrayList<Drużyna>> listaParDrużyn;
//     private int liczbaRund = 0;
//     private int obecnaRunda = 0;

//     public Turnieje() {
//         super();
//         this.listaZwycięzcówRundy = new HashMap<Integer,Drużyna>();
//         this.listaParDrużyn = new ArrayList<ArrayList<Drużyna>>();
//     }

//     @Override
//     public void pokażTabele(){
//         for (Wynik wynikIMeczu : getListaWyników()) {
//             System.out.println(wynikIMeczu.getDrużyna1().getNazwa() + " vs " + wynikIMeczu.getDrużyna2().getNazwa());
//             System.out.println(wynikIMeczu.getWynik1() + " : " + wynikIMeczu.getWynik2());
//         }
//         System.out.println("\n\n" + getListaPunktów());
//     };
//     @Override
//     public void zapiszWyniki(Wynik wynikMeczu) throws ZłaParaDrużynException, ZaDużoMeczyWRundzieException {
//         if (getListaZwycięzcówRundy().size() > getListaParDrużyn().size()/2) {
//             throw new ZaDużoMeczyWRundzieException("Za dużo zwycięzców w rundzie");
//         }

//         // 
//         String nazwaDrużyny1 = wynikMeczu.getDrużyna1().getNazwa();
//         String nazwaDrużyny2 = wynikMeczu.getDrużyna2().getNazwa();

//         ArrayList<Integer> punkty = wynikMeczu.punktyZaMecz();
        
//         Integer punktyDrużyny1 = getListaPunktów().get(nazwaDrużyny1);
//         Integer punktyDrużyny2 = getListaPunktów().get(nazwaDrużyny2);
//         getListaPunktów().put(nazwaDrużyny1, punktyDrużyny1 + punkty.get(0));
//         getListaPunktów().put(nazwaDrużyny2, punktyDrużyny2 + punkty.get(1));
//         // 

//         ArrayList<Drużyna> paraDrużyn = new ArrayList<Drużyna>();
//         paraDrużyn.add(wynikMeczu.getDrużyna1());
//         paraDrużyn.add(wynikMeczu.getDrużyna2());
//         if (!getListaParDrużyn().contains(paraDrużyn)) {
//             if (!getListaParDrużyn().contains(paraDrużyn.reversed())) {
//                 throw new ZłaParaDrużynException("Taka para nie rozgrywa razem meczy");
//             }
//         }
//         for (int i=0; i<getListaParDrużyn().size(); i++){
//             if (getListaParDrużyn().get(i).contains(wynikMeczu.getZwycięzcaMeczu())) {
//                 getListaZwycięzcówRundy().put(i, wynikMeczu.getZwycięzcaMeczu());
//             }
//         }
//     }
//     public void przejdźDoNastępnejRundy() throws ZaDużoRundException, ZaMałoZwyciezcowException {
//         if (obecnaRunda == liczbaRund) {
//             throw new ZaDużoRundException("Za dużo rund (powinien być już zwycięzca)");
//         }
//         if (getListaZwycięzcówRundy().size() == getListaParDrużyn().size()/2) {
//             throw new ZaMałoZwyciezcowException("Zbyt mało zwycięzców rund żeby przejść do następnej rundy");
//         }
//         listaParDrużyn.clear();
//         obecnaRunda += 1;
//         boolean czy_pominiete = false;
//         for (Integer i : getListaZwycięzcówRundy().keySet()) {
//             if (!czy_pominiete) {
//                 ArrayList<Drużyna> paraDrużyn = new ArrayList<Drużyna>();
//                 paraDrużyn.add(getListaZwycięzcówRundy().get(i));
//                 paraDrużyn.add(getListaZwycięzcówRundy().get(i+1));
//                 listaParDrużyn.add(paraDrużyn);
//                 czy_pominiete = true;
//             }
//             else {
//                 czy_pominiete = false;
//             }
//         }
//         if (getListaZwycięzcówRundy().size() == 1) {
//             for (Drużyna drużynaZwycięska : getListaZwycięzcówRundy().values()) {
//                 System.out.println("Zwycięzca to: " + drużynaZwycięska.getNazwa());
//             }
//         }
//         getListaZwycięzcówRundy().clear();
//     }
//     protected void losujParyDrużyn(ArrayList<Drużyna> listaDrużynDoPar) throws ZłaLiczbaDrużynException {
//         if ((listaDrużynDoPar.size() & listaDrużynDoPar.size() - 1)  != 0) {
//             throw new ZłaLiczbaDrużynException("Podano złą liczbę drużyn, musi ona być potęgą dwójki");
//         }
//         ArrayList<Drużyna> posortowaneDrużyny = new ArrayList<>(listaDrużynDoPar);
//         posortowaneDrużyny.sort((d1, d2) -> Integer.compare(d1.getPoziomDrużyny(), d2.getPoziomDrużyny()));
//         int L = 0;
//         int P = posortowaneDrużyny.size() - 1;
//         for (int i = 0; i < posortowaneDrużyny.size() / 2; i++) {
//             ArrayList<Drużyna> para = new ArrayList<>();
//             para.add(posortowaneDrużyny.get(L));
//             para.add(posortowaneDrużyny.get(P));
//             listaParDrużyn.add(para);
//             L++;
//             P--;
//         }
//         int liczbaDzielona = listaParDrużyn.size();
//         while (liczbaDzielona != 0 && liczbaDzielona % 2 == 0) {
//             liczbaDzielona = liczbaDzielona / 2;
//             liczbaRund += 1;
//         } 
//         if (liczbaRund != 0) {
//             liczbaRund += 1;
//         }
//     }
//     public ArrayList<ArrayList<Drużyna>> getListaParDrużyn() {
//         return listaParDrużyn;
//     }
//     public int getLiczbaRund() {
//         return liczbaRund;
//     }
//     public HashMap<Integer, Drużyna> getListaZwycięzcówRundy() {
//         return listaZwycięzcówRundy;
//     }
// }

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
    public void zwróćXDrużynPoPunktacji(int ileDrużynDoZwrócenia) {
        // do zmiany
        // ArrayList<Drużyna> listaDrużynDoPunktacji = new ArrayList<Drużyna>();
        // ArrayList<Integer> listaPunktacji = new ArrayList<Integer>();
        // for (Integer integer : getListaPunktów().keySet()) {
            
        // }
        System.out.println(getListaPunktów());
    }
}

// class LigaPlusTurniej extends Turnieje {
//     private int liczbaDrużynDoEtapuTurnieju;
//     private Liga etapLigi;
//     public LigaPlusTurniej(int liczbaMeczBezpośrednich, int liczbaDrużynDoEtapuTurnieju) throws IllegalArgumentException {
//         if ((liczbaDrużynDoEtapuTurnieju & liczbaDrużynDoEtapuTurnieju - 1)  != 0) {
//             throw new IllegalArgumentException("Niepoprawna liczba drużyn do etapu turnieju");
//         }
//         super();
//         this.etapLigi = new Liga(liczbaMeczBezpośrednich);
//         this.liczbaDrużynDoEtapuTurnieju = liczbaDrużynDoEtapuTurnieju;
//     }
//     public Liga getEtapLigi() {
//         return etapLigi;
//     }
// }

// class GrupyPlusTurniej {
//     private int liczby_grup;

//     public GrupyPlusTurniej() {
//     }

// }

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
    }
}