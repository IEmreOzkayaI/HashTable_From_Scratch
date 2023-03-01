import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    private List<String> dataBase = new ArrayList<String>();

    public void readFile(String file) {
        try {

            Scanner scan = new Scanner(new File(file));
            scan.useDelimiter("\\W+");

            while (scan.hasNext()) {
                dataBase.add(scan.next());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getFileSize() {
        return dataBase.size();
    }

    public List<String> getFile() {
        return dataBase;
    }

    public Iterator<String> getFileIterator() {
        return dataBase.iterator();
    }
}
