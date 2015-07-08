package com.daphshez.EddieTools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class FooTool implements Callable<Void>
{
    private final Logger logger = LoggerFactory.getLogger(FooTool.class);
    private final FooToolProperties properties;

    public FooTool(FooToolProperties properties)
    {
        this.properties = properties;
    }

    public Void call() throws Exception
    {
        logger.info("Number is: " + properties.getN());
        logger.info("Float is: " + properties.getF());
        logger.info("String is: " + properties.getS());
        logger.info("Boolean is: " + properties.isB());
        logger.info("Path is: " + properties.getP());

        return null;
    }
}
