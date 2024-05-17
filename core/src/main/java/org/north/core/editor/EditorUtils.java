package org.north.core.editor;

import org.north.core.component.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class EditorUtils {

    public static Object getComponentFieldValue(Component component, Field field) throws IllegalAccessException {
        return field.get(component);
    }

    public static Field[] getComponentFields(Component component) {
        Collection<Field> names = new ArrayList<>();
        Class<? extends Component> componentClass = component.getClass();
        for (Field field : componentClass.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers) && !Modifier.isPrivate(modifiers))
                names.add(field);
        }
        return names.toArray(new Field[0]);
    }
}
