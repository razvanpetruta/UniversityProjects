import socket, threading, struct

HOST = "localhost"
PORT = 8888
clients = []
threads = []

def worker(client, address):
    print(f"Client: {address} connected")
    while True:
        string = client.recv(1024).decode("ascii")
        if string == "stop":
            break
        try:
            with open(string, "r") as f:
                content = f.read()
                client.sendall(struct.pack("!I", len(content)))
                client.sendall(content.encode("ascii"))
        except FileNotFoundError:
            client.sendall(struct.pack("!i", -1))

if __name__ == "__main__":
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((HOST, PORT))
        s.listen(5)
        while True:
            client, address = s.accept()
            if client not in clients:
                clients.append(client)
            t = threading.Thread(target=worker, args=(client, address))
            t.start()
            threads.append(t)