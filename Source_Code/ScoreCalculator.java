// Class Description: Calculates and updates the score, lines, and level in Classic Mode.

package TetrisProgram;

public class ScoreCalculator {

    private int score;
    private int linesCleared;
    private int level;

    public ScoreCalculator() {
        this.score = 0;
        this.linesCleared = 0;
        this.level = 1;
    }

    // Reset the score system
    public void reset() {
        score = 0;
        linesCleared = 0;
        level = 1;
    }

    // Soft drop: 1 point per cell
    public void addSoftDrop(int cells) {
        score += cells;
    }

    // Hard drop: 2 points per cell
    public void addHardDrop(int cells) {
        score += cells * 2;
        System.out.println("System: Hard drop used. (+ " + (cells * 2) + " pts)");
    }

    // Standard line clear
    public void addLineClear(int lines) {
        int points = switch (lines) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };
        int total = points * level;
        score += total;
        addLines(lines);

        String text = switch (lines) {
            case 1 -> "SINGLE";
            case 2 -> "DOUBLE";
            case 3 -> "TRIPLE";
            case 4 -> "TETRIS";
            default -> "";
        };

        System.out.println("System: Cleared " + lines + " lines. (+ " + points * level + " pts)");

        if (total > 0) {
            fireEvent(text, total);
        }
    }

    // T-Spin line clear
    public void addTSpin(int lines) {
        int points = switch (lines) {
            case 1 -> 800;
            case 2 -> 1200;
            case 3 -> 1600;
            default -> 400;
        };
        int total = points * level;
        score += total;
        addLines(lines);

        if (points != 400) {
            System.out.println("System: T-Spin " + lines + " lines! (+ " + points * level + " pts)");
        }
        else {
            System.out.println("System: T-Spin mini! (+ " + points * level + " pts)");
        }

        String text = (points == 400) ? "T-SPIN MINI" : "T-SPIN x" + lines;
        if (total > 0) {
            fireEvent(text, total);
        }

    }

    // Helper Method: Track total lines and handle level-up
    private void addLines(int lines) {
        int oldLevel = level;
        linesCleared += lines;

        while (linesCleared >= level * 10) {
            level++;
        }

        if (level != oldLevel) {
            System.out.println("System: Level up. New Level: " + level);

            // Notify GameEngine to update fall speed
            if (levelChangeListener != null) {
                levelChangeListener.levelChanged(level);
            }
        }
    }

    // Implement combos
    public void addCombo(int combo) {
        int bonus = combo * 50 * level;
        score += bonus;
        System.out.println("System: Combo x" + combo + " (+ " + bonus * level + " pts)");
        if (bonus > 0) {
            fireEvent("COMBO x" + combo, bonus);
        }
    }

    // Implement back to back bonus
    public void applyBackToBackBonus() {
        int bonus = (int)(score * 0.5);
        score += bonus;
        System.out.println("System: Back-to-Back Bonus! (+ " + bonus * level + " pts)");
        fireEvent("BACK-TO-BACK!", bonus);
        if (bonus > 0) {
            fireEvent("BACK-TO-BACK!", bonus);
        }
    }

    // Helper Method: Fires an event to the right panel based on the actions of the user
    private void fireEvent(String message, int points) {
        if (scoreEventListener != null) {
            scoreEventListener.onScoreEvent(message, points);
        }
    }

    public interface LevelChangeListener {
        void levelChanged(int newLevel);
    }

    private LevelChangeListener levelChangeListener;
    public void setLevelChangeListener(LevelChangeListener listener) {
        this.levelChangeListener = listener;
    }

    public interface ScoreEventListener {
        void onScoreEvent(String message, int points);
    }

    private ScoreEventListener scoreEventListener;

    public void setScoreEventListener(ScoreEventListener listener) {
        this.scoreEventListener = listener;
    }

    // Getters
    public int getScore() {
        return score;
    }
    public int getLinesCleared() {
        return linesCleared;
    }
    public int getLevel() {
        return level;
    }

}