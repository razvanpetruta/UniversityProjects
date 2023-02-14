import socket, threading, struct, random


start = 1
stop = 100
random.seed()
target = random.randint(start, stop)
threads = []
client_count = 0
guessed = False
guessLock = threading.Lock()
countLock = threading.Lock()
winner = None
e = threading.Event()


def worker(client, address):
    global countLock, guessLock, guessed, winner, client_count, e

    countLock.acquire()
    id_count = client_count
    client_count += 1
    countLock.release()

    print(f"client #{id_count} from: {address}")
    message = f"Hello client #{id_count}! You are entering the competition now!"
    client.sendall(message.encode("ascii"))

    while not guessed:
        try:
            n = struct.unpack("!I", client.recv(4))[0]
            print(f"client #{id_count} sent {n}")
            if n > target:
                client.sendall(b"L")
            elif n < target:
                client.sendall(b"H")
            else:
                guessLock.acquire()
                guessed = True
                if winner == None:
                    winner = threading.get_ident()
                guessLock.release()
        except Exception as e:
            print(e)
            break

    if guessed:
        if threading.get_ident() == winner:
            client.sendall(b"G")
            print(f"We have a winner: {address}")
            print(f"Thread {id_count} winner")
            e.set()
        else:
            client.sendall(b"X")

    client.close()


def reset_server():
    global e, threads, guessed, guessLock, winner, client_count, target

    while True:
        e.wait()
        for t in threads:
            t.join()
        print("all threads are finished now")
        e.clear()
        guessLock.acquire()
        threads = []
        guessed = False
        winner = None
        client_count = 0
        target = random.randint(start, stop)
        print(f"the number is: {target}")
        guessLock.release()


if __name__ == "__main__":
    print(f"the number is: {target}")
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind(("localhost", 8888))
        s.listen(5)
        t = threading.Thread(target=reset_server, daemon=True)
        t.start()
        while True:
            client, address = s.accept()
            t = threading.Thread(target=worker, args=(client, address))
            threads.append(t)
            t.start()