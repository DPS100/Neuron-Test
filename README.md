# Neuron-Test
This repository is meant to simulate small clusters of neurons preforming specific tasks.
These clusters, called *circuit* objects, are comprised of *node* objects that take one or more linear inputs and send multiple outputs to other nodes in a circuit.
Circuits are trained in groups of generations by a *trainer* object using a genetic algorithm to learn from a data set, and create a better preforming next generation.
Circuits can be individually written or read to .json files using [Google's gson](https://github.com/google/gson) serialization & deserialization library.
The end goal for this repository is to create both a method to train circuits to preform tasks efficiently and be able to visualize the learning process and the result of it.

## Version 1.0
### UNDER CONSTRUCTION
This version will be the MVP (minimum viable product).
It will have the baseline features to train circuits according to a set of inputs, visualize a circuit, read/write circuits to files, and utilize multitasking.
It will be packaged into a .jar file to be used on any JVM, with dedicated support for windows and prespective support on Linux and a Raspbian Lite x64 server.

## Verson 1.1
### FUTURE RELEASE
This version will build on the MVP to deliver easier implementation of real-time or feedback reliant training for circuits using a physics or other type of engine.
