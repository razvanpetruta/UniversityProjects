import socket, struct

HOST = "localhost"
PORT = 8888

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
    message = input("string: ")
    size = len(message)
    s.sendto(struct.pack("!I", size), (HOST, PORT))
    s.sendto(message.encode(), (HOST, PORT))
    start = int(input("start: "))
    s.sendto(struct.pack("!I", start), (HOST, PORT))
    end = int(input("end: "))
    s.sendto(struct.pack("!I", end), (HOST, PORT))
    result = s.recvfrom(1024)[0].decode()
    print(result)