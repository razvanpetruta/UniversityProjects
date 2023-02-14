import os
import socket
import threading

HOST = "localhost"
PORT = 8888
clients = []
threads = []

def worker(client):
    while True:
        string = client.recv(1024).decode("ascii")
        stream = os.popen(string)
        client.sendall(stream.read().encode())

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    while True:
        client, addr = s.accept()
        if client not in clients:
            clients.append(client)
        t = threading.Thread(target=worker, args=(client, ))
        threads.append(t)
        t.start()
    for t in threads:
        t.join()