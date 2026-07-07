# 🏎️ Nokia Racing Game

A Nokia-inspired classic racing game built with **Java Swing** demonstrating
all core Object-Oriented Programming concepts.

---

## 📁 Project Structure

```
RacingGame/
├── src/
│   └── game/
│       ├── Main.java                   ← Entry point
│       ├── core/
│       │   ├── GameWindow.java         ← JFrame window
│       │   ├── GamePanel.java          ← Game loop + rendering
│       │   ├── GameState.java          ← Enum for game states
│       │   ├── InputHandler.java       ← Keyboard input
│       │   └── TrafficManager.java     ← Enemy vehicle spawning
│       ├── entities/
│       │   ├── Vehicle.java            ← Abstract base class ⭐
│       │   ├── PlayerCar.java          ← Player's red car
│       │   ├── EnemyCar.java           ← Traffic cars
│       │   ├── Truck.java              ← Truck obstacles
│       │   └── Explosion.java          ← Visual crash effect
│       ├── ui/
│       │   ├── RoadRenderer.java       ← Road + scenery drawing
│       │   ├── HUD.java                ← On-screen stats + buttons
│       │   └── ScreenRenderer.java     ← Menu/GameOver screens
│       └── util/
│           └── Constants.java          ← All game constants
├── run.bat                             ← Run on Windows
├── run.sh                              ← Run on Linux/Mac
└── README.md                           ← This file
```

---

## 🎮 How to Run

### Requirements

- Java JDK 8 or higher installed
- Download from: https://www.java.com/download/

### Windows

```
Double-click run.bat
```

Or in terminal:

```cmd
javac -d out -sourcepath src src/game/Main.java
java -cp out game.Main
```

### Linux / Mac

```bash
chmod +x run.sh
./run.sh
```

### IntelliJ IDEA / Eclipse

1. Open project folder
2. Mark `src/` as Sources Root
3. Run `game.Main`

---

## 🕹️ Controls

| Key     | Action                |
| ------- | --------------------- |
| ← Left  | Move to left lane     |
| → Right | Move to right lane    |
| ↑ Up    | Increase speed (hold) |
| ↓ Down  | Decrease speed (hold) |
| P       | Pause / Unpause       |
| Enter   | Start game / Restart  |

---

## 📚 OOP Concepts Used (For Your Presentation)

### 1. 🔷 ABSTRACTION — `Vehicle.java`

```java
public abstract class Vehicle {
    public abstract void draw(Graphics2D g2d);  // What to draw — decided by subclass
    public abstract void update();               // How to move — decided by subclass

    // Concrete shared method — same for all vehicles
    public boolean collidesWith(Vehicle other) { ... }
}
```

- `Vehicle` defines the **blueprint** (what all vehicles have)
- Subclasses fill in the specific **implementation**

---

### 2. 🔶 ENCAPSULATION — All classes

```java
// In Vehicle.java
private int x;     // Private — cannot be accessed directly
private int y;

public int getX() { return x; }   // Access only through getter
public void setX(int x) { this.x = x; }  // Controlled modification
```

- Data is hidden; only allowed access through getter/setter methods
- Prevents accidental modification from outside the class

---

### 3. 🔵 INHERITANCE — Entity hierarchy

```
Vehicle (abstract parent)
├── PlayerCar  (extends Vehicle)
├── EnemyCar   (extends Vehicle)
└── Truck      (extends Vehicle)
```

```java
public class EnemyCar extends Vehicle { ... }
public class Truck     extends Vehicle { ... }
```

- All vehicles **inherit** `x, y, speed, collidesWith()` from `Vehicle`
- No code duplication!

---

### 4. 🟣 POLYMORPHISM — `TrafficManager.java`

```java
// One list holds BOTH EnemyCar and Truck objects!
private List<Vehicle> vehicles = new ArrayList<>();

// Java automatically calls the RIGHT draw() for each object:
for (Vehicle v : vehicles) {
    v.draw(g2d);  // EnemyCar.draw() or Truck.draw() — decided at runtime!
}
```

- Same variable type (`Vehicle`), different behavior at runtime
- This is **Runtime Polymorphism** (method overriding)

---

### 5. 🟠 INTERFACE — `InputHandler.java`

```java
public class InputHandler implements KeyListener {
    @Override public void keyPressed(KeyEvent e)  { ... }
    @Override public void keyReleased(KeyEvent e) { ... }
    @Override public void keyTyped(KeyEvent e)    { }
}
```

- `KeyListener` is a Java **interface** (contract of methods to implement)
- `implements` = "I agree to provide all these methods"

---

### 6. 🟡 ENUM — `GameState.java`

```java
public enum GameState {
    MENU, COUNTDOWN, PLAYING, CRASHED, GAME_OVER, PAUSED
}
```

- Replaces magic numbers (0=menu, 1=playing) with readable names
- Prevents invalid state values

---

### 7. 🟢 COMPOSITION — `GamePanel.java`

```java
public class GamePanel extends JPanel {
    private PlayerCar      player;          // HAS-A PlayerCar
    private TrafficManager trafficManager;  // HAS-A TrafficManager
    private HUD            hud;             // HAS-A HUD
}
```

- GamePanel is built **from** other objects (not inherited from them)
- "Has-A" relationship vs Inheritance's "Is-A" relationship

---

### 8. 🔴 NESTED CLASS — `Explosion.java`

```java
public class Explosion {
    // Particle only makes sense INSIDE Explosion
    private static class Particle {
        float x, y, vx, vy, alpha;
        // ...
    }
}
```

- Inner class is scoped to its parent class
- Organizes related code together

---

## 🎯 Game Features

- ✅ 3-lane highway with smooth scrolling
- ✅ Red player car with lane-change animation
- ✅ Enemy cars in 8 different colors
- ✅ Trucks (bigger, slower obstacles)
- ✅ Increasing difficulty every 200 KM
- ✅ Explosion particle effect on crash
- ✅ 3 lives system
- ✅ Speed meter (green → red)
- ✅ Interactive key-press button display
- ✅ Countdown timer (3-2-1-GO!)
- ✅ Grade system (D through S rank)
- ✅ Pause functionality
- ✅ Animated tree scenery
- ✅ Double-buffered rendering (no flicker)

---

## 🏆 Grading Rubric Checklist

| Requirement        | Where Implemented                   |
| ------------------ | ----------------------------------- |
| Abstraction        | `Vehicle.java` (abstract class)     |
| Encapsulation      | All classes (private + getters)     |
| Inheritance        | PlayerCar, EnemyCar, Truck          |
| Polymorphism       | TrafficManager.drawAll()            |
| Interface          | InputHandler implements KeyListener |
| Constructors       | All entity classes                  |
| Method Overriding  | draw() and update() in Vehicle      |
| Collections        | ArrayList in TrafficManager         |
| Enum               | GameState.java                      |
| Inner class        | Explosion.Particle                  |
| Game Loop / Thread | GamePanel implements Runnable       |

## License

This project is shared for educational and learning purposes.
