package auto.cn.greendaogenerate;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import auto.cn.imoocgreendaostudy.R;
import de.greenrobot.dao.query.QueryBuilder;

public class AtyTest extends Activity {
    private DaoMaster master;
    private DaoSession session;
    private SQLiteDatabase db;
    private SonDao sonDao;
    private FatherDao fatherDao;
    public void add(){

        Son son=new Son();
        son.setName("欧阳克");
        son.setAge(20);
        sonDao.insert(son);

        Father father=new Father();
        father.setName("欧阳锋");
        father.setAge(50);
        father.setSon(son);
        fatherDao.insert(father);
        Father father1=new Father();
        father1.setName("欧阳爸爸");
        father1.setAge(55);
        father1.setSon(son);
        fatherDao.insert(father1);

    }
    public void queryOneToMany(){
        List<Son> sons = sonDao.queryBuilder().list();
        for (Son son :
                sons) {
            List<Father> fathers = son.getFathers();
            for (Father father :
                    fathers) {
                Log.d("tag", "queryOneToMany() called"+son.getName()+"father:"+father.getName());
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDb();
        //设置控制台输出sql语句，filter tag：”greenDAO”
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
       // add();
        queryOneToMany();
    }

    private void openDb() {
        db = new DaoMaster.DevOpenHelper(AtyTest.this, "personotm.db", null)
                .getReadableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();
        sonDao = session.getSonDao();
        fatherDao = session.getFatherDao();
    }
}
