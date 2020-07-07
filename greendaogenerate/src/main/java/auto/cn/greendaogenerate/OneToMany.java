package auto.cn.greendaogenerate;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class OneToMany {
    public static void main(String[] args){
        Schema schema=new Schema(1,"auto.cn.greendaogenerate");
        Entity son=schema.addEntity("Son");
        son.addStringProperty("name");
        son.addIdProperty();
        son.addIntProperty("age");

        Entity father=schema.addEntity("Father");
        father.addIdProperty();
        father.addStringProperty("name");
        father.addIntProperty("age");

        Property sonId = father.addLongProperty("sonId").getProperty();

        father.addToOne(son,sonId);
        son.addToMany(father,sonId).setName("fathers");
        try {
            new DaoGenerator().generateAll(schema,"app/src/main/java/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
