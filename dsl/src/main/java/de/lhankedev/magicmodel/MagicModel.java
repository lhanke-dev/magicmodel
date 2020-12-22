package de.lhankedev.magicmodel;

import java.util.Optional;

public interface MagicModel {

    <T> Optional<T> getObjectById(String id, Class<T> expectedClass);

}
