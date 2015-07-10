# Eddie
Environment for faster coding of quick Java tools.

Eddie was written for two reasons:
1. Reducing the overheads for writing small java tools. 
2. Serving as single project for keeping tools, as an aid for (organisational) memory of available tools. 

Eddie tools are essentially scripts, that are written in Java rather than in a more script-friendly language. 

## Adding a new Eddie Tool

New tools should be added under the package com.decell.EddieTools

Create a tool by creating two classes:

**ToolNameProperties**: a pojo object with a set of setters and getters. Eddie will look in the configuration file for properties matching all the settings. So if there’s a method

```
void setFoo(boolean b)
```

Eddie will search for the following key in the properties:

```
ToolName.foo = False 
```

**ToolName**:
* implement callable<Void>, 
* receives a ToolNameProperties instance in the c’tor
* do the main processing in call()
