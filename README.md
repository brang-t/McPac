# Pac-Man-inspiriertes Java-Spiel

## 1. Projektübersicht

### 1.1 Spielkonzept

Dieses Projekt ist im Rahmen des Moduls **Embedded Systems** an der Hochschule Bonn-Rhein-Sieg entstanden. Ziel war es, Fähigkeiten im Bereich der objektorientierten Programmierung zu erwerben und anzuwenden. In der Programmiersprache Java wurde ein Spiel entwickelt, das in Optik und Spiellogik an den Arcade-Klassiker **Pac-Man** angelehnt ist.

### 1.2 Hauptmerkmale

- **Ziel:** Sammeln aller Punkte auf dem Spielfeld. Das Spiel endet bei Kontakt mit einem der Gegner (Verloren) oder mit dem letzten zu sammelnden Punkt (Gewonnen).
- **Spielersteuerung:** Pfeiltasten zur Bewegung.
- **Power-Mode:** Durch Power-Dots wird der Spieler schneller und kann Gegner essen (sie werden cyan und fliehen).
- **Gegner:** Zwei unterschiedliche Gegnertaktiken:
  - **Chasing Ghost (ROT):** Jagt den Spieler aktiv.
  - **Random Ghost (GRÜN):** Bewegt sich zufällig auf der Karte.
- **Mehrere Karten:** 3 verschiedene Labyrinth-Layouts zur Auswahl.
- **Grafisches Rendering:** Java Swing mit optimierten Rendering-Hints.

---


## 2. Klassenübersicht

### 2.1 `Game.java` – Zentrale Spielsteuerung

- Beinhaltet die Main-Loop des Spiels, erbt von `JFrame`.
- Kernaufgaben: Initialisierung des Spiels, Kartenwahl und Verwaltung des Spielzustands (Sieg/Niederlage).
- Objekt-Verwaltung: Hält die Referenzen auf alle wichtigen Spielobjekte (`Pacman`, `Ghost[]`, `GameMap`) und das `Display`.
- Game Loop (60 FPS) mittels `ScheduledExecutor`:
  - **Logik (`tick()`):** Aktualisiert Positionen, prüft Kollisionen und Status.
  - **Grafik (`render()`):** Zeichnet das Spielfeld und die Figuren neu.

### 2.2 `GameMap.java` – Labyrinth-Verwaltung

- Verwaltet das statische Spielfeld und definiert die Regeln für Bewegung und Punkte.
- Datenstruktur: Speichert das Level als 2D-Array (`Tile[][]`), basierend auf vordefinierten Integer-Matrizen  
  (z. B. `0 = Weg (Luft)`, `1 = Wand`, `2/3 = Weg (Dots)`).
- Kollisionslogik: `isFree(x, y)` ist die zentrale Schnittstelle, um zu prüfen, ob ein Feld begehbar ist (Feld != Wand).
- Spielmechanik: Überwacht den Sieg-Status durch Zählen der verbleibenden Dots (`dotCount()`) und ermöglicht den Neustart der Karte (`reset()`).
- Rendering: Zeichnet alle statischen Elemente (Wände, Punkte).

### 2.3 `Creature.java` – Basis für bewegliche Objekte

Elternklasse für Pacman und Ghosts; implementiert die grundlegende Physik-Engine.

- Zustandsverwaltung: Speichert Position (`centerX/Y` als Fließkommazahlen für weiche Bewegungen), Geschwindigkeit und Farbe.
- Bewegungs-Logik: Unterscheidet zwischen gewünschter (`preferredDirection`) und tatsächlicher (`movingDirection`) Richtung. Richtungsänderungen werden erst beim Überqueren der Tile-Mitte (`tickTurn()`) ausgeführt.
- Kollision: Prüft kontinuierlich auf Wände (`tickWallCollisions()`), um ungültige Bewegungen zu blockieren.
- Rendering: Zeichnet die Grundform und die richtungsabhängigen Augen.

### 2.4 `Pacman.java` – Die Spielfigur

> In deinem Code entspricht dies der `Player`-Klasse, hier konzeptionell als Pacman beschrieben.

- Erweitert `Creature` und implementiert die direkte Interaktion des Nutzers sowie spezielle Spielmechaniken.
- Steuerung: Verarbeitet Tastatureingaben (Pfeiltasten) über einen `KeyListener`, um die gewünschte Bewegungsrichtung zu setzen.
- Interaktion: Prüft in jedem Tick auf Kollisionen mit Dots (`tickDotCollision()`), entfernt diese von der Karte (Dot → Air) und löst ggf. den Power-Mode aus.
- PowerMode-Logik (5‑Sekunden-Timer):
  - **Pacman:** Geschwindigkeit × 1,5 und Farbwechsel (Gelb → Orange).
  - **Global:** Versetzt alle Gegner über `onPowerModeStart()` in den `FRIGHTENED`‑Zustand (Flucht).

### 2.5 `Ghost.java` – Abstrakte Gegner

- Definiert das grundlegende Verhalten aller Geister.
- Abstrakt, da das spezifische Ziel (`target`) je nach Gegnertyp variieren kann.
- Zustands-Management: Verwaltet drei Phasen via `enum State`:
  - `NORMAL`: Standard-Verfolgung (schneller).
  - `FRIGHTENED`: Fluchtmodus (langsamer, Farbe Cyan, essbar), ausgelöst durch Power-Dots.
  - `EATEN`: „Tot“ (unsichtbar), wartet auf den Respawn-Timer.
- Pathfinding (A\*): Implementiert den A\*-Algorithmus, um den kürzesten Weg zum aktuellen Ziel (`targetX/Y`) zu finden. Nutzt eine `PriorityQueue` und Manhattan-Distanz zur Berechnung.
- Abstrakte Logik: Die Methode `tickTarget()` ist abstrakt und muss von Unterklassen implementiert werden, um festzulegen, wohin der Geist eigentlich will.
- Interaktion: Handhabt Kollisionen mit dem Spieler (tötet Spieler im NORMAL-Modus oder wird selbst gefressen im FRIGHTENED-Modus) und reagiert auf globale Events (`onPowerModeStart/End`).

### 2.6 Ghost-Unterklassen

Definieren das konkrete Verhalten der Geister durch Überschreiben der Ziel-Berechnung.

- **`ChasingGhost` (ROT)**
  - Logik: Setzt das Ziel exakt auf die aktuelle Position des Spielers (`Pacman.x`, `Pacman.y`).
  - Taktik: direkte Verfolgung.
  - Schwierigkeit: Mittel (vorhersehbar, kann aber abgehängt werden).
- **`RandomGhost` (GRÜN)**
  - Logik: Wählt zufällige Koordinaten auf der Karte als Ziel (`random.nextInt()`).
  - Taktik: Unvorhersehbares Umherwandern ohne echtes Interesse am Spieler.
  - Schwierigkeit: Leicht (wirkt planlos, kann aber zufällig Wege blockieren).

### 2.7 `Node.java` – A\*-Pathfinding

- Repräsentiert einen Knoten im Suchbaum des A\*-Algorithmus (siehe `Ghost.java`).
- Berechnung von \( f(n) = g(n) + h(n) \) (Distanz vom Start + heuristische Distanz zum Ziel).
- Wichtige Attribute:
  - `int x, y` – Position des Knotens.
  - `Node previous` – Parent-Knoten (für Rekonstruktion des Pfads).
  - `int distanceFromStart` – \( g(n) \): tatsächliche Distanz vom Start.
  - `int minDistanceToGoal` – \( h(n) \): geschätzte Distanz zum Ziel (Manhattan).
- Zentrale Methoden:
  - `List<Node> neighbors()` – Gibt die 4 benachbarten Knoten zurück.
  - `Node initialDirection()` – Rekursive Rückwärtssuche: Gibt den Knoten eine Tile weit vom Start zurück.
  - `compareTo()` – Vergleich für `PriorityQueue`: sortiert nach \( f(n) \).
  - `equals()`, `hashCode()` – Vergleichsoperator für bereits besuchte Felder (z. B. in `HashSet`).

### 2.8 `Tile.java` – Oberklasse für Kartenobjekte

Struktur:

- `Tile` (abstract)
  - `Wall` – nicht begehbares Feld.
  - `Air` – leeres Feld.
  - `Dot` (abstract)
    - `RegularDot`
    - `PowerDot`

Eigenschaften:

- Jedem Tile wird (über `GameMap`) eine finale Position zugewiesen.
- Für `Wall` und die Dots werden Farbe und Größe des Tiles bzw. Punktes festgelegt.
- Über Getter-Methoden können Radius und Position der Punkte für die Kollision mit Pacman abgefragt werden.

### 2.9 `GameObject.java` – Oberklasse für alle darzustellenden Objekte

- Unterklassen: `Tile`, `GameMap` und `Creature`.
- Alle zu rendernden Spielobjekte implementieren diese Schnittstelle, um in der Game-Loop gezeichnet und ggf. aktualisiert zu werden.

### 2.10 `Display.java` – Rendering-Panel

- Bereitstellung eines `JPanel` für die Grafikausgabe.
- Verantwortlich für das Aufrufen der Game-Rendering-Methode (z. B. `game.render(g2)`).
- Setzt Rendering-Hints für bessere Grafikqualität (Antialiasing, Interpolation etc.).

---

## 3. Bibliotheken / Frameworks

Das Projekt verwendet **ausschließlich Standard-Java-Bibliotheken** (keine externen Abhängigkeiten oder Frameworks). Es ist komplett **standalone** und kompiliert mit jeder Java 8+ JDK.

### Übersicht der verwendeten Bibliotheken nach Klasse

| Klasse       | Swing/AWT                                   | Collections                         | Sonstiges      |
|--------------|---------------------------------------------|-------------------------------------|----------------|
| `Game.java`  | `javax.swing.*`, `java.awt.*`              | `Executors`, `TimeUnit`             | `ImageIcon`    |
| `Display.java` | `javax.swing.JPanel`, `java.awt.*`       | –                                   | `RenderingHints` |
| `Pacman.java` (Player) | `java.awt.*`, `java.awt.event.*`, `Ellipse2D` | –                         | `KeyListener`   |
| `Creature.java` | `java.awt.*`, `Ellipse2D`               | –                                   | –              |
| `Ghost.java` (Enemy) | `java.awt.*`, `Rectangle2D`        | `HashSet`, `PriorityQueue`, `Queue`, `Set` | –      |
| `GameMap.java` | `java.awt.*`                             | –                                   | –              |
| Dots/Tiles   | `java.awt.*`, `Ellipse2D`                  | –                                   | –              |

---

## 4. Spielablauf / Bedienungsanleitung

1. Nach Start des Programms erscheint ein Dialogfenster, in dem der Spieler eine von drei möglichen Karten auswählen muss.
2. Die gewählte Karte öffnet sich in einem neuen Fenster.
3. Die Bewegung der Spielfigur (Pacman) erfolgt über die Pfeiltasten der Tastatur. Das Spiel beginnt mit der ersten Tasteneingabe.
4. Schafft es der Spieler, alle Punkte zu sammeln, hat er gewonnen (Sieg-Meldung).
5. Kollidiert die Spielfigur mit einem der Gegner, hat der Spieler verloren (Game-Over-Meldung).
6. In beiden Fällen startet das Spiel nach der entsprechenden Meldung auf der **ausgewählten Karte** erneut.
7. Für einen Kartenwechsel ist im aktuellen Stand ein Neustart des Programms erforderlich.

---

Viel Spaß beim Spielen und Erweitern!
