import socket
import struct

HOST = "127.0.0.1"
PORT = 8888

def tcp_send_string(sock, string):
    sock.sendall(string.encode("ascii"))

def tcp_receive_int(sock):
    x = struct.unpack("!I", sock.recv(4))[0]
    return x

try:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((HOST, PORT))
        message = input("Enter a sentence: ")
        tcp_send_string(s, message)
        n = tcp_receive_int(s)
        print(f"The number of spaces are: {n}")
except Exception as e:
    print(e)