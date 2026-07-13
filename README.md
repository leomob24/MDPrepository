# 📌 Superbattles

Superbattles è un videogioco RPG a turni in cui il giocatore crea il proprio Supereroe, seleziona un superpotere unico e affronta ondate di difficoltà crescente composte da Villain e Minion. Durante le battaglie, è fondamentale gestire strategicamente attacchi, difese e cure per sopravvivere, accumulare punti esperienza e salire di livello potenziando le proprie statistiche.

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- Java 25 (LTS)
- Gradle

### Istruzioni

```bash
git clone https://github.com/leomob24/MDPrepository.git
cd MDPrepository

```

### Build del progetto

```bash
./gradlew build

```

### Esecuzione

```bash
./gradlew run

```

---

## 🤖 Uso di strumenti di AI

In questo progetto, l'Intelligenza Artificiale è stata utilizzata come assistente creativo e tutor per il perfezionamento del codice. Tutta la logica di business, l'implementazione del pattern MVC e la persistenza dei dati sono state pensate, scritte e comprese interamente da me. L'AI è intervenuta per velocizzare alcune fasi dello sviluppo e migliorare la qualità generale dell'architettura.

Nel dettaglio, gli strumenti sono stati impiegati nel seguente modo:

* **Gemini:**
* **Brainstorming:** Utilizzato nelle fasi iniziali per generare e discutere idee sul progetto, aiutandomi a delineare le meccaniche base del gioco.
* **Generazione di contenuti:** Mi ha aiutato a trovare idee creative e variegate per i nomi dei nemici (Villain e Minion) affrontati dal giocatore.


* **Claude:**
* **Design pattern e Code Quality:** Sfruttato per eseguire revisioni del codice con l'obiettivo di mantenere una rigorosa aderenza ai principi SOLID (es. estrazione delle responsabilità in classi specifiche come `AlertFactory` o nei Gestori della battaglia), evitando *code smell*.
* **Design della GUI:** Mi ha supportato nella progettazione e nell'organizzazione delle View (interfacce grafiche JavaFX), fornendo suggerimenti su come separare correttamente la logica visiva dal dominio dell'applicazione.


* **GitHub Copilot:**
* **Produttività:** Utilizzato per l'autocompletamento inline (*inline completion*) per velocizzare la scrittura di istruzioni di routine e codice ripetitivo durante lo sviluppo quotidiano.



Ogni suggerimento o frammento di codice generato dall'AI è stato analizzato, adattato manualmente alle specifiche del progetto e testato personalmente prima dell'inclusione definitiva.

---

📌 Per una descrizione tecnica più dettagliata dell’architettura, delle scelte implementative e delle meccaniche di gioco, consultare la **Wiki del repository**.
