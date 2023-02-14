import socket, struct, random, time

steps = 0

if __name__ == "__main__":
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(("localhost", 8888))
        done = False
        start = 1
        stop = 100
        random.seed()

        data = s.recv(1024).decode("ascii")
        print(data)

        while not done:
            n = random.randint(start, stop)
            s.sendall(struct.pack("!I", n))
            answer = s.recv(1)
            if answer == b"H":
                start = n + 1
            elif answer == b"L":
                stop = n - 1
            else:
                done = True
                if answer == b"G":
                    print("Winner")
                elif answer == b"X":
                    print("Loser")
            time.sleep(1)