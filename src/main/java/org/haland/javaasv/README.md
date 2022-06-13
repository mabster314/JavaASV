# JavaASV [![Build Status](https://travis-ci.com/mabster314/JavaASV.svg?token=we3CuXLzowqhRDZN7QKU&branch=master)](https://travis-ci.com/mabster314/JavaASV) [![Coverage Status](https://coveralls.io/repos/github/mabster314/JavaASV/badge.svg?branch=master)](https://coveralls.io/github/mabster314/JavaASV?branch=master)
This is the Raspberry PI source code for Webb Institute's autonomous surface vessel, or ASV. 

# Contents
[Config](JavaASV/src/main/java/org/haland/javaasv/config)
- A series of objects that pull information on the Route, Serial Communications, etc.

[Controller](JavaASV/src/main/java/org/haland/javaasv/controller)
- Includes various algorithms for altering the vehilcle's path. Included are PID, Hitz, and a trivial controllers

[Helm](JavaASV/src/main/java/org/haland/javaasv/helm)
- Pulls rudder motor messages from the server and sends it via USB (or serial) to the arduino

[Message](JavaASV/src/main/java/org/haland/javaasv/message)
- This is how various objects interact with each other. Any object can send/request data to/from the message server.

[Pilot](JavaASV/src/main/java/org/haland/javaasv/pilot)
- The GPSHatParser obtains the longitude, laditude, and heading of the vehilce. This is then sent to SimplePilot.java, which calculates the next rudder and throttle position

[Route](JavaASV/src/main/java/org/haland/javaasv/route)
- Reads the route from the config file. The RouteParser object is then fed into the pilot

[Util](JavaASV/src/main/java/org/haland/javaasv/util)
- Provides a set of mathematical functions to be used by the ASV.

# About the project
This project is a continuation of multiple student's theses. In 2016 the project began with (Wait for permission to share name) designing and constructing the model and writing code. (Ask if the code, BOM, plans, etc can be uploaded here as well). Back then, the vessel only used an arduino, so the code was written in C++.

In  2018 this project was continued by Ratinaud who re-wrote the code for path following.

In  2020 and 2021, @mabster314 added an arduino for the main computer to allow for more advanced algorithms and improved connectivity. The arduino is still utilized to provide real time response to control the hardware.
