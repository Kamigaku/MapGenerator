import com.kamigaku.dungeongenerator.Map;
import com.kamigaku.dungeongenerator.generator.GeneratorMap;
import com.kamigaku.dungeongenerator.utility.Utility;

public class Test {
 
    public static void main(String[] args) {
        //for(int i = 0; i < 1000; i++) { // @TODO : pb sur 48
        //    /*Map m = */new GeneratorMap(i * 1000 + 2)/*.getMap()*/;
        //    System.out.println("Running : " + (i+1) + " / 1000");
        //}
        //Utility.displayEntity(m.getMap());
        new GeneratorMap(49 * 1000 + 2);
    }
    
}
