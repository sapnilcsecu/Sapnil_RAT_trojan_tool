import socket, os
import threading        
import sys

def connect(ip_address, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((ip_address, int(port)))
 
    while True: 
        command =  s.recv(1024*1024).decode()
        #con_str=command.decode()
        print("the command is ",command)
        
        if 'kill' in command:
            print('kill process')
        
        elif 'terminate' in command:
           return 1
        elif 'stop' in command:
            return 2
        
        else:
            print(command)
       
            

def run():
    ip_address='172.31.2.202'
    port=8080
    #print("thread stared")
    while True:
        try:
            if connect(ip_address, port) == 1:
                return True
            if connect(ip_address, port) == 2:
                return True
        except Exception as e:
            print("Checking", ip_address, "for a connection....",str(e))
            return False

def main ():
    #print("program stared")
    threading.Thread().start()
    while True: 
        stop = run()
        if stop:
            break 
    print("client thread treminated")
if __name__ == "__main__":
    main()
