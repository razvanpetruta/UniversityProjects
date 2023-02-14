import socket, struct

HOST = "localhost"
PORT = 8888

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
    s.bind((HOST, PORT))
    size = struct.unpack("!I", s.recvfrom(4)[0])[0]
    message, address = s.recvfrom(size)
    message = message.decode()
    start = struct.unpack("!I", s.recvfrom(4)[0])[0]
    end = struct.unpack("!I", s.recvfrom(4)[0])[0]
    s.sendto(message[start:end].encode(), address)