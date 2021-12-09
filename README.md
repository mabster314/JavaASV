# JavaASV [![Build Status](https://travis-ci.com/mabster314/JavaASV.svg?token=we3CuXLzowqhRDZN7QKU&branch=master)](https://travis-ci.com/mabster314/JavaASV) [![Coverage Status](https://coveralls.io/repos/github/mabster314/JavaASV/badge.svg?branch=master)](https://coveralls.io/github/mabster314/JavaASV?branch=master)
An open-source Autonomous Surface Vessel (ASV) navigation controller written in Java.

# About the project
This project is a continuation of multiple student's theses. In 2016 the project began with (Wait for permission to share name) designing and constructing the model and writing code. (Ask if the code, BOM, plans, etc can be uploaded here as well). Back then, the vessel only used an arduino, so the code was written in C++.

In  2018 this project was continued by (name) who re-wrote the code for path following.

In  2020 and 2021, @mabster314 added an arduino for the main computer to allow for more advanced algorithms and improved connectivity. The arduino is still utilized to provide real time response to control the hardware.


## What does this code do
Inputs: - User input GPS waypoints
        - GPS signal

Code outputs:
        - Rudder position
        - Motor power

# Raspberry PI Setup

From a clean install of Raspbian...

sudo apt-get update
sudo apt-get upgrade
sudo apt-get install apache 2
sudo apt install php -y
sudo apt-get install php libapache2-mod-php -y
git clone https://github.com/mabster314/javaASV
sudo apt install minicom -y
 ** ADD IN WEBPAGE INSTALL AND SETUP


# Mechanical and Electrical Design

* Add info on where to access the mechanical and electrical design of the ASV*