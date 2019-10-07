import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args)throws SQLException, IOException {

        DbHandler dbHandler = DbHandler.getInstance("jdbc:sqlite:E:/myfin.db");
        ArrayList<String> tagsList = new ArrayList<String>();

        //это для сборки тегов из БД по таблице----------------------------------
        //tagsList = dbHandler.returnOveralLTaggsFreomTable("deer");
        //tagsList.addAll(dbHandler.returnOveralLTaggsFreomTable("deervideo"));
        //-----------------------------------------------------------------------


        //это для сборки тегов из БД по файлам и таблице--------------------------



        File dirToProcess = new File("E:\\explore\\tags\\dogvideo\\600\\");
        FileWriter nFile = new FileWriter("E:\\tempTAGstats" + "\\" + "OMFGdogvideo600");
        FileWriter nFileToDelete = new FileWriter("E:\\tempTAGstats" + "\\" + "OMFGdogvideo600delete.bat");

        for (File fileName: dirToProcess.listFiles()){
            tagsList.add(dbHandler.getTagsByFilename(fileName.getName(), "dogvideo"));
            nFile.write("file '"+fileName.getCanonicalPath()+"'\n");
            nFileToDelete.write("DEL " + fileName.getCanonicalPath()+"\n"); //нужно чтобы грохнуть это из обработанной папки
        }
        nFile.close();
        nFileToDelete.close();
        //-----------------------------------------------------------------------



        Map<String, Integer> states = new HashMap<String, Integer>();

        for (String tags: tagsList) {
            String[] words = tags.split("\\s+");
            for (String item : words) {
                System.out.println(item);

                if (states.containsKey(item)) {
                    states.put(item, states.get(item) + 1);
                } else {
                    states.put(item, 1);
                }
            }
        }

        Map<String, Integer> sortedMap = sortByValue(states);
        printMap(sortedMap);
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
    }

    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            //System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            System.out.println(entry.getKey() + ",");
        }
    }

}


