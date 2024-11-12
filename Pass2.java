import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

class Tuple {
    String mnemonic, m_class, opcode;
    int length;

    Tuple() {}

    Tuple(String s1, String s2, String s3, String s4) {
        mnemonic = s1;
        m_class = s2;
        opcode = s3;
        length = Integer.parseInt(s4);
    }
}

class SymTuple {
    String symbol, address, length;

    SymTuple(String s1, String s2, String i1) {
        symbol = s1;
        address = s2;
        length = i1;
    }
}

class LitTuple {
    String literal, address, length;

    LitTuple() {}

    LitTuple(String s1, String s2, String i1) {
        literal = s1;
        address = s2;
        length = i1;
    }
}

public class Pass2 {
    static int lc, iSymTabPtr = 0, iLitTabPtr = 0, iPoolTabPtr = 0;
    static int poolTable[] = new int[10];
    static Map<String, Tuple> MOT;
    static ArrayList<SymTuple> symtable;
    static ArrayList<LitTuple> littable;
    static Map<String, String> regAddressTable;
    static PrintWriter out_pass2;

    // Initialize the symbol table, literal table, and register address table
    static void initializeTables() throws Exception {
        symtable = new ArrayList<>();
        littable = new ArrayList<>();
        regAddressTable = new HashMap<>();

        String s;
        BufferedReader br;
        
        // Load symbol table
        br = new BufferedReader(new InputStreamReader(new FileInputStream("symtable.txt")));
        while ((s = br.readLine()) != null) {
            // Skip empty lines
            if (s.trim().isEmpty()) continue;

            StringTokenizer st = new StringTokenizer(s, "\t", false);
            if (st.countTokens() < 2) continue; // Skip malformed lines
            symtable.add(new SymTuple(st.nextToken(), st.nextToken(), ""));
        }
        br.close();

        // Load literal table
        br = new BufferedReader(new InputStreamReader(new FileInputStream("littable.txt")));
        while ((s = br.readLine()) != null) {
            // Skip empty lines
            if (s.trim().isEmpty()) continue;

            StringTokenizer st = new StringTokenizer(s, "\t", false);
            if (st.countTokens() < 2) continue; // Skip malformed lines
            littable.add(new LitTuple(st.nextToken(), st.nextToken(), ""));
        }
        br.close();

        // Register Address Table
        regAddressTable.put("AREG", "1");
        regAddressTable.put("BREG", "2");
        regAddressTable.put("CREG", "3");
        regAddressTable.put("DREG", "4");
    }

    // Pass 2 assembly process
    static void pass2() throws Exception {
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("output_pass1.txt")));
        out_pass2 = new PrintWriter(new FileWriter("output_pass2.txt"), true);

        String s;
        while ((s = input.readLine()) != null) {
            s = s.replaceAll("(\\()", " ");
            s = s.replaceAll("(\\))", " ");
            String[] ic_tokens = tokenizeString(s, " ");

            if (ic_tokens == null || ic_tokens.length == 0) {
                continue;
            }

            String output_str = "";
            String mnemonic_class = ic_tokens[1];
            String[] m_tokens = tokenizeString(mnemonic_class, ",");
            if (m_tokens[0].equalsIgnoreCase("IS")) {
                output_str += ic_tokens[0] + " ";  // Location Counter
                output_str += m_tokens[1] + " ";  // Opcode

                // Process operands
                for (int i = 2; i < ic_tokens.length; i++) {
                    String[] opr_tokens = tokenizeString(ic_tokens[i], ",");
                    if (opr_tokens[0].equalsIgnoreCase("RG")) {
                        output_str += opr_tokens[1] + " ";
                    } else if (opr_tokens[0].equalsIgnoreCase("S")) {
                        int index = Integer.parseInt(opr_tokens[1]);
                        output_str += symtable.get(index).address + " ";
                    } else if (opr_tokens[0].equalsIgnoreCase("L")) {
                        int index = Integer.parseInt(opr_tokens[1]);
                        output_str += littable.get(index).address + " ";
                    }
                }
            } else if (m_tokens[0].equalsIgnoreCase("DL")) {
                output_str += ic_tokens[0] + " ";
                if (m_tokens[1].equalsIgnoreCase("02")) {  // Data literal type 02
                    String[] opr_tokens = tokenizeString(ic_tokens[2], ",");
                    output_str += "00 00" + opr_tokens[1] + " ";
                }
            }

            System.out.println(output_str);
            out_pass2.println(output_str);
        }

        input.close();
    }

    // Tokenize a string based on the given separator
    static String[] tokenizeString(String str, String separator) {
        StringTokenizer st = new StringTokenizer(str, separator, false);
        String[] s_arr = new String[st.countTokens()];
        for (int i = 0; i < s_arr.length; i++) {
            s_arr[i] = st.nextToken();
        }
        return s_arr;
    }

    // Main function to start the process
    public static void main(String[] args) throws Exception {
        initializeTables();
        pass2();
    }
}
