package mapRender;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TilePropertiesManager {
	private static Map<Integer, MapTile> tileTemplates = new HashMap<>();
	
    public static void loadTiles() {
    	
        try {
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(
            		TilePropertiesManager.class.getResourceAsStream("/tiles_data.json")
            	);
            List<MapTile> loadedTiles = gson.fromJson(reader, new TypeToken<List<MapTile>>(){}.getType());

            for (MapTile obj : loadedTiles) {
            	tileTemplates.put(obj.id, obj);
            	
            }
            
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static MapTile getTile(int id) {
    	return tileTemplates.get(id);
    }
}
