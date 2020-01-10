import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;

import java.awt.*;

public class Robot {

    private SerialPort serialPort;
    private String name;
    private int status;
    private int baudrate;
    private int databits;
    private int stopbits;
    private int parity;
    private String currentTask;
    private boolean isInit;


    public Robot(String name, String com) {
        this.name = name;
        this.serialPort = new SerialPort(com);
        this.baudrate = 115200;
        this.databits = 8;
        this.stopbits = 1;
        this.parity = 0;
        this.status = 0;
        this.currentTask = "--";
        this.isInit = false;
    }

    public Robot(String name, int com) {
        this.name = name;
        this.serialPort = new SerialPort("COM" + com);
        this.baudrate = 115200;
        this.databits = 8;
        this.stopbits = 1;
        this.parity = 0;
    }

    public void init() {
        System.out.println("Init");
        try {
            if(!isInit) {
                this.serialPort.openPort();
                System.out.println();
                this.serialPort.setParams(this.baudrate, this.databits, this.stopbits, this.parity);
                this.isInit = true;
                System.out.println("Done");
            }
        } catch (SerialPortException e) {
            System.out.println(e);
            this.isInit = false;
            System.out.println("Error");
        }
    }

    public void send(String message) {
        System.out.println(message);
        try {
            serialPort.writeString(message.length() + message);
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

    public boolean sendStatus(String message) {
        try {
            send(message);

            byte[] buffer = serialPort.readBytes(1);
            if (buffer.equals("t")) {
                return true;
            } else if (buffer.equals("f")) {
                return false;
            } else {
                return false;
            }
        } catch (SerialPortException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public void close() {
        try {
            serialPort.closePort();
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

    public String getPort() {
        return serialPort.getPortName();
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        String state = "error";
        switch (this.status) {
            case 0:
                state = "N/A";
                break;
            case 1:
                state = "idle";
                break;
            case 2:
                state = "driving";
                break;
            case 3:
                state = "Following line";
                break;
            case 4:
                state = "alert";
                break;
            case 5:
                state = "error";
                break;
            default:
                state = "default";

        }
        return state;
    }

    public String getCurrentTask() {
        return currentTask;
    }

    public boolean getStateComponents(int component) {
        switch (component) {
//          speaker
            case 1:
                return sendStatus("P");
//          lights
            case 2:
                return sendStatus("L");
            default:
                return false;

        }
    }
}
