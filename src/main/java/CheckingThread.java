import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CheckingThread extends Thread {
    private List<String> string = Collections.synchronizedList(new LinkedList<>());
    private Map<String, String> mapOfFoundLines = new ConcurrentHashMap<>();
    private String pattern;
    private boolean csvFileIsReading = true;
    private Integer csvFileColumn;

    public CheckingThread(String pattern, Integer csvFileColumn) {
        this.pattern = pattern;
        this.csvFileColumn = csvFileColumn;
    }

    public void addString(String string) {
        this.string.add(string);
    }

    @Override
    public void run() {
        String currentStr;
        String str;
        while (string.size() > 0 || csvFileIsReading) {
            if (string.size() > 0) {
                currentStr = string.remove(0);
                str = currentStr.split(",")[csvFileColumn];
                if (str.startsWith(pattern)) {
                    mapOfFoundLines.put(str, currentStr);
                }
            }
        }
    }

    public void setFlag(boolean flag) {
        this.csvFileIsReading = flag;
    }


    public Map<String, String> getFoundLines() {
        return mapOfFoundLines;
    }
}
