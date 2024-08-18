An APK application was decompiled (APK to smali code). 
A function was embedded in the application to send the application's database via HTTPS protocol to a specified server address. 
The database is transmitted in JSON format. 
The code was written in Java, translated to smali, and embedded into the target application. 
The project also contains a Flask microservice that receives the database, parses the JSON, and generates graphs (graph generation for demonstrating the microservice's functionality). 

The official application was "modified" for conducting research and centralized data collection from Android devices. 
The purpose of the research is to collect data on the concentration of hydrogen and methane in exhaled air using a breath test, the interface of which is implemented through an Android application.

The data on hydrogen and methane are used, supplemented with sequencing data, and utilized to build a model for predicting the concentration of hydrogen and methane producers in the human gut.
