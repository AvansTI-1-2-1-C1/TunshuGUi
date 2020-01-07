import java.util.ArrayList;

public class GuiLogic {

    private ArrayList<Robot> robots;
    private Robot selected;


    public GuiLogic() {
        this.robots = new ArrayList<>();
        addRobot("No robot selected", 999);
        addRobot("Bot 4", 4);
        addRobot("Bot 2", 6);
        this.selected = robots.get(1);
    }

    public void addRobot(String name, String com) {
        this.robots.add(new Robot(name, com));
    }

    public void addRobot(String name, int com) {
        this.robots.add(new Robot(name, com));
    }

    public void speedSetting( Number speed ){
        this.selected.send("o");
         System.out.println(speed);
         button(DriveCommands.SetSpeed, speed.toString());
//        this.robots.get(selected).send(speed.toString());
    }

    public void button(DriveCommands command) {
        switch (command) {
            //Forward(w)
            case Forward:
                System.out.println('w');
                this.selected.send("w");
                break;
            //Backwards(s)
            case Backward:
                this.selected.send("s");
                break;
            //Left(a)
            case Left:
                this.selected.send("a");
                break;
            //Right(d)
            case Right:
                this.selected.send("d");
                break;
            //Handbrake(space)
            case Brake:
                this.selected.send(" ");
                break;
            //Faster(e)
            case Faster:
                this.selected.send("e");
                break;
            //Slower(q)
            case Slower:
                this.selected.send("q");
                break;
            //Mute(m)
            case Mute:
                this.selected.send("m");
                break;
            //LineFollower(r)
            case LineFollower:
                this.selected.send("r");
                break;
            //Hand break(h)
            case Handbrake:
                this.selected.send("h");
                break;
            //All other keys
            default:
                break;
        }

    }

    public void button(DriveCommands command, String value) {
        switch (command) {
            //Set speed(o)
            case SetSpeed:
                if (value.equals("t")){
                    this.selected.send("ot");
                }else if (value.equals("f")){
                    this.selected.send("of");
                }
                break;
            //Set Light state(l)
            case SetLight:
                if (value.equals("t")){
                    this.selected.send("lt");
                }else if (value.equals("f")){
                    this.selected.send("lf");
                }
                break;
            //Set Speaker state(p)
            case SetSound:
                if (value.equals("t")){
                    this.selected.send("pt");
                }else if (value.equals("f")){
                    this.selected.send("pf");
                }
                break;
            //All other keys
            default:
                break;
        }

    }

    public ArrayList<Robot> getRobots() {
        return robots;
    }

    public Robot getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = this.robots.get(selected);
        System.out.println("Selected robot: "+this.selected.getName());
    }
}
