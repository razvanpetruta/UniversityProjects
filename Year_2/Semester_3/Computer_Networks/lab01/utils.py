"""
! - (from or to) network
c - char
h - short
H - unsigned short
i - int
I - unsigned int
q - long long
Q - unsigned long long
f - float
d - double
"""


import struct, socket


# # # # # # # # # # # # # # # # # # #
# SENDING DATA VIA TCP
# # # # # # # # # # # # # # # # # # #
def tcp_send_int(sock, x):
    sock.sendall(struct.pack("!I", x))
    # sock.send(struct.pack("!I", x))

def tcp_receive_int(sock):
    x = struct.unpack("!I", sock.recv(4))[0]
    return x

def tcp_send_string(sock, string):
    sock.sendall(string.encode("ascii"))
    # sock.send(string.encode("ascii"))

def tcp_receive_string(sock, size=1024):
    string = sock.recv(size).decode("ascii")
    return string


# # # # # # # # # # # # # # # # # # #
# SERVER 
# # # # # # # # # # # # # # # # # # #
def server():
    HOST = "127.0.0.1"
    PORT = 8888
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((HOST, PORT))
        s.listen()
        conn, addr = s.accept() # new socket
        # do something
        # send answer


# # # # # # # # # # # # # # # # # # #
# CLIENT
# # # # # # # # # # # # # # # # # # #
def client():
    HOST = "127.0.0.1"
    PORT = 8888
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((HOST, PORT))
        # do something
        # send something
        # receive the answer