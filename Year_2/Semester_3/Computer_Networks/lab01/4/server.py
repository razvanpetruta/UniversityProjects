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

def merge_sort(string1, string2):
    i = 0
    j = 0
    result = ""
    while i < len(string1) and j < len(string2):
        if string1[i] < string2[j]:
            result += string1[i]
            i += 1
        else:
            result += string2[j]
            j += 1
    while i < len(string1):
        result += string1[i]
        i += 1
    while j < len(string2):
        result += string2[j]
        j += 1
    return result

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    conn, addr = s.accept()
    len1 = tcp_receive_int(conn)
    string1 = tcp_receive_string(conn, len1)
    len2 = tcp_receive_int(conn)
    string2 = tcp_receive_string(conn, len2)
    result = merge_sort(string1, string2)
    tcp_send_string(conn, result)