package com.bom.sangue.sanguebom.persistence.dao;

import android.content.ContentValues;
import android.content.Context;

import com.bom.sangue.sanguebom.persistence.bean.User;

/**
 * Created by alan on 25/11/15.
 */
public class UserDAO extends GenericDAO<User> {

    private static final String TABLE = "User";
    // FIXME
    public static final String SCRIPT_CREATE_TABLE = "CREATE TABLE User ( id INTEGER PRIMARY KEY autoincrement, login TEXT, password TEXT, email TEXT, token TEXT)";
    public static final String SCRIPT_DELETE_TABLE =  "DROP TABLE IF EXISTS " + TABLE;

    private static final String ID = "id";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String TOKEN = "token";

    private static UserDAO instance;

    public static UserDAO getInstance(Context context) {
        if(instance == null || !instance.dataBase.isOpen())
            instance = new UserDAO(context);
        return instance;
    }

    private UserDAO(Context context) {
        super(context);
    }

    @Override
    public String getPrimaryKeyName() {
        return ID;
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public ContentValues entityToContentValues(User entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, entity.getId());
        contentValues.put(PASSWORD, entity.getLogin());
        contentValues.put(LOGIN, entity.getPassword());
        contentValues.put(EMAIL, entity.getEmail());
        contentValues.put(TOKEN, entity.getToken());
        return contentValues;
    }

    @Override
    public User contentValuesToEntity(ContentValues contentValues) {
        User user = new User();
        user.setId(contentValues.getAsLong(ID));
        user.setLogin(contentValues.getAsString(LOGIN));
        user.setPassword(contentValues.getAsString(PASSWORD));
        user.setEmail(contentValues.getAsString(EMAIL));
        user.setToken(contentValues.getAsString(TOKEN));
        return user;
    }

    public void deleteMyUser() {
        User user = new User();
        user.setId(1l);
        super.delete(user);
    }
}
