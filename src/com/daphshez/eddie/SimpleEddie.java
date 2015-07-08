package com.daphshez.eddie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * User: daphna
 * Date: 06/08/2014
 */
public class SimpleEddie
{
    private static final String PROPERTIES_FILE_NAME = "conf/eddie.properties";
    private static final Logger logger = LoggerFactory.getLogger(SimpleEddie.class);

    public static void main(String[] args) throws Exception
    {
        // load the properties file from the path
        InputStream in = SimpleEddie.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (in == null)
            throw new RuntimeException("Can't find " + PROPERTIES_FILE_NAME + " in classpath");
        Properties properties = new Properties();
        properties.load(in);

        // find the name of the class to run
        String appToRun = properties.getProperty("AppToRun");
        if (appToRun == null)
            throw new RuntimeException("AppToRun value missing from properties file");
        logger.info("Trying to execute " + appToRun);

        // we expect the properties object to be the first parameter of the app
        Class appToRunClass;
        try {
            appToRunClass = Class.forName(appToRun);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find app to run: " + appToRun, e);
        }

        // we expect app to run to be callable
        if (!Callable.class.isAssignableFrom(appToRunClass))
            throw new RuntimeException(appToRun + " should implement the interface Callable!");

        Constructor[] constructors = appToRunClass.getConstructors();
        if (constructors.length != 1)
            throw new RuntimeException(appToRunClass + ": number of constructors != 1 " + Arrays.toString(constructors));

        Constructor constructor = constructors[0];



        Class propertiesObjectClass = constructor.getParameterTypes()[0];

        // does the properties object need a Properties object in its constructor ?
        // otherwise assume no parameters c'tor

        Object propertiesObject;
        try {
            Constructor propertiesObjectConstructor = propertiesObjectClass.getConstructor(Properties.class);
            propertiesObject = propertiesObjectConstructor.newInstance(properties);
        } catch (NoSuchMethodException e) {
            try {
                propertiesObject = propertiesObjectClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                throw new RuntimeException("Failed to instantiate properties class " + propertiesObjectClass, e1);
            }
        }

        // load the properties
        SimpleEddiePropertyLoader simpleEddiePropertyLoader = new SimpleEddiePropertyLoader(properties);
        simpleEddiePropertyLoader.fillPropertiesObject(propertiesObject, appToRunClass.getSimpleName());

        // now we can instantiate the app we want to run
        Callable appToRunObject;
        try {
                appToRunObject = (Callable) constructor.newInstance(propertiesObject);
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to execute constructor " + constructor);
        }

        appToRunObject.call();

    }
}
