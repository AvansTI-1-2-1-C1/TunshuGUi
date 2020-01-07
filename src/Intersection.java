public class Intersection {
    private int tentativeDistance;
    private boolean isVisited;
    private boolean isBlocked;

    /**
     * constructor sets the intersection to has not been visited and sets the tentative distance to infinite
     */
    public Intersection() {
        this.tentativeDistance = Integer.MAX_VALUE;
        this.isVisited = false;
        this.isBlocked = false;
    }

    @Override
    public String toString() {
        return ""+ tentativeDistance;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public int getTentativeDistance() {
        return tentativeDistance;
    }

    public void setTentativeDistance(int tentativeDistance) {
        this.tentativeDistance = tentativeDistance;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean isVisited() {
        return isVisited;
    }
}
