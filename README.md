# Simulation_RMS-EDF

### Use the simulation
Just fill the form, add your tasks an hit start!

### Config files
Any number of config files ( with arbitrary names ) can be created and added in "src/config". They will be parsed and read on runtime.

The config file must look like the following:

```
<scheduler>,<simulation runtime>,
<id>,<period>,<execution>
<id>,<period>,<execution>
<id>,<period>,<execution>
```

scheduler : must either be "EDF" or "RMS"<br/>
simulation runtime : must be a number or can be left empty<br/>
id, period and execution : must be numbers. An arbitrary number of tasks can be added. However, no tasks 
or more than 10 will result in an error when the simulation starts.<br/>
<br/><br/>
==> If these criteria are not met, an error will be displayed.

### JavaFx Setup
https://openjfx.io/
