package org.zama.reporter.entity

/**
 * @author Zakir Magdum.
 */
class EntityMap extends LinkedHashMap<String, Entity> {
    Entity entity(String type, String id, String name) {
        def key = KeyUtils.key(type, id)
        def entity = get(key) ?: new Entity(type, id, name)
        put(key, entity);
        return entity;
    }
    Entity entity(String type, String id) {
        return entity(type, id, id)
    }
    Entity entity(Entity parent, String type, String id, String name) {
        def key = KeyUtils.key(parent, type, id)
        def entity = get(key) ?: new Entity(type: type, key: key, name: name)
        put(key, entity);
        return entity;
    }
    Entity entity(Entity parent, String type, String id) {
        return entity(parent, type, id, id);
    }
}
