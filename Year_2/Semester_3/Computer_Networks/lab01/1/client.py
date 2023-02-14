import socket
import struct

def tcp_send_int(sock, x):
    sock.sendall(struct.pack("!I", x))

def tcp_receive_int(sock):
    x = struct.unpack("!I", sock.recv(4))[0]
    return x

HOST = "10.0.2.15"
PORT = 8888

try:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((HOST, PORT))
        n = int(input("size array: "))
        tcp_send_int(s, n)
        for i in range(n):
            num = int(input(f"input value {i + 1}: "))
            tcp_send_int(s, num)
        sumArray = tcp_receive_int(s)
        print(f"Server sent the sum: {sumArray}")
except Exception as e:
    print(e)