package ru.finance2.simplecrmandroid.helpers;

/**
 * Created by faiz on 09.12.2016.
 */
public class ApiHelper {
    final static String TWO_WAYS_ENTITY_SYNC_API_SUBPATH = "two-ways-entity-sync-api/"; // путь к контроллеру синхронизации двусторонней сущности
    final static String ONE_WAY_ENTITY_SYNC_API_SUBPATH  = "one-way-entity-sync-api/"; // путь к контроллеру api синхронизации односторонней сущности
    /**
     * Возвращает значение из поля настроек "Ввести URL сервера синхронизации БД"<br/>
     * (По логике там должна содержаться строка из протокола и домена, например "http://192.168.1.30/")<br/>
     * @return возвращает строку из поля настроек либо пустую строку, если там ничего нет
     */
    public static String getBasicSyncPath() {
        return "http://192.168.1.38/";
    }

    public static String getOneWayEntitySyncApiPath() {
        return getBasicSyncPath()+ONE_WAY_ENTITY_SYNC_API_SUBPATH;
    }

    public static String getTwoWaysEntitySyncApiPath() {
        return getBasicSyncPath()+TWO_WAYS_ENTITY_SYNC_API_SUBPATH;
    }

    public static String getToken() {
        return "1";
    }
}
