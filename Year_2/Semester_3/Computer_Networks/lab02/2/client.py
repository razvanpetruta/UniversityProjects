import socket, struct

HOST = "localhost"
PORT = 8888

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    while True:
        string = input("file path: ")
        s.sendall(string.encode("ascii"))
        if string == "stop":
            break
        size = struct.unpack("!i", s.recv(4))[0]
        if size >= 0:
            print(size)
            content = s.recv(size).decode("ascii")
            with open(string + "-copy", "w") as f:
                f.write(content)
        else:
            print(f"File: {string} does not exist")