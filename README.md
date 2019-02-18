# Jr Frame Control

This project implements the protocol for STAR III / Jr as specified by patent US4916539. This project includes a TCP socket data receiver which accepts frame data formatted in JSON, then takes that data and converts them to the appropriate dataframe format and writes it over serial to a receiver program, nominally located on an Arduino. A receiver program to drive the Jr FIFO directly is located here: https://kalos.wsrproject.org/jessecar/jr-frame-sender

(C) 2019 WSRProject

## Usage

jr-frame-control.jar COMPORT TCPPORT