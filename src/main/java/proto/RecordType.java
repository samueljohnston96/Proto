package proto;

public enum RecordType {
    DEBIT((byte) 0),
    CREDIT((byte) 1),
    START_AUTOPAY((byte) 2),
    END_AUTOPAY((byte) 3);

    private final byte typeByte;

    RecordType(byte typeByte) {
        this.typeByte = typeByte;
    }

    public static RecordType valueOf(byte i) {
        switch(i) {
            case(0):
                return RecordType.DEBIT;
            case(1):
                return RecordType.CREDIT;
            case(2):
                return RecordType.START_AUTOPAY;
            case(3):
                return RecordType.END_AUTOPAY;
        }
        // If an invalid recordType is given return null since any actual value will also cause problems.  This should
        // Never happen, but if it does the NPE will be easy to trace back here.
        return null;
    }
}
