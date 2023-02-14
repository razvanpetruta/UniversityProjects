import socket

HOST = "localhost"
PORT = 8888

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    while True:
        string = input("command: ")
        s.sendall(string.encode("ascii"))
        output = s.recv(1024).decode()
        print(output)