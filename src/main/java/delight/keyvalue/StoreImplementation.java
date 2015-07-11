package delight.keyvalue;

public interface StoreImplementation<K, V> extends Store<K, V> {

    public void clearCache();

}
