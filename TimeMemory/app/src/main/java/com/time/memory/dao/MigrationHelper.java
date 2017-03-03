package com.time.memory.dao;


import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.time.memory.util.CLog;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Qiu
 * @version V1.3
 * @Description:数据库版本更新辅助类
 * @date 2017/1/20 11:31
 */
public final class MigrationHelper {
	public static boolean DEBUG = false;
	private static String TAG = "MigrationHelper";
	private static final String SQLITE_MASTER = "sqlite_master";
	private static final String SQLITE_TEMP_MASTER = "sqlite_temp_master";

	public MigrationHelper() {
	}

	public static void migrate(SQLiteDatabase db, Class... daoClasses) {
		StandardDatabase database = new StandardDatabase(db);
		printLog("【The Old Database Version】" + db.getVersion());
		printLog("【Generate temp table】start");
		generateTempTables(database, daoClasses);
		printLog("【Generate temp table】complete");
		dropAllTables(database, true, daoClasses);
		createAllTables(database, false, daoClasses);
		printLog("【Restore data】start");
		restoreData(database, daoClasses);
		printLog("【Restore data】complete");
	}

	public static void restoreData(SQLiteDatabase db, Class... daoClasses) {
		StandardDatabase database = new StandardDatabase(db);
		restoreData(database, daoClasses);
	}

	private static void generateTempTables(Database db, Class... daoClasses) {
		for (int i = 0; i < daoClasses.length; ++i) {
			String tempTableName = null;
			DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
			String tableName = daoConfig.tablename;
			if (!isTableExists(db, false, tableName)) {
				printLog("【New Table】" + tableName);
			} else {
				try {
					tempTableName = daoConfig.tablename.concat("_TEMP");
					StringBuilder e = new StringBuilder();
					e.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";");
					db.execSQL(e.toString());
					StringBuilder insertTableStringBuilder = new StringBuilder();
					insertTableStringBuilder.append("CREATE TEMPORARY TABLE ").append(tempTableName);
					insertTableStringBuilder.append(" AS SELECT * FROM ").append(tableName).append(";");
					db.execSQL(insertTableStringBuilder.toString());
					printLog("【Table】" + tableName + "\n ---Columns-->" + getColumnsStr(daoConfig));
					printLog("【Generate temp table】" + tempTableName);
				} catch (SQLException var8) {
					Log.e(TAG, "【Failed to generate temp table】" + tempTableName, var8);
				}
			}
		}

	}

	private static boolean isTableExists(Database db, boolean isTemp, String tableName) {
		if (db != null && !TextUtils.isEmpty(tableName)) {
			String dbName = isTemp ? "sqlite_temp_master" : "sqlite_master";
			String sql = "SELECT COUNT(*) FROM " + dbName + " WHERE type = ? AND name = ?";
			Cursor cursor = null;
			int count = 0;

			try {
				cursor = db.rawQuery(sql, new String[]{"table", tableName});
				if (cursor == null || !cursor.moveToFirst()) {
					boolean e = false;
					return e;
				}

				count = cursor.getInt(0);
			} catch (Exception var11) {
				var11.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}

			}

			return count > 0;
		} else {
			return false;
		}
	}

	private static String getColumnsStr(DaoConfig daoConfig) {
		if (daoConfig == null) {
			return "no columns";
		} else {
			StringBuilder builder = new StringBuilder();

			for (int i = 0; i < daoConfig.allColumns.length; ++i) {
				builder.append(daoConfig.allColumns[i]);
				builder.append(",");
			}

			if (builder.length() > 0) {
				builder.deleteCharAt(builder.length() - 1);
			}

			return builder.toString();
		}
	}

	private static void dropAllTables(Database db, boolean ifExists, @NonNull Class... daoClasses) {
		reflectMethod(db, "dropTable", ifExists, daoClasses);
		//dropMethod(db, "dropTable", ifExists, daoClasses);
		printLog("****************************************************************************************************************************************************************************************【Drop all table】");
	}

	private static void createAllTables(Database db, boolean ifNotExists, @NonNull Class... daoClasses) {
		reflectMethod(db, "createTable", ifNotExists, daoClasses);
		printLog("****************************************************************************************************************************************************************************************【create all table】");

	}

	private static void dropMethod(Database db, String methodName, boolean isExists, @NonNull Class... daoClasses) {
		if (daoClasses.length >= 1) {
			try {
				int length = daoClasses.length;
				for (int i = 0; i < length; ++i) {
					String tableName = new DaoConfig(db, daoClasses[i]).tablename;
					dropTable(db, isExists, tableName);
					CLog.e(TAG, "******************************tableName:" + tableName);
				}
			} catch (Exception var9) {
				var9.printStackTrace();
			}
		}
	}

	private static void reflectMethod(Database db, String methodName, boolean isExists, @NonNull Class... daoClasses) {
		if (daoClasses.length >= 1) {
			try {
				Class[] e = daoClasses;
				int var5 = daoClasses.length;
				for (int i = 0; i < var5; ++i) {
					Class cls = e[i];
					Method method = cls.getMethod(methodName, new Class[]{Database.class, Boolean.TYPE});
					CLog.e(TAG, " i:  " + i + "    cls:" + cls.getName().toString() + "    methodName:" + methodName);
					method.invoke((Object) null, new Object[]{db, Boolean.valueOf(isExists)});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void restoreData(Database db, Class... daoClasses) {
		for (int i = 0; i < daoClasses.length; ++i) {
			DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
			String tableName = daoConfig.tablename;
			String tempTableName = daoConfig.tablename.concat("_TEMP");
			if (isTableExists(db, true, tempTableName)) {
				try {
					List e = getColumns(db, tempTableName);
					ArrayList properties = new ArrayList(e.size());

					for (int dropTableStringBuilder = 0; dropTableStringBuilder < daoConfig.properties.length; ++dropTableStringBuilder) {
						String insertTableStringBuilder = daoConfig.properties[dropTableStringBuilder].columnName;
						if (e.contains(insertTableStringBuilder)) {
							properties.add(insertTableStringBuilder);
						}
					}

					if (properties.size() > 0) {
						String var11 = TextUtils.join(",", properties);
						StringBuilder var13 = new StringBuilder();
						var13.append("INSERT INTO ").append(tableName).append(" (");
						var13.append(var11);
						var13.append(") SELECT ");
						var13.append(var11);
						var13.append(" FROM ").append(tempTableName).append(";");
						db.execSQL(var13.toString());
						printLog("【Restore data】 to " + tableName);
					}

					StringBuilder var12 = new StringBuilder();
					var12.append("DROP TABLE ").append(tempTableName);
					db.execSQL(var12.toString());
					printLog("【Drop temp table】" + tempTableName);
				} catch (SQLException var10) {
					Log.e(TAG, "【Failed to restore data from temp table 】" + tempTableName, var10);
				}
			}
		}
	}

	private static void dropTable(Database db, boolean ifExists, String table) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"" + table + "\"";
		db.execSQL(sql);
	}

	private static List<String> getColumns(Database db, String tableName) {
		Object columns = null;
		Cursor cursor = null;

		try {
			cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 0", (String[]) null);
			if (null != cursor && cursor.getColumnCount() > 0) {
				columns = Arrays.asList(cursor.getColumnNames());
			}
		} catch (Exception var8) {
			var8.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}

			if (null == columns) {
				columns = new ArrayList();
			}
		}
		return (List) columns;
	}

	private static void printLog(String info) {
		if (DEBUG) {
			Log.e(TAG, info);
		}

	}
}
