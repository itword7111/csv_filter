import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Manager implements Runnable {
    private String pattern;
    private Integer csvFileColumn;
    private List<CheckingThread> threads=new LinkedList<>();

    public Manager(String pattern,Integer csvFileColumn) {
        this.pattern = pattern;
        this.csvFileColumn=csvFileColumn;
    }

    public void run()  {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\airports.csv"));
            String csvFileLine;
            Map<String,String> foundLines = new LinkedHashMap<>();
            int threadIndex=0;
            boolean isThreadsAlive = true;

            for(int i=0;i<3;i++){
                CheckingThread thread = new CheckingThread(pattern,csvFileColumn);
                thread.start();
                threads.add(thread);
            }

            while((csvFileLine = reader.readLine()) != null){
                if(threadIndex<threads.size()-1){
                    threadIndex++;
                }
                else {
                    threadIndex=0;
                }
                this.threads.get(threadIndex).addString(csvFileLine);

            }

            for (CheckingThread tread:threads) {
                tread.setFlag(false);
            }

            while (isThreadsAlive){
                isThreadsAlive=false;
                for (CheckingThread thread:threads) {
                    if(thread.isAlive()){
                        isThreadsAlive=true;
                    }
                    foundLines.putAll(thread.getFoundLines());
                }
                foundLines.entrySet().stream().sorted(((o1, o2) -> o1.getKey().compareTo(o2.getKey()))).close();
            }
            foundLines.entrySet().stream().sorted(((o1, o2) -> o1.getKey().compareTo(o2.getKey()))).forEach((n) -> System.out.println(n.getValue()));
            System.out.println("Найдено "+foundLines.size()+" строк");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
