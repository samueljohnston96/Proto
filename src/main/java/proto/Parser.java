package proto;

import java.io.*;
import java.math.BigDecimal;


public class Parser {
    private final long vipUserId = 2456938384156277127L;

    private BigDecimal vipUserBalance = new BigDecimal(0);
    private int numberOfRecords = 0;

    // Use BigDecimal since the maximum number of debits or credits is undefined and could be massive.
    private BigDecimal totalDebits = new BigDecimal(0);
    private BigDecimal totalCredits = new BigDecimal(0);

    // Can be int since we know there will be at most 100 entries.
    private int autoStarts = 0;
    private int autoStop = 0;

    public Parser() {

    }

    public void parseTransaction(String fileName) {
        try {
            FileInputStream logs = new FileInputStream(fileName);
            DataInputStream reader = new DataInputStream(logs);
            // First read the 4 chars "MPS7"
            reader.readByte();
            reader.readByte();
            reader.readByte();
            reader.readByte();
            // Read the version
            reader.readByte();
            // Get the number of records
            numberOfRecords = reader.readInt();
            BigDecimal balance;
            // Read in each record (I use "i <= numberOfRecords" since "i < #ofRecords" doesn't hit all the records)
            for(int i = 0; i <= numberOfRecords; i++) {
                RecordType type = RecordType.valueOf(reader.readByte());
                // Read timestamp
                reader.readInt();
                // Read in long UserId
                long userId = reader.readLong();
                switch(type) {
                    case DEBIT:
                        // Get the amount
                        balance = new BigDecimal(reader.readDouble());

                        // If this is the vip add the balance to their counter
                        if (userId == vipUserId) {
                            vipUserBalance = vipUserBalance.add(balance);
                        }

                        // Add to the total debits
                        totalDebits = totalDebits.add(balance);

                        break;
                    case CREDIT:
                        // Get the amount
                        balance = new BigDecimal(reader.readDouble());

                        // If this is the vip add the balance to their counter
                        if (userId == vipUserId) {
                            vipUserBalance = vipUserBalance.add(balance);
                        }

                        // Add to the total credits
                        totalCredits = totalCredits.add(balance);
                        break;
                    case END_AUTOPAY:
                        // Increment the count of autoPay stops.
                        autoStop++;
                        break;
                    case START_AUTOPAY:
                        // Increment the count of autoPay starts.
                        autoStarts++;
                        break;
                }
            }

            System.out.println("Total debits: " + totalDebits.toPlainString());
            System.out.println("Total credits: " + totalCredits.toPlainString());
            System.out.println("Number of Autopays started: " + autoStarts);
            System.out.println("Number of Autopays ended: " + autoStop);
            System.out.println("Balance for user: " + vipUserId + " is " + vipUserBalance.toPlainString());

        } catch (FileNotFoundException e) {
            System.err.println("File " + fileName + " does not exist and therefore cannot be parsed.");
            return;
        } catch (IOException e) {
            System.err.println("IOException thrown while attempting to parse file.");
            return;
        }
    }
}
