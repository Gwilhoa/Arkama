package fr.mediapi.arkama;

public interface SubPlugin {

    default boolean onLoad() {
        return true;
    }

    default void onUnload() {
    }
}
