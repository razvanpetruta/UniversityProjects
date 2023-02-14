import socket
import struct

HOST = "127.0.0.1"
PORT = 8888

def tcp_send_int(sock, x):
    sock.sendall(struct.pack("!I", x))

def tcp_receive_string(sock, size=1024):
    string = sock.recv(size).decode("ascii")
    return string

try:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((HOST, PORT))
        s.listen()
        conn, addr = s.accept()
        message = tcp_receive_string(conn)
        count = 0
        for letter in message:
            if letter == ' ':
                count += 1
        tcp_send_int(conn, count)
except Exception as e:
    print(e)