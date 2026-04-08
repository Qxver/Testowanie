import java.util.List;

public class Main {
    public static void main(String[] args) {
        int N = 20;
        Szachownica szachownica = new Szachownica(N);
        Hetman hetman = new Hetman();

        String poleStartowe = "E5";
        List<String> przeszkody = List.of("D5");

        var atakowane = hetman.calculateAttack(poleStartowe, szachownica.N, przeszkody);

        System.out.println("Szachownica rozmiaru: " + N + "x" + N);
        System.out.println("Hetman na polu: " + poleStartowe);
        System.out.println("Przeszkody na polach: " + przeszkody);
        System.out.println("Atakowane pola: " + atakowane);
    }
}