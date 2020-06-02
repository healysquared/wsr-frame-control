# WSR Frame Control

This project implements the protocol for STAR III / Jr as specified by patent US4916539. This project includes a TCP socket data receiver which accepts frame data formatted in JSON, then takes that data and converts them to the appropriate dataframe format and writes it over serial to a receiver program, nominally located on an Arduino.

This project is far from complete. I will work on adding documentation soon for program usage. 

## Usage

jr-frame-control.jar COMPORT TCPPORT DELAY

## Libraries

This project uses the jSerialComm (https://github.com/Fazecast/jSerialComm) and the Google Gson library (https://github.com/google/gson)