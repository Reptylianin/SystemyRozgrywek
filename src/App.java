import java.util.ArrayList;
import java.util.HashMap;

class ZłaLiczbaDrużynException extends Exception {
    public ZłaLiczbaDrużynException(String message) {super(message);}
}

class ZaDużoMeczówBezpośrednichException extends Exception {
    public ZaDużoMeczówBezpośrednichException(String message) {super(message);}
}

class ZłyWynikKarnychException extends Exception {
    public ZłyWynikKarnychException(String message) {super(message);}
}

class Rozgrywka {
    private ArrayList<Drużyna> listaDrużyn;
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
    public void zapiszWyniki(Wynik wynikMeczu){
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
    private ArrayList<ArrayList<Drużyna>> listaParDrużyn;

    public Turnieje(ArrayList<ArrayList<Drużyna>> listaParDrużyn) {this.listaParDrużyn = listaParDrużyn;}

    public void pokazTabele() {
        // do zrobienia
    }
    public void zapiszWyniki(Drużyna druzyna1, Drużyna druzyna2, Wynik wynik) {
        // do zrobienia
    }
    protected void losujParyDrużyn() {
        // do zrobienia
    }
}

class Liga extends Rozgrywka {
    private int liczbaMeczBezpośrednich;
    Liga(int liczbaMeczBezpośrednich) {
        super();
        this.liczbaMeczBezpośrednich = liczbaMeczBezpośrednich;
    }
    @Override
    public void pokażTabele() {
        for (Wynik wynikIMeczu : getListaWyników()) {
            System.out.println(wynikIMeczu.getDrużyna1().getNazwa() + " vs " + wynikIMeczu.getDrużyna2().getNazwa());
            System.out.println(wynikIMeczu.getWynik1() + " : " + wynikIMeczu.getWynik2());
        }
        System.out.println("\n\n" + getListaPunktów());
    }
    @Override
    public void zapiszWyniki(Wynik wynikMeczu) {
        getListaWyników().add(wynikMeczu);
        Integer punktyDrużyny1 = getListaPunktów().get(wynikMeczu.getDrużyna1().getNazwa());
        Integer punktyDrużyny2 = getListaPunktów().get(wynikMeczu.getDrużyna2().getNazwa());
        getListaPunktów().put(wynikMeczu.getDrużyna1().getNazwa(), punktyDrużyny1 + wynikMeczu.getWynik1());
        getListaPunktów().put(wynikMeczu.getDrużyna2().getNazwa(), punktyDrużyny2 + wynikMeczu.getWynik2());
    }
}

// class Turniej extends Turnieje {

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

    ArrayList<Integer> punktyZaMecz() {
        //do zrobienia
        ArrayList<Integer> a = new ArrayList<Integer>(2);
        return a;
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Drużyna drużynaA = new Drużyna("Korsarze", 1);
        Drużyna drużynaB = new Drużyna("Marynarze", 2);
        Liga liga1 = new Liga(2);
        liga1.zajerestrujDrużyne(drużynaA);
        liga1.zajerestrujDrużyne(drużynaB);

        ArrayList<Integer> wynikmeczu1 = new ArrayList<Integer>();
        wynikmeczu1.add(0);
        wynikmeczu1.add(1);
        Wynik wynik1 = new Wynik(drużynaA, drużynaB, wynikmeczu1);
        liga1.zapiszWyniki(wynik1);

        ArrayList<Integer> wynikmeczu2 = new ArrayList<Integer>();
        wynikmeczu2.add(2);
        wynikmeczu2.add(3);
        Wynik wynik2 = new Wynik(drużynaA, drużynaB, wynikmeczu2);
        liga1.zapiszWyniki(wynik2);

        liga1.pokażTabele();
    }
}
