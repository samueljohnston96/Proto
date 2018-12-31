package proto;

public class Proto {
    public static void main(String[] args) {
        Parser parser = new Parser();
        parser.parseTransaction("txnlog.dat");
    }
}
