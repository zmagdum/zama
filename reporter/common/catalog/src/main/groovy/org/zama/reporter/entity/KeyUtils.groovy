package org.zama.reporter.entity

/**
 * @author Zakir Magdum.
 */
class KeyUtils {
    static String key(String type, String id) { return "$type|$id" }
    static String key(Entity entity, String type, String id) { return "$type|$id|$entity.key" }
}
