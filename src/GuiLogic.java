import java.util.ArrayList;

public class GuiLogic {

    private ArrayList<Robot> robots;
    private int selected;


    public GuiLogic() {
        this.robots = new ArrayList<>();
        this.selected = 1;
        addRobot("No robot selected", 999);
        addRobot("Bot 4", 4);
    }

    public void addRobot(String name, String com) {
        this.robots.add(new Robot(name, com));
    }

    public void addRobot(String name, int com) {
        this.robots.add(new Robot(name, com));
    }

    public void speedSetting( Number speed ){
        this.robots.get(selected).send("o");
         System.out.println(speed);
//        this.robots.get(selected).send(speed.toString());
    }

    public void button(DriveCommands command) {
        switch (command) {
            //Forward(w)
            case Forward:
                System.out.println('w');
                this.robots.get(selected).send("w");
                break;
            //Backwards(s)
            case Backward:
                this.robots.get(this.selected).send("s");
                break;
            //Left(a)
            case Left:
                this.robots.get(this.selected).send("a");
                break;
            //Right(d)
            case Right:
                this.robots.get(this.selected).send("d");
                break;
            //Handbrake(space)
            case Brake:
                this.robots.get(this.selected).send(" ");
                break;
            //Faster(e)
            case Faster:
                this.robots.get(this.selected).send("e");
                break;
            //Slower(q)
            case Slower:
                this.robots.get(this.selected).send("q");
                break;
            //Mute(m)
            case Mute:
                this.robots.get(this.selected).send("m");
                break;
            //LineFollower(r)
            case LineFollower:
                this.robots.get(this.selected).send("r");
                break;
            //Hand break(h)
            case Handbrake:
                this.robots.get(this.selected).send("h");
                break;
            //Set speed(o)
            case SetSpeed:

                break;
            //Set Light state(l)
            case SetLight:

                break;
            //Set Speaker state(p)
            case SetSound:

                break;
            //All other keys
            default:
                break;
        }

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
