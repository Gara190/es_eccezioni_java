import net.datafaker.Faker;
import java.util.Random;


// ==========================================
// ECCEZIONI E CLASSI PER L'ESERCIZIO 1 (BANCA)
// ==========================================

class SaldoInsufficienteException extends Exception {
    private double saldoAttuale;
    private double importoRichiesto;

    public SaldoInsufficienteException(double saldoAttuale, double importoRichiesto) {
        super("Tentativo di prelievo fallito: fondi non sufficienti.");
        this.saldoAttuale = saldoAttuale;
        this.importoRichiesto = importoRichiesto;
    }

    public double getDeficit() {
        return importoRichiesto - saldoAttuale;
    }

    public double getSaldoAttuale() {
        return saldoAttuale;
    }

    public double getImportoRichiesto() {
        return importoRichiesto;
    }
}

class ImportoNonValidoException extends RuntimeException {
    public ImportoNonValidoException(String message) {
        super(message);
    }
}

class ContoBancario {
    private double saldo;

    public ContoBancario(double saldoIniziale) {
        this.saldo = saldoIniziale;
    }

    public void preleva(double importo) throws SaldoInsufficienteException {
        if (importo <= 0) {
            throw new ImportoNonValidoException("L'importo del prelievo deve essere maggiore di zero.");
        }
        if (importo > saldo) {
            throw new SaldoInsufficienteException(saldo, importo);
        }
        saldo -= importo;
        System.out.println("Prelievo di " + importo + " euro completato. Saldo residuo: " + saldo + " euro.");
    }

    public double getSaldo() {
        return saldo;
    }
}


// ====================================================
// ECCEZIONI E CLASSI PER L'ESERCIZIO 2 (SUPEREROI)
// ====================================================

class EroeNonIdoneoException extends Exception {
    private String nomeEroe;
    private int livelloPotere;

    public EroeNonIdoneoException(String nomeEroe, int livelloPotere) {
        super("L'eroe " + nomeEroe + " non possiede un livello di potere sufficiente (Potere: " + livelloPotere + ").");
        this.nomeEroe = nomeEroe;
        this.livelloPotere = livelloPotere;
    }

    public String getNomeEroe() { return nomeEroe; }
    public int getLivelloPotere() { return livelloPotere; }
}

class Reclutatore {
    private Faker faker = new Faker();
    private Random random = new Random();

    public void valutaEroe(int difficoltaMissione) throws EroeNonIdoneoException {
        String nomeEroe = faker.superhero().name();
        int potere = random.nextInt(100) + 1; // Genera da 1 a 100

        System.out.println("Candidato valutato -> Eroe: " + nomeEroe + " | Potere: " + potere);

        if (potere < difficoltaMissione) {
            throw new EroeNonIdoneoException(nomeEroe, potere);
        }

        System.out.println(">>> SUCCESSO! L'eroe " + nomeEroe + " è stato reclutato con successo!\n");
    }
}


// ==========================================
// CLASSE PRINCIPALE (MAIN)
// ==========================================

public class ApplicazioneUnica {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("TEST ESERCIZIO 1: CONTO CORRENTE (Checked vs Unchecked)");
        System.out.println("==================================================");

        ContoBancario conto = new ContoBancario(500.0);

        // Scenario 1: Prelievo eccessivo (Checked Exception)
        System.out.println("\n--- SCENARIO 1: Prelievo eccessivo ---");
        try {
            conto.preleva(700.0);
        } catch (SaldoInsufficienteException e) {
            System.out.println("Errore catturato: " + e.getMessage());
            System.out.println("Deficit di denaro richiesto: " + e.getDeficit() + " euro.");
        } finally {
            System.out.println("[FINALLY] Blocco eseguito. Saldo attuale: " + conto.getSaldo() + " euro.");
        }

        // Scenario 2: Prelievo negativo (Unchecked Exception)
        System.out.println("\n--- SCENARIO 2: Prelievo negativo ---");
        try {
            conto.preleva(-50.0);
        } catch (ImportoNonValidoException e) {
            System.out.println("Errore runtime catturato: " + e.getMessage());
        } catch (SaldoInsufficienteException e) {
            throw new RuntimeException(e);
        }

        // Scenario 3: Prelievo valido
        System.out.println("\n--- SCENARIO 3: Prelievo valido ---");
        try {
            conto.preleva(200.0);
        } catch (SaldoInsufficienteException e) {
            System.out.println("Errore: " + e.getMessage());
        }


        System.out.println("\n==================================================");
        System.out.println("TEST ESERCIZIO 2: RECLUTAMENTO SUPEREROI (Datafaker)");
        System.out.println("==================================================");

        int difficoltaMissione = 75;
        Reclutatore reclutatore = new Reclutatore();

        System.out.println("Avvio processo di reclutamento (Difficoltà missione: " + difficoltaMissione + ")\n");

        for (int i = 1; i <= 5; i++) {
            System.out.println("--- Tentativo #" + i + " ---");
            try {
                reclutatore.valutaEroe(difficoltaMissione);
            } catch (EroeNonIdoneoException e) {
                System.out.println("-> SCARTATO: " + e.getMessage() + "\n");
            }
        }

        System.out.println("==================================================");
        System.out.println("ESECUZIONE COMPLETATA");
        System.out.println("==================================================");
    }
}