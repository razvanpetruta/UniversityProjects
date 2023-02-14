import socket
import struct

HOST = "localhost"
PORT = 8888

def tcp_send_string(sock, string):
    sock.sendall(string.encode("ascii"))

def tcp_receive_string(sock, size=1024):
    string = sock.recv(size).decode("ascii")
    return string

def tcp_send_int(sock, x):
    sock.sendall(struct.pack("!I", x))
    # sock.send(struct.pack("!I", x))

def tcp_receive_int(sock):
    x = struct.unpack("!I", sock.recv(4))[0]
    return x

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    string1 = input("string 1: ")
    tcp_send_int(s, len(string1))
    tcp_send_string(s, string1)
    string2 = input("string 2: ")
    tcp_send_int(s, len(string2))
    tcp_send_string(s, string2)
    result = tcp_receive_string(s)
    print(result)