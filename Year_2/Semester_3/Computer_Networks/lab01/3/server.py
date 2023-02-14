import socket

HOST = "localhost"
PORT = 8888

def tcp_receive_string(sock, size=1024):
    string = sock.recv(size).decode("ascii")
    return string

def tcp_send_string(sock, string):
    sock.sendall(string.encode("ascii"))

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    conn, addr = s.accept()
    print(f"{addr} has connected")
    string = tcp_receive_string(conn)
    tcp_send_string(conn, string[::-1])