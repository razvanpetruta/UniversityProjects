import socket
import struct

def tcp_send_int(sock, x):
    sock.sendall(struct.pack("!I", x))

def tcp_receive_int(sock):
    x = struct.unpack("!I", sock.recv(4))[0]
    return x

HOST = "192.168.19.191"
PORT = 8888

try:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((HOST, PORT))
        s.listen()
        conn, addr = s.accept() # new socket
        print(f"client address: {addr}")
        n = tcp_receive_int(conn)
        sumArray = 0
        for i in range(n):
            num = tcp_receive_int(conn)
            sumArray += num
        tcp_send_int(conn, sumArray)
except Exception as e:
    print(e)
    