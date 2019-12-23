import java.util.ArrayList;

public class GuiLogic {

    private ArrayList<Robot> robots;
    private int selected;


    public GuiLogic() {
        this.robots = new ArrayList<>();
        this.selected = 0;
        addRobot("No robot selected", 999);
    }

    public void addRobot(String name, String com) {
        this.robots.add(new Robot(name, com));
    }

    public void addRobot(String name, int com) {
        this.robots.add(new Robot(name, com));
    }

    public void buttonDriveForward() {


    }

    public ArrayList<Robot> getRobots() {
        return robots;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
