package algorithms;

import io.WebCrawler;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Roshan on 2/14/2017.
 */
public class ChangeDetector {
    public static HashMap<String, ArrayList<String>> compare(HashMap<String, String> addressVal1, HashMap<String, String> addressVal2){

        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        result.put("Similar", new ArrayList<String>());
        result.put("Changed", new ArrayList<String>());
        result.put("New", new ArrayList<String>());
        result.put("Missing", new ArrayList<String>());

        for(String key: addressVal1.keySet()){
            if(addressVal2.containsKey(key)){
                if(addressVal1.get(key).hashCode() == addressVal2.get(key).hashCode()){
                    result.get("Similar").add(key);
                }else{
                    result.get("Changed").add(key + WebCrawler.TAG_SEPARATOR + addressVal1.get(key) + WebCrawler.TAG_SEPARATOR + addressVal2.get(key));
                }
            }else{
                result.get("Missing").add(key);
            }
        }

        for(String key: addressVal2.keySet()){
            if(!addressVal1.containsKey(key)){
                result.get("New").add(key);
            }
        }

        return result;
    }
}
