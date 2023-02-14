import socket

HOST = "server ip"
PORT = 8888

def tcp_send_int(sock, x):
    sock.sendall(str(x).encode("ascii"))

def tcp_send_string(sock, string):
    sock.sendall(string.encode("ascii"))

def tcp_receive_string(sock, size=1024):
    string = sock.recv(size).decode("ascii")
    return string

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    while True:
        n = int(input("length: "))
        tcp_send_int(s, n)
        for i in range(n):
            c = input(f"char {i}: ")
            tcp_send_string(s, c)
        result = tcp_receive_string(s, n)
        print(f"Server sent: {result}")