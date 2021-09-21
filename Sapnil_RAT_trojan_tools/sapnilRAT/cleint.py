import socket, os
import threading        
import numpy, sys

def connect(ip_address, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((ip_address, int(port)))
 
    while True: 
        command =  s.recv(1024*1024).decode()
        
        
        if 'kill' in command:
            print('kill process')
        
        elif 'terminate' in command:
           return 1
        elif 'stop' in command:
            return 2
        
        else:
            print(command.decode())
       
            

def run():
    ip_address='127.0.0.1'
    port=8080
    while True:
        try:
            if connect(ip_address, port) == 1:
                return True
            if connect(ip_address, port) == 2:
                return True
        except:
            print("Checking", ip_address, "for a connection....")
            return False

def main ():
    threading.Thread().start()
    while True: 
        stop = run()
        if stop:
            break 
    print("client thread treminated")
if __name__ == "__main__":
    main()
