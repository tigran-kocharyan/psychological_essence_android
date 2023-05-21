package ru.hse.pe.utils.callback;

/**
 * Интерфейс для обработки события успешной подкачки данных firebase
 */
public interface FirebaseSuccessListener {
    void onDataFound(boolean isDataFetched);
}
