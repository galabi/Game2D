package mapRender;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ObjectPropertiesManager {
	
	private static Map<Integer, MapObject> objectTemplates = new HashMap<>();
	
    public static void loadObjects() {
    	
        try {
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(
            	    ObjectPropertiesManager.class.getResourceAsStream("/objects_data.json")
            	);
            List<MapObject> loadedObjects = gson.fromJson(reader, new TypeToken<List<MapObject>>(){}.getType());

            for (MapObject obj : loadedObjects) {
                objectTemplates.put(obj.id, obj);
            }
            
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static MapObject getObject(int id) {
    	return objectTemplates.get(id);
    }
}