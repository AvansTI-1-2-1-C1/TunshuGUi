import jssc.SerialPort;
import jssc.SerialPortException;

public class Robot {

    private SerialPort serialPort;
    private int baudrate;
    private int databits;
    private int stopbits;
    private int parity;

    public Robot(String com) {
        this.serialPort = new SerialPort(com);
        this.baudrate = 115200;
        this.databits = 8;
        this.stopbits = 1;
        this.parity = 0;
    }

    public Robot(int com){
        this.serialPort = new SerialPort("COM" + com);
        this.baudrate = 115200;
        this.databits = 8;
        this.stopbits = 1;
        this.parity = 0;
    }

    public void init(){
        try{
            this.serialPort.openPort();
            System.out.println();
            serialPort.setParams(this.baudrate,this.databits, this.stopbits, this.parity);
        }
        catch (SerialPortException e){
            System.out.println(e);
        }
    }

    public void send(String message){
       try {
           serialPort.writeString(message);

           byte[] buffer = serialPort.readBytes(10);

           for (int i = 0; i < 10; i++) {
               System.out.print(buffer[i] + '-');
           }
       }
       catch (SerialPortException ex){
           System.out.println(ex);
       }
    }

    public void close(){
        try {
            serialPort.closePort();
        }
        catch (SerialPortException ex){
            System.out.println(ex);
        }
    }

    public String getPort() {
        return serialPort.getPortName();
    }
}
