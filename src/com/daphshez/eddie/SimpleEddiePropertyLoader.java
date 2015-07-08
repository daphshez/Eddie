package com.daphshez.eddie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 */
public class SimpleEddiePropertyLoader
{
    private final Logger logger = LoggerFactory.getLogger(SimpleEddiePropertyLoader.class);
    private final Properties properties;

    public SimpleEddiePropertyLoader(Properties properties)
    {
        this.properties = properties;
        // keep the properties in lower case.
        properties.stringPropertyNames().stream()
                .forEach(key->this.properties.setProperty(key.toLowerCase(),properties.getProperty(key)));
    }

    // todo: add date, enum...
    public void fillPropertiesObject (Object object, String prefix) throws InvocationTargetException, IllegalAccessException
    {
        for (Method setter : getListOfSetters(object.getClass()))
        {
            Class parameterType = getParameterType(setter);

            String propertyName = setter.getName().substring(3);

            if (parameterType.equals(int.class))
                setter.invoke(object,getIntValue(prefix, propertyName));
            else if (parameterType.equals(float.class))
                setter.invoke(object,getFloatValue(prefix, propertyName));
            else if (parameterType.equals(double.class))
                setter.invoke(object,getDoubleValue( prefix, propertyName));
            else if (parameterType.equals(String.class))
                setter.invoke(object,getStringValue( prefix, propertyName));
            else if (parameterType.equals(Path.class))
                setter.invoke(object,getPathValue( prefix, propertyName));
            else if (parameterType.equals(File.class))
                setter.invoke(object,getFileValue( prefix, propertyName));
            else if (parameterType.equals(boolean.class))
                setter.invoke(object,getBooleanValue(prefix, propertyName));
            else
                throw new RuntimeException("Unsupported type " + parameterType);
        }
    }

    private boolean getBooleanValue(String prefix, String name)
    {
        String propertyName = getPropertyName(prefix, name);
        String property = properties.getProperty(propertyName);
        if (property == null)
            throw new RuntimeException("Property " + propertyName + " missing from Properties object");
        try {
            return Boolean.parseBoolean(property);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error parsing boolean from value=<" + property + "> at key <" + propertyName + ">");
        }
    }

    private int getIntValue(String prefix, String name)
    {
        String propertyName = getPropertyName(prefix, name);
        String property = properties.getProperty(propertyName);
        if (property == null)
            throw new RuntimeException("Property " + propertyName + " missing from Properties object");
        try {
            return Integer.parseInt(property);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error parsing int from value=<" + property + "> at key <" + propertyName + ">");
        }
    }

    private String getPropertyName(String prefix, String name)
    {
        return (prefix + "." + name).toLowerCase();
    }

    private float getFloatValue(String prefix, String name)
    {
        String propertyName = getPropertyName(prefix, name);
        String property = properties.getProperty(propertyName);
        if (property == null)
            throw new RuntimeException("Property " + propertyName + " missing from Properties object");
        try {
            return Float.parseFloat(property);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error parsing float from value=<" + property + "> at key <" + propertyName + ">");
        }
    }

    private double getDoubleValue(String prefix, String name)
    {
        String propertyName = getPropertyName(prefix, name);
        String property = properties.getProperty(propertyName);
        if (property == null)
            throw new RuntimeException("Property " + propertyName + " missing from Properties object");
        try {
            return Double.parseDouble(property);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error parsing double from value=<" + property + "> at key <" + propertyName + ">");
        }
    }

    private String getStringValue( String prefix, String name)
    {
        String propertyName = getPropertyName(prefix, name);
        String property = properties.getProperty(propertyName);
        if (property == null)
            throw new RuntimeException("Property " + propertyName + " missing from Properties object");
        return property;
    }

    private Path getPathValue(String prefix, String name)
    {
        String propertyName = getPropertyName(prefix, name);
        String property = properties.getProperty(propertyName);
        if (property == null)
            throw new RuntimeException("Property " + propertyName + " missing from Properties object");
        return Paths.get(property);
    }

    private File getFileValue(String prefix, String name)
    {
        String propertyName = getPropertyName(prefix, name);
        String property = properties.getProperty(propertyName);
        if (property == null)
            throw new RuntimeException("Property " + propertyName + " missing from Properties object");
        return new File(property);
    }





    // setters are methods with name that start with "set" and with a single parameter
    private List<Method> getListOfSetters(Class clazz)
    {
        logger.debug("Loading list of setters for " + clazz);

        List<Method> setters = new LinkedList<>();

        Method[] methods = clazz.getMethods();
        logger.debug(clazz + " has " + methods.length + " methods");

        LinkedList<String> paramNames = new LinkedList<>();

        for (Method method : methods)
        {
            if (method.getName().startsWith("set"))
            {
                if (method.getParameterCount() == 1) {
                    setters.add(method);
                    paramNames.add(method.getName().substring(3));
                }
                else
                    logger.debug(method + " looks like setter, but getParameterCount != 1, skipping");
            }
        }

        logger.debug(setters.size() + " setters found: " + paramNames);
        return setters;
    }

    private Class getParameterType(Method setter)
    {
        Class<?>[] parameterTypes = setter.getParameterTypes();
        assert parameterTypes.length == 1;
        return parameterTypes[0];
    }


}
