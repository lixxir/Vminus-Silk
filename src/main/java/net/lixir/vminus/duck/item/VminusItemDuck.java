package net.lixir.vminus.duck.item;

public interface VminusItemDuck {
    default boolean vminus$isBanned() {
        return false;
    }
}
