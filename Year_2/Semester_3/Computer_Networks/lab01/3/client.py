from email import message
import socket

HOST = "localhost"
PORT = 8888

def tcp_send_string(sock, string):
    sock.sendall(string.encode("ascii"))

def tcp_receive_string(sock, size=1024):
    message = sock.recv(size).decode("ascii")
    return message

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    message = input("message: ")
    tcp_send_string(s, message)
    reversed_string = tcp_receive_string(s)
    print(reversed_string)