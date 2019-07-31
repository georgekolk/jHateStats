import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args)throws SQLException {


        DbHandler dbHandler = DbHandler.getInstance("jdbc:sqlite:D:/myfin.db");
        ArrayList<String> tagsList = dbHandler.returnOveralLTaggsFreomTable("deer");
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
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }

}


