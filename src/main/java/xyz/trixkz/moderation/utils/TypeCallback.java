package xyz.trixkz.moderation.utils;

import java.io.Serializable;

public interface TypeCallback<T> extends Serializable {

    void callback(T paramT);
}
