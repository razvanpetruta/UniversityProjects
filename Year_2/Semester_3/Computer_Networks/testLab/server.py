import socket
import threading

HOST = "server ip"
PORT = 8888

clients = []
threads = []

def tcp_receive_int(sock):
    x = int(tcp_receive_string(sock, 4))
    return x

def tcp_send_string(sock, string):
    sock.sendall(string.encode("ascii"))

def tcp_receive_string(sock, size=1024):
    string = sock.recv(size).decode("ascii")
    return string

def worker(client, addr):
    try:
        while True:
            n = tcp_receive_int(client)
            characters = []
            for i in range(n):
                c = tcp_receive_string(client, 1)
                print(f"{addr} sent: {c}")
                characters.append(c)
            result = ""
            for c in characters:
                result += c
            tcp_send_string(client, result)
    except Exception:
        pass

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    while True:
        client, addr = s.accept()
        print(f"{addr} connected")
        if client not in clients:
            clients.append(client)
        t = threading.Thread(target=worker, args=(client, addr))
        threads.append(t)
        t.start()
    for t in threads:
        t.join()