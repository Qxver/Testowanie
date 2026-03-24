public class Main {
    public static void main(String[] args) {
        int N = 8;
        Szachownica szachownica = new Szachownica(N);
        Hetman hetman = new Hetman();

        String poleStartowe = "E5";

        var atakowane = hetman.atakowanePolaHetmana(poleStartowe, szachownica.N);

        System.out.println("Szachownica rozmiaru: " + N + "x" + N);
        System.out.println("Hetman na polu: " + poleStartowe);
        System.out.println("Atakowane pola: " + atakowane);
    }
}