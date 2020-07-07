package auto.cn.greendaogenerate;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyClass {
    public static void main(String[] args){
        Schema schema = new Schema(1, "auto.cn.imoocgreendaostudy");
        Entity son = schema.addEntity("Son");//表名尽量以大写开头
        son.addStringProperty("name");
        son.addIntProperty("age");
        son.addIdProperty();
        Property fatherId = son.addLongProperty("fatherId").getProperty();

        Entity father = schema.addEntity("Father");
        father.addStringProperty("name");
        father.addIntProperty("age");
        father.addIdProperty();

        son.addToOne(father,fatherId);
        try {
            new DaoGenerator().generateAll(schema,"app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
