import serial
import time
import datetime as dt
arduino = serial.Serial(port='/dev/ttyUSB0', baudrate=9600, timeout=.1)
def write_read(x):
    arduino.write(bytes(x, 'utf-8'))
    time.sleep(0.05)
    data = arduino.readline()
    return data
while True:
    with open('data.csv', 'a') as file:
    	data = f'\n{dt.datetime.now()},{arduino.readline()}'
    	data = data.replace("b'", "")
    	data = data.replace("\\r\\n'", "")
    	file.write(data)
    	print(data)
    	
    time.sleep(10)