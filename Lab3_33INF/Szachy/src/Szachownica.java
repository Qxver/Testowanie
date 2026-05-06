import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Szachownica {
    public int N;
    public ArrayList<String> pola = new ArrayList<>();
    public String hetmanPos;
    public List<String> przeszkody = new ArrayList<>();

    public Szachownica(int rozmiar) {
        this.N = rozmiar;
        generujPola();
    }

    private void generujPola() {
        pola.clear();
        for (int i = 0; i < N; i++) {
            char litera = (char) ('A' + i);
            for (int j = 1; j <= N; j++) {
                pola.add(litera + "" + j);
            }
        }
    }

    // Walidacja logiki biznesowej
    public void ustawHetmana(String pole) {
        if (!pola.contains(pole)) throw new IllegalArgumentException("Pole poza szachownicą!");
        if (przeszkody.contains(pole)) throw new IllegalStateException("Pole zajęte przez przeszkodę!");
        this.hetmanPos = pole;
    }

    public void dodajPrzeszkode(String pole) {
        if (!pola.contains(pole)) throw new IllegalArgumentException("Pole poza szachownicą!");
        if (pole.equals(hetmanPos)) throw new IllegalStateException("Tu już stoi hetman!");
        this.przeszkody.add(pole);
    }

    // Zapis do pliku CSV (N;hetman;przeszkody)
    public void zapisz(String sciezka) throws IOException {
        String dane = N + ";" + hetmanPos + ";" + String.join(",", przeszkody);
        Files.writeString(Path.of(sciezka), dane);
    }

    // Odczyt z pliku
    public void odczytaj(String sciezka) throws IOException {
        String dane = Files.readString(Path.of(sciezka));
        String[] czesci = dane.split(";");
        this.N = Integer.parseInt(czesci[0]);
        generujPola(); // Odbudowanie listy pól na podstawie nowego rozmiaru

        this.hetmanPos = czesci[1];
        this.przeszkody = new ArrayList<>();
        if (czesci.length > 2 && !czesci[2].isEmpty()) {
            this.przeszkody.addAll(Arrays.asList(czesci[2].split(",")));
        }
    }
}