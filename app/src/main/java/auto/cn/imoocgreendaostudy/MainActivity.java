package auto.cn.imoocgreendaostudy;

import android.Manifest;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import de.greenrobot.dao.query.LazyList;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

public class MainActivity extends AppCompatActivity {
    private DaoMaster master;
    private DaoSession session;
    private SQLiteDatabase db;
    private SonDao sonDao;
    private FatherDao fatherDao;

    private void openDb() {
        db = new DaoMaster.DevOpenHelper(MainActivity.this, "person.db", null)
                .getReadableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();
        sonDao = session.getSonDao();
        fatherDao = session.getFatherDao();
    }

    private void addPerson() {
        Son son = new Son();
        son.setName("nate");
        son.setAge(18);
        Father father = new Father();
        father.setName("tom");
        father.setAge(40);
        long fatherId = fatherDao.insert(father);
        son.setFatherId(fatherId);
        sonDao.insert(son);

        Son son1 = new Son();
        son1.setName("guojing");
        son1.setAge(28);
        Father father1 = new Father();
        father1.setName("hongqigong");
        father1.setAge(70);
        long fatherId1 = fatherDao.insert(father1);
        son1.setFatherId(fatherId1);
        sonDao.insert(son1);
        Son son2 = new Son();
        son2.setName("欧阳克");
        son2.setAge(28);
        Father father2 = new Father();
        father2.setName("欧阳锋");
        father2.setAge(60);
        long fatherId2 = fatherDao.insert(father2);
        son2.setFatherId(fatherId2);
        sonDao.insert(son2);
    }

    private void querySon() {
        //List<Son> sons = sonDao.queryBuilder().list();//实时加载到内存
        //懒加载，常用于级联查询，只有在用到时对象的某个属性时，才将数据库中的文本转换为对象
        LazyList<Son> sons = sonDao.queryBuilder().listLazy();
        for (Son son : sons) {
            Log.e("tag", "querySon() called" + son);
        }
        sons.close();//需要手动关闭游标
    }

    public void queryEq() {
        Son guojing = sonDao.queryBuilder().where(SonDao.Properties.Name.eq("guojing")).unique();
        Log.d("tag", "queryEq() called" + guojing);
    }

    //
    public void queryLike() {
        List data = sonDao.queryBuilder().where(SonDao.Properties.Name.like("nate%")).list();
        Log.d("tag", "queryLike() called" + data);
    }

    //
    public void queryBetween() {
        List data = sonDao.queryBuilder().where(SonDao.Properties.Age.between(20, 30)).list();
        Log.d("tag", "queryBetween() called" + data);
    }

    //>
    public void queryGt() {
        List data = sonDao.queryBuilder().where(SonDao.Properties.Age.gt(17)).list();
        Log.d("tag", "queryGt() called" + data);
    }

    //<
    public void queryLt() {
        List data = sonDao.queryBuilder().where(SonDao.Properties.Age.lt(27)).list();
        Log.d("tag", "queryLt() called" + data);
    }

    //!=
    public void queryNotEq() {
        List data = sonDao.queryBuilder().where(SonDao.Properties.Age.notEq(18)).list();
        Log.d("tag", "queryNotEq() called" + data);
    }

    //>=
    public void queryGe() {
        List data = sonDao.queryBuilder().where(SonDao.Properties.Age.ge(18)).list();
        Log.d("tag", "queryGe() called" + data);
    }

    //查询并排序
    public void queryOrder() {
        List data = sonDao.queryBuilder().where(SonDao.Properties.Name.like("g%")).orderAsc(SonDao.Properties.Age).list();
        Log.d("tag", "queryOrder() called" + data);
    }

    //自定义查询Sql
    public void querySql() {
        List data = sonDao.queryBuilder().where(
                new WhereCondition.StringCondition("FATHER_ID IN " +
                        "(SELECT _ID FROM FATHER WHERE Age <50 )")
        ).build().list();
        Log.d("tag", "querySql() called" + data);
    }
    //多线程查询
    public void queryThread(){
        final Query query = sonDao.queryBuilder().build();
        new Thread(){
            @Override
            public void run() {
                //错误的写法，程序崩溃
                //List data = query.list();
                List data = query.forCurrentThread().list();
                Log.d("tag", "queryThread() called"+data);
            }
        };
    }
   //一对一查询
    public void queryOneByOne(){
        List<Son> sons = sonDao.queryBuilder().list();
        for (Son son :sons) {
            Log.d("tag", "queryOneByOne() called"+son.getFather().getName());
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
        // addPerson();
        // querySon();
        //queryEq();
        //queryLike();
        //queryBetween();
        //queryGt();
//        queryOrder();
       // querySql();
//        queryThread();
        queryOneByOne();
    }
}
