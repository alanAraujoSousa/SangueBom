package com.bom.sangue.sanguebom.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan on 11/11/15.
 */
public class DBProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.bom.sangue.sanguebom.provider.DBProvider");

    // Authority do nosso provider, a ser usado nas Uris.
    public static final String AUTHORITY =
            "com.bom.sangue.sanguebom.provider.DBProvider";

    // Nome do arquivo que irá conter o banco de dados.
    private static  final String DATABASE_NAME = "sanguebom.db";

    // Versao do banco de dados.
    // Este valor é importante pois é usado em futuros updates do DB.
    private static  final int  DATABASE_VERSION = 1;

    // Nome da tabela que irá conter o usuario.
    private static final  String USER_TABLE = "user";

    // Nome da tabela que ira conter todas as doaçoes.
    private static final String DONATION_TABLE = "donations";

    // 'Id' da Uri referente às notas do usuário.
    private  static final int USERS = 1;

    // Tag usada para imprimir os logs.
    public static final String TAG = "DBProvider";

    // Instância da classe utilitária
    private DBHelper mHelper;

    // Uri matcher - usado para extrair informações das Uris
    private static final UriMatcher mMatcher;

    private static HashMap<String, String> mProjection;

// static {
//        mProjection = new HashMap<String, String>();
//        mProjection.put(User.USER_ID, Notes.NOTE_ID);
//        mProjection.put(Notes.TEXT, Notes.TEXT);
//    }

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(AUTHORITY, USER_TABLE, USERS);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        mHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    public static final class  User implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + DBProvider.AUTHORITY + "/user");

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + DBProvider.AUTHORITY;

        public static final String USER_ID = "_id";

        public static Map<String, String> USER_COLUMNS = new HashMap<String, String>();

        static {
            USER_COLUMNS.put("login", "LONGTEXT");
        }
    }

    private static class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String userColumns = "";
            for (Map.Entry<String, String> entry : User.USER_COLUMNS.entrySet()) {
                String columnName = entry.getKey();
                String columnType = entry.getValue();
                userColumns += ", " + columnName + " " + columnType;
            }
            db.execSQL("CREATE TABLE " + USER_TABLE + " (" +
                    User.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                    userColumns + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            // Como ainda estamos na primeira versão do DB,
            // não precisamos nos preocupar com o update agora.
        }
    }
}
