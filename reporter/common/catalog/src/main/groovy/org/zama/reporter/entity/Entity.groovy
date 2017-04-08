package org.zama.reporter.entity

import org.zama.reporter.catalog.Catalog
import org.zama.reporter.catalog.PropertyDef

import java.text.NumberFormat

/**
 * @author Zakir Magdum.
 */
class Entity extends Expando {
    private NumberFormat nf = NumberFormat.getInstance();
    Entity(String type, String id, String name) {
        this.type = type
        this.key = KeyUtils.key(type, id);
        this.name = name;
    }

    void setProperty(String property, Object value) {
        if (["type", "key", "name"].contains(property)) {
            super.setProperty(property, value);
            return;
        }
        Catalog catalog = Catalog.instance();
        PropertyDef pr = catalog.getDefinition(this.type, property);
        if (pr == null) {
            throw new RuntimeException("Property $type.$property not found in Catalog.")
        }
        super.setProperty(property, convert(pr, value));
    }

    Object convert(PropertyDef property, Object value) {
        if (value instanceof String && property && property.dataType != "string") {
            switch(property.dataType) {
                case "number":
                case "integer":
                case "long":
                    return convertNumber(value);
                case "bool": return convertBoolean(value);
            }
        }
        return value;
    }

    Boolean convertBoolean(String value) {
        return ["poweredon", "true", "yes", "1", "on"].contains(value?.toLowerCase() ?: "false");
    }
    Number convertNumber(String value) {
        if (value == null) {
            return value;
        }
        return nf.parse(value.trim().replaceAll("\"", ""))
    }
}
