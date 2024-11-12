import java.io.*;
import java.util.*;

public class pass2 {

    // Definition Table to hold the macro definition
    static class DefinitionTable {
        int index;
        String definition;
        List<ArgumentListArray> argList;
        DefinitionTable next;

        DefinitionTable() {
            this.argList = new ArrayList<>();
            this.next = null;  // Initialize next as null
        }
    }

    // Name Table to hold the macro name and its corresponding index in Definition Table
    static class NameTable {
        int index;
        String name;
        DefinitionTable dtIndex;
        NameTable next;

        NameTable() {
            this.next = null;  // Initialize next as null
        }
    }

    // Argument List array holds the arguments used in the macro definition
    static class ArgumentListArray {
        int index;
        String arg;
        ArgumentListArray next;

        ArgumentListArray() {
            this.next = null;  // Initialize next as null
        }
    }

    // Table Pointers
    static DefinitionTable dtHead = null;
    static NameTable ntHead = null;
    static ArgumentListArray alHead = null;

    // Counters
    static int MDTC = 1;
    static int MNTC = 1;
    static int alIndex = 1;

    // Method to find argument in the argument list
    static ArgumentListArray findArgIndex(String arg) {
        ArgumentListArray temp = alHead;
        while (temp != null) {
            if (temp.arg.equals(arg)) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }

    // Method to find name in the name table and return its corresponding definition table
    static DefinitionTable findName(String name) {
        NameTable temp = ntHead;
        while (temp != null) {
            if (temp.name.equals(name)) {
                return temp.dtIndex;
            }
            temp = temp.next;
        }
        return null;
    }

    // Pass 1 - Populating the tables
    static void pass1(BufferedReader reader) throws IOException {
        String line;
        NameTable ntTemp = null;
        DefinitionTable dtTemp = null;
        ArgumentListArray alTemp = null;

        while ((line = reader.readLine()) != null) {
            if (line.contains("MACRO")) {
                // Identifying the macro name
                String[] tokens = line.split("\\s+");
                System.out.println("\nMACRO " + tokens[0] + " Detected...");

                // Initialize Name Table if not already
                if (ntHead == null) {
                    ntHead = new NameTable();
                    ntTemp = ntHead;
                } else {
                    ntTemp.next = new NameTable();
                    ntTemp = ntTemp.next;
                }
                ntTemp.index = MNTC++;
                ntTemp.name = tokens[0];
                System.out.println(tokens[0] + " added into Name Table");

                // Identifying the arguments that need to be added to the argument list array
                for (int i = 1; i < tokens.length; i++) {
                    if (!tokens[i].equals("MACRO")) {
                        if (alHead == null) {
                            alHead = new ArgumentListArray();
                            alTemp = alHead;
                        } else {
                            alTemp.next = new ArgumentListArray();
                            alTemp = alTemp.next;
                        }
                        alTemp.index = alIndex++;
                        alTemp.arg = tokens[i];
                        System.out.println("Argument " + alTemp.arg + " added into argument list array");
                    }
                }

                // Initialize Definition Table if not already
                if (dtHead == null) {
                    dtHead = new DefinitionTable();
                    dtTemp = dtHead;
                } else {
                    dtTemp.next = new DefinitionTable();
                    dtTemp = dtTemp.next;
                }
                dtTemp.definition = ntTemp.name;
                System.out.println("Definition table entry created for " + ntTemp.name);
                ntTemp.dtIndex = dtTemp;

                // Read the macro body until "MEND"
                while ((line = reader.readLine()) != null && !line.equals("MEND")) {
                    tokens = line.split("\\s+");
                    int isArg = 0;
                    int index = 0;
                    while (tokens.length > index) {
                        if (isArg == 0) {
                            dtTemp.next = new DefinitionTable();
                            dtTemp = dtTemp.next;
                            dtTemp.index = MDTC++;
                            dtTemp.definition = tokens[index];
                            System.out.println("Entry appended for " + dtTemp.definition + " at index " + dtTemp.index);
                            isArg = 1;
                        } else {
                            if (findArgIndex(tokens[index]) == null) {
                                alTemp.next = new ArgumentListArray();
                                alTemp.next.index = alTemp.index + 1;
                                alTemp = alTemp.next;
                                alTemp.arg = tokens[index];
                                dtTemp.argList.add(alTemp);
                            } else {
                                dtTemp.argList.add(findArgIndex(tokens[index]));
                            }
                        }
                        index++;
                    }
                }
            }
        }
        System.out.println("\nAll tables are updated. Pass 1 Complete!");
    }

    // Pass 2 - Expanding macros
    static void pass2(BufferedReader reader, BufferedWriter writer) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            DefinitionTable temp = findName(line.trim());
            if (temp != null) {
                while (temp != null && !temp.definition.equals("MEND")) {
                    writer.write("-\t" + temp.definition + "\t" + (temp.argList.size() > 0 ? temp.argList.get(0).arg : "") + "\t" + (temp.argList.size() > 1 ? temp.argList.get(1).arg : "") + "\n");
                    temp = temp.next;
                }
            }
        }
        System.out.println("\nOutput file updated with expanded code. Pass 2 Complete!");
    }

    public static void main(String[] args) throws IOException {
        File inputFile = new File("macro_input.asm");
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true));

        System.out.println("\nPass 1 in progress");
        pass1(reader);

        reader.close();  // Close the reader after Pass 1
        reader = new BufferedReader(new FileReader(inputFile));  // Reopen the file for Pass 2

        System.out.println("\nPass 2 in progress");
        pass2(reader, writer);

        reader.close();
        writer.close();  // Close the writer after Pass 2
    }
}
