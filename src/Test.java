import com.kamigaku.dungeongenerator.Map;
import com.kamigaku.dungeongenerator.generator.GeneratorMap;
import com.kamigaku.dungeongenerator.utility.Utility;

public class Test {
 
    public static void main(String[] args) {
        Map m = null;
        /*for(int i = 0; i < 1000; i++) {
            m = new GeneratorMap(i * 1000 + 2).getMap();
            System.out.println("Running : " + (i+1) + " / 1000");
            //Utility.displayEntity(m.getMap());
        }*/
        //m = new GeneratorMap(12387 * 1000 + 2, 800, 1000, 20, 40, 20, 40).getMap();
        //System.out.println("Running : " + (i+1) + " / 1000");
        //Utility.displayEntity(m.getMap());
        for(int i = 0; i < 10; i++) {
            long current = System.currentTimeMillis();
        m = new GeneratorMap(1234801419 + (i * 10)).getMap();
        //Utility.displayEntity(m.getMap());
        /*m = new GeneratorMap(10, 30).getMap();
        Utility.displayEntity(m.getMap());*/
        /*m = new GeneratorMap(1234897411l, 20, 36).getMap();
        Utility.displayEntity(m.getMap());*/
        /*m = new GeneratorMap(3, 4, 3, 4).getMap();
        Utility.displayEntity(m.getMap());
        m = new GeneratorMap(1234897411l, 10, 50, 20, 60).getMap();
        Utility.displayEntity(m.getMap());
        m = new GeneratorMap(40, 80, 20, 40, 20, 50).getMap();
        Utility.displayEntity(m.getMap());
        m = new GeneratorMap(1234897411l, 20, 50, 10, 20, 20, 30).getMap();
        Utility.displayEntity(m.getMap());*/
        /*m = new GeneratorMap(1472206212l, 40, 80, 20, 40, 20, 50).getMap();
        Utility.displayEntity(m.getMap());*/
        System.out.println(System.currentTimeMillis() - current + " ms");
        }
    }
    
}
