/*
    Copyright 2004 Ernest Micklei @ PhilemonWorks.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
*/
package com.philemonworks.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * ToStringBuilder is a generic Helper class that creates a String
 * representation of the state of any Object. The format is:
 * 
 * {short classname}[field1=value,field2=value,....]
 * 
 * Optionally, the builder can show fields for which the value cannot be
 * accessed through reflection or through a standard getter. In addition, the
 * builder can hide fields for which the value is null.
 * 
 * @author E.M.Micklei
 */
public class ToStringBuilder {
    /**
     * This controls whether private fields are included or not
     */
    boolean accessErrorOccurred = false;
    /**
     * This controls whether fields values (up to a reference depth) are also presented by the receiver 
     */
    int depth = 0;

    /**
     * Return a String representing the full state of an Object by inspecting
     * all its fields.
     */
    public static String build(Object anObject) {
        return new ToStringBuilder().buildFrom(anObject, true, true, 0);
    }

    /**
     * Return a String representing the full state of an Object by inspecting
     * all its fields.
     * @param hideErrors :
     *                tells the builder to ignore fields for which the value cannot
     *                be accessed
     * @param hideNullValues :
     *                tells the builder to hide fields for which the value is null.
     * @param recursionDepth TODO
     */
    public static String build(Object anObject, boolean hideErrors, boolean hideNullValues, int recursionDepth) {
        return new ToStringBuilder().buildFrom(anObject, hideErrors, hideNullValues, recursionDepth);
    }

    /**
     * Return a String representing the full state of an Object by inspecting
     * all its fields.
     * @param hideErrors :
     *                tells the builder to ignore fields for which the value cannot
     *                be accessed
     * @param hideNullValues :
     *                tells the builder to hide fields for which the value is null.
     * @param recursionDepth TODO
     */
    public String buildFrom(Object anObject, boolean hideErrors, boolean hideNullValues, int recursionDepth) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(shortNameOf(anObject.getClass()));
        buffer.append('[');
        List fields = new ArrayList();
        this.collectDeclaredFieldsInto(anObject.getClass(), fields);
        for (int f = 0; f < fields.size(); f++) {
            Field each = (Field) fields.get(f);
            Object value = getValueOf(each, anObject);
            if (!(value == null && hideNullValues)) {
                if (!(hideErrors && accessErrorOccurred)) {
                    buffer.append(each.getName());
                    buffer.append('=');
                    this.appendTo(value, buffer);
                    if (f != (fields.size() - 1))
                        buffer.append(',');
                }
            }
        }
        buffer.append(']');
        return buffer.toString();
    }
    /**
     * Collect all fields declared by the targetClass and all its superclasses.
     * Excludes static fields.
     * @param targetClass
     * 					the class where to start collecting
     * @param fields
     * 					the container of Field instances
     */
    private void collectDeclaredFieldsInto(Class targetClass, List fields) {
        if (targetClass == null)
            return;
        this.collectDeclaredFieldsInto(targetClass.getSuperclass(), fields);
        Field[] localFields = targetClass.getDeclaredFields();
        for (int i = 0; i < localFields.length; i++) {
            if (!Modifier.isStatic(localFields[i].getModifiers()))
                    fields.add(localFields[i]);
        }
    }

    /**
     * Append to the buffer a String presentation of a value. Collections are
     * displayed by their element type and size. Do not recursively send
     * toString() to Objects.
     * 
     * @param value
     * @param buffer
     */
    private void appendTo(Object value, StringBuffer buffer) {
        if (value.getClass() == Boolean.class || value.getClass() == Integer.class || value.getClass() == Byte.class
                || value.getClass() == Long.class || value.getClass() == Character.class
                || value.getClass() == Float.class || value.getClass() == Double.class) {
            buffer.append(value);
            return;
        }
        if (value instanceof String) {
            buffer.append(value);
            return;
        }
        if (value.getClass().isArray()) {
            Object[] valueArray = (Object[]) value;
            if (valueArray.length == 0)
                buffer.append("[0]");
            else {
                buffer.append(this.shortNameOf(valueArray[0].getClass()));
                buffer.append("[");
                buffer.append(valueArray.length);
                buffer.append("]");
            }
            return;
        }
        if (value instanceof List) {
            List valueList = (List) value;
            if (valueList.size() == 0)
                buffer.append("{0}");
            else {
                buffer.append(this.shortNameOf(valueList.get(0).getClass()));
                buffer.append("{");
                buffer.append(valueList.size());
                buffer.append("}");
            }
            return;
        }
        if (depth == 0) {
        	buffer.append("a");
        	buffer.append(this.shortNameOf(value.getClass()));
        	buffer.append("@");
        	buffer.append(value.hashCode());
        } else {
        	buffer.append(ToStringBuilder.build(value,true,true, depth - 1));
        }        
    }

    /**
     * Return the abbreviated name of the class. It strips the namespace part
     * from the qualified name.
     */
    private String shortNameOf(Class aClass) {
        String fullName = aClass.getName();
        int dot = fullName.lastIndexOf('.');
        return fullName.substring(dot + 1, fullName.length());
    }

    /**
     * Try to access the value of a field for an object. First try by inspecting
     * the field and if that fails try using a getter. Return the result. Tell
     * the builder whether an error has occurrred.
     */
    private Object getValueOf(Field aField, Object anObject) {
        Object result;
        try {
            result = aField.get(anObject);
            accessErrorOccurred = false;
        } catch (IllegalArgumentException e) {
            result = getValueUsingGetterOf(aField, anObject);
        } catch (IllegalAccessException e) {
            result = getValueUsingGetterOf(aField, anObject);
        }
        return result;
    }

    /**
     * Try to access the value of a field using the getter method (if any).
     * Return the result. Tell the builder whether an error has occurrred.
     */
    private Object getValueUsingGetterOf(Field aField, Object anObject) {
        Class itsClass = anObject.getClass();
        Method getter = null;
        Object result;
        try {
            getter = this.lookupMethod(getterNameFor(aField), itsClass);
        } catch (SecurityException e) {
            accessErrorOccurred = true;
            return "*secure*";
        } catch (NoSuchMethodException e) {
            accessErrorOccurred = true;
            return "*no getter*";
        }
        try {
            result = getter.invoke(anObject, new Object[0]);
        } catch (IllegalArgumentException e1) {
            accessErrorOccurred = true;
            return "*illegal*";
        } catch (IllegalAccessException e1) {
            accessErrorOccurred = true;
            return "*illegal*";
        } catch (InvocationTargetException e1) {
            accessErrorOccurred = true;
            return "*error*";
        }
        accessErrorOccurred = false;
        return result;
    }
    /**
     * Look up a method in the class hierarchy
     * @param methodName : name of the method
     * @param hereClass : where to start searching
     * @return the Method instance
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public Method lookupMethod(String methodName, Class hereClass) throws NoSuchMethodException, SecurityException {
       if (hereClass == null) throw new NoSuchMethodException(methodName);
       Method method;
       try {
           method = hereClass.getMethod(methodName, new Class[0]);
        } catch (NoSuchMethodException ex) {
            // retry using super
            return lookupMethod(methodName,hereClass.getSuperclass());
        }
        return method;
    }
    /**
     * Return the standard name of an operation that can get the value of a
     * field. So if the field is named <b>salary </b> then operation returned is
     * <b>getSalary</b>. Boolean typed fields use the <b>is </b> prefix.
     */
    private String getterNameFor(Field field) {
        String prefix = (field.getType() == boolean.class) ? "is" : "get";
        String fieldName = field.getName();
        return prefix + (fieldName.substring(0, 1).toUpperCase()) + (fieldName.substring(1, fieldName.length()));
    }
}