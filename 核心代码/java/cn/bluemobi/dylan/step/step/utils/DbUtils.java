package cn.bluemobi.dylan.step.step.utils;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.List;
/**
 * 数据库操作工具类，使用了LiteOrm数据库框架，导入liteorm包
 * Created by 夏天 on 2018/8/9.
 */
public class DbUtils {
    public static String DB_NAME;
    public static LiteOrm liteOrm;
    //创建数据库
    public static void createDb(Context _activity, String DB_NAME) {
        DB_NAME = DB_NAME + ".db";
        if (liteOrm == null) {
            liteOrm = LiteOrm.newCascadeInstance(_activity, DB_NAME);
            liteOrm.setDebugged(true);
        }
    }
    public static LiteOrm getLiteOrm() {
        return liteOrm;
    }
    //插入
    public static <T> void insert(T t) {
        liteOrm.save(t);
    }
    //插入列表
    public static <T> void insertAll(List<T> list) {
        liteOrm.save(list);
    }
    //查询所有该类的实例
    public static <T> List<T> getQueryAll(Class<T> cla) {
        return liteOrm.query(cla);
    }
    //所有
    public static <T> List<T> getQueryByWhere(Class<T> cla, String field, String[] value) {
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value));
    }
    //分页
    public static <T> List<T> getQueryByWhereLength(Class<T> cla, String field, String[] value, int start, int length) {
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value).limit(start, length));
    }
    public static <T> void deleteAll(Class<T> cla) {
        liteOrm.deleteAll(cla);
    }

    public static <T> int deleteWhere(Class<T> cla, String field, String[] value) {
        return liteOrm.delete(cla, new WhereBuilder(cla).where(field + "!=?", value));
    }
    public static <T> void update(T t) {
        liteOrm.update(t, ConflictAlgorithm.Replace);
    }
    public static <T> void updateALL(List<T> list) {
        liteOrm.update(list);
    }
    //关闭
    public static void closeDb() {
        liteOrm.close();
    }

}
