package com.example.common_module.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.utils.LogUtils;
import com.example.common_module.App;
import com.example.common_module.db.mode.ConfigDataModel;
import com.example.common_module.db.mode.ConfigDataModelDao;
import com.example.common_module.db.mode.DaoMaster;
import com.example.common_module.db.mode.DaoSession;
import com.example.common_module.db.mode.EntryModel;
import com.example.common_module.db.mode.EntryModelDao;
import com.example.common_module.db.mode.ShootDataModel;
import com.example.common_module.db.mode.ShootDataModelDao;
import com.example.common_module.db.mode.SingleShootDataModel;
import com.example.common_module.db.mode.SingleShootDataModelDao;
import com.example.common_module.db.mode.UserModel;
import com.example.common_module.db.mode.UserModelDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;


/**
 * 断点续传
 * 数据库工具类-geendao运用
 * <p>
 * Note 私有方法暂时用不到
 */

public class DbDownUtil {
    private static DbDownUtil db;
    private final static String dbName = "tests_db";
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;


    public DbDownUtil() {
        context = App.getContext();
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }


    /**
     * 获取单例
     *
     * @return
     */
    public static DbDownUtil getInstance() {
        if (db == null) {
            synchronized (DbDownUtil.class) {
                if (db == null) {
                    db = new DbDownUtil();
                }
            }
        }
        return db;
    }


    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }


    public void saveEntry(EntryModel model) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EntryModelDao downInfoDao = daoSession.getEntryModelDao();
        downInfoDao.insert(model);
    }

    public void updateEntry(EntryModel model) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EntryModelDao downInfoDao = daoSession.getEntryModelDao();
        try {
            downInfoDao.update(model);
        } catch (Exception e) {
            LogUtils.e("updateEntry downInfoDao.update exception", e.getMessage());
            LogUtils.e("model = " + model.toString());
        }
    }

    private void deleteEntry(EntryModel model) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EntryModelDao downInfoDao = daoSession.getEntryModelDao();
        downInfoDao.delete(model);
    }


    private EntryModel queryEntryById(long Id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EntryModelDao downInfoDao = daoSession.getEntryModelDao();
        QueryBuilder<EntryModel> qb = downInfoDao.queryBuilder();
        qb.where(EntryModelDao.Properties.Id.eq(Id));
        List<EntryModel> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    private List<EntryModel> queryDownAll() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EntryModelDao downInfoDao = daoSession.getEntryModelDao();
        QueryBuilder<EntryModel> qb = downInfoDao.queryBuilder();
        return qb.list();
    }

    public long insert(SingleShootDataModel singleShootDataModel) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        SingleShootDataModelDao dao = daoSession.getSingleShootDataModelDao();
        return dao.insertOrReplace(singleShootDataModel);
    }

    public long insert(ShootDataModel shootDataModel) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ShootDataModelDao dao = daoSession.getShootDataModelDao();
        return dao.insertOrReplace(shootDataModel);
    }

    public long insertUser(UserModel userModel) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserModelDao dao = daoSession.getUserModelDao();
        return dao.insertOrReplace(userModel);
    }

    public UserModel findUser(long userId) {
        DaoSession daoSession = getReadDaoSession();
        List<UserModel> list = daoSession.queryRaw(UserModel.class, "where USER_ID = ?", String.valueOf(userId));
        return list.isEmpty() ? null : list.get(0);
    }
    public void deleteUser(long userId) {
        DaoSession daoSession = getReadDaoSession();
        UserModelDao dao = daoSession.getUserModelDao();
        dao.deleteByKey(userId);
    }
    public void deleteShootData(long id) {
        DaoSession daoSession = getReadDaoSession();
        ShootDataModelDao dao = daoSession.getShootDataModelDao();
        dao.deleteByKey(id);
    }

    public List<UserModel> findAllUser() {
        DaoSession daoSession = getReadDaoSession();
        UserModelDao dao = daoSession.getUserModelDao();
        return dao.queryBuilder().orderDesc(UserModelDao.Properties.CreateTime).list();
    }

    public List<UserModel> findAllUserByTimeAndName(long start_time, long end_time, String userName) {
        DaoSession daoSession = getReadDaoSession();
        UserModelDao dao = daoSession.getUserModelDao();
        return dao.queryBuilder()
                .where(
                        UserModelDao.Properties.CreateTime.between(start_time, end_time),
                        UserModelDao.Properties.Name.eq(userName)
                )
                .orderDesc(UserModelDao.Properties.CreateTime)
                .list();
    }

    public List<UserModel> findAllUserByTime(long start_time, long end_time) {
        DaoSession daoSession = getReadDaoSession();
        UserModelDao dao = daoSession.getUserModelDao();
        return dao.queryBuilder()
                .where(UserModelDao.Properties.CreateTime.between(start_time, end_time))
                .orderDesc(UserModelDao.Properties.CreateTime)
                .list();
    }
    public List<ShootDataModel> findAllShootDataModelByTime(long start_time, long end_time) {
        DaoSession daoSession = getReadDaoSession();
        ShootDataModelDao dao = daoSession.getShootDataModelDao();
        return dao.queryBuilder()
                .where(ShootDataModelDao.Properties.CreateTime.between(start_time, end_time))
                .orderDesc(ShootDataModelDao.Properties.CreateTime)
                .list();
    }

    public long insertConfigDataModel(ConfigDataModel configDataModel) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ConfigDataModelDao dao = daoSession.getConfigDataModelDao();
        return dao.insertOrReplace(configDataModel);
    }

    public List<ConfigDataModel> findAllConfigData() {
        DaoSession daoSession = getReadDaoSession();
        ConfigDataModelDao dao = daoSession.getConfigDataModelDao();
        return dao.queryBuilder().orderDesc(ConfigDataModelDao.Properties.CreateTime).list();
    }

    public List<ShootDataModel> searchListBout(List<Integer> ids) {
        if (ids.isEmpty())
            return Collections.emptyList();
        DaoSession daoSession = getReadDaoSession();
        ShootDataModelDao dao = daoSession.getShootDataModelDao();
        QueryBuilder<ShootDataModel> queryBuilder = dao.queryBuilder();
        queryBuilder.where(ShootDataModelDao.Properties.BoutId.in(ids));
        List<ShootDataModel> models = queryBuilder.list();
        for (int i = 0; i < models.size(); i++) {
            dao.load(models.get(i).getId());
        }
        return models;
    }


    public DaoSession getWriteDaoSession() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        return daoMaster.newSession();
    }

    public DaoSession getReadDaoSession() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        return daoMaster.newSession();
    }

}

